package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealsDao;
import ru.javawebinar.topjava.dao.MealsMapDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    public static final Logger log = getLogger(MealServlet.class);
    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private MealsDao mealsDao;

    @Override
    public void init() {
        mealsDao = new MealsMapDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("date"), formatter);
        Meal meal = new Meal(dateTime, request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
        String mealId = request.getParameter("id");
        if (mealId == null || mealId.isEmpty()) {
            mealsDao.add(meal);
        } else {
            meal.setId(Integer.parseInt(mealId));
            mealsDao.update(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String forward = "";
        String action = request.getParameter("action");
        if (action == null) {
            forward = LIST_MEAL;
            request.setAttribute("mealTos", MealsUtil.filteredByStreams(mealsDao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
        } else {
            switch (action) {
                case "delete":
                    int mealId = Integer.parseInt(request.getParameter("id"));
                    mealsDao.delete(mealId);
                    response.sendRedirect("meals");
                    break;
                case "edit":
                    forward = INSERT_OR_EDIT;
                    mealId = Integer.parseInt(request.getParameter("id"));
                    Meal meal = mealsDao.getById(mealId);
                    request.setAttribute("meal", meal);
                    break;
                case "insert":
                    forward = INSERT_OR_EDIT;
                    break;
                default:
                    forward = LIST_MEAL;
                    break;
            }
        }
        if (!forward.isEmpty())
            request.getRequestDispatcher(forward).forward(request, response);
    }
}
