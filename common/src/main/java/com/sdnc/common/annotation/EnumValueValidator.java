package com.sdnc.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 *
 * 枚举值注解的校验类
 *
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

	private String[] strValues;
	private int[] intValues;

	@Override
	public void initialize(EnumValue constraintAnnotation) {
		strValues = constraintAnnotation.strVal();
		intValues = constraintAnnotation.intVal();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value instanceof String) {
			for (String str : strValues) {
				if (str.equals(value)) {
					return true;
				}
			}
		} else if (value instanceof Integer val) {
			for (int num : intValues) {
				if (num == val.intValue()) {
					return true;
				}
			}
		}

		return false;
	}

}