
DROP TABLE IF EXISTS area;

CREATE TABLE area
(
  area_id INT AUTO_INCREMENT, #从company表找到area 必须有一个外键 这里用自增主键来代表一个area
  name VARCHAR(30),
  distinct_name VARCHAR(30), 
  PRIMARY KEY (area_id)
)
DEFAULT CHARSET = utf8,
ENGINE = InnoDB;
CREATE INDEX idx_name ON area (name);
CREATE INDEX idx_area_id ON area (area_id);

