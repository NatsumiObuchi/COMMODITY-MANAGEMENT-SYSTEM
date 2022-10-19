package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Category;

@Repository
public class CategoryRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Category> CATEGORY_ROW_MAPPER = (rs, i) -> {
		Category cat = new Category();
		cat.setId(rs.getInt("id"));
		cat.setParent(rs.getInt("parent"));
		cat.setName(rs.getString("name"));
		cat.setNameAll(rs.getString("name_all"));

		return cat;
	};
	
	/**
	 * 親カテゴリーを検索
	 * @return categoryList
	 */
	public List<Category> findParent() {

		String sql = "SELECT id, parent, name, name_all FROM category WHERE parent = 0 ORDER BY id";

		List<Category> categoryList = template.query(sql, CATEGORY_ROW_MAPPER);
		return categoryList;
	}
	

	/**
	 * 子カテゴリーを検索
	 * @return categoryList
	 */
	public List<Category> findChild(Integer id) {

		String sql = "SELECT id, parent, name, name_all FROM category WHERE (parent = :id) AND (name_all IS NULL) ORDER BY id";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id",id);
		List<Category> categoryList = template.query(sql, param, CATEGORY_ROW_MAPPER);
		return categoryList;
	}

	/**
	 * 孫カテゴリーを検索
	 * @return categoryList
	 */
	public List<Category> findGrandChild(Integer id) {

		String sql = "SELECT id, parent, name, name_all FROM category WHERE (parent = :id) AND (name_all IS NOT NULL) ORDER BY id";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id",id);
		List<Category> categoryList = template.query(sql, param ,CATEGORY_ROW_MAPPER);
		return categoryList;
	}

}
