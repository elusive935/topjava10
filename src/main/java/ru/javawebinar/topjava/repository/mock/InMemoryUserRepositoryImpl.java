package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
//        for (int i = 0; i < 10; i++) {
//            repository.put(counter.incrementAndGet(), new User(i, "user"+(50-i), "email", "password", Role.ROLE_ADMIN));
//        }
        repository.put(counter.incrementAndGet(), new User(0, "Alena", "email", "password", Role.ROLE_ADMIN));
        repository.put(counter.incrementAndGet(), new User(0, "Sereja", "email2", "password", Role.ROLE_ADMIN));
        repository.put(counter.incrementAndGet(), new User(0, "Kolya", "email", "password", Role.ROLE_ADMIN));
        repository.put(counter.incrementAndGet(), new User(0, "Sereja", "email1", "password", Role.ROLE_ADMIN));
        repository.put(counter.incrementAndGet(), new User(0, "Sereja", "email3", "password", Role.ROLE_ADMIN));
    }

    @Override
    public boolean delete(int id) {
        LOG.info("delete " + id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        LOG.info("save " + user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
        }
        return repository.put(user.getId(), user);
    }

    @Override
    public User get(int id) {
        LOG.info("get " + id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        LOG.info("getAll");
        return new ArrayList<>(repository.values()).stream()
                .sorted(Comparator.comparing(User::getName))
                .sorted(Comparator.comparing(User::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        LOG.info("getByEmail " + email);
        return repository.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
