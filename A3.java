import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.print.DocFlavor.STRING;

import java.sql.ResultSet;

public class A3{

    static String url = "jdbc:postgresql://localhost:5432/a3";
    static String user = "postgres";
    static String password = "Oy6138183599!";
    
    // connect application to my postgresql database 
    public static void setUpDB(){
        
        try{
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            if(conn != null){
                System.out.println("Connected to PostgreSQL successfully!"); 
            }
            else{
                System.out.println("Connection failed"); 
            }
            conn.close(); 
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace(); 
        }
    }

    // get all students in the database
    public static void getAllStudents(){
        try(Connection conn = DriverManager.getConnection(url, user, password)){

            Statement stmt = conn.createStatement();
            // sql queries to retrieve info 
            String SQL = "SELECT * From students";

            ResultSet rs = stmt.executeQuery(SQL); 

            // retrieve data from each row and print it 
            while(rs.next()){
                int studentId = rs.getInt("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String enrollmentDate = rs.getString("enrollment_date");

                System.out.println("Student ID: " + studentId);
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Enrollment Date: " + enrollmentDate);
                System.out.println("---------------------------------------------");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    // add a new student
    public static void addStudent(String fname, String lname, String email, String enrollmentDate){
        try(Connection conn = DriverManager.getConnection(url, user, password)){
            // sql query to add new student
            String SQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(SQL);
            // add new student to table given the parameters
            preparedStatement.setString(1, fname);
            preparedStatement.setString(2, lname);
            preparedStatement.setString(3, email);
            preparedStatement.setDate(4, java.sql.Date.valueOf(enrollmentDate));
            int addedRows = preparedStatement.executeUpdate();

            // error handling -- checking if student was added or not
            if(addedRows > 0){
                System.out.println("New student added!");
            }
            else{
                System.out.println("Insertion failed!");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    // update email of an existing student given their id
    public static void updateStudent(int student_id, String new_email){
        try(Connection conn = DriverManager.getConnection(url, user, password)){
            // sql query to update student @ student_id's email 
            String SQL = "UPDATE students SET email = ? WHERE student_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(SQL);
            // update student email 
            preparedStatement.setString(1, new_email);
            preparedStatement.setInt(2, student_id);
            int updatedRows = preparedStatement.executeUpdate();

            // error handling -- checking if update was successful or not
            if(updatedRows > 0){
                System.out.println("Student: " + student_id + " email updated to: " + new_email);
            }
            else{
                System.out.println("Failed to update email for student: " + student_id);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    // delete student with the given student id
    public static void deleteStudent(int student_id){
        try(Connection conn = DriverManager.getConnection(url, user, password)){
            // sql query to delete student with id student_id 
            String SQL = "DELETE FROM students WHERE student_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(SQL);
            preparedStatement.setInt(1, student_id);
            int deletedRows = preparedStatement.executeUpdate();

            // error handling to check if student was deleted or not
            if(deletedRows > 0){
                System.out.println("Deleted student: " + student_id);
            }
            else{
                System.out.println("Student ID: " + student_id + " not found.");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        setUpDB();
        System.out.println("STUDENTS: ");
        getAllStudents();
        
    }

}