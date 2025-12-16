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
    // 게시판에 insert하기
    int insertApplication(LshApplyVO vo);
    
    int insertApply(
    		@Param("userId") String userId,
            @Param("eventId") int eventId
    );
    
    LshTripVO selectTrip(int tripId);
    List<LshTripVO> selectTripsByEvent(int Id);

    // City
    List<LshCityVO> selectAllCities();
    LshCityVO selectCity(@Param("cityName") String cityName);
    
    // 중복 신청 여부 체크
    int existsApplication(
            @Param("userId") String userId,
            @Param("eventId") int eventId
    );
    // 특정 이벤트의 신청 수
    int countApplicationsByEvent(int eventId);
    
    // trip_applications 테이블에 튶플 생성시 trips테이블의 current_people를 업데이트함
    void updateCurrentPeople(@Param("eventId") int eventId, @Param("count") int count);
    
    LshTripVO getTripByEvent(@Param("eventId") Integer eventId);
    int incrementTripCurrentPeople(@Param("eventId") int eventId);
    int updateTripStatus(@Param("eventId") int eventId, @Param("status") String status);
    
    // 게시판을 trip_applications 테이블을 참조토록 변경
    List<LshApplyListVO> selectAllApplicationsWithEvent();
    
    // 거리 업데이트 : trip객체에 distance를 같이 넘겨야 함
    LshTripVO getTripById(@Param("tripId") int tripId);
    
    LshTripVO selectTripByUserAndEvent(
    		@Param("userId") String userId, 
    		@Param("eventId") int eventId
    );
    // 이벤트 + 유저 신청건 조회
    LshApplyVO selectByUserAndEvent(
            @Param("userId") String userId,
            @Param("eventId") int eventId
    );
    
    // 신청 취소
    int deleteTripApplication(
            @Param("eventId") int eventId,
            @Param("userId") String userId
    );
    // status 상태 저장
    void updateApplicationStatus(
		@Param("userId") String userId,
        @Param("eventId") int eventId,
        @Param("status") String status);

}


