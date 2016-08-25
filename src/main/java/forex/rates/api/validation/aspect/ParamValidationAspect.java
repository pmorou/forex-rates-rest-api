package forex.rates.api.validation.aspect;

import forex.rates.api.validation.validator.ParamValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
public class ParamValidationAspect {

    private List<ParamValidator> paramValidators;

    public ParamValidationAspect(List<ParamValidator> paramValidators) {
	this.paramValidators = paramValidators;
    }

    private class Parameter {
	private Class<?> type;
	private Optional<?> value;
	private Annotation[] annotations;

	Parameter(Class<?> type, Annotation[] annotations, Object value) {
	    this.type = type;
	    this.annotations = annotations;
	    this.value = Optional.ofNullable(value);
	}

	Class<?> getType() {
	    return type;
	}

	Annotation[] getAnnotations() {
	    return annotations;
	}

	Optional<?> getValue() {
	    return value;
	}

    }

    @Pointcut("execution(* forex.rates.api.controller.*Controller.*(..))")
    public void anyControllerMethod() {
    }

    @Around("anyControllerMethod()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
	List<Parameter> parameters = collectParameters(proceedingJoinPoint);
	List<Object> newArgs = new ArrayList<>();

	for (Parameter parameter : parameters) {
	    Optional<?> optional = parameter.getValue();
	    Object value = optional.orElse(null);

	    for (ParamValidator validator : paramValidators) {
		Class<?> parameterType = parameter.getType();
		Annotation[] annotations = parameter.getAnnotations();
		if (validator.supports(parameterType, annotations)) {
		    value = validator.validate(optional);
		    break;
		}
	    }
	    newArgs.add(value);
	}
	return proceedingJoinPoint.proceed(newArgs.toArray());
    }

    private List<Parameter> collectParameters(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
	MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
	Class[] parameterTypes = methodSignature.getParameterTypes();
	Object[] parameterValues = proceedingJoinPoint.getArgs();
	Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();

	List<Parameter> parameters = new ArrayList<>();
	for (int i = 0; i < parameterValues.length; i++) {
	    parameters.add(
		    new Parameter(
			    parameterTypes[i],
			    parameterAnnotations[i],
			    parameterValues[i]
		    )
	    );
	}
	return Collections.unmodifiableList(parameters);
    }

}
