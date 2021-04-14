package ro.ubb.socket.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

@Configuration
public class JdbcConfig {
    @Bean
    JdbcOperations jdbcOperations() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean
    DataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:postgresql://localhost:5432/restaurantjdbc");
        basicDataSource.setUsername("postgres");
        basicDataSource.setPassword("friends");
        basicDataSource.setInitialSize(2);
        return basicDataSource;
    }
}
