package forex.rates.api.validation.validator;

import forex.rates.api.dataset.DataSetContext;
import forex.rates.api.exception.IllegalParameterException;
import forex.rates.api.service.AvailableCurrenciesService;
import forex.rates.api.validation.annotation.ValidBase;
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

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class BaseParamValidatorTest {

    private static final Annotation INVALID_ANNOTATION = () -> ValidatorUtils.InvalidAnnotation.class;
    private static final Annotation VALID_ANNOTATION = () -> ValidBase.class;
    private static final Class<?> INVALID_PARAMETER_TYPE = ValidatorUtils.InvalidParameterType.class;
    private static final Class<?> VALID_PARAMETER_TYPE = String.class;
    private static final List<String> AVAILABLE_CURRENCIES = Arrays.asList("USD", "EUR", "JPY");

    private @Mock AvailableCurrenciesService availableCurrenciesService;
    private @Mock DataSetContext dataSetContext;

    private BaseParamValidator baseParamValidator;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	baseParamValidator = new BaseParamValidator(dataSetContext, availableCurrenciesService);
	Mockito.when(availableCurrenciesService.getCodeList()).thenReturn(AVAILABLE_CURRENCIES);
    }

    @Test
    @Parameters
    public void shouldSupport(Class<?> givenParameterType, Annotation[] givenAnnotations) throws Exception {
	// When
	boolean result = baseParamValidator.supports(givenParameterType, givenAnnotations);

	// Then
	assertTrue(result);
    }

    public Object[] parametersForShouldSupport() {
	return new Object[]{
		new Object[]{VALID_PARAMETER_TYPE, new Annotation[]{INVALID_ANNOTATION, VALID_ANNOTATION}}
	};
    }

    @Test
    @Parameters
    public void shouldNotSupport(Class<?> givenParameterType, Annotation[] givenAnnotations) throws Exception {
	// When
	boolean result = baseParamValidator.supports(givenParameterType, givenAnnotations);

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
    public void shouldBeValidAndReturnUpperCase(String given, String expected) throws Exception {
	// Given
	Optional<String> givenOptional = ofNullable(given);

	// When
	String result = baseParamValidator.validate(givenOptional);

	// Then
	assertThat(result).isEqualTo(expected);
    }

    public Object[] parametersForShouldBeValidAndReturnUpperCase() {
	return new Object[]{
		new Object[]{"EUR", "EUR"},
		new Object[]{"eur", "EUR"}
	};
    }

    @Test
    public void shouldBeNotValidAndReturnDefaultValue() {
	// Given
	Optional<String> givenNull = Optional.ofNullable(null);
	Mockito.when(dataSetContext.getBaseCurrency()).thenReturn("USD");

	// When
	String result = baseParamValidator.validate(givenNull);

	// Then
	assertThat(result).isEqualTo("USD");
    }

    @Test(expected = IllegalParameterException.class)
    @Parameters
    public void shouldNotBeValidAndThrowException(String given) throws Exception {
	// Given
	Optional<String> givenOptional = of(given);

	// When
	String result = baseParamValidator.validate(givenOptional);
    }

    public Object[] parametersForShouldNotBeValidAndThrowException() {
	return new Object[]{
		new Object[]{""},
		new Object[]{"UNKNOWN"},
		new Object[]{"unknown"}
	};
    }

}