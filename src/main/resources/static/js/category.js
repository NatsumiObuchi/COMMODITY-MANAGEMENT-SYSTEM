window.onpageshow = () => {

	fetch("http://localhost:8080/searchParent")
		.then(responce => {
			console.log(responce);
			return responce.json();
		})
		.then(data => {
			console.log(data);

			let parentList = data;
			console.log(parentList);
			console.log(parentList[1].name);

			const selecter = document.querySelector("select[name='parent']");
			for (const category of parentList) {
				let op = document.createElement('option');
				op.value = category.id;
				op.textContent = category.name;
				console.log("parent : " + category.name);
				selecter.appendChild(op);
			}
		})
		.catch(error => {
			console.error("通信失敗 : ", error);
		})

	
	document.querySelector("select[name='parent']").onchange = (e) => {
		console.log("中カテゴリ");
		let id = e.target.value;
		if(id.length !== 0){

			console.log(e.target.value);
			
			const url = new URL('http://localhost:8080/searchChild');
			url.searchParams.append('id',id);
			
			fetch(url)
			.then(responce => {
				console.log(responce);
				return responce.json();
			})
			.then(data => {
				console.log(data);
				
				let childList = data;
				console.log(childList);
				console.log(childList[1].name);
				
				const selecter = document.querySelector("select[name='child']");
				for (const category of childList) {
					let op = document.createElement('option');
					op.value = category.id;
					op.textContent = category.name;
					console.log("parent : " + category.name);
					selecter.appendChild(op);
				}
			})
			.catch(error => {
				console.error("通信失敗 : ", error);
			})
		}
	}


	document.querySelector("select[name='child']").onchange = (e) => {
		console.log("小カテゴリ");

		console.log(e.target.value);

		let id = e.target.value;
		const url = new URL('http://localhost:8080/searchGrandChild');
		url.searchParams.append('id',id);

		fetch(url)
		.then(responce => {
			console.log(responce);
			return responce.json();
		})
		.then(data => {
			console.log(data);

			let GrandChildList = data;
			console.log(GrandChildList);
			console.log(GrandChildList[1].name);

			const selecter = document.querySelector("select[name='grandChild']");
			for (const category of GrandChildList) {
				let op = document.createElement('option');
				op.value = category.id;
				op.textContent = category.name;
				console.log("parent : " + category.name);
				selecter.appendChild(op);
			}
		})
		.catch(error => {
			console.error("通信失敗 : ", error);
		})
	}
}
