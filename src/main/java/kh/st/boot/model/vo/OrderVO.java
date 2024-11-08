package kh.st.boot.model.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderVO {

	private String od_id;			// 주문 번호
	private String od_name;			// 주문자명
	private String mb_id;			// 주문자 아이디
	private int od_price;			// 결제금액
	private int od_point;			// 주문 금액에 따른 지급될 포인트
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Date od_date;			// 결제일
	private String od_status;		// 주문 상태
	private String od_st_code;		// 주식 코드
	private String od_st_name; 		// 주식명
	private int od_qty; 			// 주식 주 개수
	private int od_st_price; 		// 주식 주당 가격
	private int od_percent_price;	// 주식 거래 수수료 금액
}
