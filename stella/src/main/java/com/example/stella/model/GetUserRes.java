package com.example.stella.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetUserRes {
    // *** Chat ***
    private int chatIdx;
    private int roomIdx;
    private String message;

    // *** Post ***
//    private int postID;
//    private int userID;
//    private String title;
//    private String imgUrl;

    // *** USER ***
//    private int userID;
//    private String name;
//    private String phoneNum;
//    private String email;
}
