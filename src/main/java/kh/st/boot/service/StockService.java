package kh.st.boot.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import kh.st.boot.dao.AdminDAO;
import kh.st.boot.dao.DepositDAO;
import kh.st.boot.dao.MemberDAO;
import kh.st.boot.dao.OrderDAO;
import kh.st.boot.dao.StockDAO;
import kh.st.boot.model.vo.AccountVO;
import kh.st.boot.model.vo.AdminVO;
import kh.st.boot.model.vo.DepositVO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.model.vo.OrderVO;
import kh.st.boot.model.vo.PointVO;
import kh.st.boot.model.vo.ReservationVO;
import kh.st.boot.model.vo.StockJisuVO;
import kh.st.boot.model.vo.StockPriceVO;
import kh.st.boot.model.vo.StockVO;
import kh.st.boot.pagination.PageMaker;
import kh.st.boot.pagination.StockCriteria;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor	
public class StockService {
	
	private StockDAO stockDao;
	private MemberDAO memberDao;
	private AdminDAO adminDao;
	private OrderDAO orderDao;
	private DepositDAO depositDao;
	
	private DepositService depositService;
	
	public StockVO getCompanyOne(String st_code) {
		StockVO stock = stockDao.getStockCompany(st_code);
		return stock;
	}
	
	public void insertStockCompany(StockVO newStock) {
		StockVO stock = getCompanyOne(newStock.getSt_code());
		if(stock != null) {
			return;
		} else {
			if(newStock.getSt_status() != "") {
				newStock.setSt_status("상장폐지");
			} else {
				newStock.setSt_status("정상");
			}
			stockDao.insertStockCompany(newStock);
		}
	} 
	
	public List<StockVO> getCompanyList(String type, StockCriteria cri) {
		if(cri != null) {
			if(cri.getMrk() != null) {
				return stockDao.getCompanyListMrk(type, cri);
			}
		}
		return stockDao.getCompanyList(type, cri);
	}
	
	public StockPriceVO getStockPrice(String si_date, String st_code) {
		StockPriceVO price;
		if(si_date == null) {
			price = stockDao.getStockPriceOne(st_code);
		} else {
			price = stockDao.getStockPrice(si_date, st_code);
		}
		return price;
	}
	
	public boolean insertPrice(StockPriceVO price) {
		StockPriceVO oldPrice = getStockPrice(price.getSi_date(), price.getSt_code());
		if(oldPrice == null) {
			return stockDao.insertStockPrice(price);
		} else {
			return false;
		}
	}
	
	public void updateCompanyType(String st_code, String st_type, String st_status) {
		StockVO stock = getCompanyOne(st_code);
		 
		if(stock == null) return;
		
		stockDao.companyType(st_code, st_type, st_status);
	}

	public PageMaker getPageMaker(StockCriteria cri) {
		int count = 0;
		if(cri.getMrk() != null) {
			count = stockDao.getCountMrk(cri);
		} else {
			count = stockDao.getCount(cri);
		}
		return new PageMaker(10, cri, count);
	}
	
	public List<StockPriceVO> getStockInfoList(String st_code) {
		List<StockPriceVO> list = stockDao.getStockInfoList(st_code);
		return list;
	}
	
	public List<StockPriceVO> getStockInfoListDate(String st_code, String type) {
		String to_date = null, from_date = null;
		
		if(type.equals("3month")) {
			// 년월 형식으로 변환 (yyyyMM)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
			// 2개월 전 날짜
	        to_date = LocalDate.now().minusMonths(2).format(formatter);
	        // 1개월 후 날짜
	        from_date = LocalDate.now().plusMonths(1).format(formatter);
		    
		} else if(type.equals("year")) {
			// 년형식으로 변환 (yyyy)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
			// 현재년도
	        to_date = LocalDate.now().format(formatter);
	        // 1년후
	        from_date = LocalDate.now().plusYears(1).format(formatter);
		} else if(type.equals("month")) {
			// 년월 형식으로 변환 (yyyyMM)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
			// 오늘 날짜
	        to_date = LocalDate.now().format(formatter);
	        // 1개월 후 날짜
	        from_date = LocalDate.now().plusMonths(1).format(formatter);
		} 
		
		/*else if(type.equals("7days")) {
			// 년월일 형식으로 변환 (yyyyMMdd)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			// 7일 전 날짜
	        to_date = LocalDate.now().minusDays(7).format(formatter);
	        // 오늘 날짜로부터 1일 후 
	        from_date = LocalDate.now().plusDays(1).format(formatter);
		} */
		
		List<StockPriceVO> list = stockDao.getStockInfoListDate(st_code, to_date, from_date);
		return list;
	}

	public int getCompanyStockCount(String st_code) {
		StockVO stock = getCompanyOne(st_code);
		
		if(stock == null ) return 0;
		
		return stockDao.getCountStockPrice(st_code);
	}

	public StockJisuVO getOldJisu(String date, String type) {
		return stockDao.getOldJisu(date, type);
	}

	public boolean insertStockJisu(StockJisuVO jisu) {
		return stockDao.insertStockJisu(jisu);
	}
	
