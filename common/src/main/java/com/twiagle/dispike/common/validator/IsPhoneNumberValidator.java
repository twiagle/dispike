package com.twiagle.dispike.common.validator;

import com.twiagle.dispike.common.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsPhoneNumberValidator implements ConstraintValidator<IsPhoneNumber, String> {

    private boolean required;

    @Override
    public void initialize(IsPhoneNumber constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String var1, ConstraintValidatorContext var2) {
        if (required) {
            return ValidatorUtil.isPhoneNumber(var1);
        } else {
            if (StringUtils.isEmpty(var1)) {
                return true;
            } else {
                return ValidatorUtil.isPhoneNumber(var1);
            }
        }
    }
}
