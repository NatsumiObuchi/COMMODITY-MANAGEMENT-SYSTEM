package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
	
	/**
	 * 全件検索
	 * @return　itemList
	 */
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

	/**
	 * idから商品を一件検索
	 * @param id　itemId
	 * @return item
	 */
	public Item findById(Integer id) {
		
		String sql = "SELECT i.id, i.name, i.condition, c.name_all as category, i.brand, i.price, i.shipping, i.description "
				+ "FROM items i "
				+ "LEFT JOIN category c "
				+ "ON i.category = c.id "
				+ "WHERE i.id = :id "
				+ "ORDER BY id;";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Item item = template.queryForObject(sql, param,ITEMS_ROW_MAPPER);
		
		return item;
	}

	/**
	 * 商品を1件追加
	 * @param item
	 */
	public void insertItem(Item item) {
		
		String sql = "INSERT INTO items (id,name,condition,category,brand,price,shipping,description) "
				+ "VALUES(:id, :name, :condition, :category, :brand, :price, :shipping, :description);";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", item.getId())
				.addValue("name", item.getName()).addValue("condition", item.getCondition())
				.addValue("category", item.getCategory()).addValue("brand", item.getBrand())
				.addValue("price", item.getPrice()).addValue("shipping", item.getShipping())
				.addValue("description", item.getDescription());
				
		template.update(sql, param);
		
	}
	
	
}
