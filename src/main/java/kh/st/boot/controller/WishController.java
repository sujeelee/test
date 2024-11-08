package kh.st.boot.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.model.vo.WishVO;
import kh.st.boot.service.StocksHeaderService;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
@RequestMapping("/stock")
public class WishController {
	
	private StocksHeaderService stocksHeaderService;//v
	
	@PostMapping("/wish")
	@ResponseBody
	public Map<String, String> stockWish(Model model, @RequestParam String st_code, @RequestParam String status, Principal principal, HttpServletRequest req, HttpServletResponse res) {
		
		Map<String, String> result = new HashMap<String, String>();
		
		MemberVO member = (MemberVO) model.getAttribute("member");
		String mb_id = member.getMb_id();
		
		if(mb_id == null) {
			result.put("res", "fail");
			result.put("msg", "로그인한 회원만 이용가능합니다.");
			return result;
		}
		
		boolean boRes = false;
		
		WishVO chkWish = stocksHeaderService.wishCheck(st_code, mb_id);
		
		if(chkWish == null) {
			status = "N";
		} else {
			status = "Y";
		}

		status = status.toUpperCase();
		
		
		if(status.equals("Y")) {
			boRes = stocksHeaderService.wishDelete(st_code, mb_id);
		} else {
			boRes =stocksHeaderService.wishInsert(st_code, mb_id);
		}
		
		if(boRes) {
			result.put("res", "success");
			if(status.equals("Y")) {
				result.put("msg", "위시를 정상적으로 삭제했습니다.");
			} else {
				result.put("msg", "위시를 정상적으로 등록했습니다.");
			}
		} else {
			result.put("res", "fail");
			result.put("msg", "위시를 처리하지 못했습니다.");
		}
		
		return result;
	}
}
