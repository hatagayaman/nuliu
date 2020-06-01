create table if not exists sample (
	id INT
	,name VARCHAR
);

create table if not exists movie (
	id INT DEFAULT 0 NOT NULL AUTO_INCREMENT
	,title VARCHAR
	,img_url VARCHAR
	,category1 INT
	,tag VARCHAR
	,intro VARCHAR
	,original_site VARCHAR
	,original_link VARCHAR
	,u_date TIMESTAMP
	, del_flg INT default 0
);

create table if not exists movie_tag (
  movie_id INT not null
  , name VARCHAR not null
  , del_flg INT default 0
);

create table if not exists movie_views (
  movie_id INT not null
  , views INT
  , udate TIMESTAMP
);

create table if not exists mst_category (
  code INT not null
  , name VARCHAR not null
  , del_flg INT default 0
);

create table if not exists mst_original_site (
  code INT
  , name VARCHAR not null
  , del_flg INT default 0
);