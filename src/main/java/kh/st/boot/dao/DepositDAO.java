package kh.st.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kh.st.boot.model.dto.AccountChkDTO;
import kh.st.boot.model.vo.AccountVO;
import kh.st.boot.model.vo.DepositOrderVO;
import kh.st.boot.model.vo.DepositVO;
import kh.st.boot.model.vo.OrderVO;
import kh.st.boot.model.vo.ReservationVO;
import kh.st.boot.model.vo.SendVO;
import kh.st.boot.pagination.TransCriteria;

public interface DepositDAO {

	String findMaxOrderId(@Param("date")String date, @Param("type")String type);

	DepositOrderVO getOrderCheck(@Param("od_id")String do_od_id);

	void insertOrderData(@Param("do")DepositOrderVO newOrder);

	boolean updateOrder(@Param("do")DepositOrderVO upOrder);

	AccountVO getAccount(@Param("mb_no")int mb_no);

	void insertAccountDeposit(@Param("ac")AccountVO ac); 
	
	void updateAccountDeposit(@Param("ac")AccountVO ac, @Param("totalPrice")int totalPrice); 
	
	void insertDepositLog(@Param("de")DepositVO deposit);

	void deleteStatusStay(@Param("mb_id")String mb_id);

	int getCount(@Param("cri")TransCriteria cri, @Param("mb_id")String mb_id);

	List<DepositVO> getDepositMember(@Param("mb_id")String mb_id, @Param("cri")TransCriteria cri);

	List<OrderVO> getOrderMemberBySell(@Param("mb_id")String mb_id);

	List<OrderVO> getOrderMemberByBuy(@Param("mb_id")String mb_id);

	List<OrderVO> getOrderMember(@Param("mb_id")String mb_id);

	List<OrderVO> getOrderMemberBySellDate(@Param("mb_id")String mb_id, @Param("now")String now);

	List<OrderVO> getOrderMemberByBuyDate(@Param("mb_id")String mb_id, @Param("now")String now);

	List<OrderVO> getOrderMemberByDate(@Param("mb_id")String mb_id, @Param("now")String now);

	int getCountByPoint(@Param("cri")TransCriteria cri, @Param("mb_id")String mb_id);

	AccountChkDTO chkAccount(@Param("account")String account);

	boolean insertSend(@Param("se")SendVO send);

	SendVO getSendInfo(@Param("no")String ds_no);

}
