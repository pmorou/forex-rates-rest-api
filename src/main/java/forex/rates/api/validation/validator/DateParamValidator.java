package forex.rates.api.validation.validator;

import forex.rates.api.service.DateTimeProviderService;
import forex.rates.api.validation.annotation.Date;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class DateParamValidator implements ParamValidator<String> {

    private final DateTimeProviderService dateTimeProviderService;

    private String message = "The date you requested is invalid: ";

    public DateParamValidator(DateTimeProviderService dateTimeProviderService) {
	this.dateTimeProviderService = dateTimeProviderService;
    }

    @Override
    public void setMessage(String message) {
	this.message = message;
    }

    @Override
    public boolean supports(Class<?> parameterType, Annotation... annotations) {
	if (String.class.isAssignableFrom(parameterType)) {
	    return Arrays.stream(annotations)
		    .map(Annotation::annotationType)
		    .anyMatch(Date.class::equals);
	}
	return false;
    }

    @Override
    public String validate(Optional<String> object) {
	return object.filter(this::isValidOrElseThrow)
		.orElse(getDefaultValue());
    }

    private boolean isValidOrElseThrow(String date) {
	try {
	    LocalDate parsedDate = LocalDate.parse(date);
	    if (parsedDate.isAfter(dateTimeProviderService.getTodaysDate())) {
		throw new IllegalArgumentException(message + date);
	    }
	} catch (DateTimeParseException e) {
	    throw new IllegalArgumentException(message + date);
	}
	return true;
    }

    private String getDefaultValue() {
	return dateTimeProviderService.getTodaysDateAsString();
    }

}
