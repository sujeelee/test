package kh.st.boot.dao;

import org.apache.ibatis.annotations.Param;

import kh.st.boot.model.vo.MemberVO;

public interface MemberDAO {
	
	MemberVO findById(@Param("id")String username);

    void serUserCookie(@Param("user")MemberVO user);
    
    void add_Fail_Number(@Param("id")String mb_id);
    
    void reset_Fail_Number(@Param("id")String mb_id);

    Boolean join(@Param("user")MemberVO new_User);

	MemberVO findIdByCookie(@Param("sid")String sid);

	boolean updateUserAccount(@Param("account")String mb_account, @Param("id")String mb_id);

	void insertAccount(String mb_id);

    void updateStopTime(@Param("id")String username);

    void reset_stop_time(@Param("id")String username);

    MemberVO findByEmail(@Param("email")String email);

    boolean setTemporaryPassword(@Param("pw")String encodingOption, @Param("id")String me_id);
    
	boolean updateLevel(@Param("mb")MemberVO member);
}
