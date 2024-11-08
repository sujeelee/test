package kh.st.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kh.st.boot.dao.NewspaperDAO;
import kh.st.boot.model.vo.AdmMemberVO;
import kh.st.boot.model.vo.NewsPaperVO;
import kh.st.boot.pagination.AdmNewsCriteria;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.PageMaker;

@Service
public class newspaperService {
	
	@Autowired
	private NewspaperDAO newspaperDAO;

	public List<NewsPaperVO> getAllNewspapers(Criteria cri) {
		return newspaperDAO.selectAllNewspapers(cri);
	}

	public boolean addNewspaper(String np_name, byte np_use) {
		NewsPaperVO oldNews = newspaperDAO.getNewsOne(np_name);
		if (oldNews != null) {
			return false;
		}
		return newspaperDAO.insertNewspaper(np_name, np_use);
	}

	public boolean updateNewspaper(String np_name, byte np_use) {
		NewsPaperVO oldNews = newspaperDAO.getNewsOne(np_name);
		if (oldNews != null) {
			return false;
		}
		return newspaperDAO.insertNewspaper(np_name, np_use);
	}
	public void deleteNewspaper(NewsPaperVO NewsPaperVO) {
		newspaperDAO.deleteNewspaper(NewsPaperVO);
	}


	public PageMaker getPageMaker(Criteria cri) {
		int count = newspaperDAO.selectCountList(cri);
		return new PageMaker(10, cri, count);
	}

	public List<AdmMemberVO> getSearchNews(AdmNewsCriteria cri) {
		return newspaperDAO.searchNews(cri);
	}

	public PageMaker getPageMakerSearch(AdmNewsCriteria cri) {
		int totalCount = newspaperDAO.selectTotalCount(cri);
		return new PageMaker(10, cri, totalCount);
	}

	public boolean getUseChange(String np_no, byte useByte) {
		newspaperDAO.UseChange(np_no, useByte);
		return true;
	}

}