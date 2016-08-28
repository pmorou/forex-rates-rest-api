package forex.rates.api.validation.annotation;

import forex.rates.api.validation.validator.BaseParamValidator;

import java.lang.annotation.*;

/**
 * Declares that a parameter should be validated by {@link BaseParamValidator}.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Base {

}
