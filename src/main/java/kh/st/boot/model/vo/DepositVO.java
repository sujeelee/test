package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepositVO {
	private int de_no;//예치금 로그 기본키
	private String de_content; //예치금 정보
	private String de_datetime; //실행된 일자
	private String de_stock_code; //주식을 샀다면?
	private String mb_id; //회원아이디
	private int de_num; //사용/충전된 예치금가
	private int de_before_num; //거래전에 얼마 있으셧어여?
	
	private String content_view; //거래내역에서 조회할때 써볼게용
}
