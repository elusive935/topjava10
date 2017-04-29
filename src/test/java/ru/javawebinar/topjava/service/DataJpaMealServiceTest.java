package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.MATCHER;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {

    @Test
    public void testGetWithUser(){
        Meal meal = service.getWithUser(MEAL1_ID+3, USER_ID);
        MATCHER.assertEquals(MEAL4, meal);
        UserTestData.MATCHER.assertEquals(USER, meal.getUser());
    }

    @Test
    public void testGetWithUserNotFound(){
        thrown.expect(NotFoundException.class);
        Meal meal = service.getWithUser(MEAL1_ID+3, ADMIN_ID);
    }

}
