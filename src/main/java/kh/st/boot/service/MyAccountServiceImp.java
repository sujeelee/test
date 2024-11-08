package kh.st.boot.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kh.st.boot.dao.DepositDAO;
import kh.st.boot.dao.MemberDAO;
import kh.st.boot.dao.MyAccountDAO;
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
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MyAccountServiceImp implements MyAccountService {
	
	private MyAccountDAO myAccountDao;
	private PasswordEncoder passwordEncoder;
	private DepositDAO depositDao;
	private MemberDAO memberDao;
	
	private StockService stockService;
	
	@Override
	public AccountVO getAccountById(String mb_id) {
		if(mb_id == null) {
			return null;
		}
		return myAccountDao.selectAccountById(mb_id);
	}
	
	@Override
	public List<DepositVO> getDepositListByDate(String mb_id, String date) {
		if(mb_id == null || date == null) {
			return null;
		}
		return myAccountDao.selectDepositListByDate(mb_id, date);
	}

	@Override
	public List<PointVO> getPointList(TransCriteria cri, String mb_id) {
		if(mb_id == null) {
			return null;
		}
		return myAccountDao.selectPointList(cri, mb_id);
	}

	@Override
	public boolean checkPw(MemberVO user, String password) {
		if(user == null) {
			return false;
		}
		// matches : 왼쪽에는 암호화 안된 비번, 오른쪽은 암호화된 비번
		if(passwordEncoder.matches(password, user.getMb_password())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updatePw(String mb_id, String mb_password) {
		String encPw = passwordEncoder.encode(mb_password);
		return myAccountDao.updatePw(mb_id, encPw);
	}

	@Override
	public boolean deleteUser(MemberVO user) {
		return myAccountDao.deleteUser(user.getMb_id());
	}

	@Override
	public List<StockVO> getStockList() {
		return myAccountDao.selectStockList();
	}

	@Override
	public MemberApproveVO getMemberApprove(int mb_no) {
		return myAccountDao.selectMemberApprove(mb_no);
	}

	@Override
	public void insertMemberApprove(MemberApproveVO ma) {
		myAccountDao.insertMemberApprove(ma);
	}

	@Override
	public boolean deleteMemberApprove(int mb_no) {
		return myAccountDao.deleteMemberApprove(mb_no);
	}

	@Override
	public String getStockName(String mp_company) {
		return myAccountDao.getStockName(mp_company);
	}
	
	@Override
	public AccountVO getAccountAmt(String mb_id) {
		MemberVO mb = memberDao.findById(mb_id);
		if(mb == null) return null;
		AccountVO ac = depositDao.getAccount(mb.getMb_no());
		return ac;
	}
	
	@Override
	public DepositOrderVO getDepositOrder(String od_id) {
		return depositDao.getOrderCheck(od_id);
	}
	
	@Override
	public PageMaker getPageMaker(TransCriteria cri, String mb_id) {
		int count = depositDao.getCount(cri, mb_id);
		return new PageMaker(2, cri, count);
	}
	
	@Override
	public List<DepositVO> getDepositList(String mb_id, TransCriteria cri) {
		if(mb_id == null) {
			return null;
		}
		return depositDao.getDepositMember(mb_id, cri);
	}

	@Override
	public List<OrderVO> getOrderListBySell(String mb_id) {
		if(mb_id == null) {
			return null;
		}
		return depositDao.getOrderMemberBySell(mb_id);
	}

	@Override
	public List<OrderVO> getOrderListByBuy(String mb_id) {
		if(mb_id == null) {
			return null;
		}
		return depositDao.getOrderMemberByBuy(mb_id);
	}

	@Override
	public List<OrderVO> getOrderList(String mb_id) {
		if(mb_id == null) {
			return null;
		}
		return depositDao.getOrderMember(mb_id);
	}

	@Override
	public List<OrderVO> getOrderListBySellDate(String mb_id, String now) {
		if(mb_id == null || now == null) {
			return null;
		}
		return depositDao.getOrderMemberBySellDate(mb_id, now);
	}
	
	@Override
	public List<OrderVO> getOrderListByBuyDate(String mb_id, String now) {
		if(mb_id == null || now == null) {
			return null;
		}
		return depositDao.getOrderMemberByBuyDate(mb_id, now);
	}

	@Override
	public List<OrderVO> getOrderListByDate(String mb_id, String now) {
		if(mb_id == null || now == null) {
			return null;
		}
		return depositDao.getOrderMemberByDate(mb_id, now);
	}

	@Override
	public PageMaker getPageMakerByPoint(TransCriteria cri, String mb_id) {
		int count = depositDao.getCountByPoint(cri, mb_id);
		return new PageMaker(2, cri, count);
	}

	@Override
	public List<MyAccountStocksDTO> getMyStockList(String mb_id) {
		return myAccountDao.selectMyStockList(mb_id);
	}

	@Override
	public String getMemberStatus(int mb_no, String mb_id) {
		return myAccountDao.selectMemberStatus(mb_no, mb_id);
	}
	
    @Override
	public SendVO getSendInfo(String ds_no) {
		SendVO send = depositDao.getSendInfo(ds_no);
		return send;
	}

	@Override
	public String setContentView(DepositVO tmps) {
		String content_view = "";
		if(tmps.getDe_stock_code() == null || tmps.getDe_stock_code() == "") {
			if(tmps.getDe_content().contains("송금") || tmps.getDe_content().contains("입금")) {		
				String ds_no = tmps.getDe_content().trim().split("고유번호 : ")[1];
				SendVO send = getSendInfo(ds_no);
				if(tmps.getDe_content().contains("입금")) {
					content_view = send.getDs_send_name();
				} else {
					content_view = send.getDs_receive_name();
				}
				
			} else {
				String od_id = tmps.getDe_content().trim().split("주문번호 : ")[1];
				DepositOrderVO dov = getDepositOrder(od_id); 
				content_view = dov.getDo_name();
			}
			
		} else {
			StockVO stock = stockService.getCompanyOne(tmps.getDe_stock_code());
			content_view = stock.getSt_name();
			if(tmps.getDe_content().contains("매수 :")) {
				content_view += tmps.getDe_content().trim().split("매수 :")[1];
			} else {
				content_view += tmps.getDe_content().trim().split("매도 :")[1];
			}
		}
		return content_view;
	}


	@Override
	public boolean deleteMemberStatus(int mb_no, String status) {
		if(status == "none") {
			return false;
		}
		return myAccountDao.deleteMemberStatus(mb_no, status);
	}

	@Override
	public boolean insertStockAdd(String mb_id, int sa_qty, String sa_content) {
		if(mb_id == null) {
			return false;
		}
		return myAccountDao.insertStockAdd(mb_id, sa_qty, sa_content);
	}

	@Override
	public List<StockAddVO> getStockAddList(String mb_id, Criteria cri) {
		if(mb_id == null) {
			return null;
		}
		return myAccountDao.selectStockAddList(mb_id, cri);
	}

	@Override
	public PageMaker SelectPageMaker(Criteria cri, String mb_id) {
		int count = myAccountDao.getCountByStockAdd(mb_id);
		return new PageMaker(5, cri, count);
	}

}
