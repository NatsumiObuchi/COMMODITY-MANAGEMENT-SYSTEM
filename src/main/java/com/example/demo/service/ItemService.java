package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Item;
import com.example.demo.domain.Search;
import com.example.demo.repository.ItemRepository;

@Service
@Transactional
public class ItemService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	public List<Item> findAll(Integer nowPage, Integer pageLimit) {
		return itemRepository.findAll(nowPage,pageLimit);
	}
	
	public Item findById(Integer id) {
		return itemRepository.findById(id);
	}

	public List<Item> searchItems(Search search, Integer nowPage, Integer pageLimit){
		return itemRepository.searchItems(search, nowPage, pageLimit);
	}

	
	public void insertItem(Item item) {
		itemRepository.insertItem(item);
	}

}
