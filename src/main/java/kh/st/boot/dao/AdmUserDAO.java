package kh.st.boot.dao;

import java.util.List;

import kh.st.boot.model.vo.AdmMemberVO;

public interface AdmUserDAO {

	List<AdmMemberVO> AdmUserSearch();

	 void AdmUserUpdate(String mb_id, String mb_name, String mb_nick, String mb_hp, String mb_stop_date);

	void AdmUserDelet(String mb_id, String mb_name, String mb_nick, String mb_hp, String mb_datetime);

}
