<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <%-- ===== STUDENT DETAIL PAGE (NEW) ===== --%>
        <h1>Student Details</h1>

        <table class="table">
            <tbody>
                <tr>
                    <th>ID</th>
                    <td><c:out value="${student.id}"/></td>
                </tr>
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
                <tr>
                    <th>Created At</th>
                    <td><c:out value="${student.createdAt}"/></td>
                </tr>
                <tr>
                    <th>Updated At</th>
                    <td><c:out value="${student.updatedAt}"/></td>
                </tr>
            </tbody>
        </table>

        <div class="form-actions">
            <a href="student?action=edit&amp;id=${student.id}"
               class="btn btn-primary">Edit</a>
            <a href="student?action=list" class="btn btn-secondary">
                Back to Student List
            </a>
        </div>
        <%-- ===== END STUDENT DETAIL PAGE ===== --%>
    </div>
</body>
</html>
