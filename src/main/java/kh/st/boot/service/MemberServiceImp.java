package kh.st.boot.service;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kh.st.boot.dao.MemberDAO;
import kh.st.boot.model.dto.JoinDTO;
import kh.st.boot.model.dto.LoginDTO;
import kh.st.boot.model.util.CustomUtil;
import kh.st.boot.model.vo.MemberVO;

@Service
public class MemberServiceImp implements MemberService{

    @Autowired
	private MemberDAO memberDao;
	
    @Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
    public MemberVO login(LoginDTO user_) {
        //null일시 리턴
        if (user_ == null) {
            return null;
        }
        //id가 비었거나, 공백처리 되어있으면 DB처리할 필요 없이 리턴
        if (user_.getUsername() == null || user_.getUsername().trim().length() == 0) {
            return null;
        }
        //pw가 비었거나, 공백처리 되어있으면 DB확인하지 않고 리턴
        if (user_.getPassword() == null || user_.getPassword().trim().length() == 0) {
            return null;
        }
        //id 로 회원정보 DB에서 가져오기
        MemberVO user = findById(user_.getUsername());

        //일치하는 아이디가 없으면 null
        if (user == null) {
            return null;
        }
        //입력한 비번이, 인코딩 된 비번과 일치하는지 확인
        if (passwordEncoder.matches(user_.getPassword(), user.getMb_password())) {
            //다 일치하면 fail 횟수 0 (String id 값을 넘겨주도록 하겠습니다.)
            memberDao.reset_Fail_Number(user.getMb_id());
            //유저 반환
            return user;
        }
        //id는 있는데 비번이 불일치 되었을 시 fail + 1
        memberDao.add_Fail_Number(user.getMb_id());

        return null;
    }  
    // id로 유저 찾기
    public MemberVO findById(String id) {
        MemberVO findUser = memberDao.findById(id);
        return findUser;//없으면 null 리턴
    }

    // 쿠키정보만 수정합니다.
    @Override
    public void setUserCookie(MemberVO user) {
        if (user == null) {
            return;
        }

        memberDao.serUserCookie(user);
    }
    @Override
    public Boolean join(JoinDTO user_) {
        //닉네임과 이름은 중복이 가능하다 가정함
        

        if (user_ == null) {
            return false;
        }

        //아이디 공백 체크
        if (user_.getId() == null || user_.getId().trim().length() == 0) {
            return false;
        }
        //아이디 Regex
        if (!Check_Regex(user_.getId(), "^\\w{8,20}$") && user_.getMb_loginMethod().equals("internal")) {
            return false;
        }
        //비밀번호 공백 체크
        if (user_.getPw() == null || user_.getPw().trim().length() == 0) {
            return false;
        }
        //비밀번호 Regex
        if (!Check_Regex(user_.getPw(), "^[a-zA-Z0-9!@#$]{8,20}$")) {
            return false;    
        }
        //중복 체크
        MemberVO dup_id = findById(user_.getId());
        if (dup_id != null) {
            return false;
        }
        
        //new user 생성
        MemberVO New_User = new MemberVO();// <- 실질적으로 DB에 저장될 VO
        New_User.setMb_id(user_.getId()); //아이디
        New_User.setMb_password(passwordEncoder.encode(user_.getPw()));//인코딩해서 저장
        New_User.setMb_name(user_.getName()); //이름
        New_User.setMb_nick(user_.getNick()); //닉네임
        //전화번호에 - 가 있으면 제거
        if (user_.getPh().contains("-")) {
        	String[] moStr = user_.getPh().split("-");
        	if (moStr.length != 3) {return false;}
        	New_User.setMb_ph(moStr[0] + moStr[1] + moStr[2]);
		} else {
	        New_User.setMb_ph(user_.getPh()); //전화번호
		}
        New_User.setMb_email(user_.getEmail());
        New_User.setMb_birth(user_.getBirth());
        //addr1, 2, zip 넣어주어야 함
        if (user_.getEmailing() == null) {
        	New_User.setMb_emailing((byte) 0);
        } else {
            New_User.setMb_emailing((byte) 1);
        }
        New_User.setMb_zip(user_.getZip());
        New_User.setMb_addr(user_.getAddr());
        New_User.setMb_addr2(user_.getAddr2());
        //디폴트값 추가
        New_User.setMb_fail(0);
        New_User.setMb_level(1);
        New_User.setMb_point(50);

        //카카오 로그인인 경우
        if (user_.getMb_loginMethod().equals("kakao")) {
            New_User.setMb_loginMethod("kakao");
        }
        //네이버 로그인인 경우
        if (user_.getMb_loginMethod().equals("naver")) {
            New_User.setMb_loginMethod("naver");
        }
        


        boolean res = memberDao.join(New_User);
        System.out.println(res);
        if(!res) {
        	return false;
        } else {
        	checkAccountNum(New_User.getMb_id());
        	memberDao.insertAccount(New_User.getMb_id());
        	return true;
        }

    }
    
    private void checkAccountNum(String mb_id) {
    	int result = 0;
    	do {
    		String mb_account = createAccountNum();
    		if(!memberDao.updateUserAccount(mb_account, mb_id)) {
    			result++;
    		}
    	}while(result != 0);
    } // 회원가입이 된 유저에 계좌 번호를 추가하는 메소드입니다.

    //regex해주는 메소드 (str은 문자열, regex는 규칙입니다.)
    private boolean Check_Regex(String str, String regex){
        //자바 유딜 리젝스 페턴
        if (str != null && Pattern.matches(regex, str)) {
            return true;
        }
        return false;
    }

    @Override
    public MemberVO findIdByCookie(String sid) {
        return memberDao.findIdByCookie(sid);
    }
    
	private String createAccountNum() {
		CustomUtil cu = new CustomUtil();
		
		int firstNum = cu.getCustomNumber(2);
		int secondNum = cu.getCustomNumber(6);
		
		String first = String.valueOf(firstNum);
		String second = String.valueOf(secondNum);
		
		if(first.length() < 2) {
			while(first.length() < 2) {
				first = "0" + first;
			}
		}
		
		if(second.length() < 6) {
			while(second.length() < 6) {
				second = "0" + second;
			}
		}
		return "SID-" + first + "-" + second;
	} // 계좌번호 생성 메소드입니다.
    @Override
    public MemberVO findByEmail(String email) {
        if (email.isBlank()) {
            return null;
        }
        return memberDao.findByEmail(email);
    }

    @Override
    public boolean setTemporaryPassword(String email, String option) {
        MemberVO user = findByEmail(email);
        if (user == null) {
            return false;
        }
        String encodingOption = passwordEncoder.encode(option);//인코딩해서 저장
        return memberDao.setTemporaryPassword(encodingOption, user.getMb_id());
    }
	
}
