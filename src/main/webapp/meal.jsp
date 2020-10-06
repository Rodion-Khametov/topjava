<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="POST" action='meals' name="frmAddUser">
    ID : <input
        type="text" name="ID"
        value="<c:out value="${meal.id}" />"/> <br/>
    Date : <input
        type="datetime-local" name="date"
        value="<c:out value="${meal.dateTime}" />"/> <br/>
    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.description}" />"/> <br/>
    Calories : <input
        type="text" name="calories"
        value="<c:out value="${meal.calories}" />"/> <br/>
    <p><input type="submit" value="Save"/>
        <a href="meals?action=listMeal">
            <button type="button" class="swd-button">Cancel</button>
        </a></p>
</form>
</body>
</html>
