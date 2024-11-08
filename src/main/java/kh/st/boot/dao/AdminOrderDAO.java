package kh.st.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kh.st.boot.model.vo.admOrderPageVO;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.OrderCriteria;
import kh.st.boot.pagination.UserCriteria;

@Mapper
public interface AdminOrderDAO {

		List<admOrderPageVO> selectAlladminOrder(Criteria cri);

		List<admOrderPageVO> searchIdName(String od_name, String mb_id, String od_id);


		List<admOrderPageVO> deletOrder(String od_id);

		int selectCountList(Criteria cri);

		List<admOrderPageVO> orderSearch(String od_sh, OrderCriteria cri);

		int selectOrderCount(String od_sh, OrderCriteria cri);

		

	}


