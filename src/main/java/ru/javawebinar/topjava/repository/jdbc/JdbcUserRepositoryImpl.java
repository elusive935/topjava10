package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User result = DataAccessUtils.singleResult(users);

        List<Role> roles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", (rs, rowNum) ->
                Role.valueOf(rs.getString("role")), id);
        result.setRoles(new HashSet<>(roles));
        return result;
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User result = DataAccessUtils.singleResult(users);

        List<Role> roles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", (rs, rowNum) ->
                Role.valueOf(rs.getString("role")), result.getId());

        result.setRoles(new HashSet<>(roles));
        return result;
    }

    @Override
    public List<User> getAll() {
        List<User> queryResult = jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_roles ON users.id = user_roles.user_id " +
                                                                                "ORDER BY name, email", new RowMapperUser(User.class));
        return extractRolesAndAdd(queryResult);
    }

    private List<User> extractRolesAndAdd(List<User> queryResult){
        Map<Integer, User> map = new HashMap<>();
        for (User user:queryResult) {
            int id = user.getId();
            if (map.containsKey(id)) {
                map.get(id).addRole(user.getRoles());
            } else {
                map.put(id, user);
            }
        }
        return new ArrayList<>(map.values());
    }

    private class RowMapperUser extends BeanPropertyRowMapper<User> {
        public RowMapperUser(Class<User> mappedClass) {
            super(mappedClass);
        }

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User result = super.mapRow(rs, rowNum);
            String role = rs.getString("role");
            result.addRole(Role.valueOf(role));
            return result;
        }
    }
}
