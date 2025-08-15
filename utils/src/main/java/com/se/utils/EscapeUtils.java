package com.se.utils;

import com.google.common.html.HtmlEscapers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for escaping values.
 */
public final class EscapeUtils {

    private static final String URL_SYNTAX_REGEXP = "(?<=\\w)(\\.|://)(?=[\\w%])";

    private EscapeUtils() {
        throw new AssertionError("Constructor should not be called directly");
    }

    /**
     * Escapes html syntax in specified string.
     *
     * @param string string to transform.
     * @return string with escaped html.
     */
    public static String escapeHtml(String string) {
        return HtmlEscapers.htmlEscaper().escape(string);
    }

    /**
     * Escapes urls found in specified strings by adding spaces after '://' and '.'.
     *
     * @param string string to transform.
     * @return string with escaped urls.
     */
    public static String escapeUrls(String string) {
        String result = string;
        Pattern pattern = Pattern.compile(URL_SYNTAX_REGEXP);
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            String urlMark = matcher.group();
            result = matcher.replaceFirst(urlMark + " ");
            matcher.reset(result);
        }
        return result;
    }
}
