package edu.cwi;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {

    @Select("SELECT user_id AS id, username, password, email, first_name, last_name, role FROM users")
    List<User> findAll();

    @Select("SELECT user_id AS id, username, password, email, first_name, last_name, role " +
            "FROM users WHERE user_id = #{id}")
    User findById(int id);

    @Select("SELECT user_id AS id, username, password, email, first_name, last_name, role " +
            "FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO users (username, password, email, first_name, last_name, role) " +
            "VALUES (#{username}, #{password}, #{email}, #{firstName}, #{lastName}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "user_id")
    void insert(User user);

    @Update("UPDATE users SET username = #{username}, email = #{email}, " +
            "first_name = #{firstName}, last_name = #{lastName}, role = #{role} " +
            "WHERE user_id = #{id}")
    void update(User user);

    @Delete("DELETE FROM users WHERE user_id = #{id}")
    void delete(int id);
}