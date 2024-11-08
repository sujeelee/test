package kh.st.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kh.st.boot.model.vo.AdminVO;

@Mapper
public interface AdminDAO {
	

	// DB값 받아오기
	List<AdminVO> selectAdminList();

	AdminVO selectAdmin();

	void updateAdm(AdminVO adminVO);

	AdminVO selectAdmUser();

}
