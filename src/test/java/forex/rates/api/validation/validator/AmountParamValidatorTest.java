package forex.rates.api.validation.validator;

import forex.rates.api.dataset.DataSetContext;
import forex.rates.api.exception.IllegalParameterException;
import forex.rates.api.service.AvailableCurrenciesService;
import forex.rates.api.validation.annotation.ValidAmount;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class AmountParamValidatorTest {

    private static final Annotation INVALID_ANNOTATION = () -> InvalidAnnotation.class;
    private static final Annotation VALID_ANNOTATION = () -> ValidAmount.class;
    private static final Class<?> INVALID_PARAMETER_TYPE = Integer.class;
    private static final Class<?> VALID_PARAMETER_TYPE = String.class;

    private @interface InvalidAnnotation {}

    private @Mock AvailableCurrenciesService availableCurrenciesService;
    private @Mock DataSetContext dataSetContext;

    private AmountParamValidator amountParamValidator;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	amountParamValidator = new AmountParamValidator();
    }

    @Test
    @Parameters
    public void shouldSupport(Class<?> givenParameterType, Annotation[] givenAnnotations) throws Exception {
	// When
	boolean result = amountParamValidator.supports(givenParameterType, givenAnnotations);

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
	boolean result = amountParamValidator.supports(givenParameterType, givenAnnotations);

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
    public void shouldBeValidAndReturnSameNumber(String given, String expected) throws Exception {
	// Given
	Optional<String> givenOptional = ofNullable(given);

	// When
	String result = amountParamValidator.validate(givenOptional);

	// Then
	assertThat(result).isEqualTo(expected);
    }

    public Object[] parametersForShouldBeValidAndReturnSameNumber() {
	return new Object[]{
		new Object[]{0.1, 0.1},
		new Object[]{1, 1}
	};
    }

    @Test
    public void shouldBeNotValidAndReturnDefaultValue() {
	// Given
	Optional<String> givenNull = Optional.ofNullable(null);

	// When
	String result = amountParamValidator.validate(givenNull);

	// Then
	assertThat(result).isEqualTo("1");
    }

    @Test(expected = IllegalParameterException.class)
    @Parameters
    public void shouldNotBeValidAndThrowException(String given) throws Exception {
	// Given
	Optional<String> givenOptional = of(given);

	// When
	String result = amountParamValidator.validate(givenOptional);
    }

    public Object[] parametersForShouldNotBeValidAndThrowException() {
	return new Object[]{
		new Object[]{""},
		new Object[]{"0"},
		new Object[]{"0,1"},
		new Object[]{"-1"},
		new Object[]{"Text"},
		new Object[]{"O"}
	};
    }

}