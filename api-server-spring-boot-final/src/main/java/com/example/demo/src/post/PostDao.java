package com.example.demo.src.post;


import com.example.demo.src.post.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository //  [Persistence Layer에서 DAO를 명시하기 위해 사용]

/**
 * DAO란?
 * 데이터베이스 관련 작업을 전담하는 클래스
 * 데이터베이스에 연결하여, 입력 , 수정, 삭제, 조회 등의 작업을 수행
 */
public class PostDao {

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    // ******************************************************************************

    /**
     * DAO관련 함수코드의 전반부는 크게 String ~~~Query와 Object[] ~~~~Params, jdbcTemplate함수로 구성되어 있습니다.(보통은 동적 쿼리문이지만, 동적쿼리가 아닐 경우, Params부분은 없어도 됩니다.)
     * Query부분은 DB에 SQL요청을 할 쿼리문을 의미하는데, 대부분의 경우 동적 쿼리(실행할 때 값이 주입되어야 하는 쿼리) 형태입니다.
     * 그래서 Query의 동적 쿼리에 입력되어야 할 값들이 필요한데 그것이 Params부분입니다.
     * Params부분은 클라이언트의 요청에서 제공하는 정보(~~~~Req.java에 있는 정보)로 부터 getXXX를 통해 값을 가져옵니다. ex) getEmail -> email값을 가져옵니다.
     *      Notice! get과 get의 대상은 카멜케이스로 작성됩니다. ex) item -> getItem, password -> getPassword, email -> getEmail, postID -> getPostID
     * 그 다음 GET, POST, PATCH 메소드에 따라 jabcTemplate의 적절한 함수(queryForObject, query, update)를 실행시킵니다(DB요청이 일어납니다.).
     *      Notice!
     *      POST, PATCH의 경우 jdbcTemplate.update
     *      GET은 대상이 하나일 경우 jdbcTemplate.queryForObject, 대상이 복수일 경우, jdbcTemplate.query 함수를 사용합니다.
     * jdbcTeplate이 실행시킬 때 Query 부분과 Params 부분은 대응(값을 주입)시켜서 DB에 요청합니다.
     * <p>
     * 정리하자면 < 동적 쿼리문 설정(Query) -> 주입될 값 설정(Params) -> jdbcTemplate함수(Query, Params)를 통해 Query, Params를 대응시켜 DB에 요청 > 입니다.
     * <p>
     * <p>
     * DAO관련 함수코드의 후반부는 전반부 코드를 실행시킨 후 어떤 결과값을 반환(return)할 것인지를 결정합니다.
     * 어떠한 값을 반환할 것인지 정의한 후, return문에 전달하면 됩니다.
     * ex) return this.jdbcTemplate.query( ~~~~ ) -> ~~~~쿼리문을 통해 얻은 결과를 반환합니다.
     */

    /**
     * 참고 링크
     * https://jaehoney.tistory.com/34 -> JdbcTemplate 관련 함수에 대한 설명
     * https://velog.io/@seculoper235/RowMapper%EC%97%90-%EB%8C%80%ED%95%B4 -> RowMapper에 대한 설명
     */

    // 게시물 포스팅
    public int createPost(PostPostReq postPostReq) {
        String createPostQuery = "insert into Post (userID, title, imgUrl) VALUES (?,?,?)"; // 실행될 동적 쿼리문
        Object[] createPostParams = new Object[]{postPostReq.getUserID(), postPostReq.getTitle(), postPostReq.getImgUrl()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createPostQuery, createPostParams);

        String lastInserIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 postID번호를 반환한다.
    }


