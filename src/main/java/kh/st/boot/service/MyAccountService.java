package kh.st.boot.service;

import java.util.List;

import kh.st.boot.model.dto.MyAccountStocksDTO;
import kh.st.boot.model.vo.AccountVO;
import kh.st.boot.model.vo.DepositOrderVO;
import kh.st.boot.model.vo.DepositVO;
import kh.st.boot.model.vo.MemberApproveVO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.model.vo.OrderVO;
import kh.st.boot.model.vo.PointVO;
import kh.st.boot.model.vo.SendVO;
import kh.st.boot.model.vo.StockAddVO;
import kh.st.boot.model.vo.StockVO;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.PageMaker;
import kh.st.boot.pagination.TransCriteria;

public interface MyAccountService {
	
	AccountVO getAccountById(String mb_id);
	
	List<DepositVO> getDepositListByDate(String mb_id, String date);

	List<PointVO> getPointList(TransCriteria cri, String mb_id);

	boolean checkPw(MemberVO user, String password);

	boolean updatePw(String name, String mb_password);

	boolean deleteUser(MemberVO user);

	List<StockVO> getStockList();

	MemberApproveVO getMemberApprove(int mb_no);

	void insertMemberApprove(MemberApproveVO ma);

	boolean deleteMemberApprove(int mb_no);

	String getStockName(String mp_company);

	List<DepositVO> getDepositList(String mb_id, TransCriteria cri);

	PageMaker getPageMaker(TransCriteria cri, String mb_id);

	AccountVO getAccountAmt(String mb_id);

	DepositOrderVO getDepositOrder(String od_id);

	List<OrderVO> getOrderListBySell(String mb_id);

	List<OrderVO> getOrderListByBuy(String mb_id);

	List<OrderVO> getOrderList(String mb_id);

	List<OrderVO> getOrderListBySellDate(String mb_id, String now);
	
	List<OrderVO> getOrderListByBuyDate(String mb_id, String now);

	List<OrderVO> getOrderListByDate(String mb_id, String now);

	PageMaker getPageMakerByPoint(TransCriteria cri, String mb_id);

	List<MyAccountStocksDTO> getMyStockList(String mb_id);

	String getMemberStatus(int mb_no, String mb_id);

	SendVO getSendInfo(String ds_no);

	String setContentView(DepositVO tmps);

	boolean deleteMemberStatus(int mb_no, String status);

	boolean insertStockAdd(String mb_id, int sa_qty, String sa_content);

	List<StockAddVO> getStockAddList(String mb_id, Criteria cri);

	PageMaker SelectPageMaker(Criteria cri, String mb_id);


}
