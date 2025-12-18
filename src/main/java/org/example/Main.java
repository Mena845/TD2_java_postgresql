package org.example;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Connection Successful!");
        } catch (SQLException e) {
            System.out.println("Connection Failed");
        }
    }

}
