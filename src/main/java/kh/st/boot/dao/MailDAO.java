package kh.st.boot.dao;

import kh.st.boot.model.vo.MailVO;

public interface MailDAO {

	boolean setMailCode(String evc_email, int code);
	
	MailVO getMailCode(String evc_email);

    boolean deleteMailCodeByMail(String evc_email);
    
}
