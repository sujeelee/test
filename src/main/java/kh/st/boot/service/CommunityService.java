package kh.st.boot.service;


import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import kh.st.boot.dao.CommunityDAO;
import kh.st.boot.dao.SearchDAO;
import kh.st.boot.model.vo.BoardVO;
import kh.st.boot.model.vo.CommentVO;
import kh.st.boot.model.vo.CommunityActionVO;
import kh.st.boot.model.vo.FollowVO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommunityService {

    private CommunityDAO communityDao;
    private SearchDAO searchDao;
    
	public void insertBoard(BoardVO newBoard) {
		communityDao.insertBoard(newBoard);		
	}

	public List<BoardVO> getBoardList(String st_code, String mb_id) {
		if(st_code == null) {
			return null;
		}
		List<BoardVO> list = communityDao.getBoardList(st_code);
	    if (list == null || list.isEmpty()) {
	        return list;
	    }
		for(BoardVO tmp : list) {	   
			CommunityActionVO ca = new CommunityActionVO();
			ca.setCg_num(tmp.getWr_no());
			ca.setCg_type("board");
			ca.setMb_id(mb_id);
			
			   if (mb_id != null) { // 로그인한 사용자
			        CommunityActionVO actionStatus = communityDao.checkUserActions(ca);
			        String fo_mb_id = tmp.getMb_id();

			        // tmp.getMb_id()가 null인지 체크
			        if (fo_mb_id == null) {
			            tmp.setFollowing("n"); // 기본값 설정
			        } else {
			            String followingStatus = communityDao.followingStatus(mb_id, fo_mb_id);
			            tmp.setFollowing(followingStatus);
			        }

			        // actionStatus가 null이 아닐 경우에만 속성 설정
			        if (actionStatus != null) {
			            // 좋아요와 신고 상태를 설정
			            tmp.setCg_like(actionStatus.getCg_like().equals("like") ? "active" : "");
			            tmp.setCg_report(actionStatus.getCg_report().equals("report") ? "active" : "");
			        } else {
			            // actionStatus가 null일 경우 기본값 설정
			            tmp.setCg_like("");
			            tmp.setCg_report("");
			        }

			    } else {
			        // 로그인하지 않은 사용자에 대해 기본값 설정
			        tmp.setCg_like("");
			        tmp.setCg_report("");
			        tmp.setFollowing("n");
			    }
			}
			return list;
	}
	public List<CommentVO> getCommentList(int co_id, int wr_no ,String mb_id) {
	    if(co_id == 0) {
	        return null;
	    }
	    
	    List<CommentVO> colist = communityDao.getCommentList(wr_no); // 댓글 목록 조회
	    if (colist == null || colist.isEmpty()) {
	        return colist; // 비어있는 경우 그냥 반환
	    }
	    
	    for(CommentVO tmp : colist) {
	        CommunityActionVO ca = new CommunityActionVO();
	        ca.setCg_num(tmp.getWr_no());
	        ca.setCg_type("comment");
	        ca.setMb_id(mb_id);

	        // 로그인한 사용자의 경우
	        if (mb_id != null) {
	            CommunityActionVO actionStatus = communityDao.checkUserActions(ca);
	            
	            // actionStatus가 null이 아닐 경우에만 속성 설정
	            if (actionStatus != null) {
	                // 좋아요와 신고 상태를 설정
	                tmp.setCg_like(actionStatus.getCg_like().equals("like") ? "active" : "");
	                tmp.setCg_report(actionStatus.getCg_report().equals("report") ? "active" : "");
	            } else {
	                // actionStatus가 null일 경우 기본값 설정
	                tmp.setCg_like("");
	                tmp.setCg_report("");
	            }
	        } else {
	            // 로그인하지 않은 사용자에 대해 기본값 설정
	            tmp.setCg_like("");
	            tmp.setCg_report("");
	        }
	    }
	    return colist; // 항상 댓글 목록 반환
	}
	public boolean setFeelAction(CommunityActionVO feel) {
	    if (feel == null || feel.getMb_id() == null || 
	            feel.getSt_code() == null || feel.getMb_id().trim().length() == 0) {
	            return false;
	        }
	        // 게시글 번호가 음수이거나 0일 수 없음
	        if (feel.getCg_num() < 1) {
	            return false;
	        }
	        boolean tmp = false;
	        CommunityActionVO tmpCA = communityDao.findBoardByObjBoardVO(feel);
	        if (tmpCA == null) {
	            tmp = communityDao.createBoardOfCommunityAction(feel);
	            // tmpCA에다가 다시 위 코드에 추가한 인설트를 다시 셀렉트로 가져온다.
	            tmpCA = communityDao.selectCommunityAction(feel.getCg_num(), feel.getMb_id());
	            tmpCA.setCg_like(null);
	            tmpCA.setCg_report(null);
	        }

	        // 좋아요 처리
	        if ("like".equals(feel.getCg_like())) {
	            if (tmpCA.getCg_like() == null || tmpCA.getCg_like().trim().isEmpty()) {
	                tmp = communityDao.updateBoardOfCommunityAction_setLike(feel);
	            } else {
	                tmp = communityDao.updateBoardOfCommunityAction_setLikeNull(feel);
	            }
	        // 신고 처리
	        } else if ("report".equals(feel.getCg_report())) {
	            if (tmpCA.getCg_report() == null || tmpCA.getCg_report().trim().isEmpty()) {
	                tmp = communityDao.updateBoardOfCommunityAction_setReport(feel);
	            } else {
	                tmp = communityDao.updateBoardOfCommunityAction_setReportNull(feel);
	            }
	        }
	        if(tmp){
	        		updateActionCounts(feel);
	        		
	        }else {
	        	System.out.println("Action failed for: " + feel);
			}
	        return tmp;	        
	}
	
	private void updateActionCounts(CommunityActionVO feel) {
	    // 게시글에 대한 좋아요 및 신고 수 업데이트
	    if ("board".equals(feel.getCg_type())) {
	        int likeCount = communityDao.getLikeCountForBoard(feel.getCg_num());
	        int reportCount = communityDao.getReportCountForBoard(feel.getCg_num());
	        boolean success = communityDao.updateBoardCounts(feel.getCg_num(), likeCount, reportCount);
	        if(success) { //실제로 업데이트가 되면
	        	if(reportCount > 9) { //신고 수가 N개 초과이면
	        		//해당 게시물의 wr_blind가 Y로 업데이트 되도록
	        		communityDao.updateBoardBlind(feel.getCg_num()); 
	        	}
	        }
	    }
	    // 댓글에 대한 좋아요 및 신고 수 업데이트
	    else if ("comment".equals(feel.getCg_type())) {
	        int likeCount = communityDao.getLikeCountForComment(feel.getCg_num());
	        int reportCount = communityDao.getReportCountForComment(feel.getCg_num());
	        boolean success = communityDao.updateCommentCounts(feel.getCg_num(), likeCount, reportCount);
	        if(success) { //실제로 업데이트가 되면
	        	if(reportCount > 9) { //신고 수가 N개 초과이면
	        		//해당 댓글의 co_blind가 Y로 업데이트 되도록
	        		communityDao.updateCommentBlind(feel.getCg_num()); 
	        	}
	        }
	    }
    }

	public boolean insertComment(CommentVO newComment) {
		return communityDao.insertComment(newComment);
		
	}
	public void updateCount() {
		communityDao.updateCount();
	}
	public List<CommentVO> getCommentList(int wr_no, String mb_id) {
		List<CommentVO> list = communityDao.getCommentList(wr_no);


	    // list가 null인지 확인
	    if (list == null) {
	        return new ArrayList<>(); // 빈 목록 반환
	    }

	    for (int i = 0; i < list.size(); i++) {
	        CommentVO comment = list.get(i);
	        
	        CommunityActionVO ca = communityDao.findActionByCommentNumber(comment.getCo_id(), mb_id);
	        
	        // ca가 null인지 확인
	        if (ca != null) {
	            // getCg_like()와 getCg_report()의 반환값이 null인지 확인
	            String likeStatus = ca.getCg_like();
	            String reportStatus = ca.getCg_report();
	            
	            // 좋아요 상태 설정
	            comment.setCg_like("like".equals(likeStatus) ? "like" : "");
	            // 신고 상태 설정
	            comment.setCg_report("report".equals(reportStatus) ? "report" : "");
	        } else {
	            // actionStatus가 null일 경우 기본값 설정
	            comment.setCg_like("");
	            comment.setCg_report("");
	        }
	    }
		
		return list;
	}

	public List<BoardVO> getBoardList(String st_code) {
		return communityDao.getBoardList(st_code);
	}

	public BoardVO getBoardbyID(int wr_no, String mb_id) {
		
		return communityDao.getBoardbyID(wr_no,mb_id);
	}

	public boolean deleteBoard(int wr_no) {
		int board = communityDao.deleteBoard(wr_no);
	    return board > 0;
		
	}

	public boolean updateBoard(BoardVO board) {
		return communityDao.updateBoard(board);
	}

	public CommentVO getCommentbyID(int wr_no, int co_id) {
		return communityDao.getCommentbyID(wr_no,co_id);
	}

	public boolean deleteComment(CommentVO comment) {
	    return communityDao.deleteComment(comment);
	}

	public boolean updateComment(CommentVO comment) {
		return communityDao.updateComment(comment) > 0;
	}
	
	public boolean followorNot(FollowVO follow) {
		
        int connection = communityDao.followorNot(follow);
        
        if (connection > 0) { // 팔로우가 존재하는 경우
        	communityDao.deleteFollower(follow);
        	searchDao.memberFollow(follow.getFo_mb_id(), -1);
            return false; // 팔로우 제거
        } else { // 팔로우가 존재하지 않는 경우
        	communityDao.insertFollower(follow);
        	searchDao.memberFollow(follow.getFo_mb_id(), 1);
            return true; // 팔로우 추가
        }
    }
}