
DROP TABLE IF EXISTS stockandfund.community_action;
CREATE TABLE stockandfund.community_action (
    cg_no INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cg_num INT NOT NULL,
    cg_type VARCHAR(50) NOT NULL,
    st_code VARCHAR(50) NOT NULL,
    mb_id VARCHAR(50) NOT NULL,
    cg_datetime DATETIME NOT NULL,
    cg_like VARCHAR(255),
    cg_report VARCHAR(255)
);

-- 총집편 적용 완료