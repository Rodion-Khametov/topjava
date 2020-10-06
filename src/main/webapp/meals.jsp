<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<HTML>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table BORDER="1" bgcolor="#FFFFFF">
    <thead>
    <th>Date</th>
    <th>Description</th>
    <th>Calories</th>
    <th colspan=2>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${mealTos}" var="meal">
        <style>
            .green {
                color: green
            }

            .red {
                color: red
            }
        </style>
        <tr class="${!meal.excess ? 'green': 'red'}">
            <td><fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
                <fmt:formatDate pattern="dd-MM-yyyy HH:mm" value="${parsedDateTime}"/></td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=insert">Add Meal</a></p>
</body>
</html>