    // 게시물 변경
    public int modifyPostTitle(PatchPostReq patchPostReq) {
        String modifyPostNameQuery = "update Post set title = ? where postID = ? "; // 해당 postID를 만족하는 Post를 해당 title로 변경한다.
        Object[] modifyPostNameParams = new Object[]{patchPostReq.getTitle(), patchPostReq.getPostID()}; // 주입될 값들(name, postID) 순

        return this.jdbcTemplate.update(modifyPostNameQuery, modifyPostNameParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }

    // 댓글 수정
    public int modifyComment(PatchCommentReq patchCommentReq) {
        String modifyCommentQuery = "update Comment set comment = ? where postID = ? and userID = ?";
        Object[] modifyCommentParams = new Object[]{patchCommentReq.getComment(), patchCommentReq.getPostID(), patchCommentReq.getUserID()};

        return this.jdbcTemplate.update(modifyCommentQuery, modifyCommentParams);
    }

//    // 좋아요 수정
//    public int modifyLikePost(PatchLikeReq patchLikeReq) {
//        String modifyLikeQuery = "update Post set postID = ? where userID = ? ";
//        Object[] modifyLikeQueryParams = new Object[]{patchLikeReq.getPostID(), patchLikeReq.getUserID()};
//
//        return this.jdbcTemplate.update(modifyLikeQuery, modifyLikeQueryParams);
//    }


    // 댓글 작성
    public int createComment(PostCommentReq postCommentReq) {
        String createCommentQuery = "insert into Comment (userID, postID, comment) VALUES (?,?,?)";
        Object[] createCommentParams = new Object[]{postCommentReq.getUserID(), postCommentReq.getPostID(), postCommentReq.getComment()};
        this.jdbcTemplate.update(createCommentQuery, createCommentParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class);
    }


    // Post 테이블에 존재하는 전체 유저들의 정보 조회
    public List<GetPostRes1> getPosts() {
        String getPostsQuery = "select * from Post"; //Post 테이블에 존재하는 모든 회원들의 정보를 조회하는 쿼리
        return this.jdbcTemplate.query(getPostsQuery,
                (rs, rowNum) -> new GetPostRes1(
                        rs.getInt("postID"),
                        rs.getInt("userID"),
                        rs.getString("title"),
                        rs.getString("imgUrl"))
                ); // 복수개의 회원정보들을 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보)의 결과 반환(동적쿼리가 아니므로 Parmas부분이 없음)
    }

    // 해당 title을 갖는 게시물 정보 조회
    public List<GetPostRes> getPostByTitle(String title) {
        String getPostsByUserIDQuery = "select * from Post where title =?"; // 해당 이메일을 만족하는 유저를 조회하는 쿼리문
        // like로 에외처리
        String getPostsByUserIDParams = title;
        return this.jdbcTemplate.query(getPostsByUserIDQuery,
                (rs, rowNum) -> new GetPostRes(
                        rs.getInt("postID"),
                        rs.getInt("userID"),
                        rs.getString("name"),
                        rs.getString("title"),
                        rs.getString("imgUrl"),
                        rs.getInt("좋아요갯수"),
                        rs.getInt("댓글갯수")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getPostsByUserIDParams); // 해당 닉네임을 갖는 모든 Post 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 해당 postID를 갖는 게시물
    public GetPostRes getPost(int postID) {
        String getPostQuery = "SELECT Post.postID, User.userID, User.name, Post.title, Post.imgUrl, count(likeIdx) as 좋아요갯수, count(distinct Comment.postID) as 댓글갯수\n" +
                "    FROM Post\n" +
                "        INNER JOIN User\n" +
                "            ON User.userID = Post.userID\n" +
                "        INNER JOIN Comment\n" +
                "            ON Post.postID = Comment.postID\n" +
                "        INNER JOIN `Like`\n" +
                "            ON `Like`.postID = Post.postID\n" +
                "    WHERE Post.postID = ?;"; // 해당 postID를 만족하는 유저를 조회하는 쿼리문
        int getPostParams = postID;
        return this.jdbcTemplate.queryForObject(getPostQuery,
                (rs, rowNum) -> new GetPostRes(
                        rs.getInt("postID"),
                        rs.getInt("userID"),
                        rs.getString("name"),
                        rs.getString("title"),
                        rs.getString("imgUrl"),
                        rs.getInt("좋아요갯수"),
                        rs.getInt("댓글갯수")),
                        getPostParams); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 해당 게시물(postID)에 좋아요를 누른 사용자 정보(아이디, 이름) 조회
    public List<GetLikeRes> getLike(int postID) {
        String getLikeQuery = "SELECT User.userID, User.name FROM `Like` INNER JOIN Post ON Post.postID = `Like`.postID INNER JOIN User ON User.userID = `Like`.userID WHERE Post.postID = ?";
        int getLikeParams = postID;
        return this.jdbcTemplate.query(getLikeQuery,
                (rs, rowNum) -> new GetLikeRes(
                        rs.getInt("userID"),
                        rs.getString("name")),
                getLikeParams);
    }

    // 내가 좋아요 누른 게시물 목록 조회 (사진만 보임)
    public List<GetUserLikeRes> getUserLike(int userID) {
        String getUserLikeQuery = "SELECT Post.imgUrl\n" +
                "    FROM `Like`\n" +
                "        INNER JOIN Post\n" +
                "            on `Like`.postID = Post.postID\n" +
                "        INNER JOIN User\n" +
                "            on `Like`.userID = User.useriD\n" +
                "    WHERE `Like`.userID = ?;";
        int getUserLikeParams = userID;
        return this.jdbcTemplate.query(getUserLikeQuery,
                (rs, rowNum) -> new GetUserLikeRes(
                        rs.getString("imgUrl")),
                getUserLikeParams);
    }

    // 해당 user가 작성한 게시물 조회
    public List<GetUserPostRes> getUserPost(int userID) {
        String getUserPostQuery = "SELECT User.userID, User.name, Post.postID, Post.imgUrl\n" +
                "    FROM Post\n" +
                "        INNER JOIN User\n" +
                "            ON User.userID = Post.userID\n" +
                "    WHERE User.userID=?;";
        int getUserPostParams = userID;
        return this.jdbcTemplate.query(getUserPostQuery,
                (rs, rowNum) -> new GetUserPostRes(
                        rs.getInt("userID"),
                        rs.getString("name"),
                        rs.getInt("postID"),
                        rs.getString("imgUrl")),
                getUserPostParams);
    }

    // 게시물 별 댓글 조회
    public List<GetCommentRes> getComment(int postID) {
        String getCommentQuery = "SELECT Post.title,User.name, Comment.comment\n" +
                "    FROM Comment\n" +
                "        INNER JOIN User\n" +
                "        ON User.userID = Comment.userID\n" +
                "        INNER JOIN Post\n" +
                "        ON Comment.postID = Post.postID\n" +
                "    WHERE Comment.postID = ?;";
        int getCommentParams = postID;
        return this.jdbcTemplate.query(getCommentQuery,
                (rs, rowNum) -> new GetCommentRes(
                        rs.getString("title"),
                        rs.getString("name"),
                        rs.getString("comment")),
                getCommentParams);
    }
}


