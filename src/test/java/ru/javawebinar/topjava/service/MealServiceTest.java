package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;

/**
 * Created by alena.nikiforova on 13.04.2017.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest {
    final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDateTime).reversed();

    @Autowired
    private MealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
        new MealTestData();
    }

    @Test
    public void testGet() throws Exception {
        MATCHER.assertEquals(MEAL_USER_1, service.get(INDEX_USER_1, UserTestData.USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testDelete() throws Exception {
        service.delete(INDEX_USER_2, UserTestData.USER_ID);
        service.get(INDEX_USER_2, UserTestData.USER_ID);
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        LocalDateTime start = LocalDateTime.of(2017, 4, 11, 8, 0, 0);
        LocalDateTime end = LocalDateTime.now();


        List<Meal> betweenDateTimes =
                service.getBetweenDateTimes(start, end, UserTestData.USER_ID);

        MATCHER.assertCollectionEquals(Arrays.asList(MEAL_USER_5, MEAL_USER_4), betweenDateTimes);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Meal> allUserMeals = service.getAll(UserTestData.USER_ID);

        MATCHER.assertCollectionEquals(Arrays.asList(MEAL_USER_5, MEAL_USER_4, MEAL_USER_3, MEAL_USER_2, MEAL_USER_1), allUserMeals);
    }

    @Test
    public void testUpdate() throws Exception {
        Meal meal = service.get(INDEX_USER_3, UserTestData.USER_ID);
        meal.setCalories(100500);
        meal.setDescription(meal.getDescription()+"suffix");
        service.update(meal, UserTestData.USER_ID);
        Meal getMeal = service.get(INDEX_USER_3, UserTestData.USER_ID);
        MATCHER.assertEquals(meal, getMeal);
    }

    @Test
    public void testSave() throws Exception {
        Meal newMeal = new Meal(LocalDateTime.of(1989, 6, 5, 6, 0, 0), "FirstMeal", 200);
        int id = service.save(newMeal, UserTestData.ADMIN_ID).getId();
        Meal getMeal = service.get(id, UserTestData.ADMIN_ID);
        MATCHER.assertEquals(newMeal, getMeal);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteForeign() throws Exception {
        service.delete(INDEX_ADMIN_1, UserTestData.USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testGetForeign() throws Exception {
        service.get(INDEX_USER_2, UserTestData.ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateForeign() throws Exception {
        Meal meal = service.get(INDEX_USER_5, UserTestData.USER_ID);
        meal.setCalories(100500);
        meal.setDescription(meal.getDescription()+"suffix");
        meal.setId(INDEX_USER_5);
        service.update(meal, UserTestData.ADMIN_ID);
    }
}