package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsMapDao implements MealsDao {

    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    private final AtomicInteger count = new AtomicInteger();

    public MealsMapDao() {
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
            meal.setId(count.incrementAndGet());
            mealMap.put(meal.getId(), meal);
        }
        return meal;
    }

    public Meal update(Meal meal) {
        if (meal != null) {
            mealMap.replace(meal.getId(), meal);
        } else {
            return null;
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
