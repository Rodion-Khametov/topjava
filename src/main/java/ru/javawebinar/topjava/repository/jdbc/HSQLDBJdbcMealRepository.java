package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Profile("hsqldb")
public class HSQLDBJdbcMealRepository extends JdbcMealRepository {

    @Autowired
    public HSQLDBJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);

    }

    @Override
    public List<Meal> getBetweenHalfOpen(Object startDateTime, Object endDateTime, int userId) {
        startDateTime = Timestamp.valueOf((String) startDateTime);
        endDateTime = Timestamp.valueOf((String) endDateTime);
        return super.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
