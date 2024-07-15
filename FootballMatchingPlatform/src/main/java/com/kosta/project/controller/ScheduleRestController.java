package com.kosta.project.controller;

import com.kosta.project.dto.MatchingScheduleListDTO;
import com.kosta.project.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleRestController {
    private final ScheduleService scheduleService;

    @GetMapping("/month/{month}")
    public ResponseEntity<Collection<MatchingScheduleListDTO>> getMatchingListByMonth(@PathVariable int month) {
        // 세션으로 유저 아이디 구하기
        // @SessionAttribute("userId") String userId
        String userId = "user001";
        Collection<MatchingScheduleListDTO> matchingScheduleListDTOS = new ArrayList<>();
        matchingScheduleListDTOS = scheduleService.getMatchingListByMonth(userId, month);

        return new ResponseEntity<>(matchingScheduleListDTOS, HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<Collection<MatchingScheduleListDTO>> getMatchingListByDate(@PathVariable String date) {
        // 날짜 포맷 검증
        if (!isValidDateFormat(date)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 세션으로 유저 아이디 구하기
        String userId = "user001";
        Collection<MatchingScheduleListDTO> matchingScheduleListDTOS = new ArrayList<>();
        matchingScheduleListDTOS = scheduleService.getMatchingListByDate(userId, date);

        return new ResponseEntity<>(matchingScheduleListDTOS, HttpStatus.OK);
    }

    // 날짜 포맷 검증
    private boolean isValidDateFormat(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
