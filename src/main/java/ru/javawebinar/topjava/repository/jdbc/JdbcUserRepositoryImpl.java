package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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
        String sql = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            jdbcTemplate.batchUpdate(sql, new RolesBatchPreparedStatementSetter(user.getId(), user.getRoles()));
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);

            Set<Role> rolesToAdd = new HashSet<>();
            Set<Role> rolesToDelete = new HashSet<>();

            List<Role> currentRoles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", new RowMapperRoles(), user.getId());
            List<Role> targetRoles = new ArrayList<>(user.getRoles());

            for (Role currentRole : currentRoles) {
                if (!targetRoles.contains(currentRole)) {
                    rolesToDelete.add(currentRole);
                }
            }
            for (Role targetRole : targetRoles) {
                if (!currentRoles.contains(targetRole)) {
                    rolesToAdd.add(targetRole);
                }
            }

            jdbcTemplate.batchUpdate(sql, new RolesBatchPreparedStatementSetter(user.getId(), rolesToAdd));
            for (Role role : rolesToDelete) {
                jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=? AND role=?", user.getId(), role.toString());
            }
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
        List<User> queryResult = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE id=?",
                new RowMapperUser(User.class), id);
        return getSingleUserWithRoles(queryResult);
    }

    @Override
    public User getByEmail(String email) {
        List<User> queryResult = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE email=?",
                new RowMapperUser(User.class), email);
        return getSingleUserWithRoles(queryResult);
    }

    @Override
    public List<User> getAll() {
        List<User> queryResult = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id ",
                new RowMapperUser(User.class));
        return getUsersWithRoles(queryResult);
    }

    private List<User> getUsersWithRoles(List<User> queryResult){
        return extractRoles(queryResult)
                .stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }

    private User getSingleUserWithRoles(List<User> queryResult){
        return DataAccessUtils.singleResult(extractRoles(queryResult));
    }

    private Collection<User> extractRoles(List<User> queryResult) {
        Map<Integer, User> map = new HashMap<>();
        for (User user : queryResult) {
            int id = user.getId();
            if (map.containsKey(id)) {
                map.get(id).addRole(user.getRoles());
            } else {
                map.put(id, user);
            }
        }
        return map.values();
    }

    private class RowMapperUser extends BeanPropertyRowMapper<User> {
        RowMapperUser(Class<User> mappedClass) {
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

    private class RowMapperRoles extends BeanPropertyRowMapper{
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Role.valueOf(rs.getString("role"));
        }
    }

    private class RolesBatchPreparedStatementSetter implements BatchPreparedStatementSetter{
        private int id;
        private List<Role> roles;

        RolesBatchPreparedStatementSetter(int id, Set<Role> roles) {
            this.id = id;
            this.roles = new ArrayList<>(roles);
        }

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setInt(1, id);
            ps.setString(2, roles.get(i).toString());
        }

        @Override
        public int getBatchSize() {
            return roles.size();
        }
    }
}
