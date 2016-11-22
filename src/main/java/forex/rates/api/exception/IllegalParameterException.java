package forex.rates.api.exception;

public class IllegalParameterException extends RuntimeException {

    public IllegalParameterException() {
    }

    public IllegalParameterException(String message) {
	super(message);
    }

}
