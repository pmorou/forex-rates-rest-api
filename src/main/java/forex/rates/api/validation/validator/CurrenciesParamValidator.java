package forex.rates.api.validation.validator;

import forex.rates.api.service.AvailableCurrenciesService;
import forex.rates.api.validation.annotation.Currencies;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CurrenciesParamValidator implements ParamValidator<List<String>> {

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
	if (List.class.isAssignableFrom(parameterType)) {
	    return Arrays.stream(annotations)
		    .map(a -> a.annotationType())
		    .anyMatch(Currencies.class::equals);
	}
	return false;
    }

    @Override
    public List<String> validate(Optional<List<String>> object) {
	return object.map(elementsToUpperCase())
		.filter(this::isValidOrElseThrow)
		.orElse(getDefaultValue());
    }

    private Function<List<String>, List<String>> elementsToUpperCase() {
	return list -> list.stream()
		.map(String::toUpperCase)
		.collect(Collectors.toList());
    }

    private boolean isValidOrElseThrow(List<String> currencies) {
	for (String currency : currencies) {
	    if (!availableCurrenciesService.getList().contains(currency)) {
		throw new IllegalArgumentException(message + currency);
	    }
	}
	return true;
    }

    private List<String> getDefaultValue() {
	return availableCurrenciesService.getList();
    }

}
