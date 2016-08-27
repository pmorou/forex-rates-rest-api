package forex.rates.api.model;

import org.springframework.http.HttpStatus;

public class ApiErrorResponse {

    private long timestamp;
    private boolean error;
    private int httpStatus;
    private String message;
    private String description;

    public ApiErrorResponse(long currentTimestamp, HttpStatus httpStatus, Exception exception) {
	this.timestamp = currentTimestamp;
	this.message = httpStatus.getReasonPhrase();
	this.httpStatus = httpStatus.value();
	this.description = exception.getMessage();
	this.error = true;
    }

    public long getTimestamp() {
	return timestamp;
    }

    public boolean isError() {
	return error;
    }

    public int getHttpStatus() {
	return httpStatus;
    }

    public String getMessage() {
	return message;
    }

    public String getDescription() {
	return description;
    }

}
