package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.MealsDao;
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
    private Dao dao;

    @Override
    public void init() throws ServletException {
        super.init();
        dao = new MealsDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("date"), formatter);
        Meal meal = new Meal(dateTime, request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
        String mealId = request.getParameter("id");
        if (mealId == null || mealId.isEmpty()) {
            dao.add(meal);
        } else {
            meal.setId(Integer.parseInt(mealId));
            dao.update(meal.getId(), meal);
        }
        request.setAttribute("mealTos", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String forward = "";
        String action = request.getParameter("action");
        if (action == null) {
            forward = LIST_MEAL;
            request.setAttribute("mealTos", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
        } else {
            switch (action) {
                case "delete":
                    int mealId = Integer.parseInt(request.getParameter("id"));
                    dao.delete(mealId);
                    request.setAttribute("mealTos", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
                    break;
                case "edit":
                    forward = INSERT_OR_EDIT;
                    mealId = Integer.parseInt(request.getParameter("id"));
                    Meal meal = dao.getById(mealId);
                    request.setAttribute("meal", meal);
                    break;
                case "insert":
                    forward = INSERT_OR_EDIT;
                    break;
            }
        }
        if ("delete".equals(action)) {
            response.sendRedirect("meals");
        } else {
            request.getRequestDispatcher(forward).forward(request, response);
        }
    }
}
