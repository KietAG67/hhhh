package com.university.dao;

import com.university.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    // Database configuration
    // Thêm allowPublicKeyRetrieval=true và chỉnh đúng mật khẩu bạn đã đặt
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "kiet1234@"; // Hãy đổi thành "kiet1234@" nếu thực tế bạn đặt pass có dấu @

    // SQL queries
    private static final String SELECT_ALL =
            "SELECT * FROM students ORDER BY student_code";
    private static final String SELECT_BY_ID =

        "SELECT * FROM students WHERE id = ?";
    private static final String INSERT_STUDENT =
            "INSERT INTO students (student_code, full_name, email, major) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_STUDENT =
            "UPDATE students SET student_code=?, full_name=?, email=?, major=? WHERE id=?";
    private static final String DELETE_STUDENT =
            "DELETE FROM students WHERE id=?";

    // ===== EXERCISE 1 - PART 1: SEARCH (NEW) =====
    private static final String SEARCH_STUDENTS =
            "SELECT * FROM students " +
            "WHERE student_code LIKE ? OR full_name LIKE ? " +
            "ORDER BY student_code";
    // ===== END EXERCISE 1 - PART 1 =====

    // ===== EXERCISE 1 - PART 2: DUPLICATE CHECK (NEW) =====
    private static final String CHECK_STUDENT_CODE =
            "SELECT COUNT(*) FROM students WHERE student_code = ?";
    private static final String CHECK_EMAIL =
            "SELECT COUNT(*) FROM students WHERE email = ?";
    // ===== END EXERCISE 1 - PART 2 =====

    // Get database connection
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Get all students
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        }
        return students;
    }

    // ===== EXERCISE 2 - PART 1: PAGINATION (NEW) =====
    public List<Student> getStudents(int page, int pageSize) throws SQLException {
        List<Student> students = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM students ORDER BY student_code LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(extractStudentFromResultSet(rs));
                }
            }
        }
        return students;
    }

    public int countStudents() throws SQLException {
        String sql = "SELECT COUNT(*) FROM students";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    // ===== END EXERCISE 2 - PART 1 =====

    // ===== EXERCISE 2 - PART 2: SORTING (NEW) =====
    public List<Student> getStudents(int page, int pageSize,
                                     String sortColumn, String sortOrder)
            throws SQLException {

        List<Student> students = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM students ORDER BY " + sortColumn + " " +
                sortOrder + " LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(extractStudentFromResultSet(rs));
                }
            }
        }
        return students;
    }
    // ===== END EXERCISE 2 - PART 2 =====

    // ===== EXERCISE 2 - PART 3: SEARCH + PAGINATION + SORT (NEW) =====
    public List<Student> getStudents(String search, int page, int pageSize,
                                     String sortColumn, String sortOrder)
            throws SQLException {

        List<Student> students = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        boolean hasSearch = search != null && !search.isBlank();

        StringBuilder sql = new StringBuilder("SELECT * FROM students ");
        if (hasSearch) {
            sql.append("WHERE student_code LIKE ? OR full_name LIKE ? ");
        }
        sql.append("ORDER BY ").append(sortColumn).append(" ")
                .append(sortOrder).append(" LIMIT ? OFFSET ?");

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int parameterIndex = 1;
            if (hasSearch) {
                String searchValue = "%" + search + "%";
                stmt.setString(parameterIndex++, searchValue);
                stmt.setString(parameterIndex++, searchValue);
            }
            stmt.setInt(parameterIndex++, pageSize);
            stmt.setInt(parameterIndex, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(extractStudentFromResultSet(rs));
                }
            }
        }
        return students;
    }

    public int countStudents(String search) throws SQLException {
        boolean hasSearch = search != null && !search.isBlank();
        String sql = "SELECT COUNT(*) FROM students";

        if (hasSearch) {
            sql += " WHERE student_code LIKE ? OR full_name LIKE ?";
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (hasSearch) {
                String searchValue = "%" + search + "%";
                stmt.setString(1, searchValue);
                stmt.setString(2, searchValue);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
    // ===== END EXERCISE 2 - PART 3 =====

    // ===== EXERCISE 1 - PART 1: SEARCH (NEW) =====
    public List<Student> searchStudents(String keyword) throws SQLException {
        List<Student> students = new ArrayList<>();
        String searchValue = "%" + keyword + "%";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_STUDENTS)) {

            stmt.setString(1, searchValue);
            stmt.setString(2, searchValue);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(extractStudentFromResultSet(rs));
                }
            }
        }
        return students;
    }
    // ===== END EXERCISE 1 - PART 1 =====

    // ===== EXERCISE 1 - PART 2: DUPLICATE CHECK (NEW) =====
    public boolean studentCodeExists(String studentCode) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_STUDENT_CODE)) {

            stmt.setString(1, studentCode);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean emailExists(String email) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_EMAIL)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    // ===== END EXERCISE 1 - PART 2 =====

    // Get student by ID
    public Student getStudentById(int id) throws SQLException {
        Student student = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    student = extractStudentFromResultSet(rs);
                }
            }
        }
        return student;
    }


    // Insert new student
    public boolean insertStudent(Student student) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_STUDENT)) {

            stmt.setString(1, student.getStudentCode());
            stmt.setString(2, student.getFullName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getMajor());

            return stmt.executeUpdate() > 0;
        }
    }

    // Update existing student
    public boolean updateStudent(Student student) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STUDENT)) {

            stmt.setString(1, student.getStudentCode());
            stmt.setString(2, student.getFullName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getMajor());
            stmt.setInt(5, student.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Delete student
    public boolean deleteStudent(int id) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_STUDENT)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Helper method to extract Student from ResultSet
    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setStudentCode(rs.getString("student_code"));
        student.setFullName(rs.getString("full_name"));
        student.setEmail(rs.getString("email"));
        student.setMajor(rs.getString("major"));
        student.setCreatedAt(rs.getTimestamp("created_at"));
        student.setUpdatedAt(rs.getTimestamp("updated_at"));
        return student;

    }
}
