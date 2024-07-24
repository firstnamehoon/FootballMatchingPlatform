package com.kosta.project.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.kosta.project.dto.InquiryDTO;
import com.kosta.project.dto.MatchingScheduleListDTO;
import com.kosta.project.dto.TeamDTO;
import com.kosta.project.dto.UserDTO;
import com.kosta.project.service.ScheduleService;
import com.kosta.project.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {
	
	private final UserService us;
	private final ScheduleService ss;
	
	//로그인하기 (세션에 본인 등급 넣기-상단 네비에 넣을 용도, 시/도)
	@GetMapping("/login")
	public UserDTO getUserLogin(@RequestParam String userId,String password){
		
		return us.getUserLogin(userId, password);
	}
	
//	@PostMapping("/login")
//	public UserDTO login(@RequestParam String userId, @RequestParam String password, HttpServletRequest request) {
//
//		System.out.println("뭔데");
//		System.out.println(userId);
//		System.out.println(password);
//
//		//1. 회원 정보 조회
//		UserDTO user = us.getUserLogin(userId, password);
//
//		//2. 세션에 회원정보 저장
//		if(user != null) {
//			HttpSession session = request.getSession();
//			session.setAttribute("loginUser", user);
//		}
//		System.out.println("user: "+user);
//		return user;
//	}
//
	//소셜 로그인하기
	@GetMapping("/snslogin")
	public UserDTO getUserSnsLogin(String userId, String password) {
		return us.getUserSnsLogin(userId, password);
	}
	
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "/login";
	}

	// 아이디 중복 여부 확인하기
	@PostMapping("/getUserId")
	@ResponseBody
	public boolean getUserIdByUserId(String userId) {
		System.out.println("getUserIdByUserId 들어옴");
		return us.getUserIdByUserId(userId);
	}

	/////////////////////////////////////

	
	// 닉네임 중복 여부 확인하기
	@PostMapping("/getUserNickname")
	public boolean getUserNicknameByNickname(@RequestParam String nickname) {
		return us.getUserNicknameByNickname(nickname);
	}
	
	// 이메일 중복 여부 확인하기
	@PostMapping("/getEmail")
	public boolean getEmailByEmail(String email) {
		return us.getEmailByEmail(email);
	}
	
	//회원가입하기
	@PostMapping("/addUserJoin")
	public Map<String, String> addUserJoin(@RequestBody UserDTO dto) {
		us.addUserJoin(dto);
		return Map.of("result","ok");
	}
	
	//이름, 이메일이 일치하는 아이디 불러오기
	@GetMapping("/getIdByNameEmail")
	public Map<String, String> getIdByNameEmail1(@RequestBody UserDTO dto) {
		return Map.of("result", us.getIdByNameAndEmail(dto.getName(), dto.getEmail()));
	}
	
	// 내 정보 불러오기
		@GetMapping("/getMyInfo")
		public Map<String, UserDTO> getMyInfoByUserId(@SessionAttribute(name ="loginUser", required = false) UserDTO user) {
			if(user == null) {
				return Map.of("result",null);
			}
			else
				return Map.of("result", us.getMyInfoByUserId(user.getUserId()));
		}
		
	// 내 팀 정보 불러오기(마이페이지)
	@GetMapping("/getMyTeamInfo")
	public Map<String, TeamDTO> getTeamInfoByUserId(@SessionAttribute(name ="loginUser", required = false) UserDTO user) {
		TeamDTO team = us.getTeamInfoByUserId(user.getUserId());
		System.out.println(team);
		if(team == null) {
			return Map.of("result", TeamDTO.builder().teamSeq(0).build());
		}
		else {
		return Map.of("result", team);
		}
	}
	
	// 내 등급 불러오기
	@GetMapping("/getMyTierAndScore")
	public Map<String, UserDTO> getMyTierAndScoreByUserId(@SessionAttribute(name ="loginUser", required = false) UserDTO user) {
		if(user == null) {
			return Map.of("result",null);
		}
		else
		return Map.of("result", us.getMyTierAndScoreByUserId(user.getUserId()));
	}
	
	// 내 전적 불러오기
	@GetMapping("/getMyMatchedInfo")
	public Map<String, UserDTO> getMyMatchedInfoByUserId(@SessionAttribute(name ="loginUser", required = false) UserDTO user) {
		if(user == null) {
			return Map.of("result",null);
		}
		else
		return Map.of("result", us.getMyMatchedInfoByUserId(user.getUserId()));
	}
	
	// 내 이메일, 닉네임 불러오기
	@GetMapping("/getNicknameAndEmail")
	public Map<String, UserDTO> getNicknameAndEmailByUserIdForMyPage(String userId) {
		return Map.of("result", us.getNicknameAndEmailByUserIdForMyPage(userId));
	}
	
	// 내 문의내역 2개(최신 2개) 불러오기
	@GetMapping("/pathTwoInquiry")
	public Map<String, List<InquiryDTO>> getTwoInquiry(@SessionAttribute(name ="loginUser", required = false)UserDTO user) {
		
		List<InquiryDTO> inq = us.getTwoInquiry(user.getUserId());
		
		if(inq == null) {
			inq.add(InquiryDTO.builder().inquirySeq(0).build());
			return Map.of("result",inq);
		}
		else
		return Map.of("result", inq);
	}
	
	
	
	// 아이디, 이메일 일치하는 비밀번호 불러오기
	@GetMapping("/getPassword")
	public Map<String, String> getPasswordByUserIdAndEmail(@RequestBody UserDTO dto) {
		return Map.of("result", us.getPasswordByUserIdAndEmail(dto.getUserId(), dto.getPassword()));
	}
	
	// 비밀번호 업데이트하기
	@PostMapping("/setPassword")
	public Map<String,String> setPasswordByUserId(@SessionAttribute(name ="loginUser", required = false) UserDTO user, @RequestParam String pw) {
		us.setPasswordByUserId(user.getUserId(), pw);
		return Map.of("result", "ok");
	}
	
	// 비밀번호 확인하기
		@GetMapping("/findMyPw")
		public boolean findMyPw(@SessionAttribute(name ="loginUser", required = false) UserDTO user, @RequestParam String pwInput){		
			return us.findMyPw(user.getUserId(), pwInput);
		}
	
	// 내 정보 수정하기
	@PostMapping("/setMyInfo")
	public Map<String, String> setMyInfoByUserId(@SessionAttribute(name ="loginUser", required = false) UserDTO user, @RequestBody UserDTO dto){
		us.setMyInfoByUserId(user,dto);
		return Map.of("result", "ok");
	}
	
	// 예정된 매칭 시퀀스 불러오기(회원탈퇴 검사)
	@GetMapping("/getFutureMatchingSeq")
	public Map<String, List<MatchingScheduleListDTO>> getFutureMatchingSeq(@SessionAttribute(name ="loginUser", required = false) UserDTO user){
		List<MatchingScheduleListDTO> match = ss.getFutureMatchingSeq(user.getUserId());
		if(match == null) {
			match.add(MatchingScheduleListDTO.builder().matchingSeq(0).build());
			return Map.of("result", match);
		}
		else 
			return Map.of("result", ss.getFutureMatchingSeq(user.getUserId()));
	}
	
	// 회원 탈퇴하기
	@PostMapping("/setUserStatusWidthdraw")
	public Map<String, String> setUserStatusByUserId(@SessionAttribute(name ="loginUser", required = false) UserDTO user){
		us.setUserStatusByUserId(user);
		return Map.of("result", "ok");
	}
	
	// 내 문의 내역 불러오기(전체)
	@GetMapping("/getAllInquiry")
	public Map<String, List<InquiryDTO>> getAllInquiry(@SessionAttribute(name ="loginUser", required = false) UserDTO user) {
		List<InquiryDTO> inq = us.getTwoInquiry(user.getUserId());
		
		if(inq == null) {
			inq.add(InquiryDTO.builder().inquirySeq(0).build());
			return Map.of("result", inq);
		}
		else
			return Map.of("result", inq);
	}
	
	// 선택한 문의 내역 상세 보기
	@GetMapping("/pathDetailInfo/{inquirySeq}")
	public Map<String, String> getDetailInfoByInquirySeq(@PathVariable("inquirySeq") int inquirySeq, HttpServletRequest request) {
		InquiryDTO inquiry = us.getDetailInfoByInquirySeq(inquirySeq);
		HttpSession session = request.getSession();
		session.setAttribute("inquiry", inquiry);
		return Map.of("result", "ok");
	}
	
	// 문의 내역 추가하기
	@PostMapping("/addInquiry")
	public Map<String, String> addInquiry(@RequestBody InquiryDTO dto) {
		us.addInquiry(dto);
		return Map.of("result", "ok");
	}
	
}

