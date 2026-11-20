package edu.cwi;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mindrot.jbcrypt.BCrypt;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;

public class UsersController implements CrudHandler {

    @Override
    public void getAll(Context ctx) {
        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            List<User> users = userMapper.findAll();
            ctx.json(users);
        }
    }

    @Override
    public void getOne(Context ctx, String resourceId) {
        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            int id = Integer.parseInt(resourceId);
            User user = userMapper.findById(id);

            if (user != null) {
                ctx.json(user);
            } else {
                ctx.status(404).json(Map.of("error", "User not found"));
            }
        }
    }

    @Override
    public void create(Context ctx) {
        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {

            UserMapper userMapper = session.getMapper(UserMapper.class);
            AccountMapper accountMapper = session.getMapper(AccountMapper.class);
            TransactionMapper transactionMapper = session.getMapper(TransactionMapper.class);

            // deserialize the body json
            User user = ctx.bodyAsClass(User.class);

            BigDecimal deposit = BigDecimal.valueOf(5.00);

            // hash the user password
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            // insert the user
            userMapper.insert(user);

            // insert an account for the new user
            Account account = new Account();

            account.setUserId(user.getId());
            account.setBalance(deposit);

            accountMapper.insert(account);

            // insert the opening transaction
            Transaction transaction = new Transaction();

            transaction.setAccountId(account.getId());
            transaction.setTransactionType(TransactionType.DEPOSIT);
            transaction.setAmount(deposit);
            transaction.setDescription("Opening deposit.");

            transactionMapper.insert(transaction);

            // commit the session
            session.commit();

            // return the new user data
            ctx.json(user);
        }
    }

    @Override
    public void update(Context ctx, String resourceId) {
        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            int id = Integer.parseInt(resourceId);

            User user = ctx.bodyAsClass(User.class);
            user.setId(id);

            userMapper.update(user);
            session.commit();

            ctx.json(user);
        }
    }

    @Override
    public void delete(Context ctx, String resourceId) {
        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            int id = Integer.parseInt(resourceId);

            userMapper.delete(id);
            session.commit();

            ctx.json(Map.of("deleted", id));
        }
    }

    public User authenticateUser(Context ctx, String username, String password) {
        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            User user = userMapper.findByUsername(username);

            if (user != null && password != null && BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
            return null;
        }
    }
}