package kh.st.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kh.st.boot.interceptor.GuestInterceptor;
import kh.st.boot.interceptor.MemberInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final GuestInterceptor guestInterceptor;
    private final MemberInterceptor memberInterceptor;

    public WebMvcConfig(GuestInterceptor guestInterceptor, MemberInterceptor memberInterceptor) {
        this.guestInterceptor = guestInterceptor;
        this.memberInterceptor = memberInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // GuestInterceptor 적용 경로 설정
        registry.addInterceptor(guestInterceptor)
                .addPathPatterns("/login", "/join"); // 로그인, 회원가입 경로


        // MemberInterceptor 적용 경로 설정
        registry.addInterceptor(memberInterceptor)
                .addPathPatterns("/member/**") // 회원 관련 경로
                .excludePathPatterns("/login", "/join"); // 특정 경로 제외

    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///D:/uploads/");
    }

}
