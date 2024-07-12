package com.kosta.project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.kosta.project.dto.CloseTimeDTO;
import com.kosta.project.dto.FieldsDTO;
import com.kosta.project.dto.MatchingAddListsDTO;
import com.kosta.project.dto.MatchingConditionDTO;
import com.kosta.project.dto.MatchingCountDTO;
import com.kosta.project.dto.MatchingsDTO;
import com.kosta.project.dto.AddMatchingDataDTO;
import com.kosta.project.dto.addMatchingListInfo;
import com.kosta.project.dto.addMatchingsDTO;
import com.kosta.project.repository.FieldMapper;
import com.kosta.project.repository.MatchingMapper;
import com.kosta.project.repository.TeamMapper;
import com.kosta.project.repository.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchingService {

	private final MatchingMapper mm;
	private final FieldMapper fm;
	private final UserMapper um;
	private final TeamMapper tm;

	public boolean isLeader(String userId) {
		boolean result = false;
		Integer teamSeq = tm.selectTeamSeq(userId);

		if(teamSeq != null) {
			result = true;
		}

		return result;
	}

	public String isTeam(String userId) {
		String msg = "true";
		int teamMemberCount = tm.selectTeamMemberCount(userId);

		if(teamMemberCount < 5) {
			return "팀원이 부족합니다.";
		}

		List<String> memberIds = tm.selectTeamMemberIds(userId);

		for(int i=0; i<memberIds.size(); i++) {
			String suspendedTime = um.selectSuspendedTime(memberIds.get(i));
			if(suspendedTime != null) {
				LocalDate suspendedDate = LocalDate.parse(suspendedTime);
				LocalDate now = LocalDate.now();
				if(now.isBefore(suspendedDate)) {
					msg = "정지된 팀원이 있습니다.";
				}
			}
		}
		return msg;
	}

	public List<MatchingsDTO> getMatchingsList(MatchingConditionDTO dto){
		List<MatchingsDTO> resList = null;
		Set<Integer> existingFieldSeqs = new HashSet<>();
		String matchingTime = dto.getMatchingTime();
		String[] matchingTimes = matchingTime.split(" ");
		ArrayList<String> matchingTimeList = new ArrayList<String>(Arrays.asList(matchingTimes));
		List<MatchingsDTO> matchingListByTime = new ArrayList<MatchingsDTO>();
		List<MatchingsDTO> matchingList = new ArrayList<MatchingsDTO>();

		for(int i=0; i<matchingTimeList.size(); i++) {
			dto.setMatchingTime(matchingTimeList.get(i));
			matchingListByTime = mm.selectMatchingsList(dto);
			for(int j=0; j<matchingListByTime.size(); j++) {
				matchingList.add(matchingListByTime.get(j));
			}
		}
		// 입력값에 따른 매칭중인 매칭정보 ex) 24-07-10, 14시 입력

		resList = matchingList;
		// 이용 가능한 구장번호 가져오기
		List<Integer> possField = fm.selectFieldSeq();

		boolean ck = false;
		// timeList : 14
		for(int i=0; i<possField.size(); i++) {

			int fieldSeq = possField.get(i);

			//time : 14, 16
			for(int j=0; j<matchingTimes.length; j++) {
				String time = matchingTimes[j];

				for(int k=0; k < resList.size(); k++) {
					if(resList.get(k).getMatchingTime() == Integer.parseInt(time) && resList.get(k).getFieldSeq()==fieldSeq) {
						ck = true;
						break;
					}
				}

				if(ck == true) {
					ck=false;
					continue;
				}

				CloseTimeDTO ctDTO = CloseTimeDTO.builder()
						.closedDate(dto.getMatchingDate())
						.closedTime(time)
						.fieldSeq(fieldSeq)
						.build();
				if(!fm.selectCloseTime(ctDTO).isEmpty())
					continue;

				//추가해줄 DTOs
				addMatchingListInfo amlDTO = fm.selectFieldAddressAndName(fieldSeq);
				MatchingsDTO addDTO = MatchingsDTO.builder()
						.matchingDate(dto.getMatchingDate())
						.matchingTime(Integer.parseInt(time))
						.fieldAddress(amlDTO.getFieldAddress())
						.fieldName(amlDTO.getFieldName())
						.fieldSeq(amlDTO.getFieldSeq())
						.build();
				resList.add(addDTO);
			}
		}
		//		for(int i=0; i<matchingTimeList.size(); i++) {
		//			String time = matchingTimeList.get(i);
		//			
		//			// possField : 3, 4, 5, 7
		//			for(int j=0; j<possField.size(); j++) {
		//				CloseTimeDTO ctDTO = CloseTimeDTO.builder()
		//						.closedDate(dto.getMatchingDate())
		//						.closedTime(time)
		//						.fieldSeq(possField.get(j))
		//						.build();
		//				
		//				if(!fm.selectCloseTime(ctDTO).isEmpty()) {
		//					continue;
		//				}
		//				
		//				// possField 구장 정보 저장 - address, name
		//				addMatchingListInfo amlDTO = fm.selectFieldAddressAndName(possField.get(j));
		//				MatchingsDTO addDTO = MatchingsDTO.builder()
		//						.matchingDate(dto.getMatchingDate())
		//						.matchingTime(Integer.parseInt(time))
		//						.fieldAddress(amlDTO.getFieldAddress())
		//						.fieldName(amlDTO.getFieldName())
		//						.fieldSeq(amlDTO.getFieldSeq())
		//						.build();
		//				
		//				if(resList.get(i).getFieldSeq() != addDTO.getFieldSeq()) {
		//					System.out.println(i + ": " + resList.get(i).getFieldSeq() + ", " + addDTO.getFieldSeq());
		//					resList.add(addDTO);
		//				}
		//			}
		//		}

		return resList;
	}

	public List<MatchingsDTO> getMatchingsListByRegion(MatchingConditionDTO dto){
		List<MatchingsDTO> resList = null;
		resList = getMatchingsList(dto);
		String matchingRegion = dto.getFieldAddress();
		String[] matchingRegions = matchingRegion.split(" ");
		ArrayList<String> matchingRegionList = new ArrayList<String>(Arrays.asList(matchingRegions));
		List<MatchingsDTO> matchingListByRegion = new ArrayList<MatchingsDTO>();

		for(int i=0; i<resList.size(); i++) {
			String region = resList.get(i).getFieldAddress().split(" ")[1];
			for(int j=0; j<matchingRegionList.size(); j++) {
				if((region.equals(matchingRegionList.get(j)))) {
					matchingListByRegion.add(resList.get(i));
				}
			}
		}

		return matchingListByRegion;
	}

	public FieldsDTO getFieldInfo(int fieldSeq) {
		FieldsDTO fDTO = null;
		fDTO = fm.selectFieldInfo(fieldSeq);

		return fDTO;
	}

	public boolean addMatcings(AddMatchingDataDTO dto) {
		for(int i=0; i<dto.getMDTO().size(); i++) {
			if(dto.getMDTO().get(i).getMatchingSeq() == 0) {
				addMatchingsDTO aDTO = addMatchingsDTO.builder()
						.matchingDate(dto.getMDTO().get(i).getMatchingDate())
						.matchingTime(dto.getMDTO().get(i).getMatchingTime())
						.fieldSeq(dto.getMDTO().get(i).getFieldSeq())
						.build();
				mm.insertMatchings(aDTO);
			}
			else {
				if(!mm.selectMatchingStatus(dto.getMDTO().get(i).getMatchingSeq()).equals("매칭중")) {
					return false;
				}
			}
		}
		
		if(dto.getType().equals("개인")) {
			mm.insertMatchingAdds(dto.getUserId());
		}
		else if(dto.getType().equals("팀")) {
			int teamSeq = tm.selectTeamSeq(dto.getUserId());
			mm.insertMatchingAddsByTeam(teamSeq);
		}

		int matchingAddSeq = mm.selectMatchingAddSeq();
		System.out.println(matchingAddSeq);
		
		for(int i=0; i<dto.getMDTO().size(); i++) {
			MatchingAddListsDTO addListDTO = MatchingAddListsDTO.builder()
					.matchingSeq(dto.getMDTO().get(i).getMatchingSeq())
					.matchingAddSeq(matchingAddSeq)
					.build();
			mm.insertMatchingAddLists(addListDTO);
			MatchingCountDTO mcDTO = MatchingCountDTO.builder()
					.matchingSeq(addListDTO.getMatchingSeq())
					.userTier(dto.getUserTier())
					.build();
			int playerCount = mm.selectMatchingMemberCount(mcDTO);
			
			if(playerCount == 10) {
				mm.updateMatchings(addListDTO.getMatchingSeq());
				mm.updateMatchingAddLists(mcDTO);
			}
		}

		return true;
	}
}