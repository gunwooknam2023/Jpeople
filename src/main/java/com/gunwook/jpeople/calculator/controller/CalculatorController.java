package com.gunwook.jpeople.calculator.controller;


import com.gunwook.jpeople.calculator.dto.CalculatorRequestDto;
import com.gunwook.jpeople.calculator.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CalculatorController {

    private final CalculatorService calculatorService;

    /**
     * 계산기
     * @param calculatorRequestDto 계산할 정보입력
     * @return 계산값
     */
    @PostMapping("/calculator")
    ResponseEntity<Double> calculator(@RequestBody CalculatorRequestDto calculatorRequestDto){
        double result = calculatorService.calculator(calculatorRequestDto);
        return ResponseEntity.ok(result);
    }

}
