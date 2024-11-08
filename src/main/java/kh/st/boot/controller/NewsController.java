package kh.st.boot.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kh.st.boot.model.vo.FileVO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.model.vo.NewsEmojiVO;
import kh.st.boot.model.vo.NewsMemberVO;
import kh.st.boot.model.vo.NewsPaperVO;
import kh.st.boot.model.vo.NewsVO;
import kh.st.boot.service.MemberService;
import kh.st.boot.service.MyAccountService;
import kh.st.boot.service.NewsService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/newspaper")
public class NewsController {
	
	private NewsService newsService;
	private MemberService memberService;
	
	@GetMapping("")
	public String newspaperList(Model model) {
		List<NewsPaperVO> list = newsService.getNewsPaperList();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String ne_datetime = format.format(date);
		model.addAttribute("list", list);
		model.addAttribute("ne_datetime", ne_datetime);
		return "newspaper/paperList";
	}
	
	@ResponseBody
	@PostMapping("/views")
	public Map<String, List<NewsVO>> views(@RequestBody NewsVO news){
		Map<String, List<NewsVO>> map = new HashMap<String, List<NewsVO>>();
		Date ne_datetime = news.getNe_datetime();
		// 서비스에게 날짜를 주고 리스트를 가져옴
		List<NewsVO> newsList = newsService.getNewsList(ne_datetime);
		map.put("newsList", newsList);
		return map;
	}
	
	@GetMapping("/newsList/{np_no}/{ne_datetime}")
	public String newsList(Model model, Principal principal, @PathVariable int np_no, @PathVariable String ne_datetime) {
		NewsPaperVO newspaper = newsService.getNewsPaper(np_no);
		List<NewsVO> newsList = newsService.getNewsList(np_no, ne_datetime);
		MemberVO user = null;
		if(principal != null) {
			String mb_id = principal.getName();
			user = memberService.findById(mb_id);
		}
		if(user != null) {
			NewsMemberVO news = newsService.getNewsMember(user.getMb_no());
			if(news != null) {
				user.setMb_news(news.getMb_news());
			}
		}
		model.addAttribute("newspaper",newspaper);
		model.addAttribute("newsList", newsList);
		model.addAttribute("ne_datetime", ne_datetime);
		model.addAttribute("user", user);
		return "newspaper/newsList";
	}
	
	@ResponseBody
	@PostMapping("/newsList/views")
	public Map<String, List<NewsVO>> newsListViews(@RequestBody NewsVO news){
		Map<String, List<NewsVO>> map = new HashMap<String, List<NewsVO>>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String ne_datetime = format.format(news.getNe_datetime());
		int np_no = news.getNp_no();
		// 서비스에게 날짜와 뉴스페이퍼 번호를 주고 리스트를 가져옴
		List<NewsVO> newsList = newsService.getNewsList(np_no, ne_datetime);
		map.put("newsList", newsList);
		return map;
	}
	
	@GetMapping("/newsDetail/{ne_no}")
	public String detail(Model model, @PathVariable int ne_no) {
		NewsVO news = newsService.getNews(ne_no);
		NewsPaperVO newspaper = newsService.getNewsPaper(news.getNp_no());
		int totalCount = news.getNe_happy() + news.getNe_angry() + news.getNe_absurd() + news.getNe_sad();
		FileVO file = newsService.getFile(ne_no);
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String ne_datetime = format.format(date);
		model.addAttribute("news", news);
		model.addAttribute("newspaper", newspaper);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("file", file);
		model.addAttribute("ne_datetime", ne_datetime);
		return "newspaper/newsDetail";
	}
	
