package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
//            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
//            adminUserController.create(new User(null, "userName", "email", "password", Role.ROLE_ADMIN));
            MealRestController controller = appCtx.getBean(MealRestController.class);
//            System.out.println(controller.save(2, new Meal(LocalDateTime.now(), "desc", 1000, 1)));
//            System.out.println(controller.delete(1, 2));
//            System.out.println(controller.getAll(1));
            System.out.println(controller.getFiltered(LocalDate.of(2015, Month.MAY, 30), LocalDate.of(2015, Month.MAY, 31), LocalTime.of(0, 0), LocalTime.of(23, 59)));



            AdminRestController adminRestController = appCtx.getBean(AdminRestController.class);
            System.out.println(adminRestController.getAll());
            System.out.println(adminRestController.getByMail("email2"));
        }
    }
}
