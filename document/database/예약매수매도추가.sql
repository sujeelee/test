ALTER TABLE `stockandfund`.`reservation` 
ADD COLUMN `re_done_date` DATETIME NULL AFTER `re_state`;

-- 총집편 적용 완료