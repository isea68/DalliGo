package com.human.dalligo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.human.dalligo.vo.LshDistanceVO;

@Mapper
public interface LshDistanceDAO {

    LshDistanceVO selectDistance(
        @Param("startCity") String startCity,
        @Param("endCity") String endCity
    );
}

