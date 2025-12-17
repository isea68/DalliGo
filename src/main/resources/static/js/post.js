function validatePost(){
	// 카테고리
	const category = document.getElementById("select").value;
	// 제목
	const title = document.getElementById("title").value.trim();
	// 내용
	const content = document.getElementById("content").value.trim();
	
	// 카테고리 체크
	if(category === "category"){
		alert("카테고리를 선택하세요.");
		return false;	
	}
	
	// 제목 체크
	if(title === ""){
		alert("제목을 입력하세요.");
		document.getElementById("title").focus();
		return false;
	}
	
	// 제목 길이 제한 (DB VARCHAR(255))
	if(title.length > 255){
		alert("제목은 250자 이내로 작성해주세요.");
		document.getElementById("title").focus();
		return false;
	}
	
	// 내용 체크
	if(content === ""){
		alert("내용을 입력하세요.");
		document.getElementById("content").focus();
		return false;
	}
	
	// 모든 검증 통과
	return true;
}