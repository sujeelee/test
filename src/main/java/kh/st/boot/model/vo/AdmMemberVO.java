package kh.st.boot.model.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdmMemberVO {
	// 받아올 값 변수 설정
	private int mb_no;
    private String mb_id;
    private String mb_password;
    private String mb_name;
    private String mb_nick;
    private String mb_ph;
    private String mb_email;
    private int mb_zip; //(11자리)
    private String mb_addr;
    private String mb_addr2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date mb_birth; //Date로 일단 변경
    private int mb_level; //(11자리)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date mb_datetime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date mb_edit_date;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date mb_stop_date;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date mb_out_date;
    private String mb_cookie;
    private String mb_cookie_limit;
    private int mb_point;
    private String mb_emailing_test;
    private int mb_emailing;
    private String mb_account;
    private String mb_stop_date_check;
    private String mb_stop;
    
	
}



 