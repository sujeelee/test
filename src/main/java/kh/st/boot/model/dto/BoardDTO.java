package kh.st.boot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardDTO {
    
	private int wr_no; //게시글 기본키 
	private String wr_category; //주식코드
	private String wr_content; //본문 내용

	private String mb_id; //작성회원
    private int mb_level;
    
	private int wr_comment; //작성된 댓글수
	private int wr_good; //좋아요갯수
	private int wr_singo; //신고갯수

    private int like;
    private int report;
}
