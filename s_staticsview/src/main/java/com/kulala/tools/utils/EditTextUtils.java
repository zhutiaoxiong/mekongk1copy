package com.kulala.tools.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EditTextUtils {
    /**输入框只能输入中文，英文数字*/
    public static String stringFilter(String str) throws
            PatternSyntaxException {
// 只允许字母、数字和汉字
        String regEx =
                "[^a-zA-Z0-9\u4E00-\u9FA5]";//正则表达式
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return
                m.replaceAll("").trim();
    }

    /**
     * 所有类型: ^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$
     * 车牌正则,只判断常用车牌
     */
    public static boolean plateNumRuleMatch(String plateNum){
        String pattern = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
        return plateNum.matches(pattern);
    }
//    /**
//     * 匹配头字中文，第二个字字母
//     */
//    public static boolean matchRule(String matchStr) {
//        String regexChinese = "[\\u4e00-\\u9fa5]";
//        String xxx = matchStr.substring(0, 1);
//        String xxxx = matchStr.substring(1, 2);
//        boolean matchResult = xxx.matches(regexChinese);
//        boolean matchResult1 = xxxx.matches("[A-Z]");
//        if (matchResult && matchResult1) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//    /**
//     * 匹配后6位字母数字组合
//     */
//
//    public static boolean matchFinalStr(String matchstr){
//        if(matchstr.equals("")||matchstr.length()<=2)return false;
//        String xxx=matchstr.substring(2,matchstr.length());
//        boolean result=xxx.matches("^[a-np-zA-NP-Z0-9]+$");
//        Log.e("匹配结果", xxx +result );
//        return result;
//    }
}
