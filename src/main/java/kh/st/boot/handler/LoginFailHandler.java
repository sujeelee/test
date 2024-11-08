package kh.st.boot.handler;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.st.boot.dao.MemberDAO;
import kh.st.boot.model.vo.MemberVO;

public class LoginFailHandler implements AuthenticationFailureHandler {

    private MemberDAO memberDao;

    @Autowired
    public LoginFailHandler(MemberDAO memberDao){
        this.memberDao = memberDao;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            
        MemberVO user = memberDao.findById(request.getParameter("username"));
        //전달할 오류 msg
        String errorMessage = "errorType";

        //아이디 비존재
        if(exception instanceof UsernameNotFoundException){
            errorMessage = "아이디나 비밀번호가 일치하지 않습니다.";

            String encodingErrorMsg = URLEncoder.encode(errorMessage, "UTF-8");
            response.sendRedirect("/member/login?error=true&id=false&pw=false&msg=" + encodingErrorMsg);
        }

        //계정 5회 실패
        if (exception instanceof LockedException) {

            if (user.getMb_out_date() != null) {
                errorMessage = "어드민에 의해 계정이 정지되었습니다. 관리자에게 문의하세요.";
                String encodingErrorMsg = URLEncoder.encode(errorMessage, "UTF-8");
                response.sendRedirect("/member/login?error=true&username=" + request.getParameter("username") + "&pw=false&pw=false&msg=" + encodingErrorMsg);
            }

            errorMessage = "로그인 5회 이상 실패하였습니다. 20분 뒤 시도해 주세요.";
            String encodingErrorMsg = URLEncoder.encode(errorMessage, "UTF-8");
            response.sendRedirect("/member/login?error=true&username=" + request.getParameter("username") +"&pw=false&msg=" + encodingErrorMsg);
        }

        //비밀번호 불일치
        if(exception instanceof BadCredentialsException){
            errorMessage = "아이디나 비밀번호가 일치하지 않습니다.";
            memberDao.add_Fail_Number(request.getParameter("username"));
            
            //4 -> 5 일때 DB에 시간 집어넣기
            if(user.getMb_fail() + 1 >= 5){
                memberDao.updateStopTime(request.getParameter("username"));
            }

            String encodingErrorMsg = URLEncoder.encode(errorMessage, "UTF-8");
            response.sendRedirect("/member/login?error=true&id=false&pw=false&msg=" + encodingErrorMsg);
        }


    }



}
