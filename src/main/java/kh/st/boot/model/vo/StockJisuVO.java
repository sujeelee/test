package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockJisuVO {
	private int ji_no; //지수정보 기본키
	private String ji_type; //코스닥인지 코스피인지 확인용
	private String ji_date; //기준일자
	private String ji_clpr;//종가
	private String ji_vs; //대비
	private String ji_fltRt; //등락율
	private String ji_hipr; //최고가
	private String ji_lopr; //최저가
	private String ji_trqu; //거래량
	private String ji_mkp; //시가
}
