package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    // 이메일 형식 체크
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    // 비밀번호 형식 체크 (특수문자, 대문자, 소문자, 숫자 필수입력 + 공백문자 허용안함 + 최소 8자 이상 최대 20자 이하)
    public static boolean isRegexPassword(String target) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    // 이미지 형식 체크
    public static boolean isRegexImgUrl(String target) {
        String regex = "^(http\\:\\/\\/[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(?:\\/\\S*)?(?:[a-zA-Z0-9_])+\\.(?:jpg|jpeg|gif|png))$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    // 날짜 형식, 전화 번호 형식 등 여러 Regex 인터넷에 검색하면 나옴.
}

