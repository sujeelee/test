package kh.st.boot.pagination;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCriteria extends Criteria{

    private String search = ""; // 초기값으로 빈 문자열 설정

}
