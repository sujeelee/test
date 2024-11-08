package kh.st.boot.model.vo;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsEmojiVO {
	
	private int em_no;			// 반응 기본키
	private int ne_no;			// 뉴스 기본키
	private int em_act;			// 사용자가 누른값
	private String mb_id;		// 회원 아이디
	private Date em_datetime;	// 반응 남긴날

}
