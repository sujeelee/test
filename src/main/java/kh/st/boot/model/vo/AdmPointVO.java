package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdmPointVO {
	
	
	private int po_no;			// 포인트 기본키 
	private int po_num ;		// 포인트
	private String po_content ;	// 지급사유
	private String po_datetime;	// 지급일
	private String mb_id ;		// 회원아이디 
	
	
	 
}
