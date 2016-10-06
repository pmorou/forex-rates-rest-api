package forex.rates.api.controller;

import forex.rates.api.model.response.ApiErrorResponse;
import forex.rates.api.service.DateTimeProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResponseEntityExceptionHandler {

    private final DateTimeProviderService dateTimeProviderService;

    public ResponseEntityExceptionHandler(DateTimeProviderService dateTimeProviderService) {
	this.dateTimeProviderService = dateTimeProviderService;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
	return new ApiErrorResponse(
		dateTimeProviderService.getCurrentTimestamp(), HttpStatus.BAD_REQUEST, e);
    }

}
