package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Category;
import com.example.demo.repository.CategoryRepository;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
//	public List<Category> findAll(){
//		return repository.findAll();
//	}
	
	public List<Category> findParent(){
		return repository.findParent();
	}
	
	public List<Category> findChild(Integer id){
		return repository.findChild(id);
	}

	public List<Category> findGrandChild(Integer id){
		return repository.findGrandChild(id);
	}
}
