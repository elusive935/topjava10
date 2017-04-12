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
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
public class MealRestController {
    private static final Logger LOG = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

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

        final LocalTime sTime = DateTimeUtil.parseTime(startTime, LocalTime.MIN);
        final LocalTime eTime = DateTimeUtil.parseTime(endTime, LocalTime.MAX);

        return MealsUtil.getFilteredWithExceeded(service.getFiltered(AuthorizedUser.id(), sDate, eDate), sTime, eTime,
                                                                AuthorizedUser.getCaloriesPerDay());
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


}