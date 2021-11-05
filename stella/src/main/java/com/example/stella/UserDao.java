package com.example.stella;
// 실질적으로 쿼리문을 작성해서 원하는 데이터를 가져오거나 insert into 문으로 데이터를 넣는 쿼리문을 작성할 수도 있음

import com.example.stella.model.GetUserRes;
import com.example.stella.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // *** USER ***
//    // 원하는 데이터 가져오기
//    public List<GetUserRes> userRes() {
//        return this.jdbcTemplate.query("Select * from User",
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("userID"),
//                        rs.getString("name"),
//                        rs.getString("phoneNum"),
//                        rs.getString("email"))
//
//        );
//    }

//    public List<GetUserIDRes> userIDRes() {
//        return this.jdbcTemplate.query("Select name, phoneNum, email from User",
//                (rs, rowNum) -> new GetUserIDRes(
//                        rs.getString("name"),
//                        rs.getString("phoneNum"),
//                        rs.getString("email"))
//                );
//    }

//    // 데이터를 넣기 (가져온 값을 model이라는 객체에 담아서 다시 Cotroller에 넘겨줘서 Client단으로 데이터를 전송함)
//    public int addUser(PostUserReq postuserReq) {
//        String createUserQuery = "insert into User (name, phoneNum, email) VALUES (?,?,?)";
//        Object[] createUserParams = new Object[]{
//                postuserReq.getName(), postuserReq.getPhoneNum(), postuserReq.getEmail()
//        };
//        this.jdbcTemplate.update(createUserQuery, createUserParams);
//
//        return this.jdbcTemplate.queryForObject("select last_insert_id()", int.class);
//    }


    // *** POST ***
    // 원하는 데이터 가져오기
//    public List<GetUserRes> userRes() {
//        return this.jdbcTemplate.query("Select * from Post",
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("postID"),
//                        rs.getInt("userID"),
//                        rs.getString("title"),
//                        rs.getString("imgUrl"))
//
//        );
//    }
//
//    // 데이터를 넣기 (가져온 값을 model이라는 객체에 담아서 다시 Cotroller에 넘겨줘서 Client단으로 데이터를 전송함)
//    public int addUser(PostUserReq postuserReq) {
//        String createUserQuery = "insert into Post (userID, title, imgUrl) VALUES (?,?,?)";
//        Object[] createUserParams = new Object[]{
//                postuserReq.getUserID(), postuserReq.getTitle(), postuserReq.getImgUrl()
//        };
//        this.jdbcTemplate.update(createUserQuery, createUserParams);
//
//        return this.jdbcTemplate.queryForObject("select last_insert_id()", int.class);
//    }
//}

    /// *** Chat ***
    public List<GetUserRes> userRes() {
        return this.jdbcTemplate.query("Select * from Chat",
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("chatIdx"),
                        rs.getInt("roomIdx"),
                        rs.getString("message"))
        );
    }

    // 데이터를 넣기 (가져온 값을 model이라는 객체에 담아서 다시 Cotroller에 넘겨줘서 Client단으로 데이터를 전송함)
    public int addUser(PostUserReq postuserReq) {
        String createUserQuery = "insert into Chat (roomIdx, message) VALUES (?,?)";
        Object[] createUserParams = new Object[]{
                postuserReq.getRoomIdx(), postuserReq.getMessage()
        };
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        return this.jdbcTemplate.queryForObject("select last_insert_id()", int.class);
    }

}