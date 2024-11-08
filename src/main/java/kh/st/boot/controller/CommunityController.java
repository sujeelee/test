package kh.st.boot.controller;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.HashMap;
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

import kh.st.boot.dao.MemberDAO;
import kh.st.boot.model.vo.BoardVO;
import kh.st.boot.model.vo.CommentVO;
import kh.st.boot.model.vo.CommunityActionVO;
import kh.st.boot.model.vo.FollowVO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.service.CommunityService;
import kh.st.boot.service.ConfigService;
import kh.st.boot.service.StocksHeaderService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/stock/{st_code}/community")
public class CommunityController {

	private CommunityService communityService;
	private StocksHeaderService stocksHeaderService;//
	private ConfigService configService;
	
	private MemberDAO memberDao;

	@GetMapping
	public String community(Model mo, Principal principal, @PathVariable String st_code) {
		mo = stocksHeaderService.getModelSet(mo, principal, st_code);
		// principal이 null일 경우 mb_id를 null로 설정
	    String mb_id = null;
	    if (principal != null) {
	        mb_id = principal.getName();
	        mo.addAttribute("userInfo", mb_id);
	    }
		List<BoardVO> list = communityService.getBoardList(st_code, mb_id);
		for(BoardVO tmp : list) {
			String txt = configService.getLvTxt(tmp.getMb_level());
			tmp.setLv_txt(txt);
		}
		mo.addAttribute("bolist", list);
		return "community/community";
	}

	@PostMapping("/insert")
	@ResponseBody
	public Map<String, Object> boardPostMethod(Model mo, @RequestParam Map<String, Object> board, Principal principal) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (principal == null) {
			result.put("res", "false");
			result.put("msg", "회원만 작성 가능합니다.");
			return result;
		}

		MemberVO user = memberDao.findById(principal.getName());
				

