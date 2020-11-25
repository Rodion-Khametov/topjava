package ru.javawebinar.topjava.util.TimeFormatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class LocalTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<LocalTimeFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        Set<Class<?>> setTypes = new HashSet<Class<?>>();
        setTypes.add(LocalTime.class);
        return setTypes;
    }

    @Override
    public Printer<?> getPrinter(LocalTimeFormat localTimeFormat, Class<?> aClass) {
        return new LocalTimeFormatter();
    }

    @Override
    public Parser<?> getParser(LocalTimeFormat localTimeFormat, Class<?> aClass) {
        return new LocalTimeFormatter();
    }
}
