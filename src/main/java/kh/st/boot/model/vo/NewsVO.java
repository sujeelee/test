package kh.st.boot.model.vo;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsVO {

	private int ne_no;			// 뉴스 기본키
	private int np_no;			// 신문사 기본코드
	private String ne_title;	// 뉴스 제목
	private String ne_content;	// 뉴스 내용
	private int ne_file;		// 첨부된 파일 개수
	private String mb_id;		// 작성 회원
	private Date ne_datetime;	// 작성일
	private Date ne_edit_date; 	// 수정일
	private String ne_name; 	// 작성자(기자)
	private int ne_happy;		// 기뻐요 누적
	private int ne_angry;		// 화나요 누적
	private int ne_absurd;		// 황당해요 누적
	private int ne_sad;			// 슬퍼요 누적
	
	private String np_name; 	// 신문사 이름
	private String fi_path;		// 파일 저장된 패스명
}