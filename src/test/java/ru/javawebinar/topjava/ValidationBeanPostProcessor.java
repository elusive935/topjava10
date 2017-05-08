package ru.javawebinar.topjava;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.repository.JpaUtil;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.lang.reflect.Field;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.Profiles.JPA;

@Component
public class ValidationBeanPostProcessor implements BeanPostProcessor {
    @Autowired
    private Environment environment;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.getSuperclass() == AbstractUserServiceTest.class) {
            String[] activeProfiles = environment.getActiveProfiles();
            boolean isProfileExpectToHaveField = false;
            for (String activeProfile : activeProfiles) {
                if (JPA.equals(activeProfile) || DATAJPA.equals(activeProfile)) {
                    isProfileExpectToHaveField = true;
                    break;
                }
            }
            if (isProfileExpectToHaveField) {
                Field[] fields = AbstractUserServiceTest.class.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType() == JpaUtil.class) {
                        try {
                            field.setAccessible(true);
                            if (field.get(bean) == null){
                                throw new IncorrectBeanException("Field JpaUtil is null in bean " + beanClass.getSimpleName());
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return bean;
    }
}
