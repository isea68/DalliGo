package com.human.dalligo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.human.dalligo.vo.*;

@Mapper
public interface LshTripDAO {

    // Trip
    int insertTrip(LshTripVO vo);
    int updateTrip(LshTripVO vo);
    int insert(LshApplyVO vo);
    LshTripVO selectTrip(int tripId);
    List<LshTripVO> selectTripsByEvent(int Id);

    // City
    List<LshCityVO> selectAllCities();
    LshCityVO selectCity(String cityName);

    // Applications
    int insertApplication(LshApplyVO vo);
    void increaseCurrentPeople(int tripId);

    List<Map<String, Object>> selectAllApplicationsWithEvent();
    
    LshTripVO selectTripByUserAndEvent(
    		@Param("userId") String userId, 
    		@Param("eventId") int eventId
    );
    
    LshApplyVO selectByUserAndEvent(
            @Param("userId") String userId,
            @Param("eventId") int eventId
    );

}

