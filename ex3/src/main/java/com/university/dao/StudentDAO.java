package com.university.dao;

import com.university.model.Student;
import java.sql.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    // ===== EXERCISE 3 - PART 2: STATISTICS QUERIES (NEW) =====
    public Map<String, Integer> countStudentsByMajor() throws SQLException {
        String sql = "SELECT major, COUNT(*) AS total FROM students " +
                "GROUP BY major ORDER BY major";
        Map<String, Integer> statistics = new LinkedHashMap<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                statistics.put(rs.getString("major"), rs.getInt("total"));
            }
        }
        return statistics;
    }

    public Map<String, Integer> countStudentsByMonthLastSixMonths()
            throws SQLException {
        String sql = "SELECT DATE_FORMAT(created_at, '%Y-%m') AS month, " +
                "COUNT(*) AS total FROM students " +
                "WHERE created_at >= DATE_FORMAT(" +
                "DATE_SUB(CURRENT_DATE, INTERVAL 5 MONTH), '%Y-%m-01') " +
                "GROUP BY DATE_FORMAT(created_at, '%Y-%m') ORDER BY month";
        Map<String, Integer> growthTrend = new LinkedHashMap<>();

        DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth currentMonth = YearMonth.now();
        for (int i = 5; i >= 0; i--) {
            growthTrend.put(currentMonth.minusMonths(i).format(monthFormat), 0);
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                growthTrend.put(rs.getString("month"), rs.getInt("total"));
            }
        }
        return growthTrend;
    }
    // ===== END EXERCISE 3 - PART 2 =====

    // ===== EXERCISE 3 - PART 3: BULK OPERATIONS (NEW) =====
    public List<Student> getStudentsByIds(List<Integer> ids) throws SQLException {
        List<Student> students = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return students;
        }

        String placeholders = String.join(",", java.util.Collections.nCopies(ids.size(), "?"));
        String sql = "SELECT * FROM students WHERE id IN (" + placeholders + ") " +
                "ORDER BY student_code";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < ids.size(); i++) {
                stmt.setInt(i + 1, ids.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(extractStudentFromResultSet(rs));
                }
            }
        }
        return students;
    }

    public boolean deleteStudents(List<Integer> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(DELETE_STUDENT)) {
                for (Integer id : ids) {
                    stmt.setInt(1, id);
                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } finally {
                    conn.close();
                }
            }
        }
    }
    // ===== END EXERCISE 3 - PART 3 =====

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
