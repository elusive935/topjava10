package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepositoryImpl() {

        for (Meal meal: MealsUtil.MEALS) {
            save(1, meal);
        }
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        } else {
            Meal getMeal = repository.get(meal.getId());
            if (getMeal.getUserId() != userId) {
                return null;
            }
        }
        meal.setUserId(userId);
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int userId, int id) {
        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != userId) {
            return false;
        }
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != userId) {
            return null;
        }
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getFiltered(userId, LocalDate.MIN, LocalDate.MAX);
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime().toLocalDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

