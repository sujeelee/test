package kh.st.boot.dao;

import java.util.List;

import kh.st.boot.model.vo.AdmPointVO;
import kh.st.boot.pagination.AdmPointCriteria;
import kh.st.boot.pagination.Criteria;


public interface AdmPointDAO {

	List<AdmPointVO> selectAll(Criteria cri);

	List<AdmPointVO> selectId(String mb_id);

	void deletPoint(int po_no);

	int selectCountList(Criteria cri);

	List<AdmPointVO> pointUserSearch(AdmPointCriteria cri);

	int selectTotalCount(AdmPointCriteria cri);

	void upPoint(String mb_id, int po_num, String po_content);

	void downPoint(String mb_id, int po_num, String po_content);

	void updateUserPoint(String mb_id, int po_num);

	
}
