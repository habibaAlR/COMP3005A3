import java.sql.*;

public class Main {
    static Connection conn;

    /**
     * Constructor to initialize the connection object to utilize it within any method
     */
    public Main() {
        // JDBC & Database credentials
        String url = "jdbc:postgresql://localhost:5432/COMP3005A3Database";
        String user = "";
        String password = "";
        try { // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Connect to the database
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        new Main();
        if (conn != null) {
            System.out.println("Connected to PostgreSQL successfully!");
            // call to methods goes here
//            addStudent("Bob", "Williams","bob@gmail.com", "2023-09-06");
//            addStudent("Rose", "Roberts","rose@gmail.com", "2023-09-02");
//            updateStudentEmail(3, "testemail@example.com");
//            deleteStudent(3);
//            deleteStudent(4);
            getAllStudents();
        } else {
            System.out.println("Failed to establish connection.");
        } // Close the connection (in a real scenario, do this in a finally
    }

    /**
     * Retrieves and displays all records from the students table.
     */
    static void getAllStudents() {
        try {
            Statement statement = conn.createStatement();
            statement.executeQuery("SELECT * FROM students " +
                    "ORDER BY student_id;");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()){
                System.out.print(resultSet.getInt("student_id") + " \t");
                System.out.print(resultSet.getString("first_name") + " \t");
                System.out.print(resultSet.getString("last_name") + " \t");
                System.out.print(resultSet.getString("email") + " \t");
                System.out.print(resultSet.getDate("enrollment_date") + " \t");
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new student record into the students table.
     * @param first_name - first name of new student
     * @param last_name - last name of new student
     * @param email - email of new student
     * @param enrollment_date - date that new student enrolled
     */
    static void addStudent(String first_name, String last_name, String email, String enrollment_date) {
        try {
            String insertSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES " +
                    "(?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)){
                pstmt.setString(1,first_name);
                pstmt.setString(2,last_name);
                pstmt.setString(3,email);
                pstmt.setDate(4, Date.valueOf(enrollment_date));
                pstmt.executeUpdate();
                System.out.println("New student added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the email address for a student with the specified student_id.
     * @param student_id - the student that requires an email update
     * @param new_email - to replace the old email
     */
    static void updateStudentEmail(int student_id, String new_email) {
        try {
            String updateSQL = "UPDATE students " +
                    "SET email=? " +
                    "WHERE student_id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)){
                pstmt.setString(1,new_email);
                pstmt.setInt(2,student_id);
                pstmt.executeUpdate();
                System.out.println("Student email updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the record of the student with the specified student_id.
     * @param student_id - student to be deleted
     */
    static void deleteStudent(int student_id) {
        try {
            String deleteSQL = "DELETE FROM students WHERE student_id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)){
                pstmt.setInt(1,student_id);
                pstmt.executeUpdate();
                System.out.println("Student deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}