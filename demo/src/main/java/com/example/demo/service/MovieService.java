package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.example.demo.data.GBConstants;
import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.MovieDto;
import com.example.demo.logic.GBUtils;

@Service
public class MovieService {
	@Autowired
	private NamedParameterJdbcTemplate jdbc;

	public long getMovieRecordCount() {
		String sql = "select count(*) as c from movie where del_flg = 0;";
		return (long) jdbc.queryForList(sql, new MapSqlParameterSource()).get(0).get("c");
	}

	public List<MovieDto> getMovieList(int page) {
		String sql =
				" select "
					+ "m.*, "
					+ "mv.views "
				+ "from "
					+ "movie m "
				+ "inner join "
					+ "movie_views mv "
						+ "on m.id = mv.movie_id"
				+ " where "
					+ "del_flg = 0 "
				+ " order by "
					+ "m.id desc "
				+ " limit " + GBConstants.NUM_PER_PAGE + " offset :start " ;
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("start", (page - 1) * GBConstants.NUM_PER_PAGE);

		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<MovieDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			MovieDto dto = new MovieDto();
			dto.setId((int) map.get("id"));
			dto.setTitle((String) map.get("title"));
			dto.setImgUrl((String) map.get("img_url"));
			dto.setMovieUrl(GBUtils.makeMovieUrl(dto.getId()));
			dto.setCategory1((int) map.get("category1"));
			dto.setTag((String) map.get("tag"));
			dto.setuDate((Date) map.get("u_date"));
			dto.setViews((int) map.get("views"));
			returnList.add(dto);
		}
		return returnList;
	}

	public MovieDto getMovieDetailList(int movieid) {
		String sql =
				" select "
					+ "m.*, "
					+ "mv.views as views "
				+ "from "
					+ "movie m inner join "
						+ "movie_views mv "
							+ "on m.id = mv.movie_id"
				+ " where "
					+ "m.id = :id "
					+ "and del_flg = 0;";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("id", movieid);
		MovieDto dto = new MovieDto();
		//非公開の場合
		List<Map<String, Object>> list = jdbc.queryForList(sql, param);
		if (CollectionUtils.isEmpty(list)) return dto;

		Map<String, Object> map = jdbc.queryForList(sql, param).get(0);

		dto.setId((int) map.get("id"));
		dto.setTitle((String) map.get("title"));
		dto.setImgUrl((String) map.get("img_url"));
		dto.setMovieUrl(GBUtils.makeMovieUrl(dto.getId()));
		dto.setOriginalSite(Integer.parseInt((String) map.get("original_site")));
		dto.setOriginalLink((String) map.get("original_link"));
		dto.setCategory1((int) map.get("category1"));
		dto.setTag((String) map.get("tag"));
		dto.setIntro((String) map.get("intro"));
		dto.setuDate((Date) map.get("u_date"));
		dto.setViews((int) map.get("views"));
		return dto;
	}

	public MovieDto getMovieDetailListForAdmin(int movieid) {
		String sql =
				" select "
					+ "m.*, "
					+ "mv.views as views "
				+ "from "
					+ "movie m inner join "
						+ "movie_views mv "
							+ "on m.id = mv.movie_id"
				+ " where "
					+ "m.id = :id;";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("id", movieid);
		Map<String, Object> map = jdbc.queryForList(sql, param).get(0);
		MovieDto dto = new MovieDto();
		dto.setId((int) map.get("id"));
		dto.setTitle((String) map.get("title"));
		dto.setImgUrl((String) map.get("img_url"));
		dto.setMovieUrl(GBUtils.makeMovieUrl(dto.getId()));
		dto.setOriginalSite(Integer.parseInt((String) map.get("original_site")));
		dto.setOriginalLink((String) map.get("original_link"));
		dto.setCategory1((int) map.get("category1"));
		dto.setTag((String) map.get("tag"));
		dto.setIntro((String) map.get("intro"));
		dto.setuDate((Date) map.get("u_date"));
		dto.setViews((int) map.get("views"));
		dto.setDelFlg((int) map.get("del_flg"));
		return dto;
	}

	@Transactional
	public int insertMovie(MovieDto dto) {
		// 動画テーブル登録
		String sql =
				"insert into movie "
				+ "("
					+ "title,"
					+ "img_url,"
					+ "category1,"
					+ "tag,"
					+ "intro,"
					+ "original_site,"
					+ "original_link,"
					+ "u_date,"
					+ "del_flg"
				+ ")"
				+ "values ("
					+ ":title, "
					+ ":img_url, "
					+ ":category1,"
					+ ":tag,"
					+ ":intro,"
					+ ":original_site,"
					+ ":original_link,"
					+ ":u_date,"
					+ ":del_flg"
				+ ");";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("title", dto.getTitle());
		param.addValue("img_url", dto.getImgUrl());
		param.addValue("category1", dto.getCategory1());
		param.addValue("tag", dto.getTag());
		param.addValue("intro", dto.getIntro());
		param.addValue("original_site", dto.getOriginalSite());
		param.addValue("original_link", dto.getOriginalLink());
		param.addValue("u_date", new Date());
		param.addValue("del_flg", dto.getDelFlg());
		return jdbc.update(sql, param);
	}

	@Transactional
	public int insertMovieView(MovieDto dto) {
		// 動画閲覧数テーブル登録
		String sql = "insert into movie_views values((select max(id) from movie), 0, :uDate);";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("uDate", new Date());
		return jdbc.update(sql, param);
	}

	@Transactional
	public int countMovieViews(int movieid) {
		String sql =
				"update movie_views "
					+ "set views = ("
						+ "select "
							+ "(views + 1) "
						+ "from "
							+ "movie_views "
						+ "where "
							+ "movie_id = :movie_id) "
				+ "where "
					+ "movie_id = :movie_id";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("movie_id", movieid);
		param.addValue("uDate", new Date());
		return jdbc.update(sql, param);
	}

	// カテゴリごとの動画件数
	public List<CategoryDto> getCategoryList() {
		String sql =
				"select "
					+ "category1, "
					+ "(select name from mst_category where code = category1) as categoryname,"
					+ "count(category1) as count "
				+ "from "
					+ "movie "
				+ "group by category1;";
		List<Map<String, Object>> resultList = jdbc.queryForList(sql, new MapSqlParameterSource());
		List<CategoryDto> returnList = new LinkedList<>();
		for (Map<String, Object> result : resultList) {
			CategoryDto dto = new CategoryDto();
			dto.setCode((int) result.get("category1"));
			dto.setName((String) result.get("categoryname"));
			dto.setCount("(" + result.get("count") + ")");
			dto.setSearchUrl("/searchCategory/" + dto.getCode());
			returnList.add(dto);
		}
		return returnList;
	}

	// カテゴリ検索
	public List<MovieDto> getMovieListByCategory(int cateid, int page, boolean paging) {
		String sql =
				"select "
					+ "m.*, "
					+ "c.name as category_name, "
					+ "mv.views "
				+ "from "
					+ "movie m inner join "
						+ "mst_category c "
						+ "on m.category1 = c.code "
					+ "inner join movie_views mv "
						+ "on m.id = mv.movie_id "
				+ "where "
					+ "m.category1 = :cateid and m.del_flg = 0 ";
		if (paging) sql += " limit " + GBConstants.NUM_PER_PAGE + " offset :start " ;
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("start", (page - 1) * GBConstants.NUM_PER_PAGE);
		param.addValue("cateid", cateid);
		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<MovieDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			MovieDto dto = new MovieDto();
			dto.setId((int) map.get("id"));
			dto.setTitle((String) map.get("title"));
			dto.setImgUrl((String) map.get("img_url"));
			dto.setMovieUrl(GBUtils.makeMovieUrl(dto.getId()));
			dto.setOriginalSite(Integer.parseInt((String) map.get("original_site")));
			dto.setOriginalLink((String) map.get("original_link"));
			dto.setCategory1((int) map.get("category1"));
			dto.setTag((String) map.get("tag"));
			dto.setIntro((String) map.get("intro"));
			dto.setuDate((Date) map.get("u_date"));
			dto.setCategoryName((String) map.get("category_name"));
			dto.setViews((int) map.get("views"));
			returnList.add(dto);
		}
		return returnList;
	}

	// 動画検索
	public List<MovieDto> getMovieListByWord(String word, int page, boolean paging) {
		String[] arr = word.split(" |　");
		String titleLike = "";
		String tagLike = "";
		boolean flag = false;
		for (int i = 0; i < arr.length; i++) {
			if (" ".equals(arr[i]) || "　".equals(arr[i])) {
				continue;
			}
			if (flag) {
				titleLike += " or ";
				tagLike += " or ";
			}
			titleLike += (" title like '%" + arr[i] + "%' ");
			tagLike += (" mt.name like '%" + arr[i] + "%' ");
			flag = true;
		}

		String sql =
				"select * from ("
				+ "select "
					+ "m.* "
					+ ", mv.views "
				+ "from "
					+ "movie m inner join "
						+ "movie_views mv "
					+ "on m.id = mv.movie_id "
						+ "and m.del_flg = 0 "
				+ "where " + titleLike
				+ "union "
				+ "select "
					+ "m.* "
					+ ", mv.views "
				+ "from "
					+ "movie m "
				+ "inner join "
					+ "movie_views mv "
					+ "on m.id = mv.movie_id "
				+ "inner join "
					+ "movie_tag mt "
					+ "on mt.movie_id = m.id "
						+ "and m.del_flg = 0 "
				+ "where " + tagLike
				+ " )";
		if (paging) {
			sql += " limit " + GBConstants.NUM_PER_PAGE + " offset :start " ;
		}
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("start", (page - 1) * GBConstants.NUM_PER_PAGE);;

		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<MovieDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			MovieDto dto = new MovieDto();
			dto.setId((int) map.get("id"));
			dto.setTitle((String) map.get("title"));
			dto.setImgUrl((String) map.get("img_url"));
			dto.setMovieUrl(GBUtils.makeMovieUrl(dto.getId()));
			dto.setCategory1((int) map.get("category1"));
			dto.setTag((String) map.get("tag"));
			dto.setuDate((Date) map.get("u_date"));
			dto.setViews((int) map.get("views"));
			returnList.add(dto);
		}
		return returnList;
	}

	// 関連動画取得
	public List<MovieDto> getRelatedMovieList(int movieid) {
		// カテゴリまたはタグが同じ動画を適当に8レコードを抽出
		String sql =
				"select "
					+ "* "
					+ ", (select views from movie_views where movie_id = id) as views "
				+ "from "
					+ "movie "
				+ "where "
					+ "("
						+ "category1 = (select category1 from movie where id = :movie_id) "
						+ "or "
						+ "tag = (select tag from movie where id = :movie_id and tag is not null)"
					+ ") "
					+ "and del_flg = 0 "
				+ "order by "
					+ "random() fetch first 8 rows only; ";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("movie_id", movieid);
		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<MovieDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			MovieDto dto = new MovieDto();
			dto.setId((int) map.get("id"));
			dto.setTitle((String) map.get("title"));
			dto.setImgUrl((String) map.get("img_url"));
			dto.setMovieUrl(GBUtils.makeMovieUrl(dto.getId()));
			dto.setCategory1((int) map.get("category1"));
			dto.setTag((String) map.get("tag"));
			dto.setuDate((Date) map.get("u_date"));
			dto.setViews((int) map.get("views"));
			returnList.add(dto);
		}
		return returnList;
	}

	@Transactional
	public int updateMovie(MovieDto dto) {
		String sql =
				"update "
					+ "movie "
				+ "set "
					+ "title = :title,"
					+ "category1 = :category1,"
					+ "img_url = :img_url,"
					+ "original_link = :original_link,"
					+ "intro = :intro,"
					+ "original_site = :original_site, "
					+ "del_flg = :del_flg "
				+ "where "
					+ "id = :id;";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("id", dto.getId());
		param.addValue("title", dto.getTitle());
		param.addValue("category1", dto.getCategory1());
		param.addValue("img_url", dto.getImgUrl());
		param.addValue("original_link", dto.getOriginalLink());
		param.addValue("intro", dto.getIntro());
		param.addValue("original_site", dto.getOriginalSite());
		param.addValue("del_flg", dto.getDelFlg());
		return jdbc.update(sql, param);
	}

	public List<MovieDto> getMovieListForAdmin() {
		String sql =
				" select "
					+ "m.*, c.name as cate_name, o.name as site_name "
				+ "from "
					+ "movie m inner join "
						+ "mst_category c "
							+ "on m.category1 = c.code "
					+ "inner join mst_original_site o "
						+ "on m.original_site = o.code "
				+ " order by m.id desc ";
		MapSqlParameterSource param = new MapSqlParameterSource();
		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<MovieDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			MovieDto dto = new MovieDto();
			dto.setId((int) map.get("id"));
			dto.setTitle((String) map.get("title"));
			dto.setMovieUrl(GBUtils.makeMovieUrl(dto.getId()));
			dto.setCategory1((int) map.get("category1"));
			dto.setuDate((Date) map.get("u_date"));
			dto.setOriginalSiteName((String)map.get("site_name"));
			dto.setCategoryName((String)map.get("cate_name"));
			dto.setStatus((int) map.get("del_flg") == 0 ? "公開" : "非公開");

			// タグ取得 h2のsqlで複数行を1レコードに結合する方法が分からない。。。
			List<Map<String, Object>> tagDBList =
					jdbc.queryForList("select name from movie_tag where del_flg = 0 and movie_id = " + dto.getId(), param);
			List<String> tagList = new ArrayList<>();
			for (Map<String, Object> tag : tagDBList) {
				tagList.add("#" + (String) tag.get("name"));
			}
			dto.setTagList(tagList);

			returnList.add(dto);
		}
		return returnList;
	}

	public List<MovieDto> getMovieListLimit(int limit) {
		String sql =
				" select "
					+ "m.*, "
					+ "mv.views "
				+ "from "
					+ "movie m inner join "
						+ "movie_views mv "
							+ "on m.id = mv.movie_id"
				+ " where del_flg = 0 "
				+ " order by m.id desc "
				+ " limit " + limit;
		MapSqlParameterSource param = new MapSqlParameterSource();
		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<MovieDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			MovieDto dto = new MovieDto();
			dto.setId((int) map.get("id"));
			dto.setTitle((String) map.get("title"));
			dto.setImgUrl((String) map.get("img_url"));
			dto.setMovieUrl(GBUtils.makeMovieUrl(dto.getId()));
			dto.setCategory1((int) map.get("category1"));
			dto.setTag((String) map.get("tag"));
			dto.setuDate((Date) map.get("u_date"));
			dto.setViews((int) map.get("views"));
			returnList.add(dto);
		}
		return returnList;
	}

	public List<MovieDto> getMovieListByCategoryLimit(int cateid, int limit) {
		String sql =
				"select "
					+ "m.*, "
					+ "c.name as category_name, "
					+ "mv.views "
				+ "from "
					+ "movie m inner join "
						+ "mst_category c "
						+ "on m.category1 = c.code "
					+ "inner join movie_views mv "
						+ "on m.id = mv.movie_id "
				+ "where "
					+ "m.category1 = :cateid and m.del_flg = 0 "
				+ " order by m.id desc "
				+ " limit " + limit;
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("cateid", cateid);
		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<MovieDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			MovieDto dto = new MovieDto();
			dto.setId((int) map.get("id"));
			dto.setTitle((String) map.get("title"));
			dto.setImgUrl((String) map.get("img_url"));
			dto.setMovieUrl(GBUtils.makeMovieUrl(dto.getId()));
			dto.setOriginalSite(Integer.parseInt((String) map.get("original_site")));
			dto.setOriginalLink((String) map.get("original_link"));
			dto.setCategory1((int) map.get("category1"));
			dto.setTag((String) map.get("tag"));
			dto.setIntro((String) map.get("intro"));
			dto.setuDate((Date) map.get("u_date"));
			dto.setCategoryName((String) map.get("category_name"));
			dto.setViews((int) map.get("views"));
			returnList.add(dto);
		}
		return returnList;
	}

	public List<MovieDto> getPopularMovieList(int page, boolean paging) {
		String sql =
				" select "
					+ "m.*, "
					+ "mv.views "
				+ "from "
					+ "movie m inner join "
						+ "movie_views mv "
							+ "on m.id = mv.movie_id"
				+ " where del_flg = 0 "
				+ " order by mv.views desc ";
		MapSqlParameterSource param = new MapSqlParameterSource();
		if (paging) {
			sql += " limit " + GBConstants.NUM_PER_PAGE + " offset :start " ;
			param.addValue("start", (page - 1) * GBConstants.NUM_PER_PAGE);
		}
		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<MovieDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			MovieDto dto = new MovieDto();
			dto.setId((int) map.get("id"));
			dto.setTitle((String) map.get("title"));
			dto.setImgUrl((String) map.get("img_url"));
			dto.setMovieUrl(GBUtils.makeMovieUrl(dto.getId()));
			dto.setCategory1((int) map.get("category1"));
			dto.setTag((String) map.get("tag"));
			dto.setuDate((Date) map.get("u_date"));
			dto.setViews((int) map.get("views"));
			returnList.add(dto);
		}
		return returnList;
	}

	public List<MovieDto> getPopularMovieListLimit(int limit) {
		String sql =
				" select "
					+ "m.*, "
					+ "mv.views "
				+ "from "
					+ "movie m "
				+ "inner join "
					+ "movie_views mv "
						+ "on m.id = mv.movie_id"
				+ " where "
					+ "del_flg = 0 "
				+ " order by "
					+ "mv.views desc "
				+ " limit " + limit;
		MapSqlParameterSource param = new MapSqlParameterSource();
		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<MovieDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			MovieDto dto = new MovieDto();
			dto.setId((int) map.get("id"));
			dto.setTitle((String) map.get("title"));
			dto.setImgUrl((String) map.get("img_url"));
			dto.setMovieUrl(GBUtils.makeMovieUrl(dto.getId()));
			dto.setCategory1((int) map.get("category1"));
			dto.setTag((String) map.get("tag"));
			dto.setuDate((Date) map.get("u_date"));
			dto.setViews((int) map.get("views"));
			returnList.add(dto);
		}
		return returnList;
	}
}
