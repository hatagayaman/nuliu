package com.example.demo.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.MovieDto;
import com.example.demo.dto.TagDto;

@Service
public class TagService {
	@Autowired
	private NamedParameterJdbcTemplate jdbc;

	public List<TagDto> getCountTagList() {
		String sql = " select * from tag where del_flg = 0;";
		MapSqlParameterSource param = new MapSqlParameterSource();
		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<TagDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			TagDto dto = new TagDto();
			dto.setId((int) map.get("id"));
			dto.setName((String) map.get("name"));
			dto.setCount("(" + (int) map.get("count") + ")");
			returnList.add(dto);
		}
		return returnList;
	}

	public List<TagDto> getTagList(int count) {
		String sql =
				" select "
					+ " name "
				+ "from "
					+ "(select "
						+ "distinct(name) "
					+ "from "
						+ "movie_tag mt "
					+ "inner join movie m "
						+ "on mt.movie_id = m.id "
						+ "and m.del_flg = 0 "
				+ "where "
					+ "mt.del_flg = 0) "
				+ "order by "
					+ "random() fetch first " + count + " rows only; ";
		MapSqlParameterSource param = new MapSqlParameterSource();
		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<TagDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			TagDto dto = new TagDto();
			dto.setName("#" + (String) map.get("name"));
			dto.setSearchUrl("/search?word=" + (String) map.get("name"));
			returnList.add(dto);
		}
		return returnList;
	}

	public List<TagDto> getTagListByMovieId(int movieid, boolean withTagMark) {
		String sql =
				" select "
					+ "* "
				+ "from "
					+ "movie_tag "
				+ "where "
					+ "movie_id = :movie_id;";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("movie_id", movieid);
		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<TagDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			TagDto dto = new TagDto();
			dto.setName((withTagMark ? "#" : "") +  (String) map.get("name"));
			dto.setSearchUrl("/search?word=" + (String) map.get("name"));
			returnList.add(dto);
		}
		return returnList;
	}

	@Transactional
	public int updateTag(MovieDto dto) {
		String sql = "delete from movie_tag where movie_id = :movie_id;";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("movie_id", dto.getId());
		jdbc.update(sql, param);

		// タグ登録
		String[] tags = dto.getTag().split(" |　");
		if (tags == null || tags.length == 0) {
			return 1;
		}
		for (int i = 0; i < tags.length; i++) {
			if (" ".equals(tags[i]) || "　".equals(tags[i])) {
				continue;
			}
			sql = "insert into movie_tag values (:movie_id, :tag ,0);";
			param = new MapSqlParameterSource();
			param.addValue("movie_id", dto.getId());
			param.addValue("tag", tags[i]);
			jdbc.update(sql, param);
		}
		return 1;
	}

	@Transactional
	public int insertMovieTag(MovieDto dto) {
		String sql = "delete from movie_tag where movie_id = :movie_id;";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("movie_id", dto.getId());
		jdbc.update(sql, param);

		// タグ登録
		String[] tags = dto.getTag().split(" |　");
		for (int i = 0; i < tags.length; i++) {
			String tag = tags[i].trim();
			if ("".equals(tag) || " ".equals(tag) || "　".equals(tag)) {
				continue;
			}
			sql = "insert into movie_tag values ((select max(id) from movie), :tag ,0);";
			param = new MapSqlParameterSource();
			param.addValue("tag", tag);
			jdbc.update(sql, param);
		}
		return 0;
	}

	public List<TagDto> getTagListByMovie() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public int updateMovieTag(MovieDto dto) {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "delete from movie_tag where movie_id = :movie_id;";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("movie_id", dto.getId());
		jdbc.update(sql, param);

		// タグ登録
		String[] tags = dto.getTag().split(" |　");
		for (int i = 0; i < tags.length; i++) {
			String tag = tags[i].trim();
			if ("".equals(tag) || " ".equals(tag) || "　".equals(tag)) {
				continue;
			}
			sql = "insert into movie_tag values (:movie_id, :tag ,0);";
			param = new MapSqlParameterSource();
			param.addValue("movie_id", dto.getId());
			param.addValue("tag", tag);
			jdbc.update(sql, param);
		}
		return 0;
	}
}
