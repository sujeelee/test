package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminLevelPageVO {

	private String lv_name; 
	private int lv_num;  
	private String lv_alpha; 
	private String lv_auto_use;
	private int lv_up_limit;
	
	
}
