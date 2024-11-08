package kh.st.boot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyStocksDTO {
    
    private int stocksQty; //총 보유 주식 수
    private int stockOrignPrice; //투자 원금
    
    private int stockNowPrice; //지금 금액 기준으로 총 금액 알려주기
    private int stockAverage; //1주 평균 금액
    private int stockPercent; //현재 다 팔면 거래 수수료 예상
}
