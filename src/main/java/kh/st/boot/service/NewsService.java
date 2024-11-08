package kh.st.boot.service;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kh.st.boot.model.vo.FileVO;
import kh.st.boot.model.vo.NewsEmojiVO;
import kh.st.boot.model.vo.NewsMemberVO;
import kh.st.boot.model.vo.NewsPaperVO;
import kh.st.boot.model.vo.NewsVO;

public interface NewsService {
	
	List<NewsPaperVO> getNewsPaperList();

	List<NewsVO> getNewsList(Date ne_datetime);

	NewsPaperVO getNewsPaper(int np_no);

	List<NewsVO> getNewsList(int np_no, String ne_datetime);

	NewsVO getNews(int ne_no);

	NewsEmojiVO getNewsEmoji(NewsEmojiVO emoji);

	void insertNewsEmoji(NewsEmojiVO emoji);

	void updateNewsEmojiCount(NewsEmojiVO newEmoji, int i);

	void updateNewsEmoji(NewsEmojiVO emoji);

	void deleteNewsEmoji(NewsEmojiVO emoji);
	
	boolean insertNews(NewsVO news, String mb_id, MultipartFile file);

	boolean updateNews(NewsVO news, String mb_id, MultipartFile file, Integer num);

	boolean deleteNews(int ne_no, String mb_id);

	FileVO getFile(int ne_no);

	List<NewsVO> getNewsList(String st_name);

	String removeHTML(String ne_content);

	List<NewsVO> getNewsListByImg();

	List<NewsVO> getNewsListByNoImg();

	NewsMemberVO getNewsMember(int mb_no);

}

