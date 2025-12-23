package com.human.dalligo.vo;

import lombok.Data;

@Data
public class LshPageVO {
	private int page;         // 현재 페이지
    private int size;         // 페이지당 개수
    private int totalCount;   // 전체 데이터 개수

    private int totalPages;   // 전체 페이지 수
    private int startPage;    // 화면에 보이는 시작 페이지
    private int endPage;      // 화면에 보이는 끝 페이지

    private boolean prev;     // 이전 버튼 활성화 여부
    private boolean next;     // 다음 버튼 활성화 여부

    public LshPageVO(int page, int size, int totalCount) {
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;

        this.totalPages = (int) Math.ceil((double) totalCount / size);

        // 화면 페이지 블록 계산 (10개씩)
        int tempEnd = (int) (Math.ceil(page / 10.0) * 10);
        this.startPage = tempEnd - 9;

        this.endPage = Math.min(tempEnd, totalPages);

        this.prev = this.startPage > 1;
        this.next = this.endPage < totalPages;
    }

    public int getOffset() {
        return (page - 1) * size;
    }
}
