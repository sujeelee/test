drop database if exists stockAndFund;

create database if not exists stockAndFund;

use stockAndFund;

CREATE TABLE `member` (
	`mb_no`	INT(11) primary key	AUTO_INCREMENT	NOT NULL,
	`mb_id`	varchar(255)	NULL,
	`mb_password`	varchar(255)	NULL,
	`mb_name`	varchar(50)	NULL,
	`mb_nick`	varchar(100)	NULL,
	`mb_ph`	varchar(100)	NULL,
	`mb_email`	varchar(255)	NULL,
	`mb_zip`	int(11)	NULL,
	`mb_addr`	varchar(255)	NULL,
	`mb_addr2`	varchar(255)	NULL,
	`mb_birth`	DATE	NULL	COMMENT '양식 8 자리 숫자
00000000
ex) 19960908',
	`mb_level`	int(11)	NULL	COMMENT '1LV~7LV 일반회원
10LV 관리자 회원',
	`mb_datetime`	DATETIME	NULL,
	`mb_edit_date`	DATETIME	NULL,
	`mb_stop_date`	DATETIME	NULL,
	`mb_out_date`	DATETIME	NULL	COMMENT '회원 탈퇴시 탈퇴일 update
회원 정보의 경우 아이디 + 이름 빼고 삭제',
	`mb_cookie`	varchar(255)	NULL,
	`mb_cookie_limit`	varchar(255)	NULL,
	`mb_fail` int DEFAULT 0,
	`mb_point`	int(11)	NULL,
	`mb_emailing`	tinyint(4)	NULL,
	`mb_account`	varchar(255)	NULL UNIQUE,
	`mb_oauthProvider` varchar(30)	NULL,
	`mb_oauthId` varchar(255)	null,
	`mb_loginMethod` varchar(50) null default 'internal',
	`mb_followers` int(4) default 0
);


-- 이메일 확인을 위한 DB추가 evc_me_id = id, code는 6자리 영, 숫자
create table `email_Verification`(
	`evc_id` INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`evc_email` VARCHAR(255) null,
	`evc_code` CHAR(6)
);


CREATE TABLE `config` (
	`cf_title`	varchar(255)	NULL,
	`cf_info`	longtext	NULL,
	`cf_privacy`	longtext	NULL,
	`cf_noemail`	longtext	NULL,
	`cf_tel`	varchar(255)	NULL,
	`cf_zip`	int(11)	NULL,
	`cf_addr`	varchar(255)	NULL,
	`cf_addr2`	varchar(255)	NULL,
	`cf_fax`	varchar(50)	NULL,
	`cf_email`	varchar(255)	NULL,
	`cf_owner_name`	varchar(255)	NULL,
	`cf_day_point`	int(11)	NULL,
	`cf_check_use`	tinyint(4)	NULL,
	`cf_od_point`	int(11)	NULL,
	`cf_percent`	int(11)	NULL
);

CREATE TABLE `order` (
	`od_id`	varchar(255)	NOT NULL,
	`od_name`	varchar(255)	NULL,
	`mb_id`	varchar(255)	NULL,
	`od_price`	int(11)	NULL,
	`od_point`	int(11)	NULL	,
	`od_date`	DATETIME	NULL,
	`od_status`	varchar(50)	NULL,
	`od_st_code`	varchar(255)	NULL,
	`od_st_name`	varchar(255)	NULL,
	`od_qty`	int(11)	NULL,
	`od_st_price`	int(11)	NULL,
	`od_percent_price`	int(11)	NULL
);

CREATE TABLE `stock` (
	`st_code`	varchar(255)	NOT NULL,
	`st_name`	varchar(255)	NULL,
	`st_qty`	int(11)	NULL,
	`st_board_cnt`	int(11)	NULL,
	`st_category`	varchar(255)	NULL,
	`st_status`	varchar(255)	NULL
);


