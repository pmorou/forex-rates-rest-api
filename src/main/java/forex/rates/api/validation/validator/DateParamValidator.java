package forex.rates.api.validation.validator;

import forex.rates.api.exception.IllegalParameterException;
import forex.rates.api.service.DateTimeProviderService;
import forex.rates.api.validation.annotation.Date;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DateParamValidator implements ParamValidator<String> {

    private final List<DayOfWeek> UNSUPPORTED_DAYS = Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
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

    private String getDefaultValue() {
	return dateTimeProviderService.getTodaysDateAsString();
    }

    private boolean isValidOrElseThrow(String date) {
	try {
	    LocalDate parsedDate = LocalDate.parse(date);
	    if (isFromFuture(parsedDate) || isUnsupportedDay(parsedDate)) {
		throwIllegalParameterException(date);
	    }
	} catch (DateTimeParseException e) {
	    throwIllegalParameterException(date);
	}
	return true;
    }

    private boolean isFromFuture(LocalDate date) {
	return date.isAfter(dateTimeProviderService.getTodaysDate());
    }

    private boolean isUnsupportedDay(LocalDate date) {
	DayOfWeek dayOfWeek = date.getDayOfWeek();
	return UNSUPPORTED_DAYS.contains(dayOfWeek);
    }

    private void throwIllegalParameterException(String date) {
	throw new IllegalParameterException(message + date);
    }

}
