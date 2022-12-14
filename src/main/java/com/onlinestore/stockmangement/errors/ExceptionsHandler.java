package com.onlinestore.stockmangement.errors;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionsHandler {
 
	private static final Integer NOTFOUND_ERROR = 0;
	private static final Integer VALIDATION_ERROR = -1;
	private static final Integer CONVERSION_ERROR = -2;
	private static final Integer CONFLICT_PRICE_ERROR = -3;

	@ExceptionHandler(value = {PriceNotFoundException.class})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody List<ErrorResponse> handleException(PriceNotFoundException ex)
    {
		log.error("not found error: " + ex.getMessage());
		String msg = String.format(
				"there is no price with brandId:'%d', productId:'%d', date:'%s'", 
				ex.getBrandId(), ex.getProductId(), ex.getDate());
		return List.of(buildErrorResponse(NOTFOUND_ERROR, msg));
    }
	
	@ExceptionHandler(value = {ConflictPricesException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody List<ErrorResponse> handleException(ConflictPricesException ex)
    {
		log.error("conflict prices error: " + ex.getMessage());
		String msg = String.format(
				"there is several prices overlap in time with the same priority (brandId:'%d', productId:'%d', date:'%s')", 
				ex.getBrandId(), ex.getProductId(), ex.getDate());
		return List.of(buildErrorResponse(CONFLICT_PRICE_ERROR, msg));
    }
	
	@ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody List<ErrorResponse> handleException(ConstraintViolationException ex)
    {
    	return ex.getConstraintViolations().stream()
    		.peek(cv -> log.error("validating error: " + cv.getMessage()))
    		.map(p -> buildErrorResponse(VALIDATION_ERROR, p.getMessage()))
    		.collect(toList());
    }
	
	@ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody List<ErrorResponse> handleException(MethodArgumentTypeMismatchException ex)
    {
		log.error("validating error: " + ex.getMessage());
		String msg = String.format(
				"endpoint conversion error: '%s' parameter has an incorrect format: '%s'", 
				ex.getName(), ex.getValue());
		return List.of(buildErrorResponse(CONVERSION_ERROR, msg));
    }
	
	static ErrorResponse buildErrorResponse(Integer code, String msg)	{
    	return ErrorResponse.builder()
    		.code(code)
    		.errorMessage(msg)
    		.build();
    }
	
}
