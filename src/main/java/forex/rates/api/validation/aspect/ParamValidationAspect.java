package forex.rates.api.validation.aspect;

import forex.rates.api.validation.validator.ParamValidator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@Slf4j
public class ParamValidationAspect {

    private final List<ParamValidator> paramValidators;

    public ParamValidationAspect(List<ParamValidator> paramValidators) {
	this.paramValidators = paramValidators;
    }

    private class Parameter {
	private Class<?> type;
	private Optional<?> value;
	private Annotation[] annotations;

	private Parameter(Class<?> type, Annotation[] annotations, Object value) {
	    this.type = type;
	    this.annotations = annotations;
	    this.value = Optional.ofNullable(value);
	}

	private Class<?> getType() {
	    return type;
	}

	private Annotation[] getAnnotations() {
	    return annotations;
	}

	private Optional<?> getValue() {
	    return value;
	}
    }

    @Pointcut("execution(* forex.rates.api.controller.*Controller.*(..))")
    public void anyControllerMethod() {
    }

    @Around("anyControllerMethod()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
	Object[] newArgsArray = collectParameters(proceedingJoinPoint).stream()
		.map(p -> getValidValueOrElseNull(
			p.getType(),
			p.getAnnotations(),
			p.getValue()))
		.collect(Collectors.toList())
		.toArray();

	log.info("Passing request to handler: {}, parameters after validation: {}",
		proceedingJoinPoint.getSignature().getName(), Arrays.deepToString(newArgsArray));

	return proceedingJoinPoint.proceed(newArgsArray);
    }

    private List<Parameter> collectParameters(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
	MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
	Class[] parameterTypes = methodSignature.getParameterTypes();
	Object[] parameterValues = proceedingJoinPoint.getArgs();
	Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();

	log.info("Passing request to handler: {}, parameters before validation: {}",
		proceedingJoinPoint.getSignature().getName(), Arrays.deepToString(parameterValues));

	return IntStream.range(0, parameterValues.length)
		.mapToObj(i -> new Parameter(
			parameterTypes[i],
			parameterAnnotations[i],
			parameterValues[i]))
		.collect(Collectors.toList());
    }

    private Object getValidValueOrElseNull(Class<?> parameterType, Annotation[] parameterAnnotations, Optional<?> optionalParameterValue) {
	return paramValidators.stream()
		.filter(pV -> pV.supports(parameterType, parameterAnnotations))
		.findFirst()
		.map(pV -> pV.validate(optionalParameterValue))
		.orElse(returnValue(optionalParameterValue));
    }

    private Object returnValue(Optional<?> optionalParameterValue) {
	return optionalParameterValue.orElse(null);
    }

}
