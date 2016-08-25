package forex.rates.api.validation.validator;

import forex.rates.api.service.AvailableCurrenciesService;
import forex.rates.api.validation.annotation.Currencies;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.junit.Assert.*;

public class CurrenciesParamValidatorTest {

    private static final Annotation INVALID_ANNOTATION = () -> InvalidAnnotation.class;
    private static final Annotation VALID_ANNOTATION = () -> Currencies.class;
    private static final Class<?> INVALID_PARAMETER_TYPE = Integer.class;
    private static final Class<?> VALID_PARAMETER_TYPE = ArrayList.class; // implements List interface
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
    public void supports_invalid_invalidType() throws Exception {
	boolean result = currenciesParamValidator.supports(INVALID_PARAMETER_TYPE);

	assertFalse(result);
    }

    @Test
    public void supports_invalid_noAnnotations() throws Exception {
	boolean result = currenciesParamValidator.supports(VALID_PARAMETER_TYPE);

	assertFalse(result);
    }

    @Test
    public void supports_invalid_validType_emptyAnnotations() throws Exception {
	boolean result = currenciesParamValidator.supports(VALID_PARAMETER_TYPE, new Annotation[]{});

	assertFalse(result);
    }

    @Test
    public void supports_invalid_validType_invalidAnnotation() throws Exception {
	boolean result = currenciesParamValidator.supports(VALID_PARAMETER_TYPE, INVALID_ANNOTATION);

	assertFalse(result);
    }

    @Test
    public void supports_valid_isList_validAnnotation() throws Exception {
	boolean result = currenciesParamValidator.supports(VALID_PARAMETER_TYPE, VALID_ANNOTATION);

	assertTrue(result);
    }

    @Test
    public void supports_valid_isNotList_validAnnotation() throws Exception {
	boolean result = currenciesParamValidator.supports(INVALID_PARAMETER_TYPE, VALID_ANNOTATION);

	assertFalse(result);
    }

    @Test
    public void supports_valid_isList_invalidAnnotation() throws Exception {
	boolean result = currenciesParamValidator.supports(VALID_PARAMETER_TYPE, INVALID_ANNOTATION);

	assertFalse(result);
    }

    @Test
    public void validate_valid_null() throws Exception {
	List<String> given = null;

	List<String> result = currenciesParamValidator.validate(ofNullable(given));

	assertNotNull(result);
    }

    @Test
    public void validate_valid_allCurrencies() throws Exception {
	List<String> given = AVAILABLE_CURRENCIES;

	List<String> result = currenciesParamValidator.validate(of(given));

	assertNotNull(result);
    }

    @Test
    public void validate_valid_allCurrenciesLowerCase() throws Exception {
	List<String> given = AVAILABLE_CURRENCIES.stream().map(String::toLowerCase).collect(Collectors.toList());

	List<String> result = currenciesParamValidator.validate(of(given));

	assertNotNull(result);
    }

    @Test
    public void validate_valid_usdUpperCase() throws Exception {
	List<String> given = singletonList("USD");

	List<String> result = currenciesParamValidator.validate(of(given));

	assertNotNull(result);
    }

    @Test
    public void validate_valid_usdLowerCase() throws Exception {
	List<String> given = singletonList("usd");

	List<String> result = currenciesParamValidator.validate(of(given));

	assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_invalidCurrency() throws Exception {
	List<String> given = singletonList("INVALID");

	currenciesParamValidator.validate(of(given));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_invalidCurrencyLowerCase() throws Exception {
	List<String> given = singletonList("invalid");

	currenciesParamValidator.validate(of(given));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_invalid_empty() throws Exception {
	List<String> given = singletonList("");

	currenciesParamValidator.validate(of(given));
    }

}