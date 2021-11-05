package com.example.post;

// Client의 요청을 받는 페이지

import com.example.post.model.GetUserRes;
import com.example.post.model.PostUserReq;
import com.example.post.model.PostUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserProvider userProvider;

    @Autowired
    public UserController(UserProvider userProvider) {
        this.userProvider = userProvider;
    }
    
    // 해당하는 API가 무엇인지 요청을 받음 -> provider에게 요청을 넘겨줌
    @GetMapping("/Post")
    public List<GetUserRes> getUser(){
        List<GetUserRes> userRes = userProvider.getUser();
        return userRes;
    }

    @ResponseBody
    @PostMapping("/Post")
    public PostUserRes postUser(@RequestBody PostUserReq postUserReq){
        PostUserRes postUserRes = userProvider.postUser(postUserReq);
        return postUserRes;
    }
}
