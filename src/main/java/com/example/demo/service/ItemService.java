package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Item;
import com.example.demo.repository.ItemRepository;

@Service
@Transactional
public class ItemService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	public List<Item> findAll() {
		return itemRepository.findAll();
	}
	
	public Item findById(Integer id) {
		return itemRepository.findById(id);
	}
	
	public void insertItem(Item item) {
		itemRepository.insertItem(item);
	}

}
