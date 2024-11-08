package kh.st.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kh.st.boot.model.dto.AdMemberCheckDTO;
import kh.st.boot.model.vo.AdmMemberVO;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.UserCriteria;

@Mapper
public interface AdminUserDAO {

	List<AdmMemberVO> selectAdmUser(Criteria cri);

	int selectCountList(Criteria cri);

	AdmMemberVO UseSelect(int mb_no);

	void UseUpdate(AdmMemberVO admMemberVO);

//	AdmMemberVO UserDelete(int mb_no);

	int UserDelete(int mb_no);

	List<AdmMemberVO> selectUser(@Param("use_sh") String use_sh, @Param("cri") UserCriteria cri // UserCriteria로 변경
	);

	int selectUserCount(@Param("use_sh") String use_sh, @Param("cri") UserCriteria cri // UserCriteria로 변경
	);

	void UserInsert(AdmMemberVO admMemberVO);

	int MemberCheck(String mb_id);
	
	
}
