package Coders.maven.project.javadb;

import com.mysql.cj.jdbc.Driver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTest {

    @BeforeAll
    static void beforeAll() {
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void testConnection() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/project_javadb";
        String username = "root";
        String password = "HSDgrc55##";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Sukses konek ke database");


        } catch (SQLException exception) {
            Assertions.fail(exception);
        }
    }

    @Test
    void testConnectionClose() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/project_javadb";
        String username ="root";
        String password = "HSDgrc55##";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {

            System.out.println("Sukses konek ke database");

        }catch (SQLException exception){
            Assertions.fail(exception);
        }
    }
}