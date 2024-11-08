package kh.st.boot.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kh.st.boot.dao.MemberDAO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.model.util.CustomUser;

@Service
public class MemberDetailService implements UserDetailsService{

	@Autowired
	MemberDAO memberDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO member = memberDao.findById(username);

		return new CustomUser(member);
	}

}