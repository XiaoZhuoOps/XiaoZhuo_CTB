package com.xiaozhuo.ctbsb.common.utils;

/**
 * @author xiaozhuo
 * @date 2021/5/3 13:22
 */
public class LatexUtil {
    public static String latexHandle(String latex){
        return latex.replaceAll("\\\\", "").
                replaceAll("\\(", "").
                replaceAll("\\)", "").
                replaceAll("\\{", "").
                replaceAll("}", "").
                replaceAll("\\[", "").
                replaceAll("]", "").
                replaceAll("displaystyle", "");
    }
}
