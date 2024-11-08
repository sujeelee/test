package kh.st.boot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FollowInfoDTO {
	private String mb_nick; //팔로우회원 닉네임
	private int follower; //팔로워
	private int following;//팔로잉
	private int fo_no; //팔로우상세 들어온 사람의 팔로우 목록에서 기본키를 가져옴
}
