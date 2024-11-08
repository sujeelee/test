package kh.st.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kh.st.boot.model.vo.BoardVO;
import kh.st.boot.model.vo.CommentVO;
import kh.st.boot.model.vo.CommunityActionVO;
import kh.st.boot.model.vo.FollowVO;

public interface CommunityDAO {

	void insertBoard(BoardVO newBoard);

	List<BoardVO> getBoardList(String st_code);

	CommunityActionVO selectCommunityAction(int cg_num, String mb_id);

	boolean insertCommunityAction(CommunityActionVO communityActionVO);
 
	boolean deleteAction(int cg_num, String mb_id);

	CommunityActionVO findBoardByObjBoardVO(CommunityActionVO feel);

	boolean createBoardOfCommunityAction(CommunityActionVO feel);

	boolean updateBoardOfCommunityAction_setLike(CommunityActionVO feel);

	boolean updateBoardOfCommunityAction_setLikeNull(CommunityActionVO feel);

    boolean updateBoardOfCommunityAction_setReport(CommunityActionVO feel);

	boolean updateBoardOfCommunityAction_setReportNull(CommunityActionVO feel);

	boolean insertComment(CommentVO newComment);

	List<CommentVO> getCommentList(int wr_no);
	
	int getLikeCountForBoard(int cg_num);

	int getReportCountForBoard(int cg_num);

	boolean updateBoardCounts(int cg_num, int likeCount, int reportCount);

	int getLikeCountForComment(int cg_num);

	int getReportCountForComment(int cg_num);

	boolean updateCommentCounts(int cg_num, int likeCount, int reportCount);

	void updateCount();

	CommunityActionVO checkUserActions(CommunityActionVO ca);

	BoardVO getBoardbyID(int wr_no, String mb_id);

	int deleteBoard(int wr_no);

	boolean updateBoard(@Param("bo")BoardVO board);

	CommentVO getCommentbyID(int wr_no, int co_id);

	boolean deleteComment(@Param("co")CommentVO comment);

	int updateComment(@Param("co") CommentVO comment);

    CommunityActionVO findActionByCommentNumber(int co_id, String mb_id); 

	int followorNot(FollowVO follow);

	void deleteFollower(FollowVO follow);

	void insertFollower(FollowVO follow);

	String followingStatus(String mb_id, String fo_mb_id);

	void updateBoardBlind(int wr_no);

	void updateCommentBlind(int co_id);
 
}
