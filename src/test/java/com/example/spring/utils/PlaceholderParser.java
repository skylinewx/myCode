package com.example.spring.utils;

import java.util.Map;

/**
 * 解析器
 */
public class PlaceholderParser {

    /**
     * 值map
     */
    private final Map<String, String> valueMap;
    /**
     * 前缀
     */
    private final String prefix;
    /**
     * 后缀
     */
    private final String suffix;
    /**
     * 分隔符
     */
    private final String delimiter;

    public PlaceholderParser(Map<String, String> valueMap, String prefix, String suffix, String delimiter) {
        this.valueMap = valueMap;
        this.prefix = prefix;
        this.suffix = suffix;
        this.delimiter = delimiter;
    }

    /**
     * demo:${${poo}lType:io}}pig
     *
     * @param str
     * @return
     */
    public String doParse(String str) {
        int suffixIndex = str.indexOf(suffix);
        if (suffixIndex == -1) {
            return str;
        }
        int lastIndex = -1;
        while (true) {
            int prefixIndex = str.indexOf(prefix, lastIndex);
            if (prefixIndex == -1) {
                break;
            }
            lastIndex = prefixIndex + prefix.length();
        }
        if (lastIndex == -1) {
            return str;
        }
        String placeholderStr = str.substring(lastIndex, suffixIndex);
        int delimiterIndex = placeholderStr.indexOf(delimiter);
        String value;
        if (delimiterIndex != -1) {
            value = valueMap.get(placeholderStr.substring(0, delimiterIndex));
            if (value == null) {
                value = placeholderStr.substring(delimiterIndex + delimiter.length());
            }
        } else {
            value = valueMap.get(placeholderStr);
        }
        String s = str.substring(0, lastIndex - prefix.length())
                + value
                + str.substring(suffixIndex + 1);
        return doParse(s);
    }
}
