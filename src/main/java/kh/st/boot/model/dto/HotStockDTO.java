package kh.st.boot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HotStockDTO {
   private int ranks; //몇위인가?
   private int cnt; //총 거래 주식량
   private String st_name; //주식명
   private String code; //주식코드 
   private int price; //한주 가격
   private int vs; //대비
   private String flt; //등락율
   
   private int trqu; //체결수량
   private String price_text; //시가총액 텍스트 
   private String mrk; //시가총액
}
