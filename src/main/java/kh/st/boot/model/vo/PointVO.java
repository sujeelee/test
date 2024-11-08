package kh.st.boot.model.vo;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PointVO {
	
	private int po_no;			// 포인트 기본키
	private int po_num;			// 포인트 점수
	private String po_content;	// 포인트 지급 사유
	private Date po_datetime;	// 포인트 지급일
	private int po_end_date;	// 포인트 유효기간
	private String mb_id;		// 회원 아이디
	
}
