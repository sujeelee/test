package kh.st.boot.model.vo;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberApproveVO {
	
	private int mp_no;			// 신청 기본키
	private String mp_type;		// 주식인지? 뉴스인지?
	private String mp_yn;		// 신청 승인 여부
	private String mp_company;	// 어느 주식? 어느 뉴스 직원인지?
	private Date mp_datetime;	// 신청일자
	private Date mp_app_date;	// 승인/거절 일자
	private int mb_no;			// 회원 번호
	
}
