package kh.st.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kh.st.boot.dao.AdmDaycheckDAO;
import kh.st.boot.model.vo.AdmDaycheckVO;
import kh.st.boot.pagination.AdmDayCheckCriteria;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.PageMaker;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PointSltIdPageService {

	@Autowired
	private AdmDaycheckDAO admDaycheckDAO;

	public List<AdmDaycheckVO> sltAllDay(Criteria cri) {

		List<AdmDaycheckVO> allDay = admDaycheckDAO.AllSelect(cri);

		for (AdmDaycheckVO dayNum : allDay) {
			String dcDays = dayNum.getDc_days();

			if (dcDays != null) {
				int count = countOnes(dcDays); // dc_days의 1 개수 세기
				dayNum.setCountDay(count); // countList에 추가
			}
		}
		return allDay;
	}
	public List<AdmDaycheckVO> sltOneDay(String mb_id) {  

		List<AdmDaycheckVO> allDay = admDaycheckDAO.OneSelect(mb_id);

		for (AdmDaycheckVO dayNum : allDay) {
			String dcDays = dayNum.getDc_days();

			if (dcDays != null) {
				int count = countOnes(dcDays); // dc_days의 1 개수 세기
				dayNum.setCountDay(count); // countList에 추가
			}
		}
		return allDay;
	}

	private int countOnes(String dcDays) {
		return (dcDays.length() - dcDays.replace("1", "").length());
	}

	public PageMaker getPageMaker(Criteria cri) {
		int count = admDaycheckDAO.selectCountList(cri);
		return new PageMaker(10, cri, count);
	}
	
	public List<AdmDaycheckVO> daychSearch(AdmDayCheckCriteria cri) {
		
		List<AdmDaycheckVO> searchDay = admDaycheckDAO.searchDaycheck(cri);
		
		for (AdmDaycheckVO dayNum : searchDay) {
			String dcDays = dayNum.getDc_days();

			if (dcDays != null) {
				int count = countOnes(dcDays); 
				dayNum.setCountDay(count); 
			}
		}
		return searchDay;
		
	}
	public PageMaker getPageMakerSearch(AdmDayCheckCriteria cri) {
		int totalcount = admDaycheckDAO.selectTotalCount(cri);
		return new PageMaker(10, cri, totalcount);
	}
	
}
