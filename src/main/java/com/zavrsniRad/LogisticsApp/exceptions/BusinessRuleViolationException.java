package com.zavrsniRad.LogisticsApp.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusinessRuleViolationException extends RuntimeException {

    private final List<String> details;

    public BusinessRuleViolationException(String message, List<String> details) {
        super(message);
        this.details = details;
    }
}
