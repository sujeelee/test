package kh.st.boot.service;

import org.springframework.stereotype.Service;

import kh.st.boot.dao.MailDAO;
import kh.st.boot.dao.MemberDAO;
import kh.st.boot.model.vo.MailVO;
import kh.st.boot.model.vo.MemberVO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailServiceImp implements MailService{

    private MailDAO mailDao;
	private MemberDAO memberDao;
	@Override
	public boolean setMailCode(String evc_email, int code) {
		if (evc_email == null) {
            return false;
        }
		MemberVO user = memberDao.findByEmail(evc_email);

		if (user != null) {
			return false;
		}

		return mailDao.setMailCode(evc_email, code);
	}

	@Override
	public boolean checkMailCode(String evc_email, int code) {
		MailVO mailVO = mailDao.getMailCode(evc_email);
		if (mailVO.getEvc_code() == code) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteMailCheckOnDB(String evc_email) {
		if (evc_email == null) {
			return false;
		}
		return mailDao.deleteMailCodeByMail(evc_email);
	}
    
}
