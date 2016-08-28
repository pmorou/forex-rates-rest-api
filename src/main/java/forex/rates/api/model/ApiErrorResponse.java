package forex.rates.api.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiErrorResponse {

    private final long timestamp;
    private final boolean error;
    private final int httpStatus;
    private final String message;
    private final String description;

    public ApiErrorResponse(long currentTimestamp, HttpStatus httpStatus, Exception exception) {
	this.timestamp = currentTimestamp;
	this.message = httpStatus.getReasonPhrase();
	this.httpStatus = httpStatus.value();
	this.description = exception.getMessage();
	this.error = true;
    }

}
