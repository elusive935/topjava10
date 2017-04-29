package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.MealTestData.ADMIN_MEALS;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest{
    @Test
    public void testGetWithMeals(){
        User user = service.getWithMeals(ADMIN_ID);
        MATCHER.assertEquals(ADMIN, user);
        MealTestData.MATCHER.assertCollectionEquals(ADMIN_MEALS, user.getMeals());

        User userSlim = service.getWithMeals(SLIM_ID);
        MATCHER.assertEquals(SLIM, userSlim);
        Assert.assertEquals(true, userSlim.getMeals().isEmpty());
    }
}
