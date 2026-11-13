<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirm Bulk Delete</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%-- ===== EXERCISE 3 - PART 3: BULK DELETE CONFIRMATION (NEW) ===== --%>
    <div class="container confirmation-container">
        <h1>Confirm Bulk Delete</h1>
        <p class="warning-message">
            Are you sure you want to delete the following students?
        </p>

        <table class="table">
            <thead>
                <tr>
                    <th>Student Code</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Major</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${students}" var="student">
                    <tr>
                        <td>${student.studentCode}</td>
                        <td>${student.fullName}</td>
                        <td>${student.email}</td>
                        <td>${student.major}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <form method="post" action="student" class="form-actions">
            <input type="hidden" name="action" value="bulkDelete">
            <c:forEach items="${students}" var="student">
                <input type="hidden" name="studentIds" value="${student.id}">
            </c:forEach>
            <button type="submit" class="btn btn-danger">Confirm Delete All</button>
            <a href="student?action=list" class="btn btn-secondary">Cancel</a>
        </form>
    </div>
    <%-- ===== END EXERCISE 3 - PART 3 ===== --%>
</body>
</html>
