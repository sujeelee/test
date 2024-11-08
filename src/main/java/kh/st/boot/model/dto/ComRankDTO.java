package kh.st.boot.model.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComRankDTO {
    private int ranks; //인기 커뮤니티 랭크
    private String title; //주식명
    private String code; //주식 코드
    private String flt; //등락율
}