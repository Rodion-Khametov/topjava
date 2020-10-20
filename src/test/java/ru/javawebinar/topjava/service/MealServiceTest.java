package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal newMeal = MealTestData.getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        Meal standardMeal = MealTestData.getNew();
        standardMeal.setId(newId);
        assertMatch(created, standardMeal);
        assertMatch(service.get(newId, USER_ID), standardMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, userMeal1.getDateTime(),
                        "Duplicate", 1150), USER_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL1_ID, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotFoundWithUserId() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL1_ID, USER_ID);
        assertMatch(meal, userMeal1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotFoundWithUserId() {
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> allBetween = service.getBetweenInclusive(null,
                null, USER_ID);
        assertMatch(allBetween, userMeal5, userMeal4, userMeal3, userMeal2, userMeal1);
    }

    @Test
    public void getBetweenExclusive() {
        List<Meal> allBetween = service.getBetweenInclusive(
                LocalDate.of(1985, Month.JANUARY, 24),
                LocalDate.of(1998, Month.JULY, 30), USER_ID);
        assertNotMatch(allBetween, userMeal5, userMeal4, userMeal3, userMeal2, userMeal1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, userMeal5, userMeal4, userMeal3, userMeal2, userMeal1);
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(USER_MEAL1_ID, USER_ID), MealTestData.getUpdated());
    }

    @Test
    public void updateNotFoundWithUserId() {
        Meal updated = MealTestData.getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }
}