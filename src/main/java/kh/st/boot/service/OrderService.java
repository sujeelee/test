package kh.st.boot.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import kh.st.boot.dao.DepositDAO;
import kh.st.boot.dao.MemberDAO;
import kh.st.boot.dao.OrderDAO;
import kh.st.boot.model.dto.MyStocksDTO;
import kh.st.boot.model.vo.AccountVO;
import kh.st.boot.model.vo.AdminVO;
import kh.st.boot.model.vo.DepositVO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.model.vo.OrderVO;
import kh.st.boot.model.vo.PointVO;
import kh.st.boot.model.vo.ReservationVO;
import kh.st.boot.model.vo.StockPriceVO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor	
public class OrderService {
	
	private OrderDAO orderDao;
	private DepositDAO depositDao;
	private MemberDAO memberDao;

	private DepositService depositService;
	private StockService stockService;
	private ConfigService configService;

	public List<ReservationVO> getReservation(String st_code, String mb_id) {
		return orderDao.getReservation(st_code, mb_id);
	}

	public String orderUpdate(Map<String, Object> form, Model model) {
		//예약으로 처리했는지, 바로 거래로 처리할건지 확인하기용
		String type = "order";
		boolean result = false;
		String state = "";
		
		MemberVO member = (MemberVO) model.getAttribute("member");
		
		if(form.get("status").equals("wait")) {
			type = "wait";
			state = "매수대기";
			ReservationVO reservation = new ReservationVO();
			reservation.setRe_st_code((String)form.get("od_st_code"));
			reservation.setRe_want_price(Integer.parseInt((String)form.get("st_price")));
		    reservation.setRe_qty(Integer.parseInt((String)form.get("od_qty")));
			reservation.setMb_id((String)form.get("mb_id"));
			
			if(form.get("od_type").equals("sell")) {
				state = "매도대기";
			}
			
			reservation.setRe_state(state);
			
			result = orderDao.insertReservation(reservation);
			
			if(result == false) {
				type = "fail";
			}
			
		} else {
			state = "매수완료";
			if(form.get("od_type").equals("sell")) {
				state = "매도완료";
			}
			OrderVO order = new OrderVO();
			DepositVO deposit = new DepositVO();
			
			AccountVO ac = depositDao.getAccount(member.getMb_no());
			
			int totalPrice = Integer.parseInt((String)form.get("od_price"));
			
			if((ac.getAc_deposit() < 0 || ac.getAc_deposit() < totalPrice) && form.get("od_type").equals("buy")) {
				return "실패";
			}
			
			int haveQty = orderDao.totalMyStock((String)form.get("od_st_code"), (String)form.get("mb_id")).getStocksQty();
			int totalQty = Integer.parseInt((String)form.get("od_qty"));
			
			//보유주식보다 많이 팔수 없음
			if(form.get("od_type").equals("sell") && (haveQty < 0 || haveQty < totalQty)) {
				return "실패";
			}
			
			int totalPoint = 0;
			int orginPrice = Integer.parseInt((String)form.get("od_st_price"));
			int totalPercentPrice = totalPrice - (orginPrice  * Integer.parseInt((String)form.get("od_qty")));
			
			
			String od_id = depositService.getOrderId("order");
			
			if(totalPercentPrice < 0) {
				totalPercentPrice = Math.abs(totalPercentPrice);
			}
			order.setOd_id(od_id);
			order.setOd_name((String)form.get("od_name"));
			order.setMb_id((String)form.get("mb_id"));
			order.setOd_price(totalPrice);
			order.setOd_percent_price(totalPercentPrice); 
			order.setOd_status(state);
			order.setOd_st_code((String)form.get("od_st_code"));
			order.setOd_st_name((String)form.get("od_st_name"));
			order.setOd_qty(totalQty);
			order.setOd_st_price(orginPrice);

			if(form.get("od_type").equals("buy")) {
				//구매일때만 포인트 지급
				totalPoint = totalPrice * Integer.parseInt((String)form.get("po_percent")) / 100;
				
				PointVO point = new PointVO();
				
				point.setMb_id(member.getMb_id());
				point.setPo_content("주문번호 : " + od_id + " 구매 지급 포인트 : " + totalPoint);
				point.setPo_num(totalPoint);
				orderDao.setPointBuy(point);
				orderDao.updateMemberPoint(point);
				
				//구매면 마이너스로 처리
				totalPrice = -totalPrice;
				totalQty = -totalQty;
			} 
			order.setOd_point(totalPoint);
			
			
			result = orderDao.insertOrder(order);
			
			
			deposit.setDe_content(state.substring(0,2) + " : " + Integer.parseInt((String)form.get("od_qty")) + "주");
			deposit.setDe_num(totalPrice);
			deposit.setMb_id(member.getMb_id());
			deposit.setDe_before_num(ac.getAc_deposit());
			deposit.setDe_stock_code((String)form.get("od_st_code"));
			
			orderDao.stockQty(totalQty, (String)form.get("od_st_code"));

			depositDao.updateAccountDeposit(ac, totalPrice);
			depositDao.insertDepositLog(deposit);
				
			
		}
		
		if(result == false) {
			state = "실패";
		}
		
		return state;
	}

	public MyStocksDTO totalMyStock(String st_code, String mb_id) {
		MyStocksDTO myStock = orderDao.totalMyStock(st_code, mb_id);
		
		if(myStock.getStocksQty() > 0) {
			StockPriceVO stockPrice = stockService.getStockPrice(null, st_code);
			int nowPrice = stockPrice.getSi_price();
			
			myStock.setStockAverage(myStock.getStockOrignPrice() / myStock.getStocksQty());
			myStock.setStockNowPrice(nowPrice * myStock.getStocksQty());
			AdminVO config = configService.getConfig();
			myStock.setStockPercent(myStock.getStockNowPrice() * config.getCf_percent() / 100);
		}
		return myStock;
	}

	public boolean deleteReservation(String st_code, String re_no) {
		return orderDao.deleteReservation(st_code, re_no);
	}
}
