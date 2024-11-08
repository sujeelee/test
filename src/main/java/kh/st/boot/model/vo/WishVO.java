package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WishVO {
	private int wi_id;//위시 기본키
	private String st_code; //주식코드
	private String mb_id; //회원 아이디
}
