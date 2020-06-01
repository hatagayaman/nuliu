create table movie {
	id	serial PRIMARY KEY,
	title	TEXT NOT NULL,
	img_url	TEXT NOT NULL,
	movie_url	TEXT NOT NULL,
	category1	TEXT NOT NULL,
	tag	TEXT NOT NULL,
	intro	TEXT NOT NULL,
	original_site	TEXT NOT NULL,
	original_link	TEXT NOT NULL,
	u_date	date
	del_flg	int DEFAULT 0
};

create table category {
	id	serial PRIMARY KEY,
	name	text not null
}