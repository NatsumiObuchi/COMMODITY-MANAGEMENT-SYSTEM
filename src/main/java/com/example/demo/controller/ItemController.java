package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import com.example.demo.service.ItemService;

/**
 * @author obuch
 *
 */
@Controller
@RequestMapping("/")
public class ItemController {

	public static final int PAGE_LIMIT = 30;

	@Autowired
	private ItemService itemService;

	@Autowired
	private HttpSession session;

	@ModelAttribute
	public ItemForm setUpItemForm() {
		return new ItemForm();
	}

	@ModelAttribute
	public SearchForm setUpSearchForm() {
		return new SearchForm();
	}

	/**
	 * 商品一覧を表示
	 * 
	 * @param model
	 * @return ページング処理のメソッドへ
	 */
	@RequestMapping("/showlist")
	public String showItemList (Model model, String nowPage, String prevPageFlg,
			String nextPageFlg, String searchFlg ) {
		System.out.println("showList searchFlg : " + searchFlg);
		session.setAttribute("source", "showlist");
		return pagenate(model, nowPage, prevPageFlg, nextPageFlg, null, searchFlg);
	}

	/**
	 * ページング処理
	 * 
	 * @param model
	 * @param nowPage     現在開いているページ番号
	 * @param prevPageFlg 前のページに進むフラグ
	 * @param nextPageFlg 後のページに進むフラグ
	 * @return 商品一覧
	 */
	@RequestMapping("/pagenate")
	public String pagenate(Model model, String nowPage, String prevPageFlg, String nextPageFlg, SearchForm form,
			 String searchFlg) {
		System.out.println("---------nowPage : " + nowPage);
		System.out.println("---------prev : " + prevPageFlg);
		System.out.println("---------next : " + nextPageFlg);
		System.out.println("-------------searchFlg : " + searchFlg);
		if (nowPage == null) {
			nowPage = "1";
		}
		int page = Integer.parseInt(nowPage);
		if ("1".equals(prevPageFlg)) {
			page--;
		}
		if ("1".equals(nextPageFlg)) {
			page++;
		}

		int count = 0;
		List<Item> itemList = new ArrayList<>();

		if (!("1".equals(searchFlg))) {
			System.out.println("2-------------------------------");

			itemList = itemService.findAll(page, PAGE_LIMIT);
			count = itemList.get(1).getCount() / PAGE_LIMIT;

			model.addAttribute("searchFlg", "");
		} else {
			System.out.println("3-------------------------------");
			System.out.println("form : " + form);
			Search search = new Search();
			if (session.getAttribute("source").equals("search")) {
				System.out.println("4--------------------------");
				BeanUtils.copyProperties(form, search);
			}
			if (session.getAttribute("source").equals("showlist")) {
				System.out.println("5--------------------------");
				search = (Search) session.getAttribute("search");
			}

			session.setAttribute("search", search);
			System.out.println("search : " + search);
			itemList = itemService.searchItems(search, page, PAGE_LIMIT);

			if (itemList.size() == 0) {
				model.addAttribute("noItemMessage", "該当の商品はありません");
			} else {
				count = itemList.get(1).getCount() / PAGE_LIMIT;
			}
			model.addAttribute("searchFlg", "1");

		}
		model.addAttribute("itemList", itemList);
		model.addAttribute("count", count);
		model.addAttribute("nowPage", page);

		System.out.println("PageCount : " + count);
		System.out.println("NowPage : " + page);
		System.out.println("==============================================");

		return "list";

	}

	/**
	 * 商品一覧画面で検索結果を表示
	 * 
	 * @param form  入力された検索条件
	 * @param model 検索結果のitemList or 商品がない場合はエラー文 を追加
	 * @return 商品一覧ページのメソッド呼び出し
	 */
	@RequestMapping("/search")
	public String search(SearchForm form, Model model, String nowPage) {

		session.setAttribute("source", "search");

		return pagenate(model, nowPage, null, null, form, "1");
	}

	/**
	 * 商品詳細を表示
	 * 
	 * @param id    表示する商品のid
	 * @param model
	 * @return 商品詳細ページへ
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
	 * @return 商品追加ページへ
	 */
	@RequestMapping("/add")
	public String add(Model model, String nowPage) {
		model.addAttribute("nowPage", nowPage);
		return "add";
	}

	/**
	 * 商品を追加する
	 * 
	 * @param form  入力された商品情報
	 * @param rs
	 * @param model
	 * @return 商品一覧ページのメソッド呼び出し
	 */
	@RequestMapping("/addItem")
	public String addItem(@Validated ItemForm form, BindingResult rs, Model model, String nowPage) {
		System.out.println(form);
		model.addAttribute("add", form);
		if (form.getParent().isEmpty() || form.getChild().isEmpty() || form.getGrandChild().isEmpty()) {
			System.out.println("カテゴリーエラー発生");
			model.addAttribute("categoryError", "すべてのカテゴリーを選択してください");
			return "add";
		}
		if (rs.hasErrors()) {
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

		itemService.insertItem(item);

		return showItemList(model, nowPage, null, null, null);
	}

}
