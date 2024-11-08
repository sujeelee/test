ALTER TABLE `stockandfund`.`member` 
ADD COLUMN `mb_follow` INT(11) NULL DEFAULT 0 AFTER `mb_account`;

--총집편 적용완료