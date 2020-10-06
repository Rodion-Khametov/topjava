package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealsDao {

    private final Map<Integer, Meal> mealMap = new HashMap<>();
    private int count = 1;

    public MealsDao() {
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

    }

    public synchronized void add(Meal meal) {
        if (meal != null) {
            meal.setId(count);
            mealMap.put(count, meal);
            count++;
        }
    }

    public synchronized void update(int id, Meal meal) {
        if (meal != null) {
            mealMap.put(id, meal);
        }
    }

    public synchronized void delete(int id) {
        mealMap.remove(id);
    }

    public List<Meal> getAllMeals() {
        List<Meal> meals = new ArrayList<>();
        for (Map.Entry<Integer, Meal> entry : mealMap.entrySet()) {
            meals.add(entry.getValue());
        }
        return meals;
    }

    public Meal getMealById(int mealId) {
        return mealMap.get(mealId);
    }


}
