package forex.rates.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Data
@JsonPropertyOrder({ "error", "httpStatus", "message", "description" })
public class ApiErrorResponse {

    @Getter(onMethod = @__(@JsonIgnore))
    private final String DEFAULT_MESSAGE = "No message available";

    private final boolean error;
    private final int httpStatus;
    private final String message;
    private final String description;

    public ApiErrorResponse(HttpStatus httpStatus, Exception exception) {
	this.message = httpStatus.getReasonPhrase();
	this.httpStatus = httpStatus.value();
	this.description = getMessage(exception);
	this.error = true;
    }

    private String getMessage(Exception exception) {
	return exception.getMessage() == null ? DEFAULT_MESSAGE : exception.getMessage();
    }

}
