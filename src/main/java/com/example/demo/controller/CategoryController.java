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

	@ResponseBody
	@RequestMapping(value = "/searchParent", method = RequestMethod.GET)
	public List<Category> searchParent() {

		List<Category> parentCategoryList = service.findParent();
		
		return parentCategoryList;
	}

	
	@ResponseBody
	@RequestMapping(value = "/searchChild" , method = RequestMethod.GET)
	public List<Category> searchChild(Integer id){

		List<Category> childCategoryList = service.findChild(id);

		return childCategoryList;
	}

	@ResponseBody
	@RequestMapping(value = "/searchGrandChild" , method = RequestMethod.GET)
	public List<Category> searchGrandChild(Integer id){

		List<Category> grandChildCategoryList = service.findGrandChild(id);
		System.out.println(grandChildCategoryList);

		return grandChildCategoryList;
	}
	


}
