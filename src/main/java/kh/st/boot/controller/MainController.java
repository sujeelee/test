package kh.st.boot.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kh.st.boot.model.dto.ComRankDTO;
import kh.st.boot.model.dto.SearchDTO;
import kh.st.boot.model.vo.BoardVO;
import kh.st.boot.model.vo.EventVO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.model.vo.NewsVO;
import kh.st.boot.service.ConfigService;
import kh.st.boot.service.EventService;
import kh.st.boot.service.NewsService;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class MainController {
	
	private NewsService newsService;
	private ConfigService configService;
	private EventService eventService;

	@GetMapping("/")
	public String home(Model mo, HttpSession session) {
		MemberVO user = (MemberVO)session.getAttribute("user");
		List<NewsVO> newsImgList = newsService.getNewsListByImg(); 	// 썸네일 있는 뉴스 최신순으로 4개
		List<NewsVO> newsList = newsService.getNewsListByNoImg();	// 썸네일 없는 뉴스 최신순으로 4개 
		List<EventVO> eventList = eventService.getEventListForMainBanner();
		
		List<ComRankDTO> comRanks = configService.getCommunityRank();

		mo.addAttribute("eventList", eventList);
		mo.addAttribute("user", user);
		mo.addAttribute("newsImgList", newsImgList);
		mo.addAttribute("newsList" ,newsList);
		mo.addAttribute("comRank", comRanks);
		return "home";
	}
	
	@GetMapping("/terms")
	public String termsList(Model model) {
		return "terms/list";
	}
	
	@GetMapping("/terms/{type}")
	public String termsContent(Model model, @PathVariable String type) {
		model.addAttribute("type", type);
		return "terms/content";
	}
	
	@PostMapping("/totalSearch")
	public String totalSearch(@RequestParam Map<String, String> param, Model model) {
		String type = (String)param.get("type");
		
		List<SearchDTO> list = configService.totalSearch(type, (String)param.get("stx"));
		model.addAttribute("resType", type);
		model.addAttribute("search", list);
		model.addAttribute("mstx", (String)param.get("stx"));
    	return "layout/searchModal :: #search-result";
	}
	
	@PostMapping("/communityAjax")
	public String communityList(@RequestParam String code, Model model) {
		List<BoardVO> list = configService.getCommunityList(code);
		model.addAttribute("commList", list);
    	return "layout/mainRank :: #comm-main-list";
	}

}
