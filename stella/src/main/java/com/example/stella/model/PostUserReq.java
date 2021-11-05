package com.example.stella.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PostUserReq {
    // *** Chat ***
    private int roomIdx;
    private String message;

    // *** Post ***
//    private int userID;
//    private String title;
//    private String imgUrl;

    // *** User ***
//    private String name;
//    private String phoneNum;
//    private String email;
}
