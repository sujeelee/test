CREATE TABLE `stockandfund`.`deposit_send` (
  `ds_no` INT NOT NULL AUTO_INCREMENT COMMENT '기본키',
  `ds_send_name` VARCHAR(255) NULL COMMENT '송금 보낸 사람명칭',
  `ds_receive_name` VARCHAR(255) NULL COMMENT '받은 이름',
  `ds_receive_account` VARCHAR(255) NULL COMMENT '받은 계좌번호',
  `ds_datetime` DATETIME NULL COMMENT '보낸일자',
  `ds_favorite` CHAR(1) NULL COMMENT '즐겨찾기 여부',
  `ds_send_price` INT NULL COMMENT '보낸 금액',
  `mb_id` VARCHAR(255) NULL COMMENT '송금한 회원 아이디',
  `ds_re_mb_id` VARCHAR(255) NULL COMMENT '송금받은 회원 아이디',
  PRIMARY KEY (`ds_no`))
COMMENT = '예치금 보내기 테이블';


--총집편 적용 완료