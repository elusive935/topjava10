package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    @PostMapping(value = "/filter")
    protected String filterMeals(HttpServletRequest request) throws ServletException, IOException {
        LocalDate startDate = DateTimeUtil.parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = DateTimeUtil.parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = DateTimeUtil.parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = DateTimeUtil.parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return ("meals");
    }

    @GetMapping
    protected String getMeals(HttpServletRequest request) throws ServletException, IOException {
        request.setAttribute("meals", getAll());
        return ("meals");
    }

    @GetMapping(value = "/create")
    protected String createMeal(HttpServletRequest request) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        request.setAttribute("meal", meal);
        return ("meal");
    }

    @GetMapping(value = "/update/{id}")
    protected String updateMeal(@PathVariable int id, HttpServletRequest request) {
        final Meal meal = get(id);
        request.setAttribute("meal", meal);
        return ("meal");
    }

    @PostMapping(value = "/save")
    protected String saveMeal(HttpServletRequest request) throws ServletException, IOException {
        final Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        if (request.getParameter("id").isEmpty()) {
            create(meal);
        } else {
            update(meal, getId(request));
        }
        return ("redirect:/meals");
    }

    @GetMapping(value = "/delete/{id}")
    protected String deleteMeal(@PathVariable int id) {
        delete(id);
        return ("redirect:/meals");
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
