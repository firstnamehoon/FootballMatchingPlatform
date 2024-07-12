package com.kosta.project.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import com.kosta.project.service.MatchingService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class MatchingRestController {
	private final MatchingService ms;

	
}