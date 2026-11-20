package edu.cwi;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface AccountMapper {

    @Select("SELECT account_id AS id, user_id, account_type, balance " +
            "FROM accounts WHERE user_id = #{userId} LIMIT 1")
    Account findByUserId(int userId);

    @Insert("INSERT INTO accounts (user_id, balance) VALUES (#{userId}, #{balance})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "account_id")
    void insert(Account account);
}
