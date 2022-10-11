package com.example.demo.domain;

import lombok.Data;

@Data
public class Category {
	
	private Integer id;
	private Integer parent;
	private String name;
	private String nameAll;

}
