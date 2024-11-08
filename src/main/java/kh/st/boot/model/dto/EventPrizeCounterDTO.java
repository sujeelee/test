package kh.st.boot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventPrizeCounterDTO {
    private int ev_no;
    private int pr_no;
    private int sumCount;
}
