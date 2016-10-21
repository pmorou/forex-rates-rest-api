package forex.rates.api.controller;

import forex.rates.api.model.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
	return new ApiErrorResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handleAnyOtherException(Exception e) {
	return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

}
