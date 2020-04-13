package org.nh.core.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy= {NullAndRegexValidator.class})
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NullAndRegexValid {
	String message() default "{paramName}参数传递错误";
	String paramName() default "未知";
	String regex() default ".*";
	boolean notNull() default true;
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
