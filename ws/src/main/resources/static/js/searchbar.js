function changeClass(){
    document.getElementById("searchDiv").classList.toggle('instant-search__results-container--visible');
    
}

window.onload = function(){

document.getElementById("testtt").addEventListener( 'click', changeClass);

}
/*Search */
function filterFunction() {
    var input, filter, ul, li, a, i;
    input = document.getElementById("myInput");
    filter = input.value.toUpperCase();
    div = document.getElementById("searchDiv");
    a = div.getElementsByTagName("a");
    for (i = 0; i < a.length; i++) {
        txtValue = a[i].textContent || a[i].innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            a[i].style.display = "";
        } else {
            a[i].style.display = "none";
        }
    }
}


/* Using the name in the searchbar in the modal*/
function pullName(){
	var userNameTagA = document.getElementById("aTagAuthorName").textContent;
	var userNameTagH = document.getElementById("hTagAuthorName");
	userNameTagH.textContent = userNameTagA;
	console.log(userNameTagA + " " + userNameTagH);

}

