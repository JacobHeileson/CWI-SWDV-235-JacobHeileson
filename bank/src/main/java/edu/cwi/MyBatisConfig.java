package edu.cwi;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class MyBatisConfig {

    private static SqlSessionFactory sqlSessionFactory;

    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
            hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/online_bank");
            hikariConfig.setUsername("root");
            hikariConfig.setPassword("MYSQLSA");

            HikariDataSource dataSource = new HikariDataSource(hikariConfig);

            Environment environment = new Environment(
                "development",
                new JdbcTransactionFactory(),
                dataSource
            );

            Configuration configuration = new Configuration(environment);
            configuration.setMapUnderscoreToCamelCase(true);

            configuration.addMapper(UserMapper.class);
            configuration.addMapper(AccountMapper.class);
            configuration.addMapper(TransactionMapper.class);

            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        }
        return sqlSessionFactory;
    }
}