package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SendVO {
	private int ds_no; //송금기본키
	private String ds_send_name; //보낸사람
	private String ds_receive_name; //받은사람
	private String ds_receive_account; //받은사람계좌
	private String ds_datetime; //보낸일시
	private char ds_favorite; //즐겨찾기한 계정인지
	private String mb_id; //보낸 회원 아이디
	private String ds_re_mb_id; //받은 회원 아이디
	private String ds_send_price; //보낸 금액
}
