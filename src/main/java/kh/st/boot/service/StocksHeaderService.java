package kh.st.boot.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import kh.st.boot.dao.StockDAO;
import kh.st.boot.model.dto.MyStocksDTO;
import kh.st.boot.model.vo.ReservationVO;
import kh.st.boot.model.vo.StockPriceVO;
import kh.st.boot.model.vo.StockVO;
import kh.st.boot.model.vo.WishVO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor	
public class StocksHeaderService {
	private StockDAO stockDao;
	
	private StockService stockService;
	private OrderService orderService;
	private MyAccountService myAccountService;

	public WishVO wishCheck(String st_code, String mb_id) {
		return stockDao.wishCheck(st_code, mb_id);
	}

	public boolean wishDelete(String st_code, String mb_id) {
		return stockDao.wishStockDelete(st_code, mb_id);
	}

	public boolean wishInsert(String st_code, String mb_id) {
		return stockDao.wishStockInsert(st_code, mb_id);
	}

	public StockPriceVO getStockPriceLater(String st_code) {
		return stockDao.getStockPriceLater(st_code);
	}
	
	public String priceTextChange(double amount) {
		String price_text = null;
		if (amount >= 1_000_000_000_000L) { // 1조 이상
		    double trillion = amount / 1_000_000_000_000L; // 조 단위로 변환
		    price_text = String.format("%.1f조원", trillion); // 소수점 첫째 자리까지 표시
		} else if (amount >= 1_000_000_000) { // 1억 이상
		    double hundredMillion = amount / 1_000_000_000; // 억 단위로 변환
		    price_text = String.format("%.1f억원", hundredMillion); // 소수점 첫째 자리까지 표시
		} else if (amount >= 1_000) { // 1천 이상
		    double thousand = amount / 1_000; // 천 단위로 변환
		    price_text = String.format("%.1f천원", thousand); // 소수점 첫째 자리까지 표시
		} else { // 그 이하
		    price_text = String.format("%.0f원", amount); // 원 단위로 표시
		}
		return price_text;
	}

	public Model getModelSet(Model model, Principal principal, String st_code) {
		
		StockPriceVO today = getStockPriceLater(st_code);
		if (today != null) {
			double amount = Double.parseDouble(today.getSi_mrkTotAmt());
			String price_text = priceTextChange(amount);
			today.setPrice_text(price_text);
		}
		
		String mb_id = null;
		
		WishVO wish = null;
		
		if(principal != null) {
			mb_id = principal.getName();
			wish = wishCheck(st_code, mb_id);
		}  
		
		StockVO stock = stockService.getCompanyOne(st_code);
		
		List<ReservationVO> reservation = null;
		MyStocksDTO myStocks = new MyStocksDTO();
		int deposit = 0;
		
		if(mb_id != null) {
			deposit = myAccountService.getAccountAmt(mb_id).getAc_deposit();
			reservation = orderService.getReservation(st_code, mb_id);
			myStocks = orderService.totalMyStock(st_code, mb_id);
		}
		
		model.addAttribute("reservation", reservation);
		model.addAttribute("deposit", deposit);
		model.addAttribute("myStocks", myStocks);
		
		model.addAttribute("stock", stock);
		model.addAttribute("today", today);
		model.addAttribute("mb_id", mb_id);
		model.addAttribute("wish", wish);
		
		return model;
	}
}
