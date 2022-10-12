package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Item;
import com.example.demo.domain.Search;

@Repository
public class ItemRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Item> ITEMS_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setCondition(rs.getInt("condition"));
		item.setCategoryName(rs.getString("category_name"));
		item.setBrand(rs.getString("brand"));
		item.setPrice(rs.getInt("price"));
		item.setShipping(rs.getInt("shipping"));
		item.setDescription(rs.getString("description"));
		return item;
	};

	
	/**
	 * StringBuilderで生成するSQL文のおおもと
	 */
	private final String QUERY = 
			"SELECT i.id, i.name, i.condition, c.name_all as category_name, i.brand, i.price, i.shipping, i.description "
			+ "FROM items i "
			+ "LEFT JOIN category c "
			+ "ON i.category = c.id ";

	private final String ORDER_BY_QUERY = "ORDER BY id ";

	int firstBuilderLength = QUERY.length();

	/**
	 * 全件検索
	 * 
	 * @return itemList
	 */
	public List<Item> findAll() {

		StringBuilder builder = new StringBuilder(QUERY);
		builder.append(ORDER_BY_QUERY + "LIMIT 30");

		List<Item> itemList = template.query(builder.toString(), ITEMS_ROW_MAPPER);

		return itemList;
	}

	/**
	 * idから商品を一件検索
	 * 
	 * @param id itemId
	 * @return item
	 */
	public Item findById(Integer id) {

		StringBuilder builder = new StringBuilder(QUERY);
		builder.append("WHERE i.id = :id " + ORDER_BY_QUERY);

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Item item = template.queryForObject(builder.toString(), param, ITEMS_ROW_MAPPER);

		return item;
	}

	// public boolean authenticationEnpty(Object obj){
	// 	if(obj.isEmpty()){
	// 		return true;
	// 	}
	// }

	/**
	 * 商品一覧ページの検索欄からの検索
	 * @param search 受け取ったパラメータ
	 * @return itemList
	 */
	public List<Item> searchItems(Search search){
		System.out.println("SerarchのRepositoryがよばれた");
		StringBuilder builder = new StringBuilder(QUERY);
		MapSqlParameterSource param = new MapSqlParameterSource();
		
		String searchtext = search.getSearchText();
		if(searchtext.isEmpty() == false || searchtext != ""){
			authenticationBuilder(builder);
			builder.append("i.name LIKE :name ");
			param.addValue("name", "%" + searchtext + "%");
			System.out.println(searchtext);
			System.out.println("1 : "+builder.toString());
		}

		Integer parent = search.getParent();
		Integer child = search.getChild();
		Integer grandChild = search.getGrandChild();
		System.out.println("------parent"+parent+"  child"+child+"   grandChild"+grandChild);
		if ( parent != null && child != null && grandChild != null){
			authenticationBuilder(builder);
			builder.append("c.id = :grandChild ");
			param.addValue("grandChild", grandChild);
			System.out.println("2 : "+builder.toString());

		} else if ( parent != null && child != null && grandChild == null) {
			authenticationBuilder(builder);
			builder.append("c.parent = :child ");
			param.addValue("child", child);
			System.out.println("3 : "+builder.toString());


		} else if(parent != null && child == null && grandChild == null) {
			authenticationBuilder(builder);
			builder.append("c.parent = :parent ");
			param.addValue("parent", parent);
			System.out.println("4 : "+builder.toString());

		}

		String brand = search.getBrand();
		if(brand.isEmpty() == false || brand != "") {
			authenticationBuilder(builder);
			builder.append("i.brand LIKE :brand ");
			param.addValue("brand", "%" + brand + "%");
			System.out.println("5 : "+builder.toString());

		}

		builder.append(ORDER_BY_QUERY);
		builder.append("LIMIT 30;");

		System.out.println("6 : "+builder.toString());

		List<Item> itemList = template.query(builder.toString(), param,ITEMS_ROW_MAPPER);
		System.out.println("repositoryitemlist----"+itemList);
		
		return itemList;
	}

	public void authenticationBuilder(StringBuilder builder) {
		if (builder.length() == firstBuilderLength) {
			builder.append("WHERE ");
		} else {
			builder.append("AND ");
		}
	}

	/**
	 * 商品を1件追加
	 * 
	 * @param item
	 */
	public void insertItem(Item item) {

		String sql = "INSERT INTO items (id,name,condition,category,brand,price,shipping,description) "
				+ "VALUES((select count(*) from items)+1 , :name, :condition, :category, :brand, :price, :shipping, :description);";

		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("name", item.getName()).addValue("condition", item.getCondition())
				.addValue("category", item.getCategoryId()).addValue("brand", item.getBrand())
				.addValue("price", item.getPrice()).addValue("shipping", item.getShipping())
				.addValue("description", item.getDescription());

		template.update(sql, param);

	}

}
