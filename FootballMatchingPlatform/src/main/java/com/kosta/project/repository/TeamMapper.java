package com.kosta.project.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kosta.project.dto.TeamDTO;

@Mapper
public interface TeamMapper {
	
	List<TeamDTO> selectTeamRankList();
	
	List<TeamDTO> selectPossibleJoinTeam();
	
	// 팀생성	
	String selectTeamName(String teamName);	// 팀 이름 중복확인
	boolean insertTeam(TeamDTO dto);
	int selectTeamSeq(String teamName);
	boolean insertTeamMember(String userId, int teamSeq);	// 팀 멤버 추가
	
	boolean insertTeamApply(String userId, int teamSeq);	// 팀 가입 신청
	boolean deleteApplyByTeamSeq(String userId, int teamSeq); 	// 가입신청 취소
	
	List<TeamDTO> selectApplyTeamList(String userId); //가입 신청된 목록
	
	TeamDTO selectTeamInfoByModal(int teamSeq);		// 모달창 팀 정보
	List<Map<String, Integer>> selectTeamMemberTierAndCount(int teamSeq); // 모달 팀원 정보
	
	boolean updateApplyTeamMemberStatus(String userId);	// 신청 상태 변경 - 팀 허락
	
	TeamDTO selectTeamInfo(String userId);
	
}
