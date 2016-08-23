package forex.rates.api.model;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private long timestamp;
    private boolean error;
    private int httpStatus;
    private String message;
    private String description;

    public ErrorResponse(long timestamp, HttpStatus httpStatus, Exception exception) {
	this.timestamp = timestamp;
	this.message = httpStatus.getReasonPhrase();
	this.httpStatus = httpStatus.value();
	this.description = exception.getMessage();
	error = true;
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
