<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student List</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Student Management System</h1>

        <div class="toolbar">
            <a href="student?action=new" class="btn btn-primary">Add New Student</a>

            <%-- ===== EXERCISE 1 - PART 1: SEARCH (NEW) ===== --%>
            <form method="get" action="student" class="search-form">
                <input type="hidden" name="action" value="search">
                <input type="text" name="keyword" value="${keyword}"
                       placeholder="Student code or full name">
                <button type="submit" class="btn">Search</button>
                <a href="student?action=list" class="btn btn-secondary">Clear</a>
            </form>
            <%-- ===== END EXERCISE 1 - PART 1 ===== --%>
        </div>

        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Student Code</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Major</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${students}" var="student">
                    <tr>
                        <td>${student.id}</td>
                        <td>${student.studentCode}</td>
                        <td>${student.fullName}</td>
                        <td>${student.email}</td>
                        <td>${student.major}</td>
                        <td>
                            <a href="student?action=view&id=${student.id}" class="btn btn-sm">View</a>
                            <a href="student?action=edit&id=${student.id}" class="btn btn-sm">Edit</a>
                            <%-- ===== EXERCISE 1 - PART 3: DELETE CONFIRMATION (UPDATED) ===== --%>
                            <a href="student?action=confirmDelete&id=${student.id}"
                               class="btn btn-sm btn-danger">Delete</a>
                            <%-- ===== END EXERCISE 1 - PART 3 ===== --%>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <%-- ===== EXERCISE 1 - PART 1: SEARCH RESULT MESSAGE (NEW) ===== --%>
        <c:choose>
            <c:when test="${searchPerformed && empty students}">
                <p class="empty-message">No results found.</p>
            </c:when>
            <c:when test="${empty students}">
                <p class="empty-message">No students found.</p>
            </c:when>
        </c:choose>
        <%-- ===== END EXERCISE 1 - PART 1 ===== --%>
    </div>
</body>
</html>
