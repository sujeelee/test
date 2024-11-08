package kh.st.boot.service;

import org.springframework.stereotype.Service;

import kh.st.boot.dao.AdminDAO;
import kh.st.boot.model.vo.AdmMemberVO;
import kh.st.boot.model.vo.AdminVO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminServiceImp implements AdminService{
	
	private AdminDAO adminDao;
	
	

	@Override
	public AdminVO getAdminH() {
		return adminDao.selectAdmin();
	}


	@Override
	public boolean admUpdate(AdminVO adminVO) {
		adminDao.updateAdm(adminVO);
		return true;
	}






	

}

