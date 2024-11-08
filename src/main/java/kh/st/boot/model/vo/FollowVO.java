package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FollowVO {
    private int fo_no; //팔로우 기본키
    private String mb_id; //팔로우 시도한 사람
    private String fo_mb_id; //팔로우한 사람
}
