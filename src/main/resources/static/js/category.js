window.onpageshow = () => {

	//商品追加ページを開いたときに親カテゴリに値を入れる
	fetch("http://localhost:8080/searchParent")
		.then(responce => {
			return responce.json();
		})
		.then(parentList => {
			console.log(parentList);
			const selecter = document.querySelector("select[name='parent']");
			//addOptionメソッドを呼び出しoptionの値を追加していく
			addOption(parentList, selecter);
		})
		.catch(error => {
			console.error("通信失敗 : ", error);
		})

	//親カテゴリを変更したときに、子カテゴリに値を入れる
	document.querySelector("select[name='parent']").onchange = (e) => {
		let id = e.target.value;
		if (!!id) {
			const url = new URL('http://localhost:8080/searchChild');
			url.searchParams.append('id', id);
			fetch(url)
				.then(responce => {
					return responce.json();
				})
				.then(childList => {
					console.log(childList);
					//先にoptionの中身を空にする
					removeGrandChildCategory();
					const selecter = removeChildCategory();
					//addOptionメソッドを呼び出しoptionの値を追加していく
					addOption(childList, selecter);
				})
				.catch(error => {
					console.error("通信失敗 : ", error);
				})
		} else {
			//親カテゴリでデフォルト値が選択された時の動作
			//子カテゴリのoptionの中身を空にする
			removeChildCategory();
			//孫カテゴリのoptionの中身を空にする
			removeGrandChildCategory();
		}
	}

	// 子カテゴリを変更したときに、孫カテゴリに値を入れる
	document.querySelector("select[name='child']").onchange = (e) => {
		let id = e.target.value;
		if (!!id) {
			const url = new URL('http://localhost:8080/searchGrandChild');
			url.searchParams.append('id', id);
			fetch(url)
				.then(responce => {
					return responce.json();
				})
				.then(GrandChildList => {
					console.log(GrandChildList);
					//先にoptionの中身を空にする
					const selecter = removeGrandChildCategory();
					//addOptionメソッドを呼び出しoptionの値を追加していく
					addOption(GrandChildList, selecter);
				})
				.catch(error => {
					console.error("通信失敗 : ", error);
				})
		} else {
			//子カテゴリでデフォルト値が選択された時は、孫カテゴリのoptionの中身を空にする
			removeGrandChildCategory();
		}
	}
}

	// htmlのプルダウンメニューにoptionを追加していくメソッド
	function addOption(categoryList, selecter) {
		for (const category of categoryList) {
			let op = document.createElement('option');
			op.value = category.id;
			op.textContent = category.name;
			console.log("category : " + category.name);
			selecter.appendChild(op);
		}
	}

	// 子カテゴリをデフォルト値以外削除するメソッド
	function removeChildCategory() {
		const childSelecter = document.querySelector("select[name='child']");
		for (let i = (childSelecter.childNodes.length - 1); i > 1; i--) {
			childSelecter.removeChild(childSelecter.childNodes[i]);
		}
		return childSelecter;
	}

	// 孫カテゴリをデフォルト値以外削除するメソッド
	function removeGrandChildCategory() {
		const grandChildSelecter = document.querySelector("select[name='grandChild']");
		for (let i = (grandChildSelecter.childNodes.length - 1); i > 1; i--) {
			grandChildSelecter.removeChild(grandChildSelecter.childNodes[i]);
		}
		return grandChildSelecter;
	}
