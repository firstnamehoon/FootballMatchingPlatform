package com.kosta.project.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import com.kosta.project.service.FastMatchingService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class FastMatchingRestController {

	private FastMatchingService fms;
	
	@PostMapping("/issuspendeduser")
	public Map<String, Boolean> isSuspendedUser(@RequestBody String userId) {
		//TODO: process POST request
		
		return Map.of("result", fms.isSuspendedUser(userId));
	}
	
	
	
}
