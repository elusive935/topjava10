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

    public Meal save(Meal meal){
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
        sDate = parseDate(startDate, LocalDate.MIN);
        eDate = parseDate(endDate, LocalDate.MAX);

        final LocalTime sTime = parseTime(startTime, LocalTime.MIN);
        final LocalTime eTime = parseTime(endTime, LocalTime.MAX);

        return MealsUtil.getFilteredWithExceeded(service.getFiltered(AuthorizedUser.id(), sDate, eDate), sTime, eTime,
                                                                MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    private LocalDate parseDate(String date, LocalDate defaultValue){
        LocalDate result;
        try {
            result = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            result = defaultValue;
        }
        return result;
    }

    private LocalTime parseTime(String time, LocalTime defaultValue){
        LocalTime result;
        try {
            result = LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            result = defaultValue;
        }
        return result;
    }
}