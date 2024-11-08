ALTER TABLE `stockandfund`.`stock` 
ADD COLUMN `st_issue` VARCHAR(255) NULL AFTER `st_status`,
ADD COLUMN `st_type` VARCHAR(255) NULL AFTER `st_issue`;

-- 총집편 적용완료