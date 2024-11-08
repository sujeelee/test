package kh.st.boot.dao;

import java.util.List;

import kh.st.boot.model.vo.AdminStock_addVO;

public interface AdminStock_addDAO {
	
	List<AdminStock_addVO> nullSelectAll();

	AdminStock_addVO SelectAll(int sa_qty, String mb_id);

	void updateAll(int sa_no, String sa_yn, String sa_feedback);

	List<AdminStock_addVO> search(String mb_id);

	void styUpdate(int sa_qty,String mb_id);


}

