package com.example.demo.src.post;


import com.example.demo.src.post.model.*;
import org.graalvm.compiler.nodes.virtual.CommitAllocationNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.Patch;
import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController // Rest API 또는 WebAPI를 개발하기 위한 어노테이션. @Controller + @ResponseBody 를 합친것.
// @Controller      [Presentation Layer에서 Contoller를 명시하기 위해 사용]
//  [Presentation Layer?] 클라이언트와 최초로 만나는 곳으로 데이터 입출력이 발생하는 곳
//  Web MVC 코드에 사용되는 어노테이션. @RequestMapping 어노테이션을 해당 어노테이션 밑에서만 사용할 수 있다.
// @ResponseBody    모든 method의 return object를 적절한 형태로 변환 후, HTTP Response Body에 담아 반환.
@RequestMapping("/posts")
// method가 어떤 HTTP 요청을 처리할 것인가를 작성한다.
// 요청에 대해 어떤 Controller, 어떤 메소드가 처리할지를 맵핑하기 위한 어노테이션
// URL(/app/users)을 컨트롤러의 메서드와 매핑할 때 사용
/**
 * Controller란?
 * 사용자의 Request를 전달받아 요청의 처리를 담당하는 Service, Prodiver 를 호출
 */
public class PostController {
    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired  // 객체 생성을 스프링에서 자동으로 생성해주는 역할. 주입하려 하는 객체의 타입이 일치하는 객체를 자동으로 주입한다.
    // IoC(Inversion of Control, 제어의 역전) / DI(Dependency Injection, 의존관계 주입)에 대한 공부하시면, 더 깊이 있게 Spring에 대한 공부를 하실 수 있을 겁니다!(일단은 모르고 넘어가셔도 무방합니다.)
    // IoC 간단설명,  메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것을 의미
    // DI 간단설명, 객체를 직접 생성하는 게 아니라 외부에서 생성한 후 주입 시켜주는 방식
    private final PostProvider postProvider;
    @Autowired
    private final PostService postService;
    @Autowired
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    public PostController(PostProvider userProvider, PostService userService, JwtService jwtService) {
        this.postProvider = userProvider;
        this.postService = userService;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }

    // ******************************************************************************

