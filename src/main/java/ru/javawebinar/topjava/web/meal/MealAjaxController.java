package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/ajax/meals")
public class MealAjaxController extends AbstractMealController {

    @PostMapping
    public void createOrUpdate(@RequestParam("id") int id,
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("datetime") String dateTime,
                               @RequestParam("description") String description,
                               @RequestParam("calories") int calories){

    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id){
        super.delete(id);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getAll(){
        return super.getAll();
    }

}
