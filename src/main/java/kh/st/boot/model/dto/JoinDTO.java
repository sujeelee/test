package kh.st.boot.model.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinDTO {
    //들어가야할 정보 id, pw, name, nick, hp, email, birth, emailing(on, null) 
    private String id;
    private String pw;
    private String name;
    private String nick;
    private String ph;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth; //DB에서는 timedate로 되어있음 시간정보가 필요하지 않기 때문에 > Date 형식으로 바꾸기
    private String emailing; // on, null 이고 memberVO에서 1 , 0 으로 mb_emailing에 넣어주기
    
    //우편번호 주소 상세주소
    private int zip;
    private String addr;
    private String addr2;
    
    //디폴트 값	
    private int mb_fail = 0;
    private int mb_level = 1;
    private int mb_point = 50;
    private String mb_loginMethod = "internal";

    //디버깅용
    @Override
    public String toString() {
        return "JoinDTO [id=" + id + ", pw=" + pw + ", name=" + name + ", nick=" + nick + ", ph=" + ph + ", email="
                + email + ", birth=" + birth + ", emailing=" + emailing + ", zip=" + zip + ", addr=" + addr + ", addr2="
                + addr2 + ", mb_fail=" + mb_fail + ", mb_level=" + mb_level + ", mb_point=" + mb_point + "]";
    }

    
    

}
