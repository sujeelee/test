package kh.st.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kh.st.boot.model.dto.ComRankDTO;
import kh.st.boot.model.dto.DashListDTO;
import kh.st.boot.model.dto.FollowInfoDTO;
import kh.st.boot.model.vo.BoardVO;
import kh.st.boot.model.vo.FollowVO;
import kh.st.boot.model.vo.NewsVO;
import kh.st.boot.pagination.Criteria;

public interface SearchDAO {
	List<DashListDTO> stockSearch(String stx);

	List<NewsVO> newsSearch(String stx);

	List<ComRankDTO> getCommunityRank();

	String getFlt(String code);

	List<BoardVO> getCommunityList(String code);

	List<FollowVO> getFollowList(String mb_id, Criteria cri);

	int getCount(String type, Criteria cri, String mb_id);

	boolean unfollow(String fo_no, String mb_id);

	List<FollowVO> getFollowViews(String fo_id, Criteria cri);

	FollowInfoDTO getFollowInfo(String fo_id);

	int getFollowNo(String fo_id, String mb_id);

	boolean follow(String fo_no, String mb_id);

	void memberFollow(@Param("id")String fo_mb_id, int cnt);
}
