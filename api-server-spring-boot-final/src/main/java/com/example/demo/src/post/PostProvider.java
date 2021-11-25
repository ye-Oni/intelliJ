package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service    // [Business Layer에서 Service를 명시하기 위해서 사용] 비즈니스 로직이나 respository layer 호출하는 함수에 사용된다.
// [Business Layer]는 컨트롤러와 데이터 베이스를 연결
/**
 * Provider란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Read의 비즈니스 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
public class PostProvider {


    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final PostDao postDao;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public PostProvider(PostDao postDao, JwtService jwtService) {
        this.postDao = postDao;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }
    // ******************************************************************************



    // Post들의 정보를 조회
    public List<GetPostRes1> getPosts() throws BaseException {
        try {
            List<GetPostRes1> getPostRes1 = postDao.getPosts();
            return getPostRes1;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 title을 갖는 Post들의 정보 조회
    public List<GetPostRes> getPostsByUserID(String title) throws BaseException {
        try {
            List<GetPostRes> getPostsRes = postDao.getPostByTitle(title);
            return getPostsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 해당 postID를 갖는 Post의 정보 조회
    public GetPostRes getPost(int postID) throws BaseException {
        try {
            GetPostRes getPostRes = postDao.getPost(postID);
            return getPostRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 게시물(postID)에 좋아요를 누른 사용자 정보(아이디, 이름) 조회
    public List<GetLikeRes> getLike(int postID) throws BaseException {
        try {
            List<GetLikeRes> getLikeRes = postDao.getLike(postID);
            return getLikeRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 내가 좋아요 누른 게시물 목록 조회
    public List<GetUserLikeRes> getUserLike(int userID) throws BaseException {
        try {
            List<GetUserLikeRes> getUserLikeRes = postDao.getUserLike(userID);
            return getUserLikeRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 특정 user가 작성한 게시물 조회
    public List<GetUserPostRes> getUserPost(int userID) throws BaseException {
        try{
            List<GetUserPostRes> getUserPostRes = postDao.getUserPost(userID);
            return getUserPostRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 작성한 댓글 조회
    public List<GetCommentRes> getComment(int postID) throws BaseException {
        try {
            List<GetCommentRes> getCommentRes = postDao.getComment(postID);
            return getCommentRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
