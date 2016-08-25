package forex.rates.api.validation.validator;

import forex.rates.api.service.DateTimeProviderService;
import forex.rates.api.validation.annotation.Date;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;
import java.time.LocalDate;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.junit.Assert.*;

public class DateParamValidatorTest {

    private static final LocalDate DATE_2000_01_01 = LocalDate.of(2000, 1, 1);
    private static final Annotation INVALID_ANNOTATION = () -> InvalidAnnotation.class;
    private static final Annotation VALID_ANNOTATION = () -> Date.class;
    private static final Class<?> INVALID_PARAMETER_TYPE = Integer.class;
    private static final Class<?> VALID_PARAMETER_TYPE = String.class;

    private @interface InvalidAnnotation {}

    private @Mock DateTimeProviderService dateTimeProviderService;

    private DateParamValidator dateParamValidator;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	dateParamValidator = new DateParamValidator(dateTimeProviderService);
	Mockito.when(dateTimeProviderService.getTodaysDateAsString()).thenReturn(DATE_2000_01_01.toString());
	Mockito.when(dateTimeProviderService.getTodaysDate()).thenReturn(DATE_2000_01_01);
    }

    @Test
    public void supports_invalidType() throws Exception {
	boolean result = dateParamValidator.supports(INVALID_PARAMETER_TYPE);

	assertFalse(result);
    }

    @Test
    public void supports_invalid_noAnnotations() throws Exception {
	boolean result = dateParamValidator.supports(VALID_PARAMETER_TYPE);

	assertFalse(result);
    }

    @Test
    public void supports_invalid_emptyAnnotations() throws Exception {
	boolean result = dateParamValidator.supports(INVALID_PARAMETER_TYPE, new Annotation[]{});

	assertFalse(result);
    }

    @Test
    public void supports_invalid_invalidAnnotation() throws Exception {
	boolean result = dateParamValidator.supports(INVALID_PARAMETER_TYPE, INVALID_ANNOTATION);

	assertFalse(result);
    }

    @Test
    public void supports_invalid_validType_invalidAnnotation() throws Exception {
	boolean result = dateParamValidator.supports(VALID_PARAMETER_TYPE, INVALID_ANNOTATION);

	assertFalse(result);
    }

    @Test
    public void supports_valid() throws Exception {
	boolean result = dateParamValidator.supports(VALID_PARAMETER_TYPE, INVALID_ANNOTATION, VALID_ANNOTATION);

	assertTrue(result);
    }

    @Test
    public void validate_valid_null() throws Exception {
	String given = null;

	String result = dateParamValidator.validate(ofNullable(given));

	assertNotNull(result);
    }

    @Test
    public void validate_valid_yesterday() throws Exception {
	String given = DATE_2000_01_01.minusDays(1).toString();

	String result = dateParamValidator.validate(of(given));

	assertNotNull(result);
    }

    @Test
    public void validate_valid_today() throws Exception {
	String given = DATE_2000_01_01.toString();

	String result = dateParamValidator.validate(of(given));

	assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_invalid_empty() throws Exception {
	String given = "";

	dateParamValidator.validate(of(given));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_invalid_unknownFormat() throws Exception {
	String given = "2000-0101";

	dateParamValidator.validate(of(given));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_invalid_fromFuture_tomorrow() throws Exception {
	String given = DATE_2000_01_01.plusDays(1).toString();

	dateParamValidator.validate(of(given));
    }

}