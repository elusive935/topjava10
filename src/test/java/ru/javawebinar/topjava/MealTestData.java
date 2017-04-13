package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

public class MealTestData {

    public static final ModelMatcher<Meal> MATCHER = new ModelMatcher<>();
    public static final int INDEX_USER_1 = START_SEQ + 2;
    public static final int INDEX_USER_2 = START_SEQ + 3;
    public static final int INDEX_USER_3 = START_SEQ + 4;
    public static final int INDEX_USER_4 = START_SEQ + 5;
    public static final int INDEX_USER_5 = START_SEQ + 6;
    public static final int INDEX_ADMIN_1 = START_SEQ + 7;
    public static final int INDEX_ADMIN_2 = START_SEQ + 8;

    public static final Meal MEAL_USER_1 = new Meal(LocalDateTime.of(2017, 4, 10, 8, 0, 0), "Breakfast", 500);
    public static final Meal MEAL_USER_2 = new Meal(LocalDateTime.of(2017, 4, 10, 13, 0, 0), "Lunch", 1000);
    public static final Meal MEAL_USER_3 = new Meal(LocalDateTime.of(2017, 4, 10, 20, 0, 0), "Dinner", 800);
    public static final Meal MEAL_USER_4 = new Meal(LocalDateTime.of(2017, 4, 11, 8, 0, 0), "Breakfast", 500);
    public static final Meal MEAL_USER_5 = new Meal(LocalDateTime.of(2017, 4, 11, 13, 0, 0), "Lunch", 1200);

    public static final Meal MEAL_ADMIN_1 = new Meal(LocalDateTime.of(2017, 4, 11, 8, 0, 0), "Breakfast", 550);
    public static final Meal MEAL_ADMIN_2 = new Meal(LocalDateTime.of(2017, 4, 11, 13, 0, 0), "Lunch", 850);

    {
        MEAL_USER_1.setId(INDEX_USER_1);
        MEAL_USER_2.setId(INDEX_USER_2);
        MEAL_USER_3.setId(INDEX_USER_3);
        MEAL_USER_4.setId(INDEX_USER_4);
        MEAL_USER_5.setId(INDEX_USER_5);
        MEAL_ADMIN_1.setId(INDEX_ADMIN_1);
        MEAL_ADMIN_2.setId(INDEX_ADMIN_2);
    }

}