CREATE TABLE `board` (
	`wr_no`	INT(11) primary key	 AUTO_INCREMENT	NOT NULL,
	`wr_category`	varchar(255)	NULL,
	`wr_content`	text	NULL,
	`mb_id`	varchar(255)	NULL,
	`wr_comment`	int(11)	NULL	DEFAULT 0,
	`wr_good`	int(11)	NULL	DEFAULT 0,
	`wr_singo`	int(11)	NULL	DEFAULT 0,
	`wr_blind`	char(4)	NULL,
	`wr_datetime` datetime DEFAULT now()
);

CREATE TABLE `stock_info` (
	`si_id`	INT(11) primary key	 AUTO_INCREMENT	NOT NULL,
	`si_date`	date	NULL,
	`si_price`	int(11)	NULL,
	`st_code`	varchar(255)	NOT NULL
);

CREATE TABLE `comment` (
	`co_id`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`wr_no`	INT(11) NOT NULL,
	`co_good` int(5) DEFAULT 0,
	`co_bad` int(5) DEFAULT 0,
	`co_content`	text	NULL,
	`mb_id`	varchar(255)	NULL,
	`co_blind`	char(4)	NULL,
	`co_datetime`	datetime DEFAULT now()
);

CREATE TABLE `wish_stock` (
	`wi_id`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`st_code`	varchar(255)	NOT NULL,
	`mb_id`	varchar(255)	NULL
);

CREATE TABLE `news_emoji` (
	`em_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`ne_no`	INT(11)	NOT NULL,
	`em_act`	int(11)	NULL	COMMENT '1 기뻐요
2 화나요
3 황당해요
4 슬퍼요',
	`mb_id`	varchar(255)	NULL,
	`em_datetime`	DATETIME	NULL
);

CREATE TABLE `news` (
	`ne_no`	INT(11)  primary key AUTO_INCREMENT	NOT NULL,
	`np_no`	INT(11)  NOT NULL,
	`ne_title`	varchar(255)	NULL,
	`ne_content`	longtext	NULL,
	`mb_id`	varchar(255)	NULL,
	`ne_datetime`	DATETIME	NULL,
	`ne_edit_date`	DATETIME	NULL,
	`ne_name`	varchar(255)	NULL,
	`ne_happy`	int(11)	DEFAULT 0,
	`ne_angry`	int(11)	DEFAULT 0,
	`ne_absurd`	int(11)	DEFAULT 0,
	`ne_sad`	int(11)	DEFAULT 0
);

CREATE TABLE `member_lv` (
	`lv_name`	varchar(255)	NULL,
	`lv_num`	int(11)	NULL,
	`lv_alpha`	varchar(255)	NULL,
	`lv_auto_use`	char(2)	NULL	DEFAULT 'N'	COMMENT 'Y/N 판별',
	`lv_up_limit`	int(11)	NULL	DEFAULT 0
);

CREATE TABLE `point` (
	`po_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`po_num`	int(11)	NULL,
	`po_content`	varchar(255)	NULL,
	`po_datetime`	DATETIME	NULL,
	`po_end_date`	DATETIME NULL,
	`mb_id`	varchar(255)	NULL
);

CREATE TABLE `follow` (
	`fo_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`mb_id`	varchar(255)	NULL,
	`fo_mb_id`	varchar(255)	NULL
);

CREATE TABLE `event` (
	`ev_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`ev_title`	varchar(255)	NULL,
	`ev_content`	longtext	NULL,
	`ev_start_level`	int(11)	NULL,
	`ev_end_level`	int(11)	NULL,
	`ev_point`	int(11)	NULL,
	`ev_datetime`	DATETIME	NULL,
	`ev_start`	DATETIME	NULL,
	`ev_end`	DATETIME	NULL,
	`ev_status`	char(7)	DEFAULT "Ending",
	`ev_form`	varchar(30)	null,
	`ev_cnt`	int(11)	NULL,
	`ev_bannerShow` TINYINT null
);

CREATE TABLE `event_list` (
	`el_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`ev_no`	INT(11)  NOT NULL,
	`mb_id`	varchar(255)	NULL,
	`el_datetime`	DATETIME	NULL
);

CREATE TABLE `day_check` (
	`dc_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`dc_datetime`	DATETIME	NULL,
	`mb_id`	varchar(255)	NULL,
	`dc_days` VARCHAR(70) null,
	`po_num`	int(11)	NULL
);

