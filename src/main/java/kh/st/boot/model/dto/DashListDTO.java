package kh.st.boot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DashListDTO {
    
    private int stocksQty; //총 보유 주식 수
    private String stockName; //주식명
    private String code; //주식코드
    private String flt; //등락율
    private String vs; //대비
    private int price; //총 보유액 혹은 현재 주식 구매 가격
}
