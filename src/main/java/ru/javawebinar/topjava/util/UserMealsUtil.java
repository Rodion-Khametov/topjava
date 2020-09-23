package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 11, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> cals = new HashMap<>();
        for (UserMeal userMeal : meals) {
            int curCalories = cals.getOrDefault(getDateOfMeal(userMeal), 0);
            cals.put(getDateOfMeal(userMeal), userMeal.getCalories() + curCalories);
        }
        List<UserMealWithExcess> mealWithExcessList = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(getTimeOfMeal(userMeal), startTime, endTime)) {
                UserMealWithExcess userMealWithExcess = createUserMealWithExcess(userMeal, cals.get(getDateOfMeal(userMeal)) > caloriesPerDay);
                mealWithExcessList.add(userMealWithExcess);
            }
        }
        return mealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> cals = meals.stream()
                .collect(Collectors.groupingBy(UserMealsUtil::getDateOfMeal, Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(getTimeOfMeal(userMeal), startTime, endTime))
                .map(userMeal -> createUserMealWithExcess(userMeal, cals.get(getDateOfMeal(userMeal)) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static UserMealWithExcess createUserMealWithExcess(UserMeal userMeal, boolean isExcess) {
        return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), isExcess);
    }

    public static LocalDate getDateOfMeal(UserMeal userMeal) {
        return userMeal.getDateTime().toLocalDate();
    }

    public static LocalTime getTimeOfMeal(UserMeal userMeal) {
        return userMeal.getDateTime().toLocalTime();
    }
}
