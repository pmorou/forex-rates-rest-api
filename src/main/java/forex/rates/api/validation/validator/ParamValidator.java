package forex.rates.api.validation.validator;

import forex.rates.api.exception.IllegalParameterException;

import java.lang.annotation.Annotation;
import java.util.Optional;

public interface ParamValidator<T> {

    /**
     * @param message message seen when validation fails
     */
    void setMessage(String message);

    /**
     * @param parameterType the {@link Class} of an instance this validation
     * is being asked to validate
     * @param annotations array of the {@link Annotation}s of an instance
     * this validation is being asked to validate
     * @return {@code true} if this {@link ParamValidator} implementation
     * can validate instances of the supplied {@code parameterType}
     * annotated with given {@code annotations}
     */
    boolean supports(Class<?> parameterType, Annotation... annotations);

    /**
     * Validate the supplied {@code object} instance, for which the
     * {@link #supports(Class, Annotation...)} method return {@code true}.
     * @param object the object that is to be validated
     * @return validated object
     * @throws IllegalParameterException when validation fails
     */
    T validate(Optional<T> object);

}
