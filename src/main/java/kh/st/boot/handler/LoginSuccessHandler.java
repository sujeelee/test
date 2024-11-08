package kh.st.boot.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.st.boot.dao.MemberDAO;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private MemberDAO memberDao;

    @Autowired
    public LoginSuccessHandler(MemberDAO memberDao){
        this.memberDao = memberDao;
    }

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication) throws IOException, ServletException {
		
        //실패 횟수 초기화
        memberDao.reset_Fail_Number(request.getParameter("username"));

		String prevPage = (String) request.getSession().getAttribute("prevPage");
        if (prevPage != null) {
            request.getSession().removeAttribute("prevPage");
        }

        // 기본 URI
        String uri = "/";
        
        if (prevPage != null && !prevPage.equals("")) {
            // 회원가입 - 로그인으로 넘어온 경우 "/"로 redirect
            // 비밀번호 찾기 > 로그인 인경우 메인으로
            if (prevPage.contains("/member/join") || prevPage.contains("/member/findPwView")) {
                uri = "/";
            } else {
                uri = prevPage;
            }
        }
		response.sendRedirect(uri);// 이전 페이지로

	}

}
