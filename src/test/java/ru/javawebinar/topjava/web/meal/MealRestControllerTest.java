package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + (MEAL1_ID + 1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(MEAL2));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + (MEAL1_ID)))
                .andExpect(status().isOk())
                .andDo(print());
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), mealService.getAll(USER_ID));
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_WITH_EXCEED.contentListMatcher(MEALS_EXC));
    }

    @Test
    public void testCreateWithLocation() throws Exception {
        Meal expected = new Meal(LocalDateTime.now(), "description", 2000);
        ResultActions resultActions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)));

        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Meal actual = MATCHER.fromJsonAction(resultActions);
        expected.setId(actual.getId());

        MATCHER.assertEquals(expected, actual);
        MATCHER.assertCollectionEquals(Arrays.asList(expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), mealService.getAll(USER_ID));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = MEAL5;
        updated.setCalories(1999);
        updated.setDescription("UpdatedMeal");
        ResultActions resultActions = mockMvc.perform(put(REST_URL + (MEAL1_ID + 4))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)));

        resultActions.andExpect(status().isOk());

        MATCHER.assertEquals(updated, mealService.get(MEAL1_ID + 4, USER_ID));
    }

    @Test
    public void testGetBetween() throws Exception {
        mockMvc.perform(get(REST_URL + "filter")
                .contentType(MediaType.APPLICATION_JSON)
                .param("start", "2015-05-31 13:00:00")
                .param("end", "2015-05-31 20:00:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_WITH_EXCEED.contentListMatcher(Arrays.asList(MEAL6_exc, MEAL5_exc)));
    }

}