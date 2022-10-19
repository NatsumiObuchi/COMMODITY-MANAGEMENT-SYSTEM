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

	// １ページの取得行数を指定
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
	 * （prev/nextボタンを押下した時に飛んでくるメソッド）
	 * @param model
	 * @return ページング処理のメソッドへ
	 */
	@RequestMapping("/showlist")
	public String showItemList(Model model, String nowPage, String prevPageFlg,
			String nextPageFlg, String searchFlg) {
		session.setAttribute("source", "showlist");
	
		return pagenate(model, nowPage, prevPageFlg, nextPageFlg, null, searchFlg);
	}

	/**
	 * 商品一覧画面で検索結果を表示
	 * （searchボタンを押下した時に飛んでくるメソッド）
	 * @param form  入力された検索条件
	 * @param model 検索結果のitemList or 商品がない場合はエラー文 を追加
	 * @return 商品一覧ページのメソッド呼び出し
	 */
	@RequestMapping("/search")
	public String search(SearchForm form, Model model, String nowPage) {
		session.setAttribute("source", "search");
		// searchFlgを0にして返す
		return pagenate(model, nowPage, null, null, form, "1");
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
		
		// nowPageがnullの場合、１ページ目を表示する
		if (nowPage == null) {
			nowPage = "1";
		}
		int page = Integer.parseInt(nowPage);

		// 次のページに進む・前のページに戻るための処理
		if ("1".equals(prevPageFlg)) {
			page--;
		}
		if ("1".equals(nextPageFlg)) {
			page++;
		}

		int count = 0;
		List<Item> itemList = new ArrayList<>();

		// 検索欄に何かしらの情報が入っていない場合
		if (!("1".equals(searchFlg))) {
			itemList = itemService.findAll(page, PAGE_LIMIT);
			count = itemList.get(1).getCount() / PAGE_LIMIT;
			model.addAttribute("searchFlg", "");

			// 検索欄に何かしらの情報が入っている場合
		} else {
			Search search = new Search();
			// 検索欄からsearchボタンを押して飛んできた場合
			if (session.getAttribute("source").equals("search")) {
				BeanUtils.copyProperties(form, search);
				// 検索欄に情報が入っている状態で、prev/nextボタンを押して飛んできた場合
			} else if (session.getAttribute("source").equals("showlist")) {
				search = (Search) session.getAttribute("search");
			}

			session.setAttribute("search", search);
			itemList = itemService.searchItems(search, page, PAGE_LIMIT);

			if (itemList.size() == 0) {
				// 検索結果が0件だった場合エラーメッセージを詰める
				model.addAttribute("noItemMessage", "該当の商品はありません");
			} else {
				// 検索結果が1件以上の場合、総ページ数を詰める
				count = itemList.get(1).getCount() / PAGE_LIMIT;
			}
			model.addAttribute("searchFlg", "1");

		}
		model.addAttribute("itemList", itemList);
		model.addAttribute("count", count);
		model.addAttribute("nowPage", page);

		return "list";

	}

	/**
	 * 商品詳細を表示
	 * 
	 * @param id 表示する商品のid
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
		model.addAttribute("add", form);
		// カテゴリの値が３つすべて入っていなければ、エラー文をadd.htmlに返す
		if (form.getParent().isEmpty() || form.getChild().isEmpty() || form.getGrandChild().isEmpty()) {
			model.addAttribute("categoryError", "すべてのカテゴリーを選択してください");
			return "add";
		}
		if (rs.hasErrors()) {
			return "add";
		}

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
