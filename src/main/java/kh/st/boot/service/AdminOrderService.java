package kh.st.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kh.st.boot.dao.AdminOrderDAO;
import kh.st.boot.dao.NewspaperDAO;
import kh.st.boot.model.vo.NewsPaperVO;
import kh.st.boot.model.vo.admOrderPageVO;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.OrderCriteria;
import kh.st.boot.pagination.PageMaker;
import kh.st.boot.pagination.UserCriteria;

@Service
public class AdminOrderService {
	
	@Autowired
	private AdminOrderDAO adminOrderDAO ;


	public List<admOrderPageVO> getAllsltAdminOrder(Criteria cri) {
		return adminOrderDAO.selectAlladminOrder(cri);
	}

	public PageMaker getPageMaker(Criteria cri) {
		int count = adminOrderDAO.selectCountList(cri);
		return new PageMaker(10, cri, count);
	}
	
	public List<admOrderPageVO> searchNameId(String od_name, String mb_id,  String od_id) {
		return 	adminOrderDAO.searchIdName(od_name,mb_id,od_id );
		
	}

	public List<admOrderPageVO> deletOrderNum(String od_id) {
		return adminOrderDAO.deletOrder(od_id);
	}

	public List<admOrderPageVO> getOrderSearch(String od_sh, OrderCriteria cri) {
		return adminOrderDAO.orderSearch(od_sh, cri);
	}
	
	 public PageMaker getPageMakerSearch(OrderCriteria cri, String od_sh) {
	        int totalCount = adminOrderDAO.selectOrderCount(od_sh, cri); // 검색된 전체 결과 수 반환
	        return new PageMaker(10, cri, totalCount); // PageMaker 생성 (displayPageNum을 10으로 설정)
	    }



	
}

