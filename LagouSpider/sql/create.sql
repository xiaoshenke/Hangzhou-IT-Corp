########################
# Drop all table
########################

# company的表名会变化的 但是area这张表是不会变的
# 杭州的区数据太少了 没必要多一张表

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

#FOREIGN KEY (course_id) REFERENCES course (courses_id);

