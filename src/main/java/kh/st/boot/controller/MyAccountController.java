package kh.st.boot.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kh.st.boot.model.dto.MyAccountStocksDTO;
import kh.st.boot.model.util.DateUtil;
import kh.st.boot.model.vo.AccountVO;
import kh.st.boot.model.vo.DepositVO;
import kh.st.boot.model.vo.MemberApproveVO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.model.vo.OrderVO;
import kh.st.boot.model.vo.PointVO;
import kh.st.boot.model.vo.StockAddVO;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.PageMaker;
import kh.st.boot.pagination.TransCriteria;
import kh.st.boot.service.MemberService;
import kh.st.boot.service.MyAccountService;
import kh.st.boot.service.NewsService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/myaccount")
public class MyAccountController {
	
	private MyAccountService myAccountService;
	private MemberService memberService;
	private NewsService newsService;
	
	@GetMapping("/asset")
	public String asset(Model model, Principal principal) {
		//로그인상태가 아닐 시
        if (principal == null) {
        	model.addAttribute("msg", "회원만 이용가능합니다.\n로그인 페이지로 이동합니다.");
        	model.addAttribute("url", "/member/login");
        	
            return "util/msg";
        }
		String mb_id = principal.getName();
		// 회원 아이디로 예치금 잔액을 가져오는 코드
		AccountVO account = myAccountService.getAccountById(mb_id);
		String lastWeek = DateUtil.getLastWeek(); // 지난주 날짜 정보
		// 일주일 전까지의 거래 내역을 가져옴
		List<DepositVO> depositList = myAccountService.getDepositListByDate(mb_id, lastWeek);
		int money = 0;			// 얻은 이익
		int rateOfReturn = 0;	// 수익률
		List<Map<String, Object>> graphData = new ArrayList<>();
		if(depositList != null) {
			for(DepositVO deposit : depositList) {
				money += deposit.getDe_num();
				// 각 거래의 날짜와 금액을 graphData에 추가
		        Map<String, Object> graphItem = new HashMap<>();
		        graphItem.put("date", deposit.getDe_datetime());	// 거래 날짜
		        graphItem.put("amount", deposit.getDe_num());		// 거래 금액
		        graphData.add(graphItem);
	
			}
			int begin = account.getAc_deposit() - money; // 초기 금액
			if (begin != 0) {
			    rateOfReturn = (money * 100) / begin;
			}
		}
		// 구매 가능 금액 account.ac_deposit
		// 투자 중인 금액
		int stockMoney = 0; // 투자중인 금액
		List<MyAccountStocksDTO> myStockList = myAccountService.getMyStockList(mb_id);
		for(MyAccountStocksDTO tmp : myStockList) {
			if(myStockList != null) {
				stockMoney += tmp.getStockOrignPrice();
			}
		}
		// 월 수익
		Date now = new Date();
		int buyAmount = 0, sellAmount = 0;
        String today = new SimpleDateFormat("yyyy-MM").format(now);
        List<OrderVO> buyList = myAccountService.getOrderListByBuyDate(mb_id, today);
        List<OrderVO> sellList = myAccountService.getOrderListBySellDate(mb_id, today);
        if(buyList != null && buyList.size() != 0) {
	        for(OrderVO order : buyList) {
	        	buyAmount += order.getOd_price();
	        }
        }
        if(sellList != null && sellList.size() != 0) {
        	for(OrderVO order : sellList) {
        		sellAmount += order.getOd_price();
        	}
        }
        int proceeds = sellAmount - buyAmount;
        // 전체 수익
        List<OrderVO> buyListAll = myAccountService.getOrderListByBuy(mb_id);
        List<OrderVO> sellListAll = myAccountService.getOrderListBySell(mb_id);
        int buyAmountAll = 0;
        int sellAmountAll = 0;
        if(buyListAll != null && buyListAll.size() != 0) {
	        for(OrderVO order : buyListAll) {
	        	buyAmountAll += order.getOd_price();
	        }
        }
        if(sellListAll != null && sellListAll.size() != 0) {
        	for(OrderVO order : sellListAll) {
        		sellAmountAll += order.getOd_price();
        	}
        }
        // 수익 금액
        int proceedsAll = sellAmountAll - buyAmountAll;
        // 포인트
		MemberVO user = memberService.findById(mb_id);
		int point = user.getMb_point();
		model.addAttribute("money", money);
		model.addAttribute("account", account);
		model.addAttribute("rateOfReturn", rateOfReturn);
		model.addAttribute("graphData", graphData);
		model.addAttribute("stockMoney", stockMoney);
		model.addAttribute("proceeds", proceeds);
		model.addAttribute("proceedsAll", proceedsAll);
		model.addAttribute("point", point);
		return "myaccount/asset";
	}
	
