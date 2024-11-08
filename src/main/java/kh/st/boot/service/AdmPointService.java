package kh.st.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kh.st.boot.dao.AdmPointDAO;
import kh.st.boot.model.vo.AdmPointVO;
import kh.st.boot.pagination.AdmPointCriteria;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.PageMaker;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdmPointService {

	@Autowired
	private AdmPointDAO admPointDAO;

	
	public List<AdmPointVO> allselect(Criteria cri) {
		return 	admPointDAO.selectAll(cri);
	}


	public List<AdmPointVO> idSelect(String mb_id) {
		return 	admPointDAO.selectId(mb_id);
	}


	public void plusminus(String mb_id, int po_num,String pointType, String po_content) {
		if(pointType.equals("plus")) {
			admPointDAO.upPoint(mb_id,po_num,po_content);
			admPointDAO.updateUserPoint(mb_id, po_num);
		}else if(pointType.equals("minus")) {
			po_num = -po_num;
			System.out.println(po_num);
			admPointDAO.downPoint(mb_id,po_num,po_content);
			admPointDAO.updateUserPoint(mb_id, po_num);
		}
		
	}


	public void delete(int po_no) {
		admPointDAO.deletPoint(po_no);
	}


	public PageMaker getPageMaker(Criteria cri) {
		int count = admPointDAO.selectCountList(cri);
		return new PageMaker(10, cri, count);
	}


	public List<AdmPointVO> getPointUserSearch(AdmPointCriteria cri) {
		return 	admPointDAO.pointUserSearch(cri);
	}


	public PageMaker getPageMaker(AdmPointCriteria cri) {
		int totalCount = admPointDAO.selectTotalCount(cri);
		return new PageMaker(10, cri, totalCount);
	}


}
