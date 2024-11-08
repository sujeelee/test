package kh.st.boot.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import kh.st.boot.dao.DepositDAO;
import kh.st.boot.model.dto.AccountChkDTO;
import kh.st.boot.model.vo.AccountVO;
import kh.st.boot.model.vo.DepositOrderVO;
import kh.st.boot.model.vo.DepositVO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.model.vo.SendVO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor	
public class DepositService {
	
	private DepositDAO depositDao;
	private MemberService memberService;
	
	@Transactional
	public String getOrderId(String type) {
		LocalDate now = LocalDate.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
        
        // 현재 날짜로 시작하는 가장 큰 주문 번호 찾기
        String maxOrderId = depositDao.findMaxOrderId(date, type);
        
        int newOrderIdNumber;
        if (maxOrderId == null) {
            // 해당 날짜로 시작하는 주문이 없는 경우 1로 시작
            newOrderIdNumber = 1;
        } else {
            // 마지막 4자리 숫자를 가져와서 1 증가
            int lastOrderIdNumber = Integer.parseInt(maxOrderId.substring(6));
            newOrderIdNumber = lastOrderIdNumber + 1;
        }

        // 새로운 주문 번호 생성 (yyMMdd + 4자리 숫자)
        String newOrderId = date + String.format("%04d", newOrderIdNumber);
        
		return newOrderId;
	}

	public String insertOrder(DepositOrderVO newOrder) {
		String od_id = newOrder.getDo_od_id();
		DepositOrderVO chk = depositDao.getOrderCheck(od_id);
		if(chk != null) {
			od_id = getOrderId("");
			newOrder.setDo_od_id(od_id);
		}
		depositDao.insertOrderData(newOrder);
		
		return od_id;
	}

	@SuppressWarnings("null")
	public boolean updateOrder(DepositOrderVO upOrder, Model model) {
		DepositOrderVO chk = depositDao.getOrderCheck(upOrder.getDo_od_id());
		
		if(chk == null) {
			return false;
		}
		
		MemberVO mb = (MemberVO) model.getAttribute("member");
		
		if(mb == null) return false;
		
		AccountVO ac = depositDao.getAccount(mb.getMb_no());
		
		DepositVO deposit = new DepositVO();
		
		deposit.setDe_content("예치금 충전 : " + chk.getDo_price() + "원 주문번호 : " + upOrder.getDo_od_id());
		deposit.setDe_num(chk.getDo_price());
		deposit.setMb_id(mb.getMb_id());
		deposit.setDe_before_num(0);
		
		if(ac == null) {
			AccountVO newaAc = new AccountVO();
			newaAc.setMb_no(mb.getMb_no());
			newaAc.setAc_deposit(chk.getDo_price());
			depositDao.insertAccountDeposit(newaAc);
		} else {
			deposit.setDe_before_num(ac.getAc_deposit());
			depositDao.updateAccountDeposit(ac, chk.getDo_price());
		}
		
		depositDao.insertDepositLog(deposit);
		
		return depositDao.updateOrder(upOrder);
	}

	public void deleteStatusStay(String mb_id) {
		depositDao.deleteStatusStay(mb_id);
	}
	
	public AccountChkDTO chkAccount(String account) {
		return depositDao.chkAccount(account);
	}

	public boolean sendInsert(Map<String, String> form) {
		
		SendVO send = new SendVO();
		
		send.setDs_send_name(form.get("send_name"));
		send.setDs_receive_name(form.get("resv_name"));
		send.setDs_receive_account(form.get("resv_acc"));
		send.setDs_send_price(form.get("price"));
		send.setMb_id(form.get("mb_id"));
		send.setDs_re_mb_id(form.get("resv_id"));
		
		MemberVO sendMb = memberService.findById(form.get("mb_id"));
		MemberVO resvMb = memberService.findById(form.get("resv_id"));
		
		DepositVO sendDeposit = new DepositVO();
		DepositVO resvDeposit = new DepositVO();
		
		int oldDeposit = sendMb.getDeposit();
		int sendPrice = Integer.parseInt(form.get("price"));
		
		int finalDeposit = oldDeposit - sendPrice;
		
		if(finalDeposit < 0) {
			return false;
		}
		
		boolean chkSend = depositDao.insertSend(send);
		
		if(chkSend == true) {
			sendDeposit.setDe_content("예치금 송금 : -" + sendPrice + "원 송금고유번호 : " + send.getDs_no());
			sendDeposit.setDe_num(-sendPrice);
			sendDeposit.setMb_id(sendMb.getMb_id());
			sendDeposit.setDe_before_num(oldDeposit);
			
			AccountVO resvAc = depositDao.getAccount(resvMb.getMb_no());
			AccountVO sendAc = depositDao.getAccount(sendMb.getMb_no());
			
			resvDeposit.setDe_before_num(0);
			
			if(resvAc == null) {
				AccountVO newResvAc = new AccountVO();
				newResvAc.setMb_no(resvMb.getMb_no());
				newResvAc.setAc_deposit(sendPrice);
				depositDao.insertAccountDeposit(newResvAc);
			} else {
				resvDeposit.setDe_before_num(resvMb.getDeposit());
				depositDao.updateAccountDeposit(resvAc, sendPrice);
			}
			
			depositDao.updateAccountDeposit(sendAc, -sendPrice);
			
			resvDeposit.setDe_content("예치금 입금 : " + sendPrice + "원 송금고유번호 : " + send.getDs_no());
			resvDeposit.setDe_num(sendPrice);
			resvDeposit.setMb_id(resvMb.getMb_id());
			
			depositDao.insertDepositLog(sendDeposit);
			depositDao.insertDepositLog(resvDeposit);
		}
		
		return chkSend;
	}
}
