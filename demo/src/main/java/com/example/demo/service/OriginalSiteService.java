package com.example.demo.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OriginalSiteDto;

@Service
public class OriginalSiteService {
	@Autowired
	private NamedParameterJdbcTemplate jdbc;

	public List<OriginalSiteDto> getOriginalSiteList() {
		String sql = " select * from mst_original_site where del_flg = 0;";
		MapSqlParameterSource param = new MapSqlParameterSource();
		List<Map<String, Object>> resultList = jdbc.queryForList(sql, param);
		List<OriginalSiteDto> returnList = new LinkedList<>();
		for (Map<String, Object> map : resultList) {
			OriginalSiteDto dto = new OriginalSiteDto();
			dto.setCode((int) map.get("code"));
			dto.setName((String) map.get("name"));
			returnList.add(dto);
		}
		return returnList;
	}

}
