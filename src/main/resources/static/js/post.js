function validateCategory(){
	const category = document.getElementById("select").value;
	
	if(category === "category"){
		alert("카테고리를 선택하세요.");
		return false;	
	}
	
	return true;
}