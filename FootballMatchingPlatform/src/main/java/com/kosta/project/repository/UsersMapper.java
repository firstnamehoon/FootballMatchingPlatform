package com.kosta.project.repository;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {
	String selectUserStatus(String userId);
	Date selectSuspenedTime(String userId);
}
