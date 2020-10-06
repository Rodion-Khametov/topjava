package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsDao implements Dao {

    private final ConcurrentHashMap<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    private final AtomicInteger count = new AtomicInteger();

    public MealsDao() {
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

    }

    public Meal add(Meal meal) {
        if (meal != null) {
            meal.setId(count.intValue());
            mealMap.put(count.intValue(), meal);
            count.incrementAndGet();
        }
        return meal;
    }

    public Meal update(int id, Meal meal) {
        if (meal != null) {
            mealMap.put(id, meal);
        }
        return meal;
    }

    public void delete(int id) {
        mealMap.remove(id);
    }

    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }

    public Meal getById(int mealId) {
        return mealMap.get(mealId);
    }
}
