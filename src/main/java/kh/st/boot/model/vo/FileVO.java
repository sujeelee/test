package kh.st.boot.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileVO {
	
	private int fi_no;
	private String fi_org_name;
	private String fi_path;
	private int fi_num;
	private int fi_reg_no;
	private String fi_type;
	
	public FileVO(String fi_path, String fi_org_name, int fi_reg_no, String fi_type) {
		this.fi_path = fi_path;
		this.fi_org_name = fi_org_name;
		this.fi_reg_no = fi_reg_no;
		this.fi_type = fi_type;
	}
}
