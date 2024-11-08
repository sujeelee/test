package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockPriceVO {
	private int si_id; //기본키
	private String si_date; //정보값을 뿌려준 날짜
	private int si_price; //가격
	private String st_code; //주식 주 고유 번호
	private String si_vs; //주식 대비
	private String si_fltRt; //등락율
	private String si_mrkTotAmt; //시가총액
	private String si_hipr; //하루 최고 고가
	private String si_lopr; //하루 최저가
	private String si_trqu; //체결수량 누적합계
	
	private String price_text; //~조원으로 보여줄래
	private int yesterPrice; //어제일자 시작가가 필요헤
}
