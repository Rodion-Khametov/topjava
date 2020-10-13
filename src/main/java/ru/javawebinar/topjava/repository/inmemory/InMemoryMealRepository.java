package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> userMealRepository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        MealsUtil.mealsForAdmin.forEach(meal -> save(meal, 2));
    }

    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            if (userMealRepository.get(userId) != null) {
                userMealRepository.get(userId).put(meal.getId(), meal);
            } else {
                userMealRepository.put(userId, new ConcurrentHashMap<Integer, Meal>() {{
                    put(meal.getId(), meal);
                }});
            }
            return meal;
        }
        // handle case: update, but not present in storage
        return checkNotFoundWithId(userMealRepository.get(userId), userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return checkNotFoundWithId(userMealRepository.get(userId), userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return checkNotFoundWithId(checkNotFoundWithId(userMealRepository.get(userId), userId).get(id), id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        if (userMealRepository.get(userId) == null) {
            userMealRepository.put(userId, new ConcurrentHashMap<>());
            return new ArrayList<>(userMealRepository.get(userId).values());
        } else {
            return userMealRepository.get(userId).values().stream()
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }
    }

    public List<Meal> getFilteredAll(int userId) {
        return getAll(userId).stream()
                .filter(meals -> DateTimeUtil.isBetweenHalfOpen(meals.getTime(), LocalTime.MIN, LocalTime.MAX))
                .collect(Collectors.toList());
    }
}

