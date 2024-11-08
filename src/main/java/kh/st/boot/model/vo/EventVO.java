package kh.st.boot.model.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventVO {

    private int ev_no;
    private String ev_title;
    private String ev_content;
    private int ev_start_level;
    private int ev_end_level;
    private int ev_point;
    private Date ev_datetime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ev_start;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ev_end;
    private String ev_status;
    private String ev_form;
    private int ev_cnt;
    private int ev_bannerShow;

    //이벤트 DB에는 fi_path, fi_path_banner     가 없습니다.
    private String fi_path; // 파일경로가 저장될 위치
    private String fi_path_banner; // 파일경로가 저장될 위치

}
