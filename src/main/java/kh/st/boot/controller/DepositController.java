package kh.st.boot.controller;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.st.boot.model.dto.AccountChkDTO;
import kh.st.boot.model.vo.DepositOrderVO;
import kh.st.boot.model.vo.SendVO;
import kh.st.boot.service.DepositService;


@Controller
@RequestMapping("/deposit")
public class DepositController {
	@Autowired
	DepositService depositService;
	
	private final String CLIENT_ID = "S1_6eaa0db1afdc41f3becb770878d67d25"; 
    private final String SECRET_KEY = "e80d068e400649a6ada66777fa350d40";
	
    @GetMapping("")
	public String home(Model model, Principal principal) {
        
    	//로그인상태가 아닐 시
        if (principal == null) {
        	model.addAttribute("msg", "회원만 이용가능합니다.\n로그인 페이지로 이동합니다.");
        	model.addAttribute("url", "/member/login");
        	
            return "util/msg";
        }
        
        depositService.deleteStatusStay(principal.getName());
        
        model.addAttribute("clientId", CLIENT_ID);
        
		return "deposit/depositOrder";
	}
    
    @PostMapping("/clientAuth")
    public String main(HttpServletRequest request, Model model){ 
        String resultMsg = request.getParameter("resultMsg");
        String resultCode = request.getParameter("resultCode"); 
        model.addAttribute("resultMsg", resultMsg);

        if (resultCode.equalsIgnoreCase("0000")) {
            // 결제 성공 비즈니스 로직 구현
        	DepositOrderVO upOrder = new DepositOrderVO();
        	
        	upOrder.setDo_auth(request.getParameter("authToken"));
        	upOrder.setDo_tno(request.getParameter("tid"));
        	upOrder.setDo_status("완료");
        	upOrder.setDo_od_id(request.getParameter("orderId"));
        	
        	boolean res = depositService.updateOrder(upOrder, model);
        	
        	if(res == false) {
        		model.addAttribute("msg" , "충전오류가 발생했습니다.");
    			model.addAttribute("url" , "/deposit");
        	} else {
	        	model.addAttribute("msg" , "예치금이 충전되었습니다.");
	        	model.addAttribute("windows", "Y");
        	}
        	
        } else {
        	model.addAttribute("msg" , resultMsg);
			model.addAttribute("url" , "/deposit");
        }
        return "util/msg";
    }
    
    @PostMapping("/insertOrder")
    @ResponseBody
    public String insertOrder(@RequestParam Map<String, Object> params, Model model, Principal principal, HttpServletRequest req, HttpServletResponse res){ 
    	String id = depositService.getOrderId(null);
    	DepositOrderVO newOrder = new DepositOrderVO();
    	// VO 클래스의 필드 목록을 가져옴
        Field[] fields = newOrder.getClass().getDeclaredFields();
        
        for (Field field : fields) {
			// 필드명을 가져옴
			String fieldName = field.getName();
			
			// Map에서 필드명과 일치하는 키가 있는지 확인
			if (params.containsKey(fieldName)) {
			    field.setAccessible(true); // private 필드에도 접근할 수 있게 설정
			try {
					Object val = (String)params.get(fieldName);
					if("do_price".equals(fieldName)) {
						val = Integer.parseInt((String) val);
					}
			    	// Map에서 해당하는 값을 가져와서 VO 필드에 할당
			        field.set(newOrder, val);
			    } catch (IllegalAccessException e) {
			        e.printStackTrace();
			    }
			}
        }
        
        newOrder.setDo_od_id(id);
		newOrder.setDo_status("대기");
		//newOrder.setMb_id(user.getMb_id()); //테스트 아이디 지금 회원가입 안됨 ㅅㅂ 20241010
		newOrder.setMb_id(principal.getName());
		id = depositService.insertOrder(newOrder);
		return id;
    }
    
    @GetMapping("/send")
	public String sendDeposit(Model model, Principal principal) {
        
    	//로그인상태가 아닐 시
        if (principal == null) {
        	model.addAttribute("msg", "회원만 이용가능합니다.\n로그인 페이지로 이동합니다.");
        	model.addAttribute("url", "/member/login");
        	
            return "util/msg";
        }
        
		return "deposit/depositSend";
	}
    
    @PostMapping("/chkAccount")
    @ResponseBody
    public Map<String, Object> chkAccount(@RequestParam String account, Principal principal, HttpServletRequest req, HttpServletResponse res){ 
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	account = "SID-" + account;
    	
    	String mb_id = principal.getName();
    	
    	AccountChkDTO chk = depositService.chkAccount(account);
    	
    	if(chk != null) {
    		if(chk.getMb_id().equals(mb_id)) {
    			result.put("res", "err");
    			result.put("msg", "본인 계좌에는 보내기를 할 수 없어요:(");
    			return result;
    		} else {
    			result.put("res", "success");
    			result.put("msg", "보내기를 사용할 수 있는 계좌에요:)");
    			result.put("account", chk);
    		}
    	} else {
    		result.put("res", "err");
			result.put("msg", "존재하지 않는 계좌에요:(");
    	}
    	
    	return result;
    }
	
    @PostMapping("/sendInsert")
    @ResponseBody
    public Map<String, Object> sendInsert(@RequestParam Map<String, String> form, Principal principal, HttpServletRequest req, HttpServletResponse res){ 
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	//넘어온 아이디와 실제 시큐리티의 아이디가 일치하지 않을때
    	if(!principal.getName().equals(form.get("mb_id"))) {
    		result.put("res", "err");
			result.put("msg", "잘못된 요청입니다.");
			
			return result;
    	}
    	
    	boolean check = depositService.sendInsert(form); 
    	
    	if(check == false) {
    		result.put("res", "err");
			result.put("msg", "보내기에 실패했습니다:(");
    	} else {
    		result.put("res", "success");
			result.put("msg", form.get("resv_name") + "님에게 보내기가 완료되었어요:)");
    	}
    	return result;
    }
}
