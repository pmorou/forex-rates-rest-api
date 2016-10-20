package forex.rates.api.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonPropertyOrder({ "error", "httpStatus", "message", "description" })
public class ApiErrorResponse {

    private final boolean error;
    private final int httpStatus;
    private final String message;
    private final String description;

    public ApiErrorResponse(HttpStatus httpStatus, Exception exception) {
	this.message = httpStatus.getReasonPhrase();
	this.httpStatus = httpStatus.value();
	this.description = exception.getMessage();
	this.error = true;
    }

}
