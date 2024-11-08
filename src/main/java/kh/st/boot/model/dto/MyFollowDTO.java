package kh.st.boot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyFollowDTO {
	private int fo_no; //팔로우 기본키
	private String mb_id; //팔로우 시도한 사람
	private String fo_mb_id; //팔로우한 사람
	
	private String fo_mb_nick; //팔로우한 사람의 닉네임
	private int cnt; //이사람이 작성한 글 개수 
}