		BoardVO newBoard = new BoardVO(); // 새로운 BoardVO 객체 생성
		newBoard.setMb_id(user.getMb_id());
		newBoard.setMb_level(user.getMb_level());
		for (Map.Entry<String, Object> entry : board.entrySet()) {
			String jsonKey = entry.getKey();
			Object value = entry.getValue();
			Field fields;
			try {
				fields = BoardVO.class.getDeclaredField(jsonKey); 
				fields.setAccessible(true);
				fields.set(newBoard, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		communityService.insertBoard(newBoard);
		
		result.put("res", "true");
		return result;
	}
	@PostMapping("delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam int wr_no, Principal principal) {
		Map<String, Object> result = new HashMap<>();
		
		String mb_id = principal.getName();
		BoardVO board = communityService.getBoardbyID(wr_no,mb_id);  
	   
	    if (board == null) {
	        result.put("res", "false");
	        result.put("msg", "게시글을 찾을 수 없습니다.");
	        return result; 
	    }

	    // 사용자가 게시글의 작성자가 아닐 경우
	    if (!board.getMb_id().equals(mb_id)) {
	        result.put("res", "false");
	        result.put("msg", "본인의 게시글만 삭제할 수 있습니다.");
	        return result; 
	    }

	    // 게시글 삭제
	    boolean isDeleted = communityService.deleteBoard(wr_no); 
	    if (isDeleted) {
	        result.put("res", "true");
	        result.put("msg", "게시글이 삭제되었습니다.");
	    } else {
	        result.put("res", "false");
	        result.put("msg", "게시글 삭제에 실패했습니다.");
	    }
	    return result; // 삭제 완료 후 결과 반환
	}
	@PostMapping("/update")
	@ResponseBody
	public Map<String, Object> updata(@RequestParam int wr_no, @RequestParam String wr_content, Principal principal) {
		Map<String, Object> result = new HashMap<>();
		String mb_id = principal.getName();
		BoardVO board = communityService.getBoardbyID(wr_no,mb_id);
	    if (board != null) {
	        board.setWr_content(wr_content);
	        System.out.println(board);
	        // 서비스 메서드를 호출하여 업데이트 수행
	        boolean updateSuccess = communityService.updateBoard(board);
	        System.out.println(updateSuccess);
	        
	        // 결과에 따라 메시지 설정
	        if (updateSuccess) {
	            result.put("res", "true");
	            result.put("msg", "게시글이 성공적으로 업데이트되었습니다.");
	        } else {
	            result.put("res", "false");
	            result.put("msg", "게시글 업데이트에 실패했습니다.");
	        }
	    } else {
	        result.put("res", "false");
	        result.put("msg", "게시글을 찾을 수 없습니다.");
	    }
		
		return result;
	}
	@PostMapping("/action")
	@ResponseBody
	public Map<String, Object> communityAction(@RequestBody CommunityActionVO feel, Principal principal,@PathVariable String st_code) {
		Map<String, Object> result = new HashMap<String, Object>();
		//principal는 오브잭트  principal.getName();는 문지열, 오브잭트의 null을 확인해 주는게 좋습니다.
		//principal가 null 일때 map 형식인 result에 key와 value를 넣어서 화면에서 사용할 수 있게 도와줍니다.
		if (principal == null) {
			result.put("res", "401");
			result.put("msg", "로그인한 회원만 이용 가능합니다.");
			return result;
		}
		
		
		//필요한 변수를 먼저 선언 해 줍니다.
		String mb_id = principal.getName();
		feel.setMb_id(mb_id);
		feel.setSt_code(st_code);
		int co_id = feel.getCg_num();
		String actions = feel.getAction();
		
		//ajax로 받아온 값에 따라 객체에 값을 재정의
		if(actions.equals("like")) {
			feel.setCg_like(actions);
		} else {
			feel.setCg_report(actions);
		}
		
		//변수들을 이용해서 DB에저장하고 필요한 값을 map에 넣습니다.
		boolean res = communityService.setFeelAction(feel);
		
		if(res){
			result.put("res", "200"); //다 정상
		} else {
			result.put("res", "204"); //DB 에 저장은 되는데 다른곳에 오류가 있음
		} //action table update 완료
		return result;
	}
	
	@PostMapping("/insertComment")
	@ResponseBody
	public Map<String, Object> CommentPostMethod(Model mo, @RequestParam String co_content, @RequestParam int wr_no, Principal principal) {
	    Map<String, Object> result = new HashMap<>();
	    
	    // 로그인 확인
	    if (principal == null) {
	        result.put("res", "error");
	        result.put("msg", "회원만 작성 가능합니다.");
	        return result;
	    }
	    
	    // 댓글 객체 생성
	    CommentVO newComment = new CommentVO();
	    newComment.setMb_id(principal.getName());
	    newComment.setWr_no(wr_no);
	    newComment.setCo_content(co_content);
	    
	    // 댓글 추가
	    boolean res = communityService.insertComment(newComment);
	    
	    if (res) {
	        communityService.updateCount();
	    }
	    
	    // 성공적인 응답 반환
	    result.put("res", res ? "true" : "false");
	    return result;
	}
	@PostMapping("/deleteComment")
	@ResponseBody
	public Map<String, Object> deleteComment(@RequestParam int wr_no,@RequestParam int co_id, Principal principal){
		Map<String, Object> result = new HashMap<>();
		
		String mb_id = null;
		
		if(principal != null) {
			mb_id = principal.getName();
		} else {
			result.put("res", "false");
	        result.put("msg", "로그인 후 이용 가능합니다.");
	        return result; 
		}
		
		CommentVO comment = communityService.getCommentbyID(wr_no, co_id);  
	   
		if (comment == null) {
			result.put("res", "false");
	        result.put("msg", "댓글을 찾을 수 없습니다.");
	        return result; 
	    }

	    // 사용자가 게시글의 작성자가 아닐 경우
	    if (!comment.getMb_id().equals(mb_id)) {
	        result.put("res", "false");
	        result.put("msg", "본인의 댓글만 삭제할 수 있습니다.");
	        return result; 
	    }

	    //댓글 삭제
	    boolean isCommentDeleted = communityService.deleteComment(comment); 
	    if (isCommentDeleted) {
	        result.put("res", "true");
	        result.put("msg",  "댓글이 삭제되었습니다.");
	        communityService.updateCount();
	    } else {
	        result.put("res", "false");
	        result.put("msg", "댓글 삭제에 실패했습니다.");
	    }
	    return result; // 삭제 완료 후 결과 반환
	}
	@PostMapping("/updateComment")
	@ResponseBody
	public Map<String, Object> updateComment(@RequestParam int wr_no,@RequestParam String co_content, @RequestParam int co_id, Principal principal){
		Map<String, Object> result = new HashMap<>();
		
		String mb_id = null;
		
		if(principal != null) {
			mb_id = principal.getName();
		} else {
			result.put("res", "false");
	        result.put("msg", "로그인 후 이용 가능합니다.");
	        return result; 
		}
		
		CommentVO comment = communityService.getCommentbyID(wr_no, co_id);
		
	    if (comment != null) {
	    	comment.setCo_content(co_content);
	        
	        // 서비스 메서드를 호출하여 업데이트 수행
	        boolean updateSuccess = communityService.updateComment(comment);
	        
	        // 결과에 따라 메시지 설정
	        if (updateSuccess) {
	            result.put("res", "true");
	            result.put("msg", "게시글이 성공적으로 업데이트되었습니다.");
	        } else {
	            result.put("res", "false");
	            result.put("msg", "게시글 업데이트에 실패했습니다.");
	        }
	    } else {
	        result.put("res", "false");
	        result.put("msg", "게시글을 찾을 수 없습니다.");
	    }
		return result;
	}
	
	@PostMapping("/replaceComment")
	public String replaceCommentList_post(Model mo, Principal principal, @RequestParam int wr_no) {
		String mb_id = null;
		
		if(principal != null) {
			mb_id = principal.getName();
		}
	    
	    // 댓글 목록 가져오기
	    List<CommentVO> colist = communityService.getCommentList(wr_no, mb_id);
	    for(CommentVO tmp : colist) {
			String txt = configService.getLvTxt(tmp.getMb_level());
			tmp.setLv_txt(txt);
		}
	 
	    if (principal != null) {
	        mo.addAttribute("userInfo", principal.getName());
	    }
	    
	    mo.addAttribute("colist", colist);
	    
	    // HTML 댓글용 템플릿 반환
	    return "community/replaceComment";
	}
	
    @PostMapping("/followorNot")
    @ResponseBody
    public Map<String, Object> addFoller(@PathVariable String st_code, @RequestParam String fo_mb_id, Principal principal){
        Map<String, Object> result = new HashMap<String, Object>();
        
		if (principal == null) {
			result.put("res", "false");
			result.put("msg", "회원만 가능합니다.");
			return result;
		}		
		String mb_id = principal.getName();
	    if (mb_id.equals(fo_mb_id)) {
	        result.put("res", "false");
	        result.put("msg", "자기 자신을 팔로우할 수 없습니다.");
	        return result;
	    }
	    FollowVO follow = new FollowVO();
		follow.setFo_mb_id(fo_mb_id);
		follow.setMb_id(mb_id);
	    boolean FolloworNot = communityService.followorNot(follow);
	    if (FolloworNot) {
	        result.put("res", "true");
	        result.put("msg", "팔로우가 추가되었습니다.");
	        result.put("text", "언팔로우");
	    } else {
	        result.put("res", "false");
	        result.put("msg", "팔로우 삭제하셨습니다..");
	        result.put("text", "팔로우");
	    }
	    


        return result;
    }
}