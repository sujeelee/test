package kh.st.boot.pagination;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransCriteria extends Criteria {
   private String type;
   private String detail;
}
