package kh.st.boot.pagination;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdmNewsCriteria extends Criteria{

    private String np_name = ""; // 초기값으로 빈 문자열 설정

}
