package com.example.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PostUserReq {
    private int userID;
    private String title;
    private String imgUrl;
    private String updateAt;
}
