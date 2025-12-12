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

}

