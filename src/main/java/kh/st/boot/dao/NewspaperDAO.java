package kh.st.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kh.st.boot.model.vo.AdmMemberVO;
import kh.st.boot.model.vo.NewsPaperVO;
import kh.st.boot.pagination.AdmNewsCriteria;
import kh.st.boot.pagination.Criteria;

@Mapper
public interface NewspaperDAO {

	List<NewsPaperVO> selectAllNewspapers(Criteria cri); // 모든 신문사 조회

	boolean insertNewspaper(@Param("np_name")String np_name, @Param("np_use") byte np_use); // 신문사 등록

	void updateNewspaper(NewsPaperVO NewsPaperVO);

	void deleteNewspaper(NewsPaperVO newspaperVO); // 신문사 삭제

	List<NewsPaperVO> selectNewspapersByStatus(@Param("np_use") int np_use); // 사용 여부에 따라 신문사 조회

	int findMaxNpNo();
	
	//왜 이렇게 했는지 주석 달기
	NewsPaperVO getNewsOne(@Param("name") String np_name);


	int selectCountList(Criteria cri);

	List<AdmMemberVO> searchNews(AdmNewsCriteria cri);

	int selectTotalCount(AdmNewsCriteria cri);

	void UseChange(String np_no, byte useByte);

	
	//  NewspaperVO 로 결과() 보내줌 
	
	
	
}
