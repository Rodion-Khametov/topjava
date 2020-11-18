package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcUserRepository.class);

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private final Validator validator = validatorFactory.getValidator();

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public static void validate(Object object, Validator validator) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        for (ConstraintViolation<Object> violation : constraintViolations) {
            if (violation != null) {
                log.error(violation.getMessage());
                throw new ConstraintViolationException(constraintViolations);
            }
        }
    }

    @Override
    public User save(User user) {
        validate(user, validator);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            List<Role> roles = new ArrayList<>(user.getRoles());
            String sql = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(sql, roles, roles.size(),

                    (preparedStatement, role) -> {
                        preparedStatement.setInt(1, user.getId());
                        preparedStatement.setString(2, Role.USER.toString());
                    });

        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users u USING user_roles r WHERE u.id = r.user_id AND id=? ", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.*, r.role FROM users u INNER JOIN user_roles r ON u.id = r.user_id WHERE id=? ", new UserExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT u.*, r.role FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE email=?", new UserExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
//        return jdbcTemplate.query("SELECT DISTINCT u.* FROM users u LEFT JOIN user_roles r ON u.id = r.user_id ORDER BY u.name, email", new UserMapper());
        return jdbcTemplate.query("SELECT u.*, r.role FROM users u LEFT JOIN user_roles r ON u.id = r.user_id ORDER BY u.name, email", new UserExtractor());

    }

    private static final class UserExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            User user;
            Set<User> users = new HashSet<>();
            Map<Integer, Set<Role>> userMap = new HashMap<>();
            Set<Role> roles = new HashSet<>();
            int i = 0;
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                user = ROW_MAPPER.mapRow(resultSet, i);
                if (!userMap.containsKey(id)) {
                    roles.clear();
                }
                roles.add(Role.valueOf(resultSet.getString("role")));
                userMap.put(id, new HashSet<>(roles));
                users.add(user);
                i++;
            }
            for (Map.Entry<Integer, Set<Role>> entry : userMap.entrySet()) {
                for (User value : users) {
                    if (value.getId().equals(entry.getKey())) {
                        value.setRoles(entry.getValue());
                    }
                }
            }
            return new ArrayList<>(users);
        }
    }
}