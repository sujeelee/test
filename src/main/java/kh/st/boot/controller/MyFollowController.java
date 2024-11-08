package kh.st.boot.controller;

import java.security.Principal;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.st.boot.model.dto.FollowInfoDTO;
import kh.st.boot.model.vo.FollowVO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.PageMaker;
import kh.st.boot.service.MyFollowService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/myaccount/follow")
public class MyFollowController {
	
	private MyFollowService myFollowService;
	
	@GetMapping("")
	public String followList(Model model, Principal principal, Criteria cri) {
		//로그인상태가 아닐 시
		String mb_id = null;
        if (principal == null) {
        	model.addAttribute("msg", "회원만 이용가능합니다.\n로그인 페이지로 이동합니다.");
        	model.addAttribute("url", "/member/login");
        	
            return "util/msg";
        }
        PageMaker pm = myFollowService.getPageMaker("list", cri, null);
        mb_id = principal.getName();
        
        List<FollowVO> list = myFollowService.getFollowList(mb_id, cri);
        
        model.addAttribute("follow", list);
        model.addAttribute("pm", pm); // 페이지 정보 추가
		return "myaccount/follow";
	}
	
	@GetMapping("/views/{fo_id}")
	public String followViews(Model model, Principal principal, Criteria cri, @PathVariable String fo_id) {
		//로그인상태가 아닐 시
		String mb_id = null;
        if (principal == null) {
        	model.addAttribute("msg", "회원만 이용가능합니다.\n로그인 페이지로 이동합니다.");
        	model.addAttribute("url", "/member/login");
        	
            return "util/msg";
        }
        
        PageMaker pm = myFollowService.getPageMaker("view", cri, fo_id);
        mb_id = principal.getName();
        
        List<FollowVO> list = myFollowService.getFollowViews(fo_id, cri);
        FollowInfoDTO foInfo = myFollowService.getFollowInfo(fo_id, mb_id);
        
        model.addAttribute("foInfo", foInfo);
        model.addAttribute("views", list);
        model.addAttribute("fo_mb_id", fo_id);
        model.addAttribute("pm", pm); // 페이지 정보 추가
		return "myaccount/followView";
	}
	
	@PostMapping("/unfollow")
	@ResponseBody
	public Map<String, String> unfollow(Model model, @RequestParam String fo_no, HttpServletRequest req, HttpServletResponse res) {
		
		Map<String, String> result = new HashMap<String, String>();
		
		MemberVO member = (MemberVO) model.getAttribute("member");
		String mb_id = member.getMb_id();
		
		if(mb_id == null) {
			result.put("res", "fail");
			result.put("msg", "로그인한 회원만 이용가능합니다.");
			return result;
		}
		
		boolean boRes = false;
		
		boRes = myFollowService.unfollow(fo_no, mb_id);
		
		if(boRes) {
			result.put("res", "success");
			result.put("msg", "언팔로우 되었습니다.");
		} else {
			result.put("res", "fail");
			result.put("msg", "언팔로우를 처리하지 못했습니다.");
		}
		
		return result;
	}
	
	@PostMapping("/follow")
	@ResponseBody
	public Map<String, String> follow(Model model, @RequestParam String fo_no, HttpServletRequest req, HttpServletResponse res) {
		
		Map<String, String> result = new HashMap<String, String>();
		
		MemberVO member = (MemberVO) model.getAttribute("member");
		String mb_id = member.getMb_id();
		
		if(mb_id == null) {
			result.put("res", "fail");
			result.put("msg", "로그인한 회원만 이용가능합니다.");
			return result;
		}
		
		boolean boRes = false;
		
		boRes = myFollowService.follow(fo_no, mb_id);
		
		if(boRes) {
			result.put("res", "success");
			result.put("msg", "팔로우 되었습니다.");
		} else {
			result.put("res", "fail");
			result.put("msg", "팔로우를 처리하지 못했습니다.");
		}
		
		return result;
	}

}
