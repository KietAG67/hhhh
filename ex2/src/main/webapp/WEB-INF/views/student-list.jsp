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

            <%-- ===== EXERCISE 2 - PART 3: SEARCH + PAGINATION + SORT (UPDATED) ===== --%>
            <form method="get" action="student" class="search-form">
                <input type="hidden" name="action" value="list">
                <input type="hidden" name="sort" value="${sort}">
                <input type="hidden" name="order" value="${order}">
                <input type="text" name="search" value="${search}"
                       placeholder="Student code or full name">
                <button type="submit" class="btn">Search</button>
                <a href="student?action=list" class="btn btn-secondary">Clear</a>
            </form>
            <%-- ===== END EXERCISE 2 - PART 3 ===== --%>
        </div>

        <%-- ===== EXERCISE 2 - PART 2: SORTABLE HEADERS (NEW) ===== --%>
        <%-- ===== USER REQUEST: ID SORTING (NEW) ===== --%>
        <c:url var="idSortUrl" value="student">
            <c:param name="action" value="list"/>
            <c:param name="search" value="${search}"/>
            <c:param name="sort" value="id"/>
            <c:param name="order" value="${sort == 'id' && order == 'asc' ? 'desc' : 'asc'}"/>
            <c:param name="page" value="1"/>
        </c:url>
        <%-- ===== END USER REQUEST: ID SORTING ===== --%>
        <c:url var="studentCodeSortUrl" value="student">
            <c:param name="action" value="list"/>
            <c:param name="search" value="${search}"/>
            <c:param name="sort" value="studentCode"/>
            <c:param name="order" value="${sort == 'studentCode' && order == 'asc' ? 'desc' : 'asc'}"/>
            <c:param name="page" value="1"/>
        </c:url>
        <c:url var="nameSortUrl" value="student">
            <c:param name="action" value="list"/>
            <c:param name="search" value="${search}"/>
            <c:param name="sort" value="name"/>
            <c:param name="order" value="${sort == 'name' && order == 'asc' ? 'desc' : 'asc'}"/>
            <c:param name="page" value="1"/>
        </c:url>
        <c:url var="emailSortUrl" value="student">
            <c:param name="action" value="list"/>
            <c:param name="search" value="${search}"/>
            <c:param name="sort" value="email"/>
            <c:param name="order" value="${sort == 'email' && order == 'asc' ? 'desc' : 'asc'}"/>
            <c:param name="page" value="1"/>
        </c:url>
        <c:url var="majorSortUrl" value="student">
            <c:param name="action" value="list"/>
            <c:param name="search" value="${search}"/>
            <c:param name="sort" value="major"/>
            <c:param name="order" value="${sort == 'major' && order == 'asc' ? 'desc' : 'asc'}"/>
            <c:param name="page" value="1"/>
        </c:url>
        <%-- ===== END EXERCISE 2 - PART 2 ===== --%>

        <table class="table">
            <thead>
                <tr>
                    <%-- ===== USER REQUEST: ID SORTING (NEW) ===== --%>
                    <th>
                        <a href="${idSortUrl}">ID
                            <c:if test="${sort == 'id'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                        </a>
                    </th>
                    <%-- ===== END USER REQUEST: ID SORTING ===== --%>
                    <th>
                        <a href="${studentCodeSortUrl}">Student Code
                            <c:if test="${sort == 'studentCode'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                        </a>
                    </th>
                    <th>
                        <a href="${nameSortUrl}">Full Name
                            <c:if test="${sort == 'name'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                        </a>
                    </th>
                    <th>
                        <a href="${emailSortUrl}">Email
                            <c:if test="${sort == 'email'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                        </a>
                    </th>
                    <th>
                        <a href="${majorSortUrl}">Major
                            <c:if test="${sort == 'major'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                        </a>
                    </th>
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

        <%-- ===== EXERCISE 2 - PART 3: SEARCH RESULT MESSAGE (UPDATED) ===== --%>
        <c:choose>
            <c:when test="${not empty search && empty students}">
                <p class="empty-message">No results found.</p>
            </c:when>
            <c:when test="${empty students}">
                <p class="empty-message">No students found.</p>
            </c:when>
        </c:choose>
        <%-- ===== END EXERCISE 2 - PART 3 ===== --%>

        <%-- ===== EXERCISE 2 - PART 1: PAGINATION (NEW) ===== --%>
        <div class="pagination">
            <c:choose>
                <c:when test="${currentPage > 1}">
                    <c:url var="previousUrl" value="student">
                        <c:param name="action" value="list"/>
                        <c:param name="search" value="${search}"/>
                        <c:param name="sort" value="${sort}"/>
                        <c:param name="order" value="${order}"/>
                        <c:param name="page" value="${currentPage - 1}"/>
                    </c:url>
                    <a href="${previousUrl}" class="page-link">Previous</a>
                </c:when>
                <c:otherwise><span class="page-link disabled">Previous</span></c:otherwise>
            </c:choose>

            <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                <c:url var="pageUrl" value="student">
                    <c:param name="action" value="list"/>
                    <c:param name="search" value="${search}"/>
                    <c:param name="sort" value="${sort}"/>
                    <c:param name="order" value="${order}"/>
                    <c:param name="page" value="${pageNumber}"/>
                </c:url>
                <c:choose>
                    <c:when test="${pageNumber == currentPage}">
                        <span class="page-link active">${pageNumber}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageUrl}" class="page-link">${pageNumber}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:choose>
                <c:when test="${currentPage < totalPages}">
                    <c:url var="nextUrl" value="student">
                        <c:param name="action" value="list"/>
                        <c:param name="search" value="${search}"/>
                        <c:param name="sort" value="${sort}"/>
                        <c:param name="order" value="${order}"/>
                        <c:param name="page" value="${currentPage + 1}"/>
                    </c:url>
                    <a href="${nextUrl}" class="page-link">Next</a>
                </c:when>
                <c:otherwise><span class="page-link disabled">Next</span></c:otherwise>
            </c:choose>
        </div>
        <p class="page-info">Page ${currentPage} of ${totalPages}</p>
        <%-- ===== END EXERCISE 2 - PART 1 ===== --%>
    </div>
</body>
</html>
