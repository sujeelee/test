package kh.st.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kh.st.boot.model.vo.FileVO;
import kh.st.boot.model.vo.NewsEmojiVO;
import kh.st.boot.model.vo.NewsMemberVO;
import kh.st.boot.model.vo.NewsPaperVO;
import kh.st.boot.model.vo.NewsVO;

public interface NewsDAO {

	List<NewsPaperVO> selectNewsPaperList();
	
	List<NewsVO> selectNewsListByDate(String formatDate);

	List<NewsVO> selectNewsListByPaper(int np_no, String ne_datetime);
	
	NewsPaperVO selectNewsPaper(int np_no);

	NewsVO selectNews(int ne_no);

	NewsEmojiVO selectNewsEmoji(NewsEmojiVO emoji);

	void insertNewsEmoji(NewsEmojiVO emoji);

	void updateNewsEmojiCount(@Param("em")NewsEmojiVO emoji, @Param("count")int count);

	void updateNewsEmoji(NewsEmojiVO emoji);

	void deleteNewsEmoji(int ne_no, String mb_id);

	boolean insertNews(NewsVO news);
	
	boolean updateNews(NewsVO news);

	boolean deleteNews(int ne_no);
	
	NewsVO selectNewsLimitOne();
	
	void insertFile(FileVO fileVo);
	
	FileVO selectFileByFiNo(int fi_no);

	FileVO selectFileByNeNo(int ne_no);
	
	void deleteFileByFiNo(int fi_no);

	void deleteFileByNeNo(int ne_no);

	List<NewsVO> selectNewsList(String st_name);

	List<NewsVO> selectNewsListByImg();

	List<NewsVO> selectNewsListByNoImg();

	NewsMemberVO selectNewsMember(int mb_no);

}
