package forex.rates.api.validation.annotation;

import forex.rates.api.validation.validator.AmountParamValidator;

import java.lang.annotation.*;

/**
 * Declares that a parameter should be validated by {@link AmountParamValidator}.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Amount {

}
