package com.luter.heimdall.starter.boot.validator.utils;


import com.luter.heimdall.starter.utils.exception.ParamsInValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ValidateHelper {
    public static void isMatch(RegexEnum regex, CharSequence input, String errMsg) {
        final boolean b = input != null && input.length() > 0 && Pattern.matches(regex.regex(), input);
        if (!b) {
            throw new ParamsInValidException(errMsg);
        }
    }

    public static boolean isMatch(RegexEnum regex, CharSequence input, boolean isThrowEx) {
        final boolean b = input != null && input.length() > 0 && Pattern.matches(regex.regex(), input);
        if (!b && isThrowEx) {
            throw new ParamsInValidException(regex.msg());
        }
        return b;
    }

    public static boolean isMatch(RegexEnum regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex.regex(), input);
    }

    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    public static List<String> getMatches(String regex, CharSequence input) {
        if (input == null) {
            return null;
        }
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    public static String[] getSplits(String input, String regex) {
        if (input == null) {
            return null;
        }
        return input.split(regex);
    }

    public static String getReplaceFirst(String input, String regex, String replacement) {
        if (input == null) {
            return null;
        }
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }

    public static String getReplaceAll(String input, String regex, String replacement) {
        if (input == null) {
            return null;
        }
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }

    public static void main(String[] args) {
        System.out.println(isMatch(RegexEnum.CHAR.regex(), "aaa1"));
    }
}
