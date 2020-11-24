package ru.javawebinar.topjava.util.DateFormatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class LocalDateFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<LocalDateFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        Set<Class<?>> setTypes = new HashSet<Class<?>>();
        setTypes.add(LocalDate.class);
        return setTypes;
    }

    @Override
    public Printer<?> getPrinter(LocalDateFormat localDateFormat, Class<?> aClass) {
        return new LocalDateFormatter();
    }

    @Override
    public Parser<?> getParser(LocalDateFormat localDateFormat, Class<?> aClass) {
        return new LocalDateFormatter();
    }
}
