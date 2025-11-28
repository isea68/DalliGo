//package com.human.dalligo.service;
//
//import java.time.LocalDateTime;
//
//
//import org.springframework.stereotype.Service;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//
//@Service
//public class WeatherScraperService {
//
//	public String getWeather(String locationCode, LocalDateTime date) {
//	    try {
//	        String url = String.format(
//	            "https://www.weather.go.kr/w/obs-climate/land/past-obs/?stn=%s&yy=%d&mm=%02d&dd=%02d",
//	            locationCode, date.getYear(), date.getMonthValue(), date.getDayOfMonth()
//	        );	        
//	        Document doc = Jsoup.connect(url).get();
//	        // 예시: 온도 가져오기
//	        Element tempElem = doc.selectFirst("table.tbl_weather tbody tr td:nth-child(4)"); 
//	        if (tempElem != null) {
//	            return tempElem.text();
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return "정보 없음";
//	}
//
//}
