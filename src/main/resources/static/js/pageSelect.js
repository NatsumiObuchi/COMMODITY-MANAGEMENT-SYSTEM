'use strict'
window.addEventListener('DOMContentLoaded', function () {

    let searchPage = this.document.getElementById('nowPageSearch');
    let count = document.getElementById('count').textContent;
    let button = this.document.getElementById('pageButton');
    let errorMessage = this.document.getElementById('errorMessage');

    searchPage.addEventListener('input', function () {

        let searchPageNum = Number(searchPage.value);
        let countNum = Number(count);
        console.log("searchPageNum : " + searchPageNum + " count : " + countNum);
        
        // 半角数字以外が入力された場合
        if (isNaN(searchPageNum)) {
            errorMessage.textContent='半角数字で入力してください';
            button.setAttribute("disabled", true);

        // 入力されたページ数が有効な範囲でない場合
        } else if (searchPageNum <= 0 || searchPageNum > countNum) {
            errorMessage.textContent='有効な数字で入力してください';
            button.setAttribute("disabled", true);

        // 入力されたページ数が有効な場合
        } else {
            errorMessage.textContent='';
            button.removeAttribute("disabled");

        }
    });
});
