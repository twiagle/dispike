package com.twiagle.dispike.common.util;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final Pattern phoneNumberPattern = Pattern.compile("1\\d{10}");

    public static boolean isPhoneNumber(String src) {
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher m = phoneNumberPattern.matcher(src);
        return m.matches();
    }
}
