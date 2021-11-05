package com.example.post;
// controller에서 클라이언트에게 받은 요청을 넘겨주는 페이지
import com.example.post.UserDao;
import com.example.post.model.GetUserRes;
import com.example.post.model.PostUserReq;
import com.example.post.model.PostUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProvider {
    // Dao로 다시 넘겨주게 됨
    private final com.example.post.UserDao userDao;

    @Autowired
    public UserProvider(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<GetUserRes> getUser(){
        List<GetUserRes> userRes = userDao.userRes();
        return userRes;
    }


    public PostUserRes postUser(PostUserReq postUserReq){
        int id= userDao.addUser(postUserReq);
        PostUserRes postUserRes = new PostUserRes(id);
        return postUserRes;
    }
}
