package kh.st.boot.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import kh.st.boot.model.vo.FileVO;
import kh.st.boot.model.vo.NewsPaperVO;
import kh.st.boot.model.vo.NewsVO;
import kh.st.boot.model.vo.StockVO;
import kh.st.boot.service.NewsService;
import kh.st.boot.service.StocksHeaderService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/stock")
public class CommunityNewsController {
	
	private StocksHeaderService stocksHeaderService;
	private NewsService newsService;
	
	@GetMapping("/{st_code}/news")
	public String communityNews(Model model, Principal principal, @PathVariable String st_code) {
		model = stocksHeaderService.getModelSet(model, principal, st_code);
		String st_name = ((StockVO)model.getAttribute("stock")).getSt_name();
		List<NewsVO> list = newsService.getNewsList(st_name);
		for(NewsVO news : list) {
			String ne_content = newsService.removeHTML(news.getNe_content());
			news.setNe_content(ne_content);
		}
		model.addAttribute("list", list);
		return "newspaper/news";
	}
	
	@GetMapping("/detail/{ne_no}")
	public String communityNewsDetail(Model model, @PathVariable int ne_no) {
		NewsVO news = newsService.getNews(ne_no);
		NewsPaperVO newspaper = newsService.getNewsPaper(news.getNp_no());
		int totalCount = news.getNe_happy() + news.getNe_angry() + news.getNe_absurd() + news.getNe_sad();
		model.addAttribute("news", news);
		model.addAttribute("newspaper", newspaper);
		model.addAttribute("totalCount", totalCount);
		return "newspaper/detail";
	}
}
