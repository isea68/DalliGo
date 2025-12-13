package com.human.dalligo.vo.academy;


import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CourseVO {
	private Integer id;
	private Integer trainerId;
	private String title;
	private LocalDateTime startDate;
	private String selectDay;
	private String location;
	private String place;
	private String supportItem;
	private Integer price;
	private String duration;
	private String prphotoOri; // file original name
	private String prphotoNew; // file changed name
	private Integer capacity;
	private String eduType;
	private String deleteToken; //삭제용 토큰 필드 추가
	
	  // Lombok @Data가 없을 경우 setter 직접 작성
    public void setDeleteToken(String deleteToken) {
        this.deleteToken = deleteToken;
    }

    // 필요하면 getter도 직접 작성
    public String getDeleteToken() {
        return deleteToken;
    }
}
