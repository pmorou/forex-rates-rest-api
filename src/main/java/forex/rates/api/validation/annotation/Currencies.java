package forex.rates.api.validation.annotation;

import forex.rates.api.validation.validator.CurrenciesParamValidator;

import java.lang.annotation.*;

/**
 * Declares that a parameter should be validated by {@link CurrenciesParamValidator}.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Currencies {

}
