package kh.st.boot.pagination;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class StockCriteria extends Criteria {
   private List<String> st_type;
   private String sfl;
   private String stx;
   private String state;
   private String mrk;
}
