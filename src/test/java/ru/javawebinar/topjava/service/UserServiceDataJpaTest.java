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
public class UserServiceDataJpaTest extends UserServiceTest{
    @Test
    public void testGetWithMeals(){
        User user = getService().getWithMeals(ADMIN_ID);
        MATCHER.assertEquals(ADMIN, user);
        MealTestData.MATCHER.assertCollectionEquals(ADMIN_MEALS, user.getMeals());

        User userSlim = getService().getWithMeals(SLIM_ID);
        MATCHER.assertEquals(SLIM, userSlim);
        Assert.assertEquals(true, userSlim.getMeals().isEmpty());
    }
}
