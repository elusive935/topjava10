package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    public Meal save(Meal meal){
        return service.save(AuthorizedUser.id(), meal);
    }

    public boolean delete(int id){
        return service.delete(AuthorizedUser.id(), id);
    }

    public Meal get(int id){
        return service.get(AuthorizedUser.id(), id);
    }

    public List<MealWithExceed> getAll(){
        return getFiltered(null, null, null, null);
    }

    public List<MealWithExceed> getFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime){
        if(startDate == null) {
            startDate = LocalDate.MIN;
        }
        if (startTime == null) {
            startTime = LocalTime.MIN;
        }
        if (endDate == null) {
            endDate = LocalDate.MAX;
        }
        if (endTime == null) {
            endTime = LocalTime.MAX;
        }
        return MealsUtil.getWithExceeded(service.getFiltered(AuthorizedUser.id(), startDate, endDate, startTime, endTime), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }
}