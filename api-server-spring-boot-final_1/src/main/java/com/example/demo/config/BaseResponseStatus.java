package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_EMPTY_PWD(false, 2018, "비밀번호를 입력해주세요."),
    POST_USERS_INVALID_PASSWORD(false, 2019,"비밀번호 형식을 확인해주세요."),
    POST_USERS_EMPTY_NAME(false, 2020, "이름/닉네임을 입력해주세요."),
    POST_USERS_EXISTS_NAME(false,2021,"중복된 이름/닉네임 입니다."),

    // [POST] /posts
    POST_POSTS_EMPTY_TITLE(false,2022,"게시물 제목을 입력해주세요."),
    POST_POSTS_INVALID_IMGURL(false,2023,"이미지 형식이 올바르지 않습니다."),
    POST_POSTS_EMPTY_USERID(false,2024,"로그인해주세요. 게시물 작성 권한이 없습니다."),
    POST_POSTS_EMPTY_IMGURL(false,2025,"이미지를 첨부해주세요."),
    POST_POSTS_EMPTY_COMMENT(false,2026,"댓글을 작성해주세요."),



    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    DUPLICATED_NAME(false, 3015, "중복된 이름/닉네임입니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_TITLE(false,4015,"게시물 제목 수정 실패"),
    MODIFY_FAIL_POSTID(false,4016,"좋아요 누르기 수정 실패"),
    MODIFY_FAIL_COMMENT(false,4017, "댓글 수정 실패"),
    MODIFY_FAIL_FOLLOW(false,4018, "팔로우 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}