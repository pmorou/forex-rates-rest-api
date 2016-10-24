package forex.rates.api.validation.validator;

import forex.rates.api.service.AvailableCurrenciesService;
import forex.rates.api.validation.annotation.Currencies;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

@Component
public class CurrenciesParamValidator implements ParamValidator<String[]> {

    private final AvailableCurrenciesService availableCurrenciesService;

    private String message = "The currency you requested is invalid: ";

    public CurrenciesParamValidator(AvailableCurrenciesService availableCurrenciesService) {
	this.availableCurrenciesService = availableCurrenciesService;
    }

    @Override
    public void setMessage(String message) {
	this.message = message;
    }

    @Override
    public boolean supports(Class<?> parameterType, Annotation... annotations) {
	if (String[].class.isAssignableFrom(parameterType)) {
	    return Arrays.stream(annotations)
		    .map(Annotation::annotationType)
		    .anyMatch(Currencies.class::equals);
	}
	return false;
    }

    @Override
    public String[] validate(Optional<String[]> object) {
	return object.map(elementsToUpperCase())
		.filter(this::isValidOrElseThrow)
		.orElse(getDefaultValue());
    }

    private Function<String[], String[]> elementsToUpperCase() {
	return list -> Arrays.stream(list)
		.map(String::toUpperCase)
		.toArray(String[]::new);
    }

    private boolean isValidOrElseThrow(String[] currencies) {
	for (String currency : currencies) {
	    if (!availableCurrenciesService.getCodeList().contains(currency)) {
		throw new IllegalArgumentException(message + currency);
	    }
	}
	return true;
    }

    private String[] getDefaultValue() {
	return availableCurrenciesService.getCodeList().stream()
		.toArray(String[]::new);
    }

}
