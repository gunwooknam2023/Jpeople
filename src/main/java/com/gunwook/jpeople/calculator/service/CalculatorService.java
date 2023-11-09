package com.gunwook.jpeople.calculator.service;

import com.gunwook.jpeople.calculator.dto.CalculatorRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculatorService {
    public double calculator(CalculatorRequestDto calculatorRequestDto) {
        double num1 = calculatorRequestDto.getNum1();
        double num2 = calculatorRequestDto.getNum2();
        String operator = calculatorRequestDto.getOperator();

        if(operator.equals("+")){
            return num1+num2;
        } else if(operator.equals("-")){
            return num1-num2;
        } else if(operator.equals("*")){
            return num1*num2;
        } else if(operator.equals("/")){
            return num1/num2;
        } else{
            throw new IllegalArgumentException("올바른 기호를 입력하세요.");
        }

    }
}
