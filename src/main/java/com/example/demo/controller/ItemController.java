package com.example.demo.controller;

import java.beans.BeanProperty;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.Item;
import com.example.demo.domain.Search;
import com.example.demo.form.ItemForm;
import com.example.demo.form.SearchForm;
import com.example.demo.service.CategoryService;
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
	
	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute
	public ItemForm setUpItemForm() {
		return new ItemForm();
	}

	@ModelAttribute
	public SearchForm setUpSearchForm(){
		return new SearchForm();
	}
	
	
	/**
	 * 商品一覧を表示
	 * 
	 * @param model
	 * @return 商品一覧ページへ
	 */
	@RequestMapping("/showlist")
	public String showItemList(Model model) {
		
		if(model.getAttribute("itemList")==null){
			List<Item> itemList = itemService.findAll();
			model.addAttribute("itemList", itemList);
		}
		
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

	@RequestMapping("/search")
	public String search(SearchForm form, Model model){
		Search search = new Search();
		BeanUtils.copyProperties(form, search);
		System.out.println(search);
		List<Item> searchItemList = itemService.searchItems(search);
		System.out.println("size : "+searchItemList.size());
		for(Item item:searchItemList){
			System.out.println("------------");
			System.out.println(item);
		}
		if(searchItemList.size()==0){
			model.addAttribute("noItemMessage","該当の商品はありません");
		}
		model.addAttribute("itemList",searchItemList);

		return showItemList(model);
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
	
	/**
	 * 
	 * 
	 * @param form
	 * @param rs
	 * @param model
	 * @return
	 */
	@RequestMapping("/addItem")
	public String addItem(@Validated ItemForm form, BindingResult rs, Model model) {
		System.out.println(form);
		if(form.getParent().isEmpty() || form.getChild().isEmpty() || form.getGrandChild().isEmpty()){
			System.out.println("カテゴリーエラー");
			model.addAttribute("categoryError", "すべてのカテゴリーを選択してください");
			return "add";
		}
		if(rs.hasErrors()) {
			System.out.println("エラーあり。add.htmlに戻す");
			return "add";
		}
		System.out.println("エラーなし！！！");
		Item item = new Item();
		item.setName(form.getName());
		item.setPrice(Integer.parseInt(form.getPrice()));
		item.setCategoryId(Integer.parseInt(form.getGrandChild()));
		item.setBrand(form.getBrand());
		item.setCondition(form.getCondition());
		item.setDescription(form.getDescription());
		item.setShipping(0);

		System.out.println(item);
		itemService.insertItem(item);
		
		return showItemList(model);
	}
	
	
}
