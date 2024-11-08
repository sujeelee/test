package kh.st.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kh.st.boot.dao.SltAdmLevelPageDAO;
import kh.st.boot.model.vo.AdmMemberVO;
import kh.st.boot.model.vo.AdminLevelPageVO;

@Service
public class SltAdmLevelPageService {

	@Autowired
	private SltAdmLevelPageDAO sltAdmLevelPageDAO;

	public List<AdminLevelPageVO> getAllssltAdminLevelPage() {
		return sltAdmLevelPageDAO.getAllssltAdminLevelPage();
	}

	public boolean addSltAdmLevel(String lv_name, int lv_num, String lv_alpha, char lv_auto_use, int lv_up_limit) {

		AdminLevelPageVO oldLv = sltAdmLevelPageDAO.getLvOne(lv_num);
		if (oldLv != null) {
			return false;
		}
		return sltAdmLevelPageDAO.insertAdmLv(lv_name, lv_num, lv_alpha, lv_auto_use, lv_up_limit);
	}

	public void dltAdmLvService(AdminLevelPageVO AdminLevelPageVO) {
		sltAdmLevelPageDAO.dltAdmLvdao(AdminLevelPageVO);
	}

	public boolean admLevUpdate(AdminLevelPageVO admLevVO) {
		return sltAdmLevelPageDAO.updateAdmLev(admLevVO);
	}


	public AdminLevelPageVO getAdmlevSel(int lv_num) {
		return sltAdmLevelPageDAO.selectAdmLev(lv_num);
	}
}
