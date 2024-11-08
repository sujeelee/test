package kh.st.boot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.codec.http.HttpHeaderValues;
import kh.st.boot.info.NaverToken;
import kh.st.boot.info.NaverUserInfo;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class NaverService {

    private final String clientId;
    private final String clientSecret;
    private final String NAVER_TOKEN_URL_HOST = "https://nid.naver.com";
    private final String NAVER_USER_URL_HOST = "https://openapi.naver.com";

    public NaverService(
            @Value("${naver.client_id}") String clientId,
            @Value("${naver.client_secret}") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getAccessTokenFromNaver(String code) {
        NaverToken naverToken = WebClient.create(NAVER_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth2.0/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(NaverToken.class)
                .block();

        // 액세스 토큰을 로깅하거나 추가 작업을 수행할 수 있습니다.
        log.info(" [Naver Service] Access Token ------> {}", naverToken.getAccessToken());

        return naverToken.getAccessToken();
    }

    public NaverUserInfo getUserInfo(String accessToken) {
        NaverUserInfo userInfo =  WebClient.create(NAVER_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/nid/me")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(NaverUserInfo.class) // NaverUserInfo 객체로 반환
                .block();

        return userInfo;
    }

}
