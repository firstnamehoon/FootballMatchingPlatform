package com.kosta.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kosta.project.dto.FieldDTO;
import com.kosta.project.repository.FieldMapper;
import com.kosta.project.repository.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FieldService {

	private final FieldMapper fm;
	
	// 구장 전체 리스트 불러오기
	public List<FieldDTO> getFieldList() {
		return fm.selectFieldList();
	}
	
	// 특정 이름을 가진 구장 불러오기
	public List<FieldDTO> getFieldListName(String fieldName) {
		return fm.selectFieldListByFieldName(fieldName);
	}
	
	// 특정 지역에 있는 구장 불러오기
	/* 리턴 타입이 리스트여야 하지 않나? (사실 잘 모름) 테스트 해보고 어찌저찌하셈ㅇㅇ */
	public FieldDTO getFieldListRegion(String fieldAddress) {
		return fm.selectFieldListByRegion(fieldAddress);
	}
	
	// 특정 지역에 있고, 특정 이름을 가진 구장 불러오기
	public FieldDTO getFieldListNameRegion(FieldDTO fieldDTO) {
		return fm.selectFieldListByFieldNameAndRegion(fieldDTO);
	}
	
	// 특정 구장 정보 불러오기(상세페이지)
	public FieldDTO getField(int fieldSeq) {
		return fm.selectField(fieldSeq);
	}
	
	
	
	
}