    /**
     * 게시물 업로드
     * [POST] /posts
     */
    // Body
    @ResponseBody
    @PostMapping("/new-posting")    // POST 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<PostPostRes> createPost(@RequestBody PostPostReq postPostReq) {
        //  @RequestBody란, 클라이언트가 전송하는 HTTP Request Body(우리는 JSON으로 통신하니, 이 경우 body는 JSON)를 자바 객체로 매핑시켜주는 어노테이션
        // title에 값이 존재하는지, 빈 값으로 요청하지는 않았는지 검사하기.
        if (postPostReq.getTitle() == null) {
            return new BaseResponse<>(POST_POSTS_EMPTY_TITLE);
        }
        // 이미지가 존재하는지, 빈 값으로 요청하지는 않았는지 검사하기.
        if (postPostReq.getImgUrl() == null) {
            return new BaseResponse<>(POST_POSTS_EMPTY_IMGURL);
        }
        // imgUrl 형식에 잘 맞는지 확인하기.
        if (!isRegexImgUrl(postPostReq.getImgUrl())) {
            return new BaseResponse<>(POST_POSTS_INVALID_IMGURL);
        }
        // userID에 값이 존재하는지, 로그인하지 않고 요청하지 않았는지 검사하기.
        if (postPostReq.getUserID() <= 0) {
            return new BaseResponse<>(POST_POSTS_EMPTY_USERID);
        }
        try {
            PostPostRes postPostRes = postService.createPost(postPostReq);
            return new BaseResponse<>(postPostRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 업로드
     * [POST] /posts/comment
     */
    // Body
    @ResponseBody
    @PostMapping("/comment")
    public BaseResponse<PostCommentRes> createComment(@RequestBody PostCommentReq postCommentReq) {
        // comment에 값이 존재하는지, 빈 값으로 요청하지는 않았는지 검사하기
        if (postCommentReq.getComment() == null) {
            return new BaseResponse<>(POST_POSTS_EMPTY_COMMENT);
        }
        // userID에 값이 존재하는지, 로그인하지 않고 요청하지 않았는지 검사하기.
        if (postCommentReq.getUserID() <= 0) {
            return new BaseResponse<>(POST_POSTS_EMPTY_USERID);
        }
        try {
            PostCommentRes postCommentRes = postService.createComment(postCommentReq);
            return new BaseResponse<>(postCommentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }


    /**
     * 모든 회원들의  조회 API
     * [GET] /posts
     * <p>
     * 또는
     * <p>
     * 특정 유저가 올린 게시물의 정보 조회 API
     * [GET] /posts? userID=
     */
    //Query String
    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetPostRes>> getPosts(@RequestParam(required = false) String title) {
        //  @RequestParam은, 1개의 HTTP Request 파라미터를 받을 수 있는 어노테이션(?뒤의 값). default로 RequestParam은 반드시 값이 존재해야 하도록 설정되어 있지만, (전송 안되면 400 Error 유발)
        //  지금 예시와 같이 required 설정으로 필수 값에서 제외 시킬 수 있음
        //  defaultValue를 통해, 기본값(파라미터가 없는 경우, 해당 파라미터의 기본값 설정)을 지정할 수 있음
        try {
            // query string인 userID가 있을 경우, 조건을 만족하는 유저정보들을 불러온다.
            List<GetPostRes> getPostsRes = postProvider.getPostsByUserID(title);
            return new BaseResponse<>(getPostsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    @GetMapping("") // (GET) 127.0.0.1:9000/app/posts
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetPostRes1>> getPosts() {
        try {
                List<GetPostRes1> getPostsRes1 = postProvider.getPosts();
                return new BaseResponse<>(getPostsRes1);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * ////     /* *
     * ////     * 특정 유저가 올린 게시물 조회하기
     * ////     * [GET] /posts/userPost/:userID
     * ////
     */
    // Path-varialbe
    @ResponseBody
    @GetMapping("/userPost/{userID}")
    public BaseResponse<List<GetUserPostRes>> getUserPost(@PathVariable("userID") int userID) {
        // @PathVariable RESTful(URL)에서 명시된 파라미터({})를 받는 어노테이션, 이 경우 userID 받아옴.
        //  null값 or 공백값이 들어가는 경우는 적용하지 말 것
        //  .(dot)이 포함된 경우, .을 포함한 그 뒤가 잘려서 들어감
        // Get Posts
        try {
            List<GetUserPostRes> getUserPostRes = postProvider.getUserPost(userID);
            return new BaseResponse<>(getUserPostRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }


    /**
     * 게시물 1개 조회 API
     * [GET] /posts/:postID
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{postID}") // (GET) 127.0.0.1:9000/app/posts/:postID
    public BaseResponse<GetPostRes> getPost(@PathVariable("postID") int postID) {
        // @PathVariable RESTful(URL)에서 명시된 파라미터({})를 받는 어노테이션, 이 경우 postId값을 받아옴.
        //  null값 or 공백값이 들어가는 경우는 적용하지 말 것
        //  .(dot)이 포함된 경우, .을 포함한 그 뒤가 잘려서 들어감
        // Get Posts
        try {
            GetPostRes getPostRes = postProvider.getPost(postID);
            return new BaseResponse<>(getPostRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }


    /**
     * 해당 게시물에 좋아요를 누른 사람들 조회하기
     * [GET] /posts/likes/:postID
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/likes/{postID}")
    public BaseResponse<List<GetLikeRes>> getLike(@PathVariable("postID") int postID) {
        try {
            List<GetLikeRes> getLikeRes = postProvider.getLike(postID);
            return new BaseResponse<>(getLikeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 내가(아직은 특정 유저) 좋아요 누른 게시물 조회하기
     * [GET] /posts/likePost/:userID
     */
    @ResponseBody
    @GetMapping ("/likePost/{userID}")
    public BaseResponse<List<GetUserLikeRes>> getUserLike(@PathVariable("userID") int userID) {
        try {
            List<GetUserLikeRes> getUserLikeRes = postProvider.getUserLike(userID);
            return new BaseResponse<>(getUserLikeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 별 댓글 남긴 유저와 내용 조회 API
     * [GET] /posts/comment/:postID
     */
    @ResponseBody
    @GetMapping("/comment/{postID}")
    public BaseResponse<List<GetCommentRes>> getComment(@PathVariable("postID") int postID) {
        try {
            List<GetCommentRes> getCommentRes = postProvider.getComment(postID);
            return new BaseResponse<>(getCommentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 유저정보변경 API
     * [PATCH] /posts/:postID
     */
    @ResponseBody
    @PatchMapping("/{postID}")
    public BaseResponse<String> modifyPostTitle(@PathVariable("postID") int postID, @RequestBody Post post) {
        try {
/**
 *********** 해당 부분은 7주차 - JWT 수업 후 주석해체 해주세요!  ****************
 //jwt에서 idx 추출.
 int postIDByJwt = jwtService.getPostID();
 //postID와 접근한 유저가 같은지 확인
 if(postID != postIDByJwt){
 return new BaseResponse<>(INVALID_USER_JWT);
 }
 //같다면 유저네임 변경
 **************************************************************************
 */
            PatchPostReq patchPostReq = new PatchPostReq(postID, post.getTitle());
            postService.modifyPostTitle(patchPostReq);

            String result = "게시물이 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 댓글 수정 API
     * [PATCH] /posts/comment/:postID/:userID
     */
    @ResponseBody
    @PatchMapping("/comment/{postID}/{userID}")
    public BaseResponse<String> modifyComment(@PathVariable("postID") int postID, @PathVariable("userID") int userID,
                                              @RequestBody Comment comment) {
        try {
            PatchCommentReq patchCommentReq = new PatchCommentReq(postID, userID, comment.getComment());

            postService.modifyComment(patchCommentReq);

            String result = "댓글이 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

//    /**
//     * 특정 게시물 좋아요 취소 API
//     * [PATCH] /posts/likes/:userID
//     */
//    @ResponseBody
//    @PatchMapping("/likes/{userID}")
//    public BaseResponse<String> modifyLikePost(@PathVariable("userID") int userID, @RequestBody Post post) {
//        try {
//            PatchLikeReq patchLikeReq = new PatchLikeReq(userID, post.getPostID());
//            postService.modifyLikePost(patchLikeReq);
//
//            String result = "좋아요가 수정되었습니다.";
//            return new BaseResponse<>(result);
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }
//}



///**
// * 유저정보삭제(탈퇴) API
// * [DELETE] /posts/:postID
// */
//    @ResponseBody
//    @DeleteMapping("/{postID}")
//    public BaseResponse<>