package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal save(int userId, Meal meal) {
        Meal result = repository.save(userId, meal);
        ValidationUtil.checkNotFound(result, "userId = "+userId);
        return result;
    }

    @Override
    public boolean delete(int userId, int id) {
        boolean result = repository.delete(userId, id);
        ValidationUtil.checkNotFound(result, "userId = "+userId);
        return result;
    }

    @Override
    public Meal get(int userId, int id) {
        Meal result = repository.get(userId, id);
        ValidationUtil.checkNotFound(result, "userId = "+userId);
        return result;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        return repository.getFiltered(userId, startDate, endDate);

    }
}