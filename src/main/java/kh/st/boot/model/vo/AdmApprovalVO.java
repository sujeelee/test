package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdmApprovalVO {

	private int mp_no;
	private String mp_type ;
	private String mp_yn ;
	private String mp_company;
	private String mp_datetime;
	private String mp_app_date;
	private int mb_no;
	private String mb_id; 
}
