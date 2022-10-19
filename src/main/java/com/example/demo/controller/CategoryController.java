package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.Category;
import com.example.demo.service.CategoryService;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService service;

	/**
	 * 親カテゴリを全件検索
	 * @return 親カテゴリのList
	 */
	@ResponseBody
	@RequestMapping(value = "/searchParent", method = RequestMethod.GET)
	public List<Category> searchParent() {

		List<Category> parentCategoryList = service.findParent();
		
		return parentCategoryList;
	}

	
	/**
	 * 子カテゴリをidから検索
	 * @param id 親カテゴリのid
	 * @return 子カテゴリのList
	 */
	@ResponseBody
	@RequestMapping(value = "/searchChild" , method = RequestMethod.GET)
	public List<Category> searchChild(Integer id){

		List<Category> childCategoryList = service.findChild(id);

		return childCategoryList;
	}

	/**
	 * 孫カテゴリをidから検索
	 * @param id 子カテゴリのid
	 * @return 孫カテゴリのList
	 */
	@ResponseBody
	@RequestMapping(value = "/searchGrandChild" , method = RequestMethod.GET)
	public List<Category> searchGrandChild(Integer id){

		List<Category> grandChildCategoryList = service.findGrandChild(id);
		System.out.println(grandChildCategoryList);

		return grandChildCategoryList;
	}
	


}
