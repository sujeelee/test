package kh.st.boot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountChkDTO {
    private String mb_id; //회원아이디
    private String account; //회원계좌번호
    private String mb_name; //회원 이름
}
