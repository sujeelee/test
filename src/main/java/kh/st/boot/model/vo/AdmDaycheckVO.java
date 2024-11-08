package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdmDaycheckVO {
	private int dc_no ;
	private String dc_datetime;
	private String mb_id;
	private String dc_days;
	private int po_num;
	private int countDay; // 이번달 출석일을 전달합니다 
}
