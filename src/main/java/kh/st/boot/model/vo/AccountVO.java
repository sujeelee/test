package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountVO {
	private int mb_no;		// 회원 고유 번호
	private int ac_deposit;	// 예치금 잔액
}
