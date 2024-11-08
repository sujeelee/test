package kh.st.boot.config;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kh.st.boot.model.dto.DashListDTO;
import kh.st.boot.model.dto.HotStockDTO;
import kh.st.boot.model.vo.AdminVO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.model.vo.StockJisuVO;
import kh.st.boot.service.ConfigService;
import kh.st.boot.service.MemberService;

@ControllerAdvice
@Controller
public class GlobalControllerAdvice {
	
    @Autowired
    private ConfigService configService;
    @Autowired
    private MemberService memberService;
    
    @ModelAttribute("config")
    public AdminVO globalConfig() {
        // configService로 config 테이블 정보를 가져와서 반환
        return configService.getConfig();
    }
    
    @ModelAttribute("member")
    public MemberVO globalMember(Principal principal) {
    	MemberVO member = new MemberVO();
    	
    	if(principal == null) {
    		member.setMb_id(null);
        } else {
        	member = memberService.findById(principal.getName());
        	//회원 팔로우에 따른 증감식
        	member = configService.getConfigLv(member);
        }
    	
        return member;
    }
    
    @ModelAttribute("hotlist")
    public List<HotStockDTO> getHotStockList(){
    	List<HotStockDTO> list = configService.getHotStockList(100);
		
		return list;
    }
    
    @ModelAttribute("top5list")
    public List<HotStockDTO> getHotStockListTop5(){
    	List<HotStockDTO> list = configService.getHotStockList(5);
		return list;
    }
    
    @ModelAttribute("top10")
    public List<HotStockDTO> getListTop10(){
    	List<HotStockDTO> list = configService.getHotStockList(10);
		return list;
    }
    
    @PostMapping("/dashlist")
    public String getDashList(@RequestParam Map<String, Object> param, Model model){
    	List<DashListDTO> list = configService.getDashList(param);

    	model.addAttribute("dashList", list);
    	
    	return "layout/sidebar :: #dash-ajax";
    }
    
    @ModelAttribute("kospi")
    public List<StockJisuVO> kospiConfig() {
        return configService.jisuConfig("kospi");
    }
    
    @ModelAttribute("kosdaq")
    public List<StockJisuVO> kosdaqConfig() {
        return configService.jisuConfig("kosdaq");
    }
    
    @ModelAttribute("krx")
    public List<StockJisuVO> krxConfig() {
        return configService.jisuConfig("krx");
    }
}
