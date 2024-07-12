package com.kosta.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FastMatchingDTO {
	int matchingSeq;
	String matchingTier;
	String matchingDate;
	int matchingTime;
	String fieldAddress;
	String fieldAddressDetail;
	String fieldImg;
	int playerCount;
	int fieldSeq;
}
