package forex.rates.api.validation.validator;

import forex.rates.api.exception.IllegalParameterException;
import forex.rates.api.service.DateTimeProviderService;
import forex.rates.api.validation.annotation.ValidDate;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class DateParamValidatorTest {

    private static final LocalDate TODAY = LocalDate.of(2001, 1, 1);
    private static final Annotation INVALID_ANNOTATION = () -> InvalidAnnotation.class;
    private static final Annotation VALID_ANNOTATION = () -> ValidDate.class;
    private static final Class<?> INVALID_PARAMETER_TYPE = Integer.class;
    private static final Class<?> VALID_PARAMETER_TYPE = String.class;

    private @interface InvalidAnnotation {}

    private @Mock DateTimeProviderService dateTimeProviderService;

    private DateParamValidator dateParamValidator;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	dateParamValidator = new DateParamValidator(dateTimeProviderService);
	Mockito.when(dateTimeProviderService.getTodaysDateAsString()).thenReturn(TODAY.toString());
	Mockito.when(dateTimeProviderService.getTodaysDate()).thenReturn(TODAY);
    }

    @Test
    @Parameters
    public void shouldSupport(Class<?> givenParameterType, Annotation[] givenAnnotations) throws Exception {
	// When
	boolean result = dateParamValidator.supports(givenParameterType, givenAnnotations);

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
	boolean result = dateParamValidator.supports(givenParameterType, givenAnnotations);

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
	String result = dateParamValidator.validate(givenOptional);

	// Then
	assertThat(result).isEqualTo(expected);
    }

    public Object[] parametersForShouldBeValidAndReturnUpperCase() {
	return new Object[]{
		new Object[]{TODAY.toString(), TODAY.toString()},
		new Object[]{getDateInThePastAsString(DayOfWeek.MONDAY), getDateInThePastAsString(DayOfWeek.MONDAY)},
		new Object[]{getDateInThePastAsString(DayOfWeek.TUESDAY), getDateInThePastAsString(DayOfWeek.TUESDAY)},
		new Object[]{getDateInThePastAsString(DayOfWeek.WEDNESDAY), getDateInThePastAsString(DayOfWeek.WEDNESDAY)},
		new Object[]{getDateInThePastAsString(DayOfWeek.THURSDAY), getDateInThePastAsString(DayOfWeek.THURSDAY)},
		new Object[]{getDateInThePastAsString(DayOfWeek.FRIDAY), getDateInThePastAsString(DayOfWeek.FRIDAY)}
	};
    }

    @Test
    public void shouldBeNotValidAndReturnDefaultValue() {
	// Given
	Optional<String> givenNull = Optional.ofNullable(null);

	// When
	String result = dateParamValidator.validate(givenNull);

	// Then
	assertThat(result).isEqualTo(TODAY.toString());
    }

    @Test(expected = IllegalParameterException.class)
    @Parameters
    public void shouldNotBeValidAndThrowException(String given) throws Exception {
	// Given
	Optional<String> givenOptional = of(given);

	// When
	String result = dateParamValidator.validate(givenOptional);
    }

    public Object[] parametersForShouldNotBeValidAndThrowException() {
	return new Object[]{
		new Object[]{""},
		new Object[]{"01-01-2001"},
		new Object[]{TODAY.plusDays(1).toString()},
		new Object[]{getDateInThePastAsString(DayOfWeek.SATURDAY)},
		new Object[]{getDateInThePastAsString(DayOfWeek.SUNDAY)}
	};
    }

    private static String getDateInThePastAsString(DayOfWeek day) {
	return TODAY.minusWeeks(1).plusDays(day.getValue() - 1).toString();
    }

}