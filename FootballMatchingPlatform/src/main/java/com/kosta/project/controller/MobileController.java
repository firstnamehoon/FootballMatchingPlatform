package com.kosta.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import com.kosta.project.service.FastMatchingService;


@Controller
@RequiredArgsConstructor
public class MobileController {
	
	private final FastMatchingService fms;

	
	@GetMapping("/fastmatchinglist")
	public String getFastMatchingList(Model model) {
		
		System.out.println("빠른 신청 리스트1번 여기까지는 잘 옴ㅇㅇㅋ");
		
		model.addAttribute("getFastMatching1", fms.getFastMatchingList());
		
		
		return "fastmatchinglist";
	}
	
	
	@GetMapping("/fastmatchinginfo/{matchingSeq}")
	public String getFastMatchingInfo(@PathVariable("matchingSeq") int matchingSeq, Model model) {
		
		System.out.println("빠른매칭 상세 정보");
		model.addAttribute("fastmatchinginfo", fms.getFastMatchingInfo(matchingSeq));
		
		return "fastmatchinginfo";
	}
	
	
	@GetMapping("/addedFieldList")
	public String getAddedFieldList() {
		return "addedFieldList";
	}
	
	@GetMapping("/addedFieldInfo")
	public String getAddedFieldInfo() {
		return "addedFieldInfo";
	}
	
	@GetMapping("/addScore")
	public String getAddScore() {
		return "addScore";
	}
	
	@GetMapping("/applyList")
	public String getApplyList() {
		return "applyList";
	}
	
	
	
	
	
	@GetMapping("/consent1")
	public String getConsent1() {
		return "consent_detail1";
	}
	
	@GetMapping("/consent2")
	public String getConsent2() {
		return "consent_detail2";
	}
	
	
	@GetMapping("/consent")
	public String getConsent() {
		return "consent";
	}
	
	@GetMapping("/createTeam")
	public String getCreateTeam() {
		return "createTeam";
	}
	
	@GetMapping("/findIDPW")
	public String getFindIDPW() {
		return "findIDPW";
	}
	
	@GetMapping("/join")
	public String getJoin() {
		return "join";
	}
	
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@GetMapping("/mainPage")
	public String getMainPage() {
		return "mainPage";
	}
	
	@GetMapping("/matching")
	public String getMatching() {
		return "matching";
	}
	
	@GetMapping("/matchinglist")
	public String getMatchingList() {
		return "matchinglist";
	}
	
	@GetMapping("/myCalender")
	public String getMyCalender() {
		return "myCalender";
	}
	
	@GetMapping("/myMatchingList")
	public String getMyMatchingList() {
		return "myMatchingList";
	}
	
	@GetMapping("/myPageAddinquiry")
	public String getMyPageAddInquiry() {
		return "mypage_addinquiry";
	}
	
	@GetMapping("/myPageCheckPW")
	public String getMyPageCheckPW() {
		return "mypage_check-pw";
	}
	
	@GetMapping("/myPageCheckUser")
	public String getMyPageCheckUser() {
		return "mypage_check-user";
	}
	
	@GetMapping("/myPageEditPW")
	public String getMyPageEditPW() {
		return "mypage_edit-pw";
	}
	
	@GetMapping("/myPageEditInfo")
	public String getMyPageEditInfo() {
		return "mypage_editInfo";
	}
	
	@GetMapping("/myPageInquiryDetail")
	public String getMyPageInquiryDetail() {
		return "mypage_inquiry-detail";
	}
	
	@GetMapping("/myPageInquiry")
	public String getMyPageInquiry() {
		return "mypage_inquiry";
	}
	
	@GetMapping("/myPage")
	public String getMyPage() {
		return "mypage";
	}
	
	@GetMapping("/ranking")
	public String getRanking() {
		return "ranking";
	}
	
	@GetMapping("/teamJoinPossible")
	public String getTeamJoinPossible() {
		return "team_JoinPossible";
	}
	
	@GetMapping("/teamMyTeam")
	public String getTeamMyTeam() {
		return "team_myTeam";
	}
	
	@GetMapping("/teamRank")
	public String getTeamRank() {
		return "team_Rank";
	}
	
	@GetMapping("/teamPage")
	public String getTeamPage() {
		return "team_Page";
	}
	
	
	
	
}
