ALTER TABLE `stockandfund`.`deposit_order` 
ADD COLUMN `do_tel` VARCHAR(255) NOT NULL AFTER `do_status`,
ADD COLUMN `do_email` VARCHAR(255) NULL AFTER `do_tel`;

ALTER TABLE `stockandfund`.`deposit` 
ADD COLUMN `de_before_num` INT NULL DEFAULT 0 AFTER `de_num`;

--총집편 적용 완료