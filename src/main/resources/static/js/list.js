// 로그인 안 했을 경우 alert 띄우기
const alertLogin = document.getElementById('alertLogin')?.value;
if (alertLogin) alert("로그인이 필요합니다.");

const ListModule = (function () {
	const container = document.getElementById('list-container');
	const currentCategoryInput = document.getElementById('currentCategory');
	const searchInput = document.getElementById('input_search');
	const searchForm = document.getElementById('searchForm');

	// 공통 fetch 함수
	async function fetchList(url) {
		try {
			const res = await fetch(url, {
				method: 'GET',
				headers: {
					"X-Requested-With": "XMLHttpRequest"
				}
			});
			const html = await res.text();
			const temp = document.createElement('div');
			temp.innerHTML = html;
			const items = temp.querySelectorAll('.list-item'); // fragment 안의 실제 item만 선택
			return {html, items};
		} catch (err) {
			console.error('게시글 로드 실패:', err);
			return {html: '', items: []};
		}
	}

	// 카테고리 버튼 클릭
	async function loadCategory(category) {
		if (currentCategoryInput) currentCategoryInput.value = category;
		const url = `/community/list?category=${encodeURIComponent(category)}`;
		const {html, items} = await fetchList(url);

		container.innerHTML = '';
		items.forEach(item => container.insertAdjacentHTML('beforeend', item.outerHTML));
	}

	// 검색 로직
	function search(event) {
		event.preventDefault(); // submit 기본동작 막기

		const searchValue = searchInput.value.trim();
		if (searchValue === '') {
			alert("검색어를 입력하세요.");
			return;
		}

		const url = `/community/list?search=${encodeURIComponent(searchValue)}`;

		fetchList(url).then(({html, items}) => {
			container.innerHTML = '';

			if (items.length === 0) {
				alert("검색 결과가 없습니다.");
				return;
			}

			// list-item만 append
			items.forEach(item => {
				container.insertAdjacentHTML('beforeend', item.outerHTML);
			});
		});
	}

	// 초기화: 이벤트 핸들러 연결
	function init() {
		if (searchForm) searchForm.addEventListener('submit', search);
		document.querySelectorAll('.category-btn').forEach(btn => {
			btn.addEventListener('click', () => loadCategory(btn.dataset.category));
		});
	}

	return {init};
})();

// DOM 준비 후 초기화
document.addEventListener("DOMContentLoaded", () => ListModule.init());