package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailVO {
    private int evc_id;
    private String evc_email;
    private int evc_code;
}
