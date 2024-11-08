package kh.st.boot.model.vo;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepositOrderVO {
	private String do_od_id; //주문번호
	private String do_name; //주문자 성명
	private String mb_id; //결제한 아이디
	private int do_price; //총 결제금액
	private String do_tno; //결제시 리턴 받는 결제번호
	private String do_auth; //결제시 리턴 받는 승인번호
	private Date do_date; //결제일자
	private String do_status; //예치금 결제 상태 (주문/결제/완료등등 이런것들...)
	private String do_tel; //구매자 연락처
	private String do_email; //구매자 이메일
}
