package kh.st.boot.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverUserInfo {

    @JsonProperty("resultcode")
    private String resultCode; // 응답 코드

    @JsonProperty("message")
    private String message; // 메시지

    @JsonProperty("response")
    private UserResponse response; // 사용자 정보 응답

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserResponse {
        @JsonProperty("id")
        private String id; // 사용자 ID

        @JsonProperty("nickname")
        private String nickname; // 사용자 닉네임

        @JsonProperty("profile_image")
        private String profileImage; // 프로필 이미지 URL

        @JsonProperty("email")
        private String email; // 이메일

        @JsonProperty("gender")
        private String gender; // 성별

        @JsonProperty("birthyear")
        private String birthyear; // 출생 연도

        @JsonProperty("age")
        private String age; // 나이

        @JsonProperty("birthday")
        private String birthday; // 생일

        @JsonProperty("phone")
        private String phone; // 전화번호

        @JsonProperty("email_verified")
        private Boolean emailVerified; // 이메일 인증 여부

        @JsonProperty("is_default_image")
        private Boolean isDefaultImage; // 기본 프로필 사진 여부

    }
}
