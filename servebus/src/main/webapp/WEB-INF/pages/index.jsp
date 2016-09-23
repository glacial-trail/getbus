<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>GetBus</title>




</head>

<body>

<div>


        <h1>GetBus.info</h1>
        <p class="lead">GetBus - билетики онлайн.</p>
        <sec:authorize access="!isAuthenticated()">
            <p><a href="<c:url value="/login" />">Войти</a></p>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <p>Ваш логин: <sec:authentication property="principal.username" /></p>
            <p><a href="<c:url value="/logout" />">Выйти</a></p>

        </sec:authorize>


    <div>
        <p>&copy; GetBus 2016</p>
    </div>

</div>
</body>
</html>
