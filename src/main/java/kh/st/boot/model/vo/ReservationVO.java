package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationVO {
	private int re_no; //예약 기본키
	private String re_datetime; //예약 신청일
	private String mb_id; //회원 아이디
	private int re_want_price; //희망금액
	private String re_st_code; //신청한 주식코드
	private int re_qty; //수량
	private String re_state; //예약 상태
	private String re_done_date; //예약체결일
}
