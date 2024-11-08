package kh.st.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kh.st.boot.info.NaverUserInfo;
import kh.st.boot.service.NaverService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class NaverLoginController {

    @Autowired
    private NaverService naverService;

    @GetMapping("oauth/naver/callback")
    public String callback(@RequestParam("code") String code, Model mo) {

        try {
            // 네이버 API -> 엑세스 토큰 get (내 사이트가 네이버의 인증을 받았는가?)
            String accessToken = naverService.getAccessTokenFromNaver(code);
            // 엑세스 토큰 -> 사용자 정보 get (인증받은 사이트라면 사용자 정보 받아오기)
            NaverUserInfo userInfo = naverService.getUserInfo(accessToken);

            // 사용자 정보가 없을 경우 에러 처리
            if (userInfo == null || userInfo.getResponse() == null) {
                log.error("네이버 로그인 컨트롤러 userInfo null");
                return "error"; // 에러 페이지 (미작성)
            }

            log.info("Naver accessToken: {}", accessToken);
            // log.info("Naver userInfo: {}", userInfo); //민감 정보라 주석처리
            log.info("Naver userInfo email: {}", userInfo.getResponse().getEmail());

            String info = "Naver";
            mo.addAttribute("where", info);
            mo.addAttribute("userPhone", userInfo.getResponse().getPhone());
            mo.addAttribute("userInfo", userInfo.getResponse().getEmail());// 이메일을 아이디로 사용
            // 회원가입 페이지로 이동 후 이메일을 아이디, 인증 요소로 사용
            return "member/join";

        } catch (Exception e) {
            log.error("네이버 로그인 컨트롤러 오류 발생 : {}", e);
            return "error"; // 에러 페이지
        }

    }

}
