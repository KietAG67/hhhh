<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirm Delete</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <%-- ===== EXERCISE 1 - PART 3: CONFIRMATION PAGE (NEW) ===== --%>
        <h1>Confirm Delete</h1>

        <div>
            <p>Are you sure you want to delete this student?</p>

            <table class="table">
                <tr>
                    <th>Student Code</th>
                    <td><c:out value="${student.studentCode}"/></td>
                </tr>
                <tr>
                    <th>Full Name</th>
                    <td><c:out value="${student.fullName}"/></td>
                </tr>
                <tr>
                    <th>Email</th>
                    <td><c:out value="${student.email}"/></td>
                </tr>
                <tr>
                    <th>Major</th>
                    <td><c:out value="${student.major}"/></td>
                </tr>
            </table>

            <form method="post" action="student?action=delete" class="form-actions">
                <input type="hidden" name="id" value="${student.id}">
                <button type="submit" class="btn btn-danger">Confirm Delete</button>
                <a href="student?action=list" class="btn btn-secondary">Cancel</a>
            </form>
        </div>
        <%-- ===== END EXERCISE 1 - PART 3 ===== --%>
    </div>
</body>
</html>
