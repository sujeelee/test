package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminStock_addVO {

private int sa_no; // sa_no						// 증/감가 기본키
	private int sa_qty; // sa_qty				// 증/감가 요청값
	private String mb_id; // mb_id				// 요청한 아이디 
	private String sa_datetime; // sa_datetime	// 작성일
	private String sa_yn; // sa_yn				// 승인여부 
	private String sa_content; // sa_content	// 요청사유 
	private String sa_feedback; // sa_feedback	// 관리자 피드백 
}