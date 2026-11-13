package com.university.controller;

import com.university.dao.StudentDAO;
import com.university.model.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/student")
public class StudentController extends HttpServlet {

    private StudentDAO studentDAO;

    // ===== EXERCISE 1 - PART 2: VALIDATION PATTERNS (NEW) =====
    private static final String STUDENT_CODE_PATTERN = "[A-Z]{2,6}IU\\d{5}";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    // ===== END EXERCISE 1 - PART 2 =====

    @Override
    public void init() {
        // Initialize DAO once when servlet is created
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Get action parameter (default to "list")
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listStudents(request, response);

                    break;
                case "view":
                    viewStudent(request, response);
                    break;
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                // ===== EXERCISE 1 - PART 3: DELETE CONFIRMATION (NEW) =====
                case "confirmDelete":
                    showDeleteConfirmation(request, response);
                    break;
                // ===== END EXERCISE 1 - PART 3 =====
                default:
                    listStudents(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("insert".equals(action)) {
                insertStudent(request, response);
            } else if ("update".equals(action)) {
                updateStudent(request, response);
                // ===== EXERCISE 1 - PART 3: DELETE BY POST (NEW) =====
            } else if ("delete".equals(action)) {
                deleteStudent(request, response);
                // ===== END EXERCISE 1 - PART 3 =====
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    // List all students
    private void listStudents(HttpServletRequest request,
                              HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        // ===== EXERCISE 2 - PART 3: SEARCH PARAMETER (NEW) =====
        String search = request.getParameter("search");
        search = search == null ? "" : search.trim();
        // ===== END EXERCISE 2 - PART 3 =====

        // ===== EXERCISE 2 - PART 1: PAGINATION (NEW) =====
        int page = 1;
        int pageSize = 5;

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }
        // ===== END EXERCISE 2 - PART 1 =====

        // ===== EXERCISE 2 - PART 2: SORTING + WHITELIST (NEW) =====
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String sortColumn;

        if (sort == null) {
            sort = "studentCode";
        }

        switch (sort) {
            // ===== USER REQUEST: ID SORTING (NEW) =====
            case "id":
                sortColumn = "id";
                break;
            // ===== END USER REQUEST: ID SORTING =====
            case "name":
                sortColumn = "full_name";
                break;
            case "email":
                sortColumn = "email";
                break;
            case "major":
                sortColumn = "major";
                break;
            case "studentCode":
            default:
                sort = "studentCode";
                sortColumn = "student_code";
                break;
        }

        order = "desc".equalsIgnoreCase(order) ? "desc" : "asc";
        // ===== END EXERCISE 2 - PART 2 =====

        // ===== EXERCISE 2 - PART 3: COMBINED QUERY (NEW) =====
        int totalStudents = studentDAO.countStudents(search);
        int totalPages = (int) Math.ceil((double) totalStudents / pageSize);
        totalPages = Math.max(totalPages, 1);

        if (page < 1) {
            page = 1;
        } else if (page > totalPages) {
            page = totalPages;
        }

        List<Student> students = studentDAO.getStudents(
                search, page, pageSize, sortColumn, order);

        request.setAttribute("students", students);
        request.setAttribute("search", search);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("sort", sort);
        request.setAttribute("order", order);
        // ===== END EXERCISE 2 - PART 3 =====

        request.getRequestDispatcher("/WEB-INF/views/student-list.jsp")
                .forward(request, response);
    }

    // View single student
    private void viewStudent(HttpServletRequest request,

                                     HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Student student = studentDAO.getStudentById(id);
        request.setAttribute("student", student);
        request.getRequestDispatcher("/WEB-INF/views/student-detail.jsp")
                .forward(request, response);
    }

    // Show new student form
    private void showNewForm(HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/student-form.jsp")
                .forward(request, response);
    }

    // Show edit student form
    private void showEditForm(HttpServletRequest request,
                              HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Student student = studentDAO.getStudentById(id);
        request.setAttribute("student", student);
        request.getRequestDispatcher("/WEB-INF/views/student-form.jsp")
                .forward(request, response);
    }

    // ===== EXERCISE 1 - PART 3: DELETE CONFIRMATION (NEW) =====
    private void showDeleteConfirmation(HttpServletRequest request,
                                        HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Student student = studentDAO.getStudentById(id);
        request.setAttribute("student", student);
        request.getRequestDispatcher("/WEB-INF/views/student-delete-confirm.jsp")
                .forward(request, response);
    }
    // ===== END EXERCISE 1 - PART 3 =====

    // Insert new student
    private void insertStudent(HttpServletRequest request,
                               HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        // ===== EXERCISE 1 - PART 2: SERVER-SIDE VALIDATION (NEW) =====
        studentCode = studentCode == null ? "" : studentCode.trim();
        fullName = fullName == null ? "" : fullName.trim();
        email = email == null ? "" : email.trim();
        major = major == null ? "" : major;

        List<String> errors = new ArrayList<>();

        if (!studentCode.matches(STUDENT_CODE_PATTERN)) {
            errors.add("Student code must match [A-Z]{2,6}IU followed by 5 digits.");
        } else if (studentDAO.studentCodeExists(studentCode)) {
            errors.add("Student code already exists.");
        }

        if (!email.matches(EMAIL_PATTERN)) {
            errors.add("Invalid email format.");
        } else if (studentDAO.emailExists(email)) {
            errors.add("Email already exists.");
        }

        Student student = new Student(studentCode, fullName, email, major);

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("student", student);
            request.getRequestDispatcher("/WEB-INF/views/student-form.jsp")
                    .forward(request, response);
            return;
        }
        // ===== END EXERCISE 1 - PART 2 =====

        studentDAO.insertStudent(student);

        response.sendRedirect("student?action=list");
    }

    // Update existing student
    private void updateStudent(HttpServletRequest request,
                               HttpServletResponse response)
            throws SQLException, IOException {


        int id = Integer.parseInt(request.getParameter("id"));
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        Student student = new Student(studentCode, fullName, email, major);
        student.setId(id);
        studentDAO.updateStudent(student);

        response.sendRedirect("student?action=list");
    }

    // ===== EXERCISE 1 - PART 3: DELETE BY POST + PRG (UPDATED) =====
    private void deleteStudent(HttpServletRequest request,
                               HttpServletResponse response)
            throws SQLException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        studentDAO.deleteStudent(id);

        response.sendRedirect("student?action=list");
    }
    // ===== END EXERCISE 1 - PART 3 =====
}