	public void reservationBuyOrder(OrderVO order) {
		//구매면 마이너스로 처리
		int totalPrice = -order.getOd_price();
		int totalQty = -order.getOd_qty();
		String od_id = depositService.getOrderId("order");
		
		if(!od_id.equals(order.getOd_id())) {
			order.setOd_id(od_id);
		}
		
		orderDao.insertOrder(order);
		
		//구매일때만 포인트 지급
		PointVO point = new PointVO();
		MemberVO member = memberDao.findById(order.getMb_id());
		
		point.setMb_id(order.getMb_id());
		point.setPo_content("주문번호 : " + order.getOd_id() + " 구매 지급 포인트 : " + order.getOd_point());
		point.setPo_num(order.getOd_point());
		
		orderDao.setPointBuy(point);
		orderDao.updateMemberPoint(point);
		
		DepositVO deposit = new DepositVO();
		deposit.setDe_content("매수 : " + order.getOd_qty() + "주");
		deposit.setDe_num(totalPrice);
		deposit.setMb_id(order.getMb_id());
		deposit.setDe_before_num(member.getDeposit());
		deposit.setDe_stock_code(order.getOd_st_code());
		
		orderDao.stockQty(totalQty, order.getOd_st_code());
		
		AccountVO ac = depositDao.getAccount(member.getMb_no());
		
		depositDao.updateAccountDeposit(ac, totalPrice);
		depositDao.insertDepositLog(deposit);
	}

	public void reservationSellOrder(OrderVO order) {
		
		int totalPrice = order.getOd_price();
		int totalQty = order.getOd_qty();
		
		MemberVO member = memberDao.findById(order.getMb_id());
		
		DepositVO deposit = new DepositVO();
		AccountVO ac = depositDao.getAccount(member.getMb_no());
		
		String od_id = depositService.getOrderId("order");
		
		if(!od_id.equals(order.getOd_id())) {
			order.setOd_id(od_id);
		}
		
		orderDao.insertOrder(order);
		
		deposit.setDe_content("매도 : " + order.getOd_qty() + "주");
		deposit.setDe_num(totalPrice);
		deposit.setMb_id(order.getMb_id());
		deposit.setDe_before_num(member.getDeposit());
		deposit.setDe_stock_code(order.getOd_st_code());
		
		orderDao.stockQty(totalQty, order.getOd_st_code());

		depositDao.updateAccountDeposit(ac, totalPrice);
		depositDao.insertDepositLog(deposit);
		
	}

	public void updateReservation(StockPriceVO stockPrice) {
		String st_code = stockPrice.getSt_code();
		StockVO stock = stockDao.getStockCompany(st_code);
		int nowPrice = stockPrice.getSi_price();
		List<ReservationVO> list = stockDao.getReservation(st_code);
		if(list != null) {
			AdminVO config = adminDao.selectAdmin();
			for(ReservationVO tmp : list) {
				String state = tmp.getRe_state(); 
				int wantQty = tmp.getRe_qty();
				int wantPrice = tmp.getRe_qty();
				if(nowPrice == wantPrice) {
					//수수료 포함 금액으로 구해줄게요.
					int totalPrice = wantQty * nowPrice;
					int percentPrice = totalPrice * config.getCf_percent() / 100;
					//일단 회원정보를 가져올게요 여기에는 예치금 잔금도 남아있어요.
					MemberVO mb = memberDao.findById(tmp.getMb_id());
					int mbDeposit = mb.getDeposit();
					
					if(state.contains("매수")) { //구매이면
						
						//구매시 줄 포인트 리턴
						int point = totalPrice * config.getCf_od_point() / 100;
						
						totalPrice =+ percentPrice;
						if(totalPrice > mbDeposit) {
							tmp.setRe_state("매수실패");
							orderDao.updateReservation(tmp);
							continue;
						} else {
							//구매로직 넣기
							tmp.setRe_state("매수완료");
							orderDao.updateReservation(tmp);
							
							OrderVO orders = new OrderVO();
							orders.setOd_id(depositService.getOrderId("order"));
							orders.setOd_name(mb.getMb_name());
							orders.setMb_id(mb.getMb_id());
							orders.setOd_price(totalPrice);
							orders.setOd_point(point);
							orders.setOd_status("매수완료");
							orders.setOd_st_code(st_code);
							orders.setOd_st_name(stock.getSt_name());
							orders.setOd_qty(wantQty);
							orders.setOd_st_price(nowPrice);
							orders.setOd_percent_price(percentPrice);
							
							reservationBuyOrder(orders);
						}
					} else if(state.contains("매도")) { //판매이면
						totalPrice =- percentPrice;
						int haveQty = orderDao.totalMyStock(st_code, mb.getMb_id()).getStocksQty();
						if(wantQty > haveQty) {
							tmp.setRe_state("매도실패");
							orderDao.updateReservation(tmp);
							continue;
						} else {
							//판매로직 넣기
							tmp.setRe_state("매도완료");
							orderDao.updateReservation(tmp);
							
							OrderVO orders = new OrderVO();
							orders.setOd_id(depositService.getOrderId("order"));
							orders.setOd_name(mb.getMb_name());
							orders.setMb_id(mb.getMb_id());
							orders.setOd_price(totalPrice);
							orders.setOd_point(0);
							orders.setOd_status("매도완료");
							orders.setOd_st_code(st_code);
							orders.setOd_st_name(stock.getSt_name());
							orders.setOd_qty(wantQty);
							orders.setOd_st_price(nowPrice);
							orders.setOd_percent_price(percentPrice);
							
							reservationSellOrder(orders);
						}
					}
				} else {
					continue;
				}
			}
		}
	}
}
