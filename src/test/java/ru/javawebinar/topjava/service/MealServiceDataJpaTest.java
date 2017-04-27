package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.MATCHER;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.MealTestData.MEAL4;
import static ru.javawebinar.topjava.UserTestData.USER;

@ActiveProfiles(Profiles.DATAJPA)
public class MealServiceDataJpaTest extends MealServiceTest {

    @Test
    public void testGetWithUser(){
        Meal meal = getService().getWithUser(MEAL1_ID+3);
        MATCHER.assertEquals(MEAL4, meal);
        UserTestData.MATCHER.assertEquals(USER, meal.getUser());
    }

}
