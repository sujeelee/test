package kh.st.boot.model.dto;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventDTO {
    // 상세이벤트 이전에 이벤트 페이지에서 사용할 예정입니다.
    private int ev_no;
    private String ev_title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ev_start;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ev_end;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ev_datetime;  // 시, 분, 초 포함

    private String ev_status;// Opening, Ending, resUser, Hidden
    private String ev_form;
    private String fi_path; // 파일경로가 저장될 위치
    private int ev_bannerShow;
}