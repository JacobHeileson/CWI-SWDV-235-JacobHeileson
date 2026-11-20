package edu.cwi;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface TransactionMapper {

    @Select("SELECT transaction_id AS id, account_id, transaction_type, amount, description, created_at " +
            "FROM transactions WHERE account_id = #{accountId} ORDER BY created_at DESC")
    List<Transaction> findByAccountId(int accountId);

    @Insert("INSERT INTO transactions (account_id, transaction_type, amount, description) " +
            "VALUES (#{accountId}, #{transactionType}, #{amount}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "transaction_id")
    void insert(Transaction transaction);
}