	@ResponseBody
	@PostMapping("/asset/period")
	public Map<String, Object> period(@RequestParam String period, Principal principal){
		Map<String, Object> map = new HashMap<String, Object>();
		String mb_id = principal.getName();
		String date = "";
		String text = "";
		switch(period) {
		case "1week":
			// 지난주 날짜 
			date = DateUtil.getLastWeek();
			text = "지난주";
			break;
		case "1month":
			// 지난달 날짜
			date = DateUtil.getLastMonth();
			text = "지난달";
			break;
		case "3month":
			// 3달전 날짜
			date = DateUtil.getLast3Month();
			text = "3달전";
			break;
		case "1year":
			// 1년전 날짜
			date = DateUtil.getLastYear();
			text = "1년전";
			break;
		}
		// 회원 아이디로 예치금 잔액을 가져오는 코드
		AccountVO account = myAccountService.getAccountById(mb_id);
		// 해당 기간들의 거래내역들을 가져옴
		List<DepositVO> depositList = myAccountService.getDepositListByDate(mb_id, date);
		int money = 0;			// 얻은 이익
		int rateOfReturn = 0;	// 수익률
		List<Map<String, Object>> graphData = new ArrayList<>();
		if(depositList != null) {
			for(DepositVO deposit : depositList) {
				money += deposit.getDe_num();
				// 각 거래의 날짜와 금액을 graphData에 추가
		        Map<String, Object> graphItem = new HashMap<>();
		        graphItem.put("date", deposit.getDe_datetime());	// 거래 날짜
		        graphItem.put("amount", deposit.getDe_num());		// 거래 금액
		        graphData.add(graphItem);
	
			}
			int begin = account.getAc_deposit() - money;	// 초기 금액
			if (begin != 0) {
			    rateOfReturn = (money * 100) / begin;
			}
		}
		map.put("text", text);
		map.put("money", money);
		map.put("rateOfReturn", rateOfReturn);
		map.put("graphData", graphData);  // 그래프에 사용할 데이터 추가
		return map;
	}
	
	@GetMapping("/settings")
	public String settings(Model model, Principal principal) {
		//로그인상태가 아닐 시
        if (principal == null) {
        	model.addAttribute("msg", "회원만 이용가능합니다.\n로그인 페이지로 이동합니다.");
        	model.addAttribute("url", "/member/login");
            return "util/msg";
        }
        MemberVO user = memberService.findById(principal.getName());
        model.addAttribute("user", user);
		return "myaccount/settings";
	}
	
	@PostMapping("/settings")
	public String settingsPost(Model model, Principal principal, MemberApproveVO mp) {
		if(myAccountService.getMemberApprove(mp.getMb_no()) == null) {
			myAccountService.insertMemberApprove(mp);
		}else {
			if(myAccountService.deleteMemberApprove(mp.getMb_no())) {
				myAccountService.insertMemberApprove(mp);
			}
		}
        MemberVO user = memberService.findById(principal.getName());
        model.addAttribute("user", user);
		return "myaccount/settings";
	}
	
