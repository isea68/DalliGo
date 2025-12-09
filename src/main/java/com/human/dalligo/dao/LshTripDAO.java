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
    void insertApplication(LshApplyVO vo);
    int countApplicationsByEvent(int eventId);
    int existsApplication(@Param("userId") String userId, @Param("eventId") int eventId);
    void increaseCurrentPeople(int tripId);
    
    // 게시판의 신청인원 합산
    List<Map<String, Object>> selectGroupedTripStatus();
    
    LshTripVO getTripByEvent(@Param("eventId") int eventId);
    int incrementTripCurrentPeople(@Param("eventId") int eventId);
    int updateTripStatus(@Param("eventId") int eventId, @Param("status") String status);

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

