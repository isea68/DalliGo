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

    public LshDistanceVO getDistance(String startCity, String endCity) {
        if (startCity.equals(endCity)) {
            // 동일 도시는 0km, 비용 0으로 처리
            LshDistanceVO vo = new LshDistanceVO();
            vo.setStartCity(startCity);
            vo.setEndCity(endCity);
            vo.setDistanceKm(BigDecimal.ZERO);
            return vo;
        }

        LshDistanceVO distance = distanceDAO.selectDistance(startCity, endCity);
        if (distance == null) {
            // 데이터 없으면 기본값
            distance = new LshDistanceVO();
            distance.setStartCity(startCity);
            distance.setEndCity(endCity);
            distance.setDistanceKm(BigDecimal.ZERO);
        }
        return distance;
    }
}

