window.addEventListener('DOMContentLoaded', function () {

    let searchPage = this.document.getElementById('nowPageSearch');
    let count = document.getElementById('count').textContent;
    let button = this.document.getElementById('pageButton');

    searchPage.addEventListener('input', function () {

        let searchPageNum = Number(searchPage.value);
        let countNum = Number(count);
        console.log("searchPageNum : " + searchPageNum + " count : " + countNum);


        if (isNaN(searchPageNum)) {
            console.log('Error-----------半角数字で入力してください。');
            button.setAttribute("disabled", true);
        } else if (searchPageNum <= 0 || searchPageNum > countNum) {
            console.log('Error-----------有効な数字を入力してください');
            button.setAttribute("disabled", true);

        } else {
            button.removeAttribute("disabled");

        }




    });


});
