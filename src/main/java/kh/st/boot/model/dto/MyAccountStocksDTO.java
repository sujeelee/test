package kh.st.boot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyAccountStocksDTO {
    
    private int stocksQty; //총 보유 주식 수
    private int stockOrignPrice; //투자 원금
    private String stockName;
    private String stockCode; 
}
