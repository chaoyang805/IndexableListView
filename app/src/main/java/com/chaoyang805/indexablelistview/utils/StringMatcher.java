package com.chaoyang805.indexablelistview.utils;

/**
 * Created by chaoyang805 on 2015/9/26.
 */
public class StringMatcher {
    /**
     * @param value   item的文本
     * @param keyword 索引列表中的字符
     * @return
     */
    public static boolean match(String keyword, String value) {
        if (value == null || keyword == null) {
            return false;
        }
        if (keyword.length() > value.length()) {
            return false;
        }
        /**
         * value的指针
         */
        int i = 0;
        /**
         * keyword 的指针
         */
        int j = 0;
        do {//key = bc value = abcde
            if (keyword.charAt(j) == value.charAt(i)) {
                i++;
                j++;
            } else if (j > 0) {
                break;
            } else {
                i++;
            }
        } while (i < value.length() && j < keyword.length());

        return (j == keyword.length());
    }
}
