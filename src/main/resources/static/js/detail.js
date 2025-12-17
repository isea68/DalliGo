document.addEventListener('DOMContentLoaded', () => {
	// 좋아요, 댓글에서 쓸 공통 게시글 번호
	const postId = parseInt(document.getElementById('postId').value, 10);
	
	//좋아요 버튼
	const likeBtn = document.getElementById('like-btn');
	const heart = likeBtn.querySelector('.heart');
	const likeCountSpan = document.getElementById('like-count');

	likeBtn.addEventListener('click', async () => {
		// 비회원이 클릭했을 경우
		if(!loginUserExists) {
			alert("로그인이 필요합니다."); 
			return;
		}
		
		try{
			const response = await fetch(`/community/post/${postId}/like`, { method: "POST" });
			const data = await response.json(); // { liked: true/false, count: 숫자 }
			
			// 하트색 바꾸기
			if(data.liked){
				heart.innerText = '❤️'; // 빨간 하트
			}else {
				heart.innerText = '🤍'; // 빈 하트
			}
			
			// 좋아요 수도 함께 갱신
			likeCountSpan.innerText = data.count;
		}catch (err) {
			console.error(err);
			alert("좋아요 처리 중 요류가 발생했습니다.");
		}
	});

	// 댓글 등록, 수정, 삭제
	const modal = document.getElementById('modal');
	const commentBtn = document.querySelector('.reaction .comment'); // 댓글 버튼 선택
	const insertBtn = document.getElementById('insert-button');
	const inputComment = document.getElementById('input-comment');
	const commentContainer = document.getElementById('comment-container');

	let editingCommentId = null; // null이면 새 댓글, 숫자면 수정 중

	// 댓글 클릭 시 모달창 열기
	if (commentBtn) { // JS에서는 truthy, falsy 평가 - 요소가 존재하면 실행
		commentBtn.addEventListener('click', () => {
			
			// 비회원이 클릭했을 경우
			if(!loginUserExists) {
				alert("로그인이 필요합니다."); 
				return;
			}
			
			// 회원이면 모달창 열기
			modal.style.display = 'block';
			inputComment.value = '';
			editingCommentId = null; // 새 댓글 모드
		});
	}

	// 모달 배경 클릭 시 닫기
	modal.addEventListener('click', (e) => {
		if (e.target === modal) {
			modal.style.display = 'none';
			inputComment.value = '';
			editingCommentId = null;
		}
	});

	// 댓글 등록 / 수정
	insertBtn.addEventListener('click', async () => {
		const content = inputComment.value.trim();
		if (!content) return alert('댓글을 입력하세요.');

		try {
			let response, comment;

			if (editingCommentId) {
				response = await fetch(`/community/modComment/${editingCommentId}`, {
					method: 'PUT',
					headers: {'Content-Type': 'application/json'},
					body: JSON.stringify({content})
				});
				if (!response.ok) throw new Error('수정 실패');

				comment = await response.json();

				// 화면 갱신
				const commentBox = commentContainer.querySelector(`[data-comment-id="${editingCommentId}"]`);
				commentBox.querySelector('.comment-text').innerText = comment.content;

			} else {
				// 새 댓글 등록
				response = await fetch('/community/uploadComment', {
					method: 'POST',
					headers: {'Content-Type': 'application/json'},
					body: JSON.stringify({postId, content})
				});
				const data = await response.json(); // 서버에서 받은 JSON
				if (!response.ok) throw new Error(data.message || '댓글 등록 실패');

				comment = data;

				// 화면에 추가
				const commentBox = document.createElement('div');
				commentBox.classList.add('comment-box');
				commentBox.setAttribute('data-comment-id', comment.id);
				commentBox.innerHTML = `
									<div class="comment-header">
										<div class="comment-username">${comment.nickName}</div>
										<div class="comment-date">${comment.inDate}</div>
									</div>
									<div class="comment-text">${comment.content}</div>
									<div class="comment-button">
										<button type="button" class="button-detail button-mod" data-id="${comment.id}">수정</button>
										<button type="button" class="button-detail button-del" data-id="${comment.id}">삭제</button>
									</div>
									`;

				commentContainer.prepend(commentBox); // 맨 위에 추가
			}

			// 입력 필드 초기화 및 모달 닫기
			inputComment.value = '';
			modal.style.display = 'none';
			editingCommentId = null;

		} catch (err) {
			console.error(err);
			alert(editingCommentId ? '댓글 수정 중 오류가 발생했습니다.' : '댓글 등록 중 오류가 발생했습니다');
		}
	});

	// 수정, 삭제 이벤트 위임
	if (commentContainer) {
		commentContainer.addEventListener('click', async (e) => {
			const target = e.target;

			// 삭제
			if (target.classList.contains('button-del')) {
				e.preventDefault();
				const commentId = parseInt(target.dataset.id, 10); // 문자열을 숫자로 변환
				if (!confirm('정말 삭제하시겠습니까?')) return;

				fetch(`/community/delComment/${commentId}`, {method: 'DELETE'})
					.then(response => {
						if (!response.ok) throw new Error('삭제 실패');
						target.closest('.comment-box').remove();
					})
					.catch(err => {
						console.error(err);
						alert('댓글 삭제 중 오류가 발생했습니다.');
					});
			}

			// 수정
			if (target.classList.contains('button-mod')) {
				e.preventDefault();
				const commentBox = target.closest('.comment-box');
				const commentId = parseInt(target.dataset.id, 10);
				const currentText = commentBox.querySelector('.comment-text').innerText;

				// 모달 열고 기존 내용 넣기
				modal.style.display = 'block';
				inputComment.value = currentText;
				editingCommentId = commentId; // 수정 모드
			}
		})
	}
});