package com.example.demo.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class ItemForm {

	private Integer id;

	@NotBlank(message = "商品名を入力してください")
	private String name;
	
	@NotBlank(message = "金額を入力してください")
	@Pattern(regexp = "^[0-9]*$", message = "半角英数字で入力してください")
	private String price;
	
	private String parent;
	private String child;
	private String grandChild;

	@NotBlank(message = "ブランド名を入力してください")
	private String brand;

	@NotNull(message = "コンディションを選択してください")
	private Integer condition;

	private Integer shipping;

	@NotBlank(message = "商品説明を入力してください")
	private String description;

}
