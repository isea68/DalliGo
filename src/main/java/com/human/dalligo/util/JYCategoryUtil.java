package com.human.dalligo.util;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class JYCategoryUtil {
	
	private final Map<String, String> CategoryMap = Map.of(
			"info", "러닝정보/노하우",
			"review", "대회정보후기",
			"recommend", "러닝코스추천",
			"mate", "러닝메이트소모임",
			"useditem", "중고거래",
			"trainshare", "훈련일지/목표공유",
			"qa", "Q&A/자유게시판"
			);
	
	public String toKorean(String english) {
		String defaultCategory = "";
		return CategoryMap.getOrDefault(english, defaultCategory);
	}

}
