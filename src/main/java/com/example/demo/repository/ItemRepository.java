package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Item;

@Repository
public class ItemRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Item> ITEMS_ROW_MAPPER = (rs , i) ->{
		Item item = new Item();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setCondition(rs.getInt("condition"));
		item.setCategory(rs.getString("category"));
		item.setBrand(rs.getString("brand"));
		item.setPrice(rs.getInt("price"));
		item.setShipping(rs.getInt("shipping"));
		item.setDescription(rs.getString("description"));
		return item;
	};
	
	public List<Item> findAll(){
		
		String sql = "SELECT i.id, i.name, i.condition, c.name_all as category, i.brand, i.price, i.shipping, i.description "
				+ "FROM items i "
				+ "LEFT JOIN category c "
				+ "ON i.category = c.id "
				+ "ORDER BY id "
				+ "LIMIT 30;";
		
		List<Item> itemList = template.query(sql, ITEMS_ROW_MAPPER);
		
		return itemList;
	}

	public Item findById(Integer id) {
		
		String sql = "SELECT i.id, i.name, i.condition, c.name_all as category, i.brand, i.price, i.shipping, i.description "
				+ "FROM items i "
				+ "LEFT JOIN category c "
				+ "ON i.category = c.id "
				+ "WHERE i.id = :id "
				+ "ORDER BY id;";
		
		MapSqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Item item = template.queryForObject(sql, param,ITEMS_ROW_MAPPER);
		
		return item;
	}
	
	
}
