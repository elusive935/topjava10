package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            Query query = em.createNamedQuery(Meal.GET_AUTH_USER, User.class);
            query.setParameter("userId", AuthorizedUser.id());
            User user = DataAccessUtils.singleResult((List<User>)query.getResultList());
            meal.setUser(user);
            em.persist(meal);
            return meal;
        } else {
            Query query = em.createNamedQuery(Meal.SAVE);
            query.setParameter("description", meal.getDescription());
            query.setParameter("calories", meal.getCalories());
            query.setParameter("date_time", meal.getDateTime());
            query.setParameter("userId", userId);
            query.setParameter("id", meal.getId());
            return query.executeUpdate() != 0 ? meal : null;
        }

    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Query query = em.createNamedQuery(Meal.DELETE);
        query.setParameter("id", id);
        query.setParameter("userId", userId);
        return query.executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Query query = em.createNamedQuery(Meal.GET_BY_ID);
        query.setParameter("id", id);
        query.setParameter("userId", userId);
        return DataAccessUtils.singleResult((List<Meal>)query.getResultList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class).setParameter("userId", userId).getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        Query query = em.createNamedQuery(Meal.ALL_SORTED_FILTERED, Meal.class);
        query.setParameter(1, startDate);
        query.setParameter(2, endDate);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}