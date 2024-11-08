ALTER TABLE `stockandfund`.`stock_info` 
ADD COLUMN `si_vs` VARCHAR(50) NULL COMMENT '대비' AFTER `st_code`,
ADD COLUMN `si_fltRt` VARCHAR(45) NULL COMMENT '등락율' AFTER `si_vs`,
ADD COLUMN `si_mrktCtq` VARCHAR(45) NULL COMMENT '시장구분 / 코스닥인지 뭔지 이런거' AFTER `si_fltRt`,
ADD COLUMN `si_mrkTotAmt` VARCHAR(45) NULL COMMENT '시가총액' AFTER `si_mrktCtq`,
ADD COLUMN `si_hipr` VARCHAR(45) NULL COMMENT '하루 최고 고가' AFTER `si_mrkTotAmt`,
ADD COLUMN `si_lopr` VARCHAR(45) NULL COMMENT '하루 최저가' AFTER `si_hipr`,
ADD COLUMN `si_trqu` VARCHAR(45) NULL COMMENT '체결수량 누적합계' AFTER `si_lopr`,
CHANGE COLUMN `si_date` `si_date` VARCHAR(255) NULL DEFAULT NULL ;

ALTER TABLE `stockandfund`.`stock_info` drop column `si_mrktCtq` ;

--총집편 적용 완료