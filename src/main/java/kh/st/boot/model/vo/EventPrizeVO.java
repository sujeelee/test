package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventPrizeVO {
    private int ep_no;
    private int ev_no; // 이벤트 넘버
    private int pr_no; // prize Number
    private int ep_count; // counter
    private String ep_prize; // prize Name (A type Event, pr_link is get this)
    private String ep_mb_id; // 이벤트 참여자 아이디
    private int ep_rank;
}
