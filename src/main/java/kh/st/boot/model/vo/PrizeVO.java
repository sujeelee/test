package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PrizeVO {
    private int pr_no;
    private String pr_link;
    private String pr_name;
    private int pr_point;
    private String pr_startLevel;
    private String pr_endLevel;
    private int ev_no;
    private int ep_rank;

    //DB에 없습니다.
    private String fi_path;
}
