package com.example.demo.form;

import lombok.Data;

@Data
public class ItemForm {

	private Integer id;
	private String name;
	private Integer condition;
	private String category;
	private String brand;
	private Integer price;
	private Integer shipping;
	private String description;

}
