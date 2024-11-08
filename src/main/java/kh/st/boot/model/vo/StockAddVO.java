package kh.st.boot.model.vo;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockAddVO {
	
	private int sa_no;
	private int sa_qty;
	private String mb_id;
	private Date sa_datetime;
	private String sa_yn;
	private String sa_content;
	private String sa_feedback;
}
