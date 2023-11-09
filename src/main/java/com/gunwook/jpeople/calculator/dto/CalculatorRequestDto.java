package com.gunwook.jpeople.calculator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CalculatorRequestDto {
    private double num1;
    private double num2;
    private String operator;
}
