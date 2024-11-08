package kh.st.boot.model.vo;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardVO {

	private int wr_no; //게시글 기본키 
	private String wr_category; //주식코드
	private String wr_content; //본문 내용
	private String mb_id; //작성회원
	private int wr_comment; //작성된 댓글수
	private int wr_good; //좋아요갯수
	private int wr_singo; //신고갯수
	private int mb_level;
	private String mb_nick;
	private String wr_blind;
	private Date wr_datetime;

	//DB 상 존재하지 않습니다.
	//report 신고, like 좋아요로 확인
	//join해서 가져올 것
	private String cg_like; //좋아요 눌렀는지 확인
	private String cg_report; // 신고 눌렀는지 확인
	private String following;
	private String lv_txt; //회원 레벨에 따른 명칭노출
	
}
