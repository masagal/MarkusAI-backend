package org.example.groupbackend.chat.sql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SqlHandler {
    private final JdbcTemplate jdbcTemplate;

    public SqlHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void execute(ExtractSQL sql) {
        jdbcTemplate.execute(sql.sqlStatement());
    }

    public String queryForString(ExtractSQL sql) {
        return jdbcTemplate.queryForObject(
                sql.sqlStatement(),
                String.class);
    }
}
