package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsPaperVO {
	
	private int np_no;		// 신문사 기본 코드
	private String np_name;	// 신문사명
	private byte np_use;	// 사용여부
}

