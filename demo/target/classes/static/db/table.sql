create table movie (
  id serial PRIMARY KEY
  , title TEXT NOT NULL
  , img_url TEXT NOT NULL
  , category1 int
  , tag TEXT NOT NULL
  , intro TEXT NOT NULL
  , original_site TEXT NOT NULL
  , original_link TEXT NOT NULL
  , u_date date  NOT NULL
  , del_flg int DEFAULT 0
);

create table mst_category (
  id serial PRIMARY KEY
  , code int
  , name TEXT NOT NULL
  , del_flg int DEFAULT 0
);

create table mst_tag (
	id serial PRIMARY KEY
	, name text not null
	, del_flg int DEFAULT 0
);

create table mst_original_site (
	id serial PRIMARY KEY
  , code int
  , name TEXT NOT NULL
  , del_flg int DEFAULT 0
);

create table movie_tag (
	movie_id int8 not null
	, name text not null
	, del_flg int DEFAULT 0
);

create table movie_views (
	movie_id int8 PRIMARY KEY
	, views int8
	, uDate  timestamp
);