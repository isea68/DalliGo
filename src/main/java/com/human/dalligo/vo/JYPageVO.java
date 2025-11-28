package com.human.dalligo.vo;

import java.util.List;

import lombok.Data;

@Data
public class JYPageVO {
    private int page;         // 현재 페이지
    private int size;         // 페이지당 개수
    private int totalCount;   // 전체 데이터 개수
    private List<JYListVO> list; // 실제 게시물 리스트

    public JYPageVO(int page, int size, int totalCount, List<JYListVO> list) {
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
        this.list = list;
    }

    public int getOffset() {
        return (page - 1) * size;
    }
    
    public boolean hasMore() {
    	return page * size < totalCount; // 더 불러올 데이터가 남아있는지 확인
    }
}
