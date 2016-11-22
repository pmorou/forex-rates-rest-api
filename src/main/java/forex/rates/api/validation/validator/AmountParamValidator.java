package forex.rates.api.validation.validator;

import forex.rates.api.validation.annotation.Amount;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@Component
public class AmountParamValidator implements ParamValidator<String> {

    private String message = "The amount you requested is invalid: ";

    @Override
    public void setMessage(String message) {
	this.message = message;
    }

    @Override
    public boolean supports(Class<?> parameterType, Annotation... annotations) {
	if (String.class.isAssignableFrom(parameterType)) {
	    return Arrays.stream(annotations)
		    .map(Annotation::annotationType)
		    .anyMatch(Amount.class::equals);
	}
	return false;
    }

    @Override
    public String validate(Optional<String> object) {
	return object.filter(this::isValidOrElseThrow)
		.orElse(getDefaultValue());
    }

    private boolean isValidOrElseThrow(String amount) {
	try {
	    if (isNotGreaterThanZero(amount)) {
		throw new IllegalArgumentException();
	    }
	} catch (IllegalArgumentException e) {
	    throw new IllegalArgumentException(message + amount);
	}
	return true;
    }

    private boolean isNotGreaterThanZero(String amount) {
	return new BigDecimal(amount).compareTo(BigDecimal.ZERO) < 1;
    }

    private String getDefaultValue() {
	return "1";
    }

}
