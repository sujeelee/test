package kh.st.boot.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kh.st.boot.model.dto.MyStocksDTO;
import kh.st.boot.model.vo.ReservationVO;
import kh.st.boot.model.vo.StockPriceVO;
import kh.st.boot.model.vo.StockVO;
import kh.st.boot.model.vo.WishVO;
import kh.st.boot.pagination.PageMaker;
import kh.st.boot.pagination.StockCriteria;
import kh.st.boot.service.MyAccountService;
import kh.st.boot.service.OrderService;
import kh.st.boot.service.StockService;
import kh.st.boot.service.StocksHeaderService;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
@RequestMapping("/stock")
public class StockController {
	
	private StockService stockService;
	private StocksHeaderService stocksHeaderService;//v
	private OrderService orderService;
	private MyAccountService myAccountService;
	
	@GetMapping(value = {"/stockList/{type}", "/stockList/{type}/{mrk}", "/stockList"})
	public String stockList(Model model, Principal principal, StockCriteria cri, @PathVariable(required = false) String type, @PathVariable(required = false) String mrk) {
		
		String mb_id = null;
		
		if(principal != null) {
			mb_id = principal.getName();
		}

		String typeText = null;
		
		if(type != null && !type.equals("all")) {
			List<String> types = new ArrayList<String>();
			types.add(type);
			cri.setSt_type(types);
			if(type.equals("KOSPI")) {
				typeText = "코스피 시장만 보기";
			} else if(type.equals("KOSDAQ")) {
				typeText = "코스닥 시장만 보기";
			}
			
		}
		String mrkText = null;
		if(mrk != null) {
			cri.setMrk(mrk);
			switch (mrk) {
			case "medium":
				mrkText = "중형주(3,000억원 ~ 1조원 미만)";
			break;
			case "large":
				mrkText = "대형주(1조원 이상)";
			break;
			default:
				mrkText = "소형주(3,000억원 미만)";
				break;
			}
		}
		
		List<StockVO> list = stockService.getCompanyList("", cri);
		PageMaker pm = stockService.getPageMaker(cri);
		
		int total = pm.getTotalCount();
		
		List<StockPriceVO> priceList = new ArrayList<StockPriceVO>();
		String price_text = "";
		for(StockVO tmp : list) {
			tmp.setWish("N");
			StockPriceVO price = stockService.getStockPrice(null, tmp.getSt_code());
			double amount = Double.parseDouble(price.getSi_mrkTotAmt()); // 문자열을 Double로 변환
			price_text = stocksHeaderService.priceTextChange(amount);
			price.setPrice_text(price_text);
			priceList.add(price);
			
			//회원일 때 주식을 찜했는지 체크해볼겟
			if(mb_id != null || mb_id != "") {
				WishVO wish = stocksHeaderService.wishCheck(tmp.getSt_code(), mb_id);
				if(wish != null) {
					tmp.setWish("Y");
				}
			}
		}
		model.addAttribute("list", list);
		model.addAttribute("prices", priceList);
		model.addAttribute("pm", pm); // 페이지 정보 추가
		model.addAttribute("total", total);
		model.addAttribute("type", type);
		model.addAttribute("mrk", mrk);
		model.addAttribute("typeText", typeText);
		model.addAttribute("mrkText", mrkText);
		model.addAttribute("mb_id", mb_id);
		
		return "stockuser/stockList";
	}
	

	@GetMapping("/{st_code}")
	public String stockDetail(Model model, Principal principal, @PathVariable String st_code) {
		
		List<StockPriceVO> list = stockService.getStockInfoListDate(st_code, "all");
		
		model = stocksHeaderService.getModelSet(model, principal, st_code); //v
		
		String mb_id = null;
		int deposit = 0;
		if(principal != null) {
			mb_id = principal.getName();
			deposit = myAccountService.getAccountAmt(mb_id).getAc_deposit();
		}
		
		
		List<ReservationVO> reservation = null;
		MyStocksDTO myStocks = new MyStocksDTO();
		
		if(mb_id != null) {
			reservation = orderService.getReservation(st_code, mb_id);
			myStocks = orderService.totalMyStock(st_code, mb_id);
		}
		
		model.addAttribute("list", list);
		model.addAttribute("reservation", reservation);
		model.addAttribute("deposit", deposit);
		model.addAttribute("myStocks", myStocks);
		return "stockuser/detail";
	}
	
	@PostMapping("/graph/{st_code}")
	@ResponseBody
	public Map<String, Object> stockDetailDate(@PathVariable String st_code, @RequestParam String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<StockPriceVO> list = stockService.getStockInfoListDate(st_code, type);
		
		map.put("list", list);
		
		return map;
	}
	
	@PostMapping("orderupdate")
	public String stockOrder(@RequestParam Map<String, Object> form, Model model, Principal principal) {
		String st_code = (String) form.get("od_st_code");
		//로그인상태가 아닐 시
        if (principal == null) {
        	model.addAttribute("msg", "회원만 이용가능합니다.\n로그인 페이지로 이동합니다.");
        	model.addAttribute("url", "/member/login");
        	
            return "util/msg";
        }
		String result = orderService.orderUpdate(form, model);
		String msg = "정상적으로 처리되지 않았습니다.";
		String url = "/stock/" + st_code;
		if(!result.equals("실패")) {
			msg = result + "처리 되었습니다.";
		}
		
		model.addAttribute("msg", msg);
    	model.addAttribute("url", url);
    	
        return "util/msg";
	}
	
	@PostMapping("/delete/{st_code}")
	@ResponseBody
	public boolean reservationDelete(@PathVariable String st_code, @RequestParam String re_no) {
		return orderService.deleteReservation(st_code, re_no);
	}
}
