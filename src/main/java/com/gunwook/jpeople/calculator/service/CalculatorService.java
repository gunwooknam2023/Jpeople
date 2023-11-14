package com.gunwook.jpeople.calculator.service;

import com.gunwook.jpeople.calculator.dto.CalculatorRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    public double calculator(CalculatorRequestDto calculatorRequestDto) {
        String calContents = calculatorRequestDto.getCalContents();
        return evaluateExpression(calContents);
    }

    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch)) {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i));
                    i++;
                }
                i--;

                numbers.push(Double.parseDouble(num.toString()));
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operators.isEmpty() && hasPrecedence(ch, operators.peek())) {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(ch);
            }
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        return (op2 != '+' && op2 != '-') || (op1 != '*' && op1 != '/');
    }

    private double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("0으로는 나눗셈이 불가능합니다.");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("기호가 잘못되었습니다.");
        }
    }
}