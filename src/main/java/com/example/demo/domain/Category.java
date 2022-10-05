package com.example.demo.domain;

import lombok.Data;

@Data
public class Category {
	
	private int categoryId;
	private int parent;
	private String categoryName;
	private String categoryNameAll;
	

}
