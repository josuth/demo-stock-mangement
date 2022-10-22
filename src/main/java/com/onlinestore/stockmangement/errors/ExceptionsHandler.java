package com.onlinestore.stockmangement.errors;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionsHandler {
 
	@ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody List<ErrorResponse> handleException(ConstraintViolationException ex)
    {
    	List<ErrorResponse> list = ex.getConstraintViolations().stream()
    		.peek(cv -> log.error("endpoint validating error: " + cv.getMessage()))
    		.map(ExceptionsHandler::buildErrorResponse)
    		.collect(Collectors.toList());
    	return list;
    }
	
	static ErrorResponse buildErrorResponse(ConstraintViolation<?> cv)	{
    	return ErrorResponse.builder()
    		.code(-1)
    		.errorMessage(cv.getMessage())
    		.build();
    }
}
