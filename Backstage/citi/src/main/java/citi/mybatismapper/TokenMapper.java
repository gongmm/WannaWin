package citi.mybatismapper;

import citi.vo.RefreshToken;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/*
 * 接口设计：刘钟博
 * 代码填充：任思远
 */

@Repository
public interface TokenMapper {

    final String selectTokenByID = "SELECT * FROM user_token WHERE userID = #{userID}";
    final String insertToken = "INSERT INTO user_token(userID, refreshToken, time) VALUES(#{userID}, #{refreshToken}, #{time}";
    final String updateToken = "UPDATE user_token SET";
    final String deleteToken = "DELETE FROM user_token WHERE userID = #{userID}";

    @Select(selectTokenByID)
    String select(String userID);

    @Insert(insertToken)
    int insert(RefreshToken token);

    @Update(updateToken)
    int update();


    @Delete(deleteToken)
    int delete(String userID);

}