	@ResponseBody
	@PostMapping("/emoji")
	public Map<String, Object> emoji(@RequestBody NewsEmojiVO emoji, Principal principal) {
		Map<String, Object> map = new HashMap<String, Object>();
		emoji.setMb_id(principal.getName());
		NewsVO news = newsService.getNews(emoji.getNe_no());
		// 이전에 선택한 이모지
		NewsEmojiVO prevEmoji = newsService.getNewsEmoji(emoji);
		if(prevEmoji == null) {
			// 사용자가 처음으로 선택한 이모지
			newsService.insertNewsEmoji(emoji);
			// 새로운 반응에 대한 카운트 증가
			newsService.updateNewsEmojiCount(emoji, 1);
			news = newsService.getNews(emoji.getNe_no());
		} else {
			// 사용자가 다른 반응으로 바꾼 경우
			if(prevEmoji.getEm_act() != emoji.getEm_act()) {
				// 이전 카운트를 감소
				newsService.updateNewsEmojiCount(prevEmoji, -1);
				// 새로운 이모지 카운트 증가
				newsService.updateNewsEmojiCount(emoji, 1);
				// 이모지 업데이트
				newsService.updateNewsEmoji(emoji);
				news = newsService.getNews(emoji.getNe_no());
			}
			// 사용자가 같은 반응을 클릭한 경우
			else {
				newsService.updateNewsEmojiCount(prevEmoji, -1);
				newsService.deleteNewsEmoji(emoji);
				news = newsService.getNews(emoji.getNe_no());
			}
		}
		int totalCount = news.getNe_happy() + news.getNe_angry() + news.getNe_absurd() + news.getNe_sad();
		map.put("news", news);
		map.put("totalCount", totalCount);
		return map;
	}
	
	@GetMapping("/newsInsert/{np_no}")
	public String insert(Model model, @PathVariable int np_no) {
		return "newspaper/newsInsert";
	}
	
	@PostMapping("/newsInsert/{np_no}")
	public String insertPost(Model model, @PathVariable int np_no ,NewsVO news, Principal principal, MultipartFile file) {
		String mb_id = principal.getName();
		boolean res = newsService.insertNews(news, mb_id, file);
		if(res) {
			model.addAttribute("msg" , "뉴스 등록 성공");
			model.addAttribute("url" , "/newspaper");
		}else {
			model.addAttribute("msg" , "뉴스 등록 실패");
			model.addAttribute("url" , "/newspaper/newsInsert/" + np_no);
		}
		
		return "util/msg";
	}
	
	@GetMapping("/newsUpdate/{ne_no}")
	public String update(Model model, @PathVariable int ne_no) {
		// 게시글을 가져옴
		NewsVO news = newsService.getNews(ne_no);
		// 첨부파일 가져옴
		FileVO file = newsService.getFile(ne_no);
		model.addAttribute("news", news);
		model.addAttribute("file", file);
		return "newspaper/newsUpdate";
	}
	

	@PostMapping("/newsUpdate/{ne_no}")
	public String updatePost(Model model, @PathVariable int ne_no ,NewsVO news, Principal principal, MultipartFile file,@RequestParam(required = false) Integer num) {
		String mb_id = principal.getName();
		boolean res = newsService.updateNews(news, mb_id, file, num);
		if(res) {
			model.addAttribute("msg" , "뉴스 수정 성공");
			model.addAttribute("url" , "/newspaper/newsDetail/" + ne_no);
		}else {
			model.addAttribute("msg" , "뉴스 수정 실패");
			model.addAttribute("url" , "/newspaper/newsDetail/" + ne_no);
		}
		return "util/msg";
	}
	
	@GetMapping("/newsDelete/{ne_no}")
	public String delete(Model model, @PathVariable int ne_no, Principal principal) {
		String mb_id = principal.getName();
		boolean res = newsService.deleteNews(ne_no, mb_id);
		if(res) {
			model.addAttribute("msg", "뉴스 삭제 성공");
			model.addAttribute("url", "/newspaper");
		}else {
			model.addAttribute("msg", "뉴스 삭제 실패");
			model.addAttribute("url", "/newspaper/newsDetail/" + ne_no);
		}
		return "util/msg";
	}
}
