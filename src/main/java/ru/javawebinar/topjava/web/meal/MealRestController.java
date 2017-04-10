package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MealRestController {
    private static final Logger LOG = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public Meal save(Map<String, String[]> params){
        String id = params.get("id")[0];
        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(params.get("dateTime")[0]),
                params.get("description")[0],
                Integer.valueOf(params.get("calories")[0]));
        LOG.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        return service.save(AuthorizedUser.id(), meal);
    }

    public boolean delete(int id){
        return service.delete(AuthorizedUser.id(), id);
    }

    public Meal get(int id){
        return service.get(AuthorizedUser.id(), id);
    }

    public List<MealWithExceed> getAll(){
        return getFiltered("", "", "", "");
    }

    public List<MealWithExceed> getFiltered(String startDate, String endDate, String startTime, String endTime){
        LocalDate sDate, eDate;
        LocalTime sTime, eTime;

        try {
            sDate = LocalDate.parse(startDate);
        } catch (DateTimeParseException e) {
            sDate = LocalDate.MIN;
        }
        try {
            eDate = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            eDate = LocalDate.MAX;
        }
        try {
            sTime = LocalTime.parse(startTime);
        } catch (DateTimeParseException e) {
            sTime = LocalTime.MIN;
        }
        try {
            eTime = LocalTime.parse(endTime);
        } catch (DateTimeParseException e) {
            eTime = LocalTime.MAX;
        }

        final LocalTime s = sTime;
        final LocalTime e = eTime;

        return MealsUtil.getWithExceeded(service.getFiltered(AuthorizedUser.id(), sDate, eDate),
                                            MealsUtil.DEFAULT_CALORIES_PER_DAY).stream()
                  .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime().toLocalTime(), s, e))
                  .collect(Collectors.toList());
    }
}