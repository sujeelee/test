package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockMemberVO {
	
	private int mb_no;			// 회원고유번호
	private String mb_stock;	// 회원 주식 코드
}