	@ResponseBody
	@PostMapping("/settings/list")
	public Map<String, Object> settingsList(String mp_type){
		Map<String, Object> map = new HashMap<String, Object>();
		List<?> list = null;
		switch(mp_type) {
		case "news":
			list = newsService.getNewsPaperList(); 
			map.put("list", list);
			break;
		case "stock":
			list = myAccountService.getStockList();
			map.put("list", list);
			break;
		}
		return map;
	}
	
	@GetMapping("/password")
	public String password() {
		return "myaccount/password";
	}
	
	@PostMapping("/password")
	@ResponseBody
	public Map<String, Object> passwordPost(Model model, Principal principal, String password, MemberVO member, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		MemberVO user = memberService.findById(principal.getName());
		boolean res = myAccountService.checkPw(user, password);
	    if(res) {
	    	if(myAccountService.updatePw(principal.getName(), member.getMb_password())) {
	    		map.put("success", true);
	    		session.removeAttribute("user");
	    	}else {
	    		map.put("success", false);
	    	}
	    } else {
	        map.put("success", false);
	    }
	    return map;
	}
	
	@GetMapping("/delete")
	public String delete() {
		return "myaccount/delete";
	}
	
	@PostMapping("/delete")
	@ResponseBody
	public Map<String, Object> deletePost(Model model, Principal principal, MemberVO member) {
		Map<String, Object> map = new HashMap<String, Object>();
		MemberVO user = memberService.findById(principal.getName());
		boolean res = myAccountService.checkPw(user, member.getMb_password());
		if(user.getMb_id().equals(member.getMb_id()) && res) {
			if(myAccountService.deleteUser(user)) {
				map.put("success", true);
			}else {
				map.put("success", false);
			}
		}else {
			map.put("success", false);
		}
	    return map;
	}
	
	@ResponseBody
	@PostMapping("/checkStatus")
	public Map<String, Object> checkStatus(Principal principal){
		Map<String, Object> map = new HashMap<String, Object>();
		MemberVO user = memberService.findById(principal.getName());
		MemberApproveVO mp = myAccountService.getMemberApprove(user.getMb_no());
		if(mp == null) {
			map.put("status", "none");
		}
		else if(mp.getMp_yn() == null) {
			if(mp.getMp_type().equals("news")) {
				map.put("status", "news");
			}
			else if(mp.getMp_type().equals("stock")) {
				map.put("status", "stock");
			}
		}
		else if(mp.getMp_yn().equals("y")) {
			map.put("status", "success");
			if(mp.getMp_type().equals("stock")) {
				mp.setMp_company(myAccountService.getStockName(mp.getMp_company()));
			}
			map.put("mp", mp);
		}
		else if(mp.getMp_yn().equals("n")) {
			map.put("status", "fail");
		}
		return map;
	}
	
