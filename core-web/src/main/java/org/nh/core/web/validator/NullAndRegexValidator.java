package org.nh.core.web.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullAndRegexValidator implements ConstraintValidator<NullAndRegexValid, Object> {

//	private NullAndRegexValid nullAndRegexValid;
	private String paramName;
	private boolean notNull;
	private String regex;
	
	@Override
	public void initialize(NullAndRegexValid constraintAnnotation) {
//		this.nullAndRegexValid = constraintAnnotation;
		this.paramName = constraintAnnotation.paramName();
		this.notNull = constraintAnnotation.notNull();
		this.regex = constraintAnnotation.regex();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		if(this.notNull) {
			if(value==null) {
				String message = this.paramName+"不能为空";
				context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
				return false;
			}else if(value.getClass()==String.class) {
				String valueStr = value.toString();
				if(StringUtils.isBlank(valueStr)) {
					String message = this.paramName+"不能为空";
					context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
					return false;
				}
			}
		}
		
		if(value==null) {
			return true;
		}else if(value.getClass()==String.class) {
			String valueStr = value.toString();
			if(StringUtils.isBlank(valueStr)) {
				return true;
			}
		}
		
		String valueStr = value.toString();
		if(!(valueStr.matches(this.regex))) {
			String message = this.paramName+"的值不符合要求";
			context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
			return false;
		}
		
		return true;
	}

}
