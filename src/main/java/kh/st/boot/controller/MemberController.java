package kh.st.boot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kh.st.boot.model.dto.JoinDTO;
import kh.st.boot.model.dto.LoginDTO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {
	
    @Autowired
	private MemberService memberService;
	
    @Value("${kakao.client_id}")
    private String kakao_client_id;

    @Value("${kakao.redirect_uri}")
    private String kakao_redirect_uri;

    @Value("${naver.client_id}")
    private String naver_client_Id;
    
    @Value("${naver.client_secret}")
    private String naver_client_secret;

    @Value("${naver.redirect_uri}")
    private String naver_redirect_uri;
    

	//로그인
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model mo){
    	
        String kakao_location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+kakao_client_id+"&redirect_uri="+kakao_redirect_uri;
        String naver_location = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="+naver_client_Id+"&state=indeX101010&redirect_uri="+ naver_redirect_uri;
        
        mo.addAttribute("kakao_location", kakao_location);
        mo.addAttribute("naver_location", naver_location);

    	String uri = request.getHeader("Referer");
	    if (uri != null && !uri.contains("/member/login")) {
	        request.getSession().setAttribute("prevPage", uri);
	    }
        //화면 미구현
        return "member/login";
    }
    //로그인post
    @PostMapping("/login")
    public String login_post(Model mo, LoginDTO user_){
    	//화면에서 id, pw, re(자동로그인 여부 => on, null로 값이 전달됨) 가져옴
        //받은 정보를 DB에서 있는지 없는지 확인 함 
        MemberVO user = memberService.login(user_);

        if (user == null) {
             //실패
        	 mo.addAttribute("msg", "로그인에 실패했습니다.\n다시 로그인하세요.");
             mo.addAttribute("url", "/member/login");//다시 로그인 하세용
             return "util/msg";
        } else {
            // 성공
            //user_가 on 값을 가져온 경우 *(null일때 오류가 난다면 수정해 주어야 할)
            if (user_.getRe() != null) {
                user.setAuto_login(true); //자동로그인 하겠습니다.
            } else {
                user.setAuto_login(false); //자동로그인 안하겠습니다.
            }
            
            //postHandle에서 사용하기 위해 mo에 user 저장 (자동로그인을 위한 re값이 처리되어 있습니다.)
            mo.addAttribute("user", user);
            //성공
            return "redirect:/";
        }
    }
    
    //로그아웃
    @GetMapping("/logout")
    public String logout(Model mo, HttpSession session, HttpServletResponse response){

        //세션에서 user 가져옵니다.
        MemberVO user = (MemberVO)session.getAttribute("user");

        //로그인상태가 아닐 시
        if (user == null) {
            mo.addAttribute("msg", "로그인 상태가 아닙니다.");
            mo.addAttribute("url", "/");
            return "util/msg";
        }

        //로그인 쿠키가 있을 경우를 대비해서
        if (user != null) {
            //DB user cookie 정보를 null로 변경
            user.setMb_cookie(null);
            user.setMb_cookie_limit(null);
            memberService.setUserCookie(user);

            // AUTO_LOGIN 쿠키 삭제
            deleteCookie(response , "AUTO_LOGIN");
        }

        //서버의 세션에서 user정보를 삭제
        session.removeAttribute("user");
        mo.addAttribute("msg", "로그아웃 되었습니다.");
        mo.addAttribute("url", "/");
        return "util/msg";
    }

    //회원가입
    @GetMapping("/join")
    public String join(Model mo){

        String info = "internal";
        mo.addAttribute("where", info);
    	return "member/join";
    }
    
    
    @PostMapping("/join")
    public String join_post(Model mo, JoinDTO user_, String ec) {

        Boolean res = false;
        
        //이메일 체크가 되었으면 t 아니면 f
        if (ec.equals("t")) {
            res = memberService.join(user_);//if 안에 있어야 함 
        }

        if (res) {
             //회원가입이 성공일 시
        	 mo.addAttribute("msg", user_.getNick() + "님, 회원이 되신것을 축하합니다\n로그인 페이지로 이동합니다.");
             mo.addAttribute("url", "/member/login");
             return "util/msg";
        } else {
            //회원가입이 실패일 시
        	mo.addAttribute("msg", "회원가입에 실패했습니다\n다시 시도해주세요.");
            mo.addAttribute("url", "/member/join");
            return "util/msg";
        }

    }

    //쿠키 사용할 일이 많아지면 Cookie Manager class를 만들어서 사용하자
    public void deleteCookie(HttpServletResponse response, String cookie_name){
        //받아온 쿠키이름을 가진 쿠키를 값 null이 들어간 상태로 생성
        Cookie cookie  = new Cookie(cookie_name, null);
        //생성한 쿠키의 기간을 0으로 설정
        cookie.setMaxAge(0);
        //화면에 쿠키를 저장(기존의 쿠키와 같은 이름으로) -> 쿠키 값과, 기간이 0으로 되서 삭제됨
        response.addCookie(cookie);
    }

    @GetMapping("/findPwView")
    public String findPw(){


        return "member/findPwView";
    }

    @ResponseBody
    @PostMapping("/find")
    public Map<String, Object> find(@RequestParam  Map<String, String> params){
        Map<String, Object> map = new HashMap<>();
        MemberVO user = memberService.findByEmail(params.get("email"));
        if (user == null) {
            map.put("res", "NotFound");
            return map;
        }
        if(params.get("findType").equals("findId")){
            map.put("userId", user.getMb_id());
            map.put("res", "sendId");
            return map;
        }
        if(params.get("findType").equals("findPassword")){
            map.put("res","sendPw");
            return map;
        }
        map.put("res", "error");
        return map;
    }

    @ResponseBody
    @PostMapping("/ajax/idCheck")
    public Map<String, Object> idCheck(@RequestParam  Map<String, String> params){
        Map<String, Object> map = new HashMap<>();
        MemberVO user = memberService.findById(params.get("id"));

        if (user == null) {
            map.put("res", true);
        } else {
            map.put("res", false);
        }
        return map;
    }


}
