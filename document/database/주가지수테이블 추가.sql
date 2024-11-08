CREATE TABLE `stockandfund`.`stock_jisu` (
  `ji_no` INT NOT NULL AUTO_INCREMENT,
  `ji_type` VARCHAR(100) NULL COMMENT '코스닥인지 코스피인지 등 타입 확인 용',
  `ji_date` VARCHAR(255) NULL COMMENT '기준일자',
  `ji_clpr` VARCHAR(100) NULL COMMENT '종가',
  `ji_vs` VARCHAR(100) NULL DEFAULT NULL COMMENT '대비',
  `ji_fltRt` VARCHAR(100) NULL COMMENT '등락율',
  `ji_mkp` VARCHAR(100) NULL COMMENT '시가',
  `ji_hipr` VARCHAR(100) NULL COMMENT '최고치',
  `ji_lopr` VARCHAR(100) NULL COMMENT '최저치',
  `ji_trqu` VARCHAR(255) NULL COMMENT '거래량',
  PRIMARY KEY (`ji_no`))
COMMENT = '코스닥, 코스피, KRX300 정보를 가져와요';


--총집편 적용완료