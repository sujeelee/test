package kh.st.boot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kh.st.boot.info.KakaoUserInfo;
import kh.st.boot.service.KakaoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class KakaoLoginController {

    @Autowired
    private KakaoService kakaoService;

    @GetMapping("/oauth/kakao/callback")
    public String callback(@RequestParam("code") String code, Model mo) {

        try {
            String accessToken = kakaoService.getAccessTokenFromKakao(code);        
            KakaoUserInfo userInfo = kakaoService.getUserInfo(accessToken); 

            if (userInfo == null || userInfo.getKakaoAccount() == null) {
                System.out.println("카카오 로그인 컨트롤러 userInfo null");
                return "error"; // 에러 페이지
            }

            // if (userInfo != null) {
            //     kakaoService.kakaoLogout(accessToken); //로그인해서 정보만 가져온 뒤 로그아웃
            //     System.out.println("카카오 로그아웃 완료");
            // }
            //카카오 로그인 >> 정보를 가져온 뒤 DB에 저장 >> 카카오 로그아웃

            log.info("Kakao accessToken: {}", accessToken);
            log.info("Kakao userInfo email: {}", userInfo.getKakaoAccount().getEmail());

            String info = "Kakao";
            mo.addAttribute("where", info);
            mo.addAttribute("userInfo", userInfo.getKakaoAccount().getEmail());
            //회원가입 페이지로 이동 후 이메일을 아이디, 인증 요소로 사용
            return "member/join"; // join.html로 렌더링
        } catch (Exception e) {
            log.error("카카오 로그인 컨트롤러 오류 발생 : {}", e);
            return "error"; // 에러 페이지
        }
    }
}







