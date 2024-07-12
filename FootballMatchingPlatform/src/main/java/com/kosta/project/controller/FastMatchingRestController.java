package com.kosta.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import com.kosta.project.dto.FastMatchingDTO;
import com.kosta.project.service.FastMatchingService;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class FastMatchingRestController {

	private final FastMatchingService fms;
	
	
	// 정지 여부 체크하기
	@PostMapping("/isSuspendedUser")
	public Map<String, Boolean> isSuspendedUser(@RequestBody String userId) {
		//TODO: process POST request
		
		return Map.of("result", fms.isSuspendedUser(userId));
	}
	
	
	// 빠른 신청 리스트
	@GetMapping("/getFastMatching1")
	public Map<String, List<FastMatchingDTO>> getFastMatching1() {
		
		return Map.of("result", fms.getFastMatchingList());
	}
	
	
	
	
}
