package kh.st.boot.auth;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import kh.st.boot.dao.MemberDAO;
import kh.st.boot.model.util.CustomUser;
import kh.st.boot.model.vo.MemberVO;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private MemberDAO memberDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        // 사용자 정보 확인
        MemberVO member = memberDao.findById(username);

        if (member == null) {
            throw new UsernameNotFoundException(username + " 은/(는) 없는 아이디 입니다.");
        }

        if (member.getMb_out_date() != null) {
            throw new LockedException("어드민에 의해 계정이 정지되었습니다. 관리자에게 문의하세요.");
        }
    
        //로그인 5회 실패로 인한 계정 잠금
        if(member.getMb_fail() >= 5){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime stopTime = member.getMb_stop_date() != null
                    ? member.getMb_stop_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                    : null;
            if (stopTime != null && now.isBefore(stopTime.plusMinutes(20))) {
                throw new LockedException("로그인 5회 이상 실패하였습니다. 잠시 뒤 시도해 주세요.");
            } else if (now.isAfter(stopTime.plusMinutes(20))) {
                memberDao.reset_Fail_Number(username);
                memberDao.reset_stop_time(username);
            }
        }   

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, member.getMb_password())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // 인증 성공 시, 사용자 정보를 담아 Authentication 객체 생성
        CustomUser customUser = new CustomUser(member);
        return new UsernamePasswordAuthenticationToken(customUser, password, customUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
