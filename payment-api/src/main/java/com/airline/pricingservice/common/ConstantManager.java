package com.airline.pricingservice.common;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ConstantManager {

    public static final String SERBIAN_LOCALE_SHORTCUT = "sr";
    public static final String ENGLISH_LOCALE_SHORTCUT = Locale.ENGLISH.toString();
    public static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    public static final String BAD_LANGUAGE_IN_HEADER_EXCEPTION_MESSAGE = "Specified language in header is not supported.";
    public static final String BAD_LANGUAGE_IN_HEADER_EXCEPTION_CODE = "400";
    public static final List<String> ACCEPTABLE_LANGUAGE_HEADER = Arrays.asList(SERBIAN_LOCALE_SHORTCUT, ENGLISH_LOCALE_SHORTCUT);


}
