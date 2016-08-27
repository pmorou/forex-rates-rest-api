package forex.rates.api.validation.validator;

import forex.rates.api.service.AvailableCurrenciesService;
import forex.rates.api.validation.annotation.Currencies;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class CurrenciesParamValidatorTest {

    private static final Annotation INVALID_ANNOTATION = () -> InvalidAnnotation.class;
    private static final Annotation VALID_ANNOTATION = () -> Currencies.class;
    private static final Class<?> INVALID_PARAMETER_TYPE = Integer.class;
    private static final Class<?> VALID_PARAMETER_TYPE = String[].class;
    private static final List<String> AVAILABLE_CURRENCIES = Arrays.asList("USD", "EUR", "JPY");

    private @interface InvalidAnnotation {}

    private @Mock AvailableCurrenciesService availableCurrenciesService;

    private CurrenciesParamValidator currenciesParamValidator;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	currenciesParamValidator = new CurrenciesParamValidator(availableCurrenciesService);
	Mockito.when(availableCurrenciesService.getList()).thenReturn(AVAILABLE_CURRENCIES);
    }

    @Test
    @Parameters
    public void shouldSupport(Class<?> givenParameterType, Annotation[] givenAnnotations) throws Exception {
	// When
	boolean result = currenciesParamValidator.supports(givenParameterType, givenAnnotations);

	// Then
	assertTrue(result);
    }

    public Object[] parametersForShouldSupport() {
	return new Object[]{
		new Object[]{VALID_PARAMETER_TYPE, new Annotation[]{INVALID_ANNOTATION, VALID_ANNOTATION}},
	};
    }

    @Test
    @Parameters
    public void shouldNotSupport(Class<?> givenParameterType, Annotation[] givenAnnotations) throws Exception {
	// When
	boolean result = currenciesParamValidator.supports(givenParameterType, givenAnnotations);

	// Then
	assertFalse(result);
    }

    public Object[] parametersForShouldNotSupport() {
	return new Object[]{
		new Object[]{INVALID_PARAMETER_TYPE, null},
		new Object[]{INVALID_PARAMETER_TYPE, new Annotation[]{}},
		new Object[]{INVALID_PARAMETER_TYPE, new Annotation[]{INVALID_ANNOTATION}},
		new Object[]{VALID_PARAMETER_TYPE, new Annotation[]{}},
		new Object[]{VALID_PARAMETER_TYPE, new Annotation[]{INVALID_ANNOTATION}}
	};
    }

    @Test
    @Parameters
    public void shouldBeValidAndNotNull(String[] given) throws Exception {
	// Given
	Optional<String[]> givenOptional = ofNullable(given);

	// When
	String[] result = currenciesParamValidator.validate(givenOptional);

	// Then
	assertNotNull(result);
    }

    public Object[] parametersForShouldBeValidAndNotNull() {
	return new Object[]{
		new Object[]{null},
		new Object[]{toStringArray(AVAILABLE_CURRENCIES)},
		new Object[]{toStringArray(toLowerCase(AVAILABLE_CURRENCIES))},
	};
    }

    private String[] toStringArray(List<String> list) {
	return list.stream()
		.toArray(String[]::new);
    }

    private List<String> toLowerCase(List<String> list) {
	return list.stream()
		.map(String::toLowerCase)
		.collect(Collectors.toList());
    }

    @Test(expected = IllegalArgumentException.class)
    @Parameters
    public void shouldNotBeValidAndThrowException(String[] given) throws Exception {
	// Given
	Optional<String[]> givenOptional = of(given);

	// When
	currenciesParamValidator.validate(givenOptional);
    }

    public Object[] parametersForShouldNotBeValidAndThrowException() {
	return new Object[]{
		new Object[]{new String[]{""}},
		new Object[]{new String[]{"UNKNOWN"}},
		new Object[]{new String[]{"unknown"}}
	};
    }

}