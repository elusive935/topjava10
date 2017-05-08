package ru.javawebinar.topjava;

import org.springframework.beans.BeansException;

/**
 * Created by alena.nikiforova on 08.05.2017.
 */
public class IncorrectBeanException extends BeansException {

    public IncorrectBeanException(String msg) {
        super(msg);
    }
}
