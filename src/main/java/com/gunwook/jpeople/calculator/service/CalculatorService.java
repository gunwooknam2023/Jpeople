package com.gunwook.jpeople.calculator.service;

import com.gunwook.jpeople.calculator.dto.CalculatorRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculatorService {
    public double calculator(CalculatorRequestDto calculatorRequestDto) {
        String op = calculatorRequestDto.getCalContents();

        String[] addSubParts = op.split("[+\\-]");
        double result = 0.0;

        for (String addSubPart : addSubParts) {
            String[] mulDivParts = addSubPart.split("[*/]");
            double mulDivResult = Double.parseDouble(mulDivParts[0]);

            for (int i = 1; i < mulDivParts.length; i++) {
                if (addSubPart.contains("*")) {
                    mulDivResult *= Double.parseDouble(mulDivParts[i]);
                } else if (addSubPart.contains("/")) {
                    mulDivResult /= Double.parseDouble(mulDivParts[i]);
                }
            }

            if (op.contains("+")) {
                result += mulDivResult;
            } else if (op.contains("-")) {
                result -= mulDivResult;
            } else {
                result = mulDivResult;
            }
        }

        return result;
    }
}

