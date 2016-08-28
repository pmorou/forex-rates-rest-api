package forex.rates.api.validation.annotation;

import forex.rates.api.validation.validator.DateParamValidator;

import java.lang.annotation.*;

/**
 * Declares that a parameter should be validated by {@link DateParamValidator}.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Date {

}
