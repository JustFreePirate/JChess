<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Main page</title>
        <script src="//code.jquery.com/jquery-1.12.0.min.js"></script>




    </head>
    <body>
        <div>
            ${sessionScope.user_profile.login}
        </div>
    </body>
</html>