	@ResponseBody
	@PostMapping("/deleteMember")
	public ResponseEntity<Map<String, Object>> deleteMember(Principal principal){
		Map<String, Object> map = new HashMap<String, Object>();
		String mb_id = principal.getName();
		MemberVO user = memberService.findById(mb_id);
		String status = myAccountService.getMemberStatus(user.getMb_no(), user.getMb_id());
		boolean res = myAccountService.deleteMemberApprove(user.getMb_no());
		if(res) {
			if(myAccountService.deleteMemberStatus(user.getMb_no(), status)) {
				map.put("status", true);
				return ResponseEntity.ok(map);
			}else {
				map.put("status", false);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
			}
		}else {
			map.put("status", false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
	}
	
	@ResponseBody
	@PostMapping("/cancel")
	public ResponseEntity<Map<String, Object>> cancel(Principal principal){
		Map<String, Object> map = new HashMap<String, Object>();
		String mb_id = principal.getName();
		MemberVO user = memberService.findById(mb_id);
		boolean res = myAccountService.deleteMemberApprove(user.getMb_no());
		if(res) {
			map.put("status", true);
			return ResponseEntity.ok(map);
		}else {
			map.put("status", false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		
	}
	
	@ResponseBody
	@PostMapping("/applyStock")
	public boolean applyStock(Principal principal, @RequestParam int sa_qty, @RequestParam String sa_content){
		String mb_id = principal.getName();
		boolean res = myAccountService.insertStockAdd(mb_id, sa_qty, sa_content);
		return res;
	}
	
	@GetMapping("/stockList")
	public String stockList(Model model, Principal principal, Criteria cri) {
		String mb_id = principal.getName();
		cri.setPerPageNum(5);
		List<StockAddVO> list = myAccountService.getStockAddList(mb_id, cri);
		PageMaker pm = myAccountService.SelectPageMaker(cri, mb_id);
		model.addAttribute("pm", pm);
		model.addAttribute("list", list);
		return "myaccount/stockList";
	}
	
    @GetMapping("/company")
    public String openCompany() {
        return "myaccount/company";
    }
	
	@GetMapping("/transactions/{type}")
	public String transactions(Model model, Principal principal, TransCriteria cri, @PathVariable String type) {
		if(principal == null) {
			model.addAttribute("msg", "회원만 이용가능합니다.\n로그인 페이지로 이동합니다.");
        	model.addAttribute("url", "/member/login");
        	
            return "util/msg";
		}
		String mb_id = principal.getName();
		cri.setPerPageNum(10); //여기서는 한번에 n개까지 보여줄게요
		// 거래내역을 가져오는 코드
		List<DepositVO> list = myAccountService.getDepositList(mb_id, cri);
		//페이지를 넣게 되
		PageMaker pm = myAccountService.getPageMaker(cri, mb_id);
		//계좌 잔액을 가져오게 되
		AccountVO ac = myAccountService.getAccountAmt(mb_id);
		
		for(DepositVO tmps : list) {
			
			String content_view = myAccountService.setContentView(tmps);
			
			tmps.setContent_view(content_view);
			tmps.setDe_content(tmps.getDe_content().trim().split(" :")[0]);
		}
		model.addAttribute("account", ac);
		model.addAttribute("list", list);
		model.addAttribute("type", type);
		model.addAttribute("pm", pm); // 페이지 정보 추가
		
		return "myaccount/transactions";
	}
	
	@GetMapping("/transactions/{type}/{detail}")
	public String transactionsAlpha(Model model, Principal principal, TransCriteria cri, @PathVariable String type, @PathVariable String detail) {
		if(principal == null) {
			model.addAttribute("msg", "회원만 이용가능합니다.\n로그인 페이지로 이동합니다.");
        	model.addAttribute("url", "/member/login");
        	
            return "util/msg";
		}
		String mb_id = principal.getName();
		cri.setPerPageNum(10); //여기서는 한번에 n개까지 보여줄게요
		// 거래내역을 가져오는 코드
		List<DepositVO> list = myAccountService.getDepositList(mb_id, cri);
		//페이지를 넣게 되
		PageMaker pm = myAccountService.getPageMaker(cri, mb_id);
		//계좌 잔액을 가져오게 되
		AccountVO ac = myAccountService.getAccountAmt(mb_id);
		
		for(DepositVO tmps : list) {
			String content_view = myAccountService.setContentView(tmps);;
			tmps.setContent_view(content_view);
			tmps.setDe_content(tmps.getDe_content().trim().split(" :")[0]);
		}
		
		model.addAttribute("account", ac);
		model.addAttribute("list", list);
		model.addAttribute("type", type);
		model.addAttribute("detail", detail);
		model.addAttribute("pm", pm); // 페이지 정보 추가
		return "myaccount/transactions";
	}
	
	@GetMapping("/profit")
	public String profit(Model model, Principal principal) {
		//로그인상태가 아닐 시
        if (principal == null) {
        	model.addAttribute("msg", "회원만 이용가능합니다.\n로그인 페이지로 이동합니다.");
        	model.addAttribute("url", "/member/login");
        	
            return "util/msg";
        }
        String mb_id = principal.getName();
        // 주식 구매를 한 거래 내역만 가져옴
        List<OrderVO> buyList = myAccountService.getOrderListByBuy(mb_id);
        // 주식 판매를 한 거래 내역만 가져옴
        List<OrderVO> sellList = myAccountService.getOrderListBySell(mb_id);
        int buyAmount = 0; // 주식 구매의 총 금액
        int sellAmount = 0; // (주식 판매 - 수수료)의 총 금액
        if(buyList != null && buyList.size() != 0) {
	        for(OrderVO order : buyList) {
	        	buyAmount += order.getOd_price();
	        }
        }
        if(sellList != null && sellList.size() != 0) {
        	for(OrderVO order : sellList) {
        		sellAmount += order.getOd_price();
        	}
        }
        // 수익 금액
        int proceeds = sellAmount - buyAmount;
        // 수익률
        int rateOfReturn = 0;
        if(buyAmount != 0) {
        	rateOfReturn = (sellAmount - buyAmount) / buyAmount * 100;
        }
        // 주식 구매/판매 전체 리스트
        List<OrderVO> list = myAccountService.getOrderList(mb_id);
        // 회원가입일
        MemberVO user = memberService.findById(mb_id);
        String mb_datetime = new SimpleDateFormat("yyyy. MM. dd").format(user.getMb_datetime());
        Date d = new Date();
        String now = new SimpleDateFormat("yyyy. MM. dd").format(d); 
        model.addAttribute("proceeds", proceeds);
        model.addAttribute("rateOfReturn", rateOfReturn);
        model.addAttribute("list", list);
        model.addAttribute("mb_datetime", mb_datetime);
        model.addAttribute("now", now);
		return "myaccount/profit";
	}
	
	@ResponseBody
	@PostMapping("/profit/date")
	public Map<String, Object> profitDate(Principal principal, String date){
		Map<String, Object> map = new HashMap<String, Object>();
		String mb_id = principal.getName();
		MemberVO user = memberService.findById(mb_id);
		List<OrderVO> buyList, sellList, list;
		int buyAmount = 0, sellAmount = 0, proceeds = 0, rateOfReturn = 0;
        String now, today;
        Date d = new Date();
		switch(date) {
		case "all":
			// 주식 구매를 한 거래 내역만 가져옴
	        buyList = myAccountService.getOrderListByBuy(mb_id);
	        // 주식 판매를 한 거래 내역만 가져옴
	        sellList = myAccountService.getOrderListBySell(mb_id);
	        if(buyList != null && buyList.size() != 0) {
		        for(OrderVO order : buyList) {
		        	buyAmount += order.getOd_price();
		        }
	        }
	        if(sellList != null && sellList.size() != 0) {
	        	for(OrderVO order : sellList) {
	        		sellAmount += order.getOd_price();
	        	}
	        }
	        proceeds = sellAmount - buyAmount;
	        if(buyAmount != 0) {
	        	rateOfReturn = (sellAmount - buyAmount) / buyAmount * 100;
	        }
	        // 주식 구매/판매 전체 리스트
	        list = myAccountService.getOrderList(mb_id);
	        // 회원가입일
	        String mb_datetime = new SimpleDateFormat("yyyy. MM. dd").format(user.getMb_datetime());
	        now = new SimpleDateFormat("yyyy. MM. dd").format(d); 
	        map.put("list", list);
	        map.put("proceeds", proceeds);
	        map.put("rateOfReturn", rateOfReturn);
	        map.put("mb_datetime", mb_datetime);
	        map.put("now", now);
	        map.put("status", "all");
			break;
		case "day":
	        now = new SimpleDateFormat("yyyy. MM. dd").format(d);
	        today = new SimpleDateFormat("yyyy-MM-dd").format(d);
			// 주식 구매를 한 거래 내역만 가져옴
	        buyList = myAccountService.getOrderListByBuyDate(mb_id, today);
	        // 주식 판매를 한 거래 내역만 가져옴
	        sellList = myAccountService.getOrderListBySellDate(mb_id, today);
	        if(buyList != null && buyList.size() != 0) {
		        for(OrderVO order : buyList) {
		        	buyAmount += order.getOd_price();
		        }
	        }
	        if(sellList != null && sellList.size() != 0) {
	        	for(OrderVO order : sellList) {
	        		sellAmount += order.getOd_price();
	        	}
	        }
	        proceeds = sellAmount - buyAmount;
	        if(buyAmount != 0) {
	        	rateOfReturn = (sellAmount - buyAmount) / buyAmount * 100;
	        }
	        // 주식 구매/판매 전체 리스트
	        list = myAccountService.getOrderListByDate(mb_id, today);
	        map.put("list", list);
	        map.put("proceeds", proceeds);
	        map.put("rateOfReturn", rateOfReturn);
	        map.put("now", now);
	        map.put("status", "day");
			break;
		case "month":
	        now = new SimpleDateFormat("yyyy. MM").format(d); 
	        today = new SimpleDateFormat("yyyy-MM").format(d);
			// 주식 구매를 한 거래 내역만 가져옴
	        buyList = myAccountService.getOrderListByBuyDate(mb_id, today);
	        // 주식 판매를 한 거래 내역만 가져옴
	        sellList = myAccountService.getOrderListBySellDate(mb_id, today);
	        if(buyList != null && buyList.size() != 0) {
		        for(OrderVO order : buyList) {
		        	buyAmount += order.getOd_price();
		        }
	        }
	        if(sellList != null && sellList.size() != 0) {
	        	for(OrderVO order : sellList) {
	        		sellAmount += order.getOd_price();
	        	}
	        }
	        proceeds = sellAmount - buyAmount;
	        if(buyAmount != 0) {
	        	rateOfReturn = (sellAmount - buyAmount) / buyAmount * 100;
	        }
	        // 주식 구매/판매 전체 리스트
	        list = myAccountService.getOrderListByDate(mb_id, today);
	        map.put("list", list);
	        map.put("proceeds", proceeds);
	        map.put("rateOfReturn", rateOfReturn);
	        map.put("now", now);
	        map.put("status", "month");
			break;
		case "year":
	        now = new SimpleDateFormat("yyyy").format(d);
	        today = new SimpleDateFormat("yyyy").format(d);
			// 주식 구매를 한 거래 내역만 가져옴
	        buyList = myAccountService.getOrderListByBuyDate(mb_id, today);
	        // 주식 판매를 한 거래 내역만 가져옴
	        sellList = myAccountService.getOrderListBySellDate(mb_id, today);
	        if(buyList != null && buyList.size() != 0) {
		        for(OrderVO order : buyList) {
		        	buyAmount += order.getOd_price();
		        }
	        }
	        if(sellList != null && sellList.size() != 0) {
	        	for(OrderVO order : sellList) {
	        		sellAmount += order.getOd_price();
	        	}
	        }
	        proceeds = sellAmount - buyAmount;
	        if(buyAmount != 0) {
	        	rateOfReturn = (sellAmount - buyAmount) / buyAmount * 100;
	        }
	        // 주식 구매/판매 전체 리스트
	        list = myAccountService.getOrderListByDate(mb_id, today);
	        map.put("list", list);
	        map.put("proceeds", proceeds);
	        map.put("rateOfReturn", rateOfReturn);
	        map.put("now", now);
	        map.put("status", "year");
			break;
		}
		return map;
	}
	
	@ResponseBody
	@PostMapping("/profit/today")
	public Map<String, Object> profitToday(Date today, String status, Principal principal){
		Map<String, Object> map = new HashMap<String, Object>();
		String mb_id = principal.getName();
		List<OrderVO> buyList, sellList, list;
		int buyAmount = 0, sellAmount = 0, proceeds = 0, rateOfReturn = 0;
		String date;
		switch(status) {
		case "day":
			date = new SimpleDateFormat("yyyy-MM-dd").format(today);
			list = myAccountService.getOrderListByDate(mb_id, date);
			// 주식 구매를 한 거래 내역만 가져옴
	        buyList = myAccountService.getOrderListByBuyDate(mb_id, date);
	        // 주식 판매를 한 거래 내역만 가져옴
	        sellList = myAccountService.getOrderListBySellDate(mb_id, date);
	        if(buyList != null && buyList.size() != 0) {
		        for(OrderVO order : buyList) {
		        	buyAmount += order.getOd_price();
		        }
	        }
	        if(sellList != null && sellList.size() != 0) {
	        	for(OrderVO order : sellList) {
	        		sellAmount += order.getOd_price();
	        	}
	        }
	        proceeds = sellAmount - buyAmount;
	        if(buyAmount != 0) {
	        	rateOfReturn = (sellAmount - buyAmount) / buyAmount * 100;
	        }
			map.put("list", list);
			map.put("proceeds", proceeds);
		    map.put("rateOfReturn", rateOfReturn);
			break;
		case "month":
			date = new SimpleDateFormat("yyyy-MM").format(today);
			list = myAccountService.getOrderListByDate(mb_id, date);
			// 주식 구매를 한 거래 내역만 가져옴
	        buyList = myAccountService.getOrderListByBuyDate(mb_id, date);
	        // 주식 판매를 한 거래 내역만 가져옴
	        sellList = myAccountService.getOrderListBySellDate(mb_id, date);
	        if(buyList != null && buyList.size() != 0) {
		        for(OrderVO order : buyList) {
		        	buyAmount += order.getOd_price();
		        }
	        }
	        if(sellList != null && sellList.size() != 0) {
	        	for(OrderVO order : sellList) {
	        		sellAmount += order.getOd_price();
	        	}
	        }
	        proceeds = sellAmount - buyAmount;
	        if(buyAmount != 0) {
	        	rateOfReturn = (sellAmount - buyAmount) / buyAmount * 100;
	        }
	        map.put("list", list);
			map.put("proceeds", proceeds);
		    map.put("rateOfReturn", rateOfReturn);
			break;
		case "year":
			date = new SimpleDateFormat("yyyy").format(today);
			list = myAccountService.getOrderListByDate(mb_id, date);
			// 주식 구매를 한 거래 내역만 가져옴
	        buyList = myAccountService.getOrderListByBuyDate(mb_id, date);
	        // 주식 판매를 한 거래 내역만 가져옴
	        sellList = myAccountService.getOrderListBySellDate(mb_id, date);
	        if(buyList != null && buyList.size() != 0) {
		        for(OrderVO order : buyList) {
		        	buyAmount += order.getOd_price();
		        }
	        }
	        if(sellList != null && sellList.size() != 0) {
	        	for(OrderVO order : sellList) {
	        		sellAmount += order.getOd_price();
	        	}
	        }
	        proceeds = sellAmount - buyAmount;
	        if(buyAmount != 0) {
	        	rateOfReturn = (sellAmount - buyAmount) / buyAmount * 100;
	        }
	        map.put("list", list);
			map.put("proceeds", proceeds);
		    map.put("rateOfReturn", rateOfReturn);
			break;
		}
		return map;
	}
	
	@GetMapping("/point/{type}")
	public String point(Model model, Principal principal, TransCriteria cri, @PathVariable String type) {
		//로그인상태가 아닐 시
        if (principal == null) {
        	model.addAttribute("msg", "회원만 이용가능합니다.\n로그인 페이지로 이동합니다.");
        	model.addAttribute("url", "/member/login");
            return "util/msg";
        }
        String mb_id = principal.getName();
        cri.setPerPageNum(10);
        PageMaker pm = myAccountService.getPageMakerByPoint(cri, mb_id);
        List<PointVO> list = null;
        switch(type) {
        case "use":
        	list = myAccountService.getPointList(cri, mb_id);
        	break;
        case "get":
        	list = myAccountService.getPointList(cri, mb_id);
        	break;
        }
		MemberVO user = memberService.findById(mb_id);
		int point = user.getMb_point();
        model.addAttribute("type", type);
        model.addAttribute("pm", pm);
        model.addAttribute("list", list);
        model.addAttribute("point", point);
		return "myaccount/point";
	}

}
