package kh.st.boot.model.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FollowViewDTO {
	private String st_name; //주식명
	private String st_code; //주식코드
	private String wr_content; //본문 내용
	private Date wr_datetime;//작성일
	private String wr_blind; //신고 게시글인지 확인
}
