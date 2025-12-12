package com.human.dalligo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.human.dalligo.vo.LshUserVO;

@Mapper
public interface LshUserDAO {

    // userId로 단일 유저 조회
    LshUserVO selectUserByUserId(@Param("userId") String userId);

    // 필요하면 ID로도 조회 가능
    LshUserVO selectUserById(@Param("id") int id);
}