CREATE TABLE `stock_add` (
	`sa_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`sa_qty`	int(11)	NULL,
	`mb_id`	varchar(255)	NULL,
	`sa_datetime`	DATETIME	NULL,
	`sa_yn`	char(2)	NULL,
	`sa_content`	text	NULL,
	`sa_feedback`	text	NULL
);

CREATE TABLE `newspaper` (
	`np_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`np_name`	varchar(255)	NULL,
	`np_use`	tinyint(4)	NULL
);

CREATE TABLE `file` (
	`fi_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`fi_org_name`	varchar(255)	NULL,
	`fi_path`	text	NULL,
	`fi_num`	int(11)	NULL,
	`fi_reg_no`	int(11)	NULL,
	`fi_type`	varchar(255)	NULL
);



CREATE TABLE `event_prize` (
	`ep_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`ev_no`	INT(11) NOT NULL,
	`pr_no`	INT(11) NOT NULL,
	`ep_count` INT(11) null,
	`ep_prize`	varchar(255)	NULL,
	`ep_mb_id`	varchar(255)	NULL,
	`ep_rank`	int(11)	NULL
);



CREATE TABLE `prize` (
	`pr_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`pr_link` VARCHAR(255) NOT NULL,
	`pr_name` VARCHAR(255) not null, 
	`pr_point`	int(11)	NULL,
	`pr_startLevel`	CHAR(1)	NULL,
	`pr_endLevel`	CHAR(1)	NULL,
	`ev_no` INT(11) NULL,
	`ep_rank`	int(11)	NULL
);

CREATE TABLE `stock_member` (
	`mb_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`mb_stock`	varchar(255)	NULL
);


CREATE TABLE `news_member` (
	`mb_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`mb_news`	varchar(255)	NULL
);

CREATE TABLE `deposit_order` (
	`do_od_id`	varchar(255)	NOT NULL,
	`do_name`	varchar(255)	NULL,
	`mb_id`	varchar(255)	NULL,
	`do_price`	int(11)	NULL,
	`do_tno`	varchar(255)	NULL,
	`do_auth`	varchar(255)	NULL,
	`do_date`	DATETIME	NULL,
	`do_status`	varchar(50)	NULL
);

CREATE TABLE `deposit` (
	`de_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`de_num` INT(11) NULL,
	`de_content`	varchar(255)	NULL,
	`de_datetime`	DATETIME	NULL,
	`de_stock_code`	varchar(255)	NULL,
	`mb_id`	varchar(255)	NULL
);

CREATE TABLE `account` (
	`mb_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`ac_deposit`	int(11)	NULL
);

CREATE TABLE `reservation` (
	`re_no`	INT(11) primary key AUTO_INCREMENT	NOT NULL,
	`re_datetime`	DATETIME	NULL,
	`mb_id`	varchar(255)	NULL,
	`re_want_price`	int(11)	NULL,
	`re_st_code`	varchar(255)	NULL,
	`re_qty`	int(11)	NULL,
	`re_state`	varchar(255)	NULL
);

CREATE TABLE `member_approve` (
	`mp_no`	int(11) primary key AUTO_INCREMENT	NOT NULL,
	`mp_type`	varchar(50)	NULL,
	`mp_yn`	char(4)	NULL,
	`mp_company`	varchar(255)	NULL,
	`mp_datetime`	DATETIME	NULL,
	`mp_app_date`	DATETIME	NULL,
	`mb_no`	INT(11) NOT NULL
);


ALTER TABLE `stock_member` ADD CONSTRAINT `FK_member_TO_stock_member_1` FOREIGN KEY (
	`mb_no`
)
REFERENCES `member` (
	`mb_no`
);

ALTER TABLE `news_member` ADD CONSTRAINT `FK_member_TO_news_member_1` FOREIGN KEY (
	`mb_no`
)
REFERENCES `member` (
	`mb_no`
);

ALTER TABLE `account` ADD CONSTRAINT `FK_member_TO_account_1` FOREIGN KEY (
	`mb_no`
)
REFERENCES `member` (
	`mb_no`
);

-- Group By 에러 해결 
SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));

-- 총집편 적용 완료