package kh.st.boot.model.util;

import java.security.SecureRandom;

import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Configuration
public class CustomUtil {    
	
    //렌덤한 숫자 반환
    public int getCustomNumber(int len){
        SecureRandom sr = new SecureRandom();
        int up = (int)Math.pow(10, len);
        return sr.nextInt(up);
    }

    //렌덤한 문자열 반환
    //길이, 대문자 사용여부, 숫자 사용여부, 특수기호 사용여부
    public String generatedString(int length, boolean useUppercase, boolean useNumbers, boolean useSpecialCharacters){
        SecureRandom sr = new SecureRandom();
        StringBuffer characters = new StringBuffer();
        //ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 !@#$%^&*
        if (useUppercase) {
            characters.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        } else {
            characters.append("abcdefghijklmnopqrstuvwxyz");
        }

        if (useNumbers) {
            characters.append("0123456789");
        }
        
        if (useSpecialCharacters) {
            characters.append("!@#$");
        }

        StringBuilder resString = new StringBuilder(length);

        for(int i = 0 ; i < length; i ++){
            int index = sr.nextInt(characters.length());
            resString.append(characters.charAt(index));
        }

        return resString.toString();
    }


}
