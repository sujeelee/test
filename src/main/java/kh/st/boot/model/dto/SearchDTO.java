package kh.st.boot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchDTO {
    private int newsCnt; //뉴스결과물 개수 리턴
    private int stockCnt; //주식결과물 개수 리턴
    private String code; //주식코드이거나 뉴스기본키
    private String title; //종목명이나 뉴스 타이틀
    private int price; //주식 가격
    private String flt; //등락율
    private String date; //뉴스 작성일
    private String newspaper; //뉴스사
    private String type; //뉴스인지 주식인지
}
