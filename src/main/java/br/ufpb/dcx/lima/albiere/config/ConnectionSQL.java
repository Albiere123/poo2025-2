package br.ufpb.dcx.lima.albiere.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSQL {

    private static final String URL = "jdbc:mysql://localhost:3306/quizPOO?useSSL=false&serverTimezone=UTC";
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro na conexão com o banco de dados: " + e.getMessage(), e);
        }
    }

}
