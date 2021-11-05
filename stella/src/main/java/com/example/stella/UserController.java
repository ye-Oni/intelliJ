package com.example.stella;

// Client의 요청을 받는 페이지


import com.example.stella.model.GetUserRes;
import com.example.stella.model.PostUserReq;
import com.example.stella.model.PostUserRes;
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

    // *** USER ***
    // 해당하는 API가 무엇인지 요청을 받음 -> provider에게 요청을 넘겨줌
//    @GetMapping("/User")
//    public List<GetUserRes> getUser(){
//        List<GetUserRes> userRes = userProvider.getUser();
//        return userRes;
//    }
//
//    @ResponseBody
//    @PostMapping("/User")
//    public PostUserRes postUser(@RequestBody PostUserReq postUserReq){
//        PostUserRes postUserRes = userProvider.postUser(postUserReq);
//        return postUserRes;
//    }

    // *** POST ***
//    @GetMapping("/Post")
//    public List<GetUserRes> getUser(){
//        List<GetUserRes> userRes = userProvider.getUser();
//        return userRes;
//    }
//
//    @ResponseBody
//    @PostMapping("/Post")
//    public PostUserRes postUser(@RequestBody PostUserReq postUserReq){
//        PostUserRes postUserRes = userProvider.postUser(postUserReq);
//        return postUserRes;
//    }

    // *** Chat ***
    @GetMapping("/Chat")
    public List<GetUserRes> getUser(){
        List<GetUserRes> userRes = userProvider.getUser();
        return userRes;
    }

    @ResponseBody
    @PostMapping("/Chat")
    public PostUserRes postUser(@RequestBody PostUserReq postUserReq){
        PostUserRes postUserRes = userProvider.postUser(postUserReq);
        return postUserRes;
    }

}
