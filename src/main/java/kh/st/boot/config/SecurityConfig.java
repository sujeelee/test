package kh.st.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import kh.st.boot.auth.CustomAuthenticationProvider;
import kh.st.boot.dao.MemberDAO;
import kh.st.boot.handler.LoginFailHandler;
import kh.st.boot.handler.LoginSuccessHandler;
import kh.st.boot.model.util.UserRole;
import kh.st.boot.service.MemberDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	@Autowired
	private MemberDetailService memberDetailService;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private MemberDAO memberDao;
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//csrf : 사이트간 공격을 막아줄때 사용하는 
		//URL에 접근 권한을 설정. MemberInterceptor, AdminInterceptor를 합친 기능이라고 생각하면 됨
        http.csrf(csrf ->csrf.disable())
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/post/insert/*", "/newspaper/insert", "/event/calendar_event", "/event/Aevent/*")//<<로그인 되기전에는 접근할 수 없어요
                //.hasAuthority(UserRole.USER.name())
                //위 URL을 권한이 "USER"인 회원만 접근하도록 설정
                //.hasRole(UserRole.USER.name())
                //위 URL권한이 "ROLE_USER"인 회원만 접근하도록 설정
                .hasAnyAuthority(UserRole.USER.name(), UserRole.ADMIN.name(), UserRole.STOCK.name(), UserRole.NEWS.name()) //여러 권한 설정
                .requestMatchers("/admin/**").hasAnyAuthority(UserRole.ADMIN.name())
                .anyRequest().permitAll()  // 그 외 요청은 인증 필요
            )
            .formLogin((form) -> form
                .loginPage("/member/login")  // 커스텀 로그인 페이지 설정
                .permitAll()           // 로그인 페이지는 접근 허용
                .loginProcessingUrl("/member/login") //실제 로그인 되는 곳
//                .usernameParameter("userId") //아이디 파라미터 명
//                .passwordParameter("password") // 비밀번호 파라미터 명
                .successHandler(new LoginSuccessHandler(memberDao))
                .failureHandler(new LoginFailHandler(memberDao))
            )
            .rememberMe((rm)->rm
            		.key("team1")
            		.rememberMeParameter("re")
            		.userDetailsService(memberDetailService)
            		.rememberMeCookieName("AUTO_LOGIN")//저장할 쿠키명
            		.tokenValiditySeconds(60*60*24*7)//쿠키 만료시간
            )
            .logout((logout) -> logout
            		.logoutUrl("/member/logout") //이 URL로  post방식으로 전송하면 자동으로 로그아웃이 실행됨
            		.logoutSuccessUrl("/")
            		.clearAuthentication(true)
            		.invalidateHttpSession(true)
            		.deleteCookies("AUTO_LOGIN", "JSESSIONID") // 로그아웃 성공 시 제거할 쿠키명그아웃 성공 시 제거할 쿠키명
            		.permitAll());  // 로그아웃도 모두 접근 가능
        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.authenticationProvider(customAuthenticationProvider);
        return auth.build();
    }

}
