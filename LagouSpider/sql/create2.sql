CREATE TABLE IF NOT EXISTS companies
(
  company_id INTEGER, ##拉勾分配的id
  area_id INT, ##区域id
  company_fullname VARCHAR(40),
  financeStage VARCHAR(30),  #融资阶段
  industryField VARCHAR(40), #领域
  detail_location VARCHAR(50),
  PRIMARY KEY (company_id)
)
DEFAULT CHARSET = utf8,
ENGINE = InnoDB;
CREATE INDEX idx_company_fullname ON companies (company_fullname);
CREATE INDEX idx_company_id ON companies (company_id);
