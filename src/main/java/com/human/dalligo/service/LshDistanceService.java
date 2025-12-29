package com.human.dalligo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.human.dalligo.dao.LshDistanceDAO;
import com.human.dalligo.vo.LshDistanceVO;

@Service
@RequiredArgsConstructor
public class LshDistanceService {

    private final LshDistanceDAO distanceDAO;

    public Integer getDistance(String startCity, String endCity) {
    	
    	// 🔥 DB 조회 직전 정규화
        startCity = normalizeForDB(startCity);
        endCity   = normalizeForDB(endCity);
        
        System.out.println("▶ 거리조회 startCity = [" + startCity + "]");
        System.out.println("▶ 거리조회 endCity   = [" + endCity + "]");

        // 1) 동일 도시면 0km
        if (startCity.equals(endCity)) {
            return 0;
        }

        // 2) DB 조회
        LshDistanceVO vo = distanceDAO.selectDistance(startCity, endCity);

        // 3) 값이 없으면 0km
        if (vo == null || vo.getDistance() == null) {
            return 0;
        }

        // 4) BigDecimal → int 변환
        return vo.getDistance().intValue();
    }
    
    private String normalizeForDB(String city) {
        if (city == null) return null;

        return city.trim()
                   .replace("시", "");
    }


}

