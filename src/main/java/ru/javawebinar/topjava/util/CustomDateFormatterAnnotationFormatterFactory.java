package ru.javawebinar.topjava.util;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class CustomDateFormatterAnnotationFormatterFactory implements AnnotationFormatterFactory<CustomDateFormatter> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        HashSet<Class<?>> fieldTypes = new HashSet<>();
        fieldTypes.add(LocalDateTime.class);
        return fieldTypes;
    }

    @Override
    public Printer<?> getPrinter(CustomDateFormatter annotation, Class<?> fieldType) {
        return new CustomFormatter();
    }

    @Override
    public Parser<?> getParser(CustomDateFormatter annotation, Class<?> fieldType) {
        return new CustomFormatter();
    }

    private static class CustomFormatter implements Formatter<LocalDateTime> {

        private DateTimeFormatter formatter;

        public CustomFormatter() {
            this.formatter = ISO_LOCAL_DATE_TIME;
        }
        
        @Override
        public LocalDateTime parse(String text, Locale locale) throws ParseException {
            return LocalDateTime.parse(text, formatter);
        }

        @Override
        public String print(LocalDateTime object, Locale locale) {
            return object.format(formatter);
        }
    }

}
