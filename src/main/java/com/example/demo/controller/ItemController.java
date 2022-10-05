package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.Item;
import com.example.demo.service.ItemService;

/**
 * @author obuch
 *
 */
@Controller
@RequestMapping("/")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	
	/**
	 * 商品一覧を表示
	 * 
	 * @param model
	 * @return 商品一覧ページへ
	 */
	@RequestMapping("/showlist")
	public String showItemList(Model model) {
		
		List<Item> itemList = itemService.findAll();
		model.addAttribute("itemList", itemList);
		
		return "list";
	}
	
	/**
	 * 商品詳細を表示
	 * 
	 * @param id　表示する商品のid
	 * @param model
	 * @return	商品詳細ページへ
	 */
	@RequestMapping("/detail")
	public String showDetail(Integer id, Model model) {
		
		Item item = itemService.findById(id);
		model.addAttribute("item", item);
		
		return "detail";
	}
	
	
	/**
	 * 商品追加ページを表示
	 * 
	 * @return　商品追加ページへ
	 */
	@RequestMapping("/add")
	public String add() {
		return "add";
	}
	
}
