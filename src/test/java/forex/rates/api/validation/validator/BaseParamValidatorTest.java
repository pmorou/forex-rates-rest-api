package forex.rates.api.validation.validator;

import forex.rates.api.service.AvailableCurrenciesService;
import forex.rates.api.validation.annotation.Base;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.junit.Assert.*;

public class BaseParamValidatorTest {

    private static final Annotation INVALID_ANNOTATION = () -> InvalidAnnotation.class;
    private static final Annotation VALID_ANNOTATION = () -> Base.class;
    private static final Class<?> INVALID_PARAMETER_TYPE = Integer.class;
    private static final Class<?> VALID_PARAMETER_TYPE = String.class;
    private static final List<String> AVAILABLE_CURRENCIES = Arrays.asList("USD", "EUR", "JPY");

    private @interface InvalidAnnotation {}

    private @Mock AvailableCurrenciesService availableCurrenciesService;

    private BaseParamValidator baseParamValidator;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	baseParamValidator = new BaseParamValidator(availableCurrenciesService);
	Mockito.when(availableCurrenciesService.getList()).thenReturn(AVAILABLE_CURRENCIES);
    }

    @Test
    public void supports_invalidType() throws Exception {
	boolean result = baseParamValidator.supports(INVALID_PARAMETER_TYPE);

	assertFalse(result);
    }

    @Test
    public void supports_invalid_noAnnotations() throws Exception {
	boolean result = baseParamValidator.supports(VALID_PARAMETER_TYPE);

	assertFalse(result);
    }

    @Test
    public void supports_invalid_emptyAnnotations() throws Exception {
	boolean result = baseParamValidator.supports(VALID_PARAMETER_TYPE, new Annotation[]{});

	assertFalse(result);
    }

    @Test
    public void supports_invalid_invalidAnnotation() throws Exception {
	boolean result = baseParamValidator.supports(VALID_PARAMETER_TYPE, INVALID_ANNOTATION);

	assertFalse(result);
    }

    @Test
    public void supports_valid() throws Exception {
	boolean result = baseParamValidator.supports(VALID_PARAMETER_TYPE, INVALID_ANNOTATION, VALID_ANNOTATION);

	assertTrue(result);
    }

    @Test
    public void validate_valid_null() throws Exception {
	String given = null;

	String result = baseParamValidator.validate(ofNullable(given));

	assertNotNull(result);
    }

    @Test
    public void validate_valid_eurUpperCase() throws Exception {
	String given = "EUR";

	String result = baseParamValidator.validate(of(given));

	assertNotNull(result);
    }

    @Test
    public void validate_valid_eurLowerCase() throws Exception {
	String given = "eur";

	String result = baseParamValidator.validate(of(given));

	assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_invalid_empty() throws Exception {
	String given = "";

	baseParamValidator.validate(of(given));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_invalid_unknown() throws Exception {
	String given = "INVALID";

	baseParamValidator.validate(of(given));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_invalid_unknown_lowerCase() throws Exception {
	String given = "invalid";

	baseParamValidator.validate(of(given));
    }

}