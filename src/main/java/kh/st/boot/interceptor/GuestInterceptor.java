package kh.st.boot.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.st.boot.model.vo.MemberVO;

@Component
public class GuestInterceptor implements HandlerInterceptor {

    // login 페이지와 join 페이지 입장 시 처리
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 세션에 저장된 'user' 정보 가져오기
        MemberVO user = (MemberVO) request.getSession().getAttribute("user");

        // 유저 정보가 있으면 home으로 리다이렉트
        if (user != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return false; // 요청을 처리하지 않음
        }
        
        // 유저 정보가 없으면 요청 진행 허용
        return true;
    }
}
