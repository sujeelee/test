package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsMemberVO {
	
	private int mb_no;		// 회원고유번호
	private String mb_news;	// 회원 신문사명
}
