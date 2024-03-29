package com.epam.library.db;

import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


@Component("dbConnectionProvider")
public class DBConnectionProvider {

    private String dbUrl;
    private String username;
    private String password;
    private String dbDriverName;

    public static Connection connection;
    private volatile static DBConnectionProvider instance;

    private DBConnectionProvider() {
        try {
            loadProperties();
            Class.forName(dbDriverName);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("C:\\Users\\Admin\\IdeaProjects\\Library\\src\\main\\resources\\db-config.properties"));

        dbUrl = properties.getProperty("db.source.url");
        username = properties.getProperty("db.source.username");
        password = properties.getProperty("db.source.password");
        dbDriverName = properties.getProperty("db.source.driverClass");

    }

    public static DBConnectionProvider getInstance() {
        if (instance == null) {
            synchronized (DBConnectionProvider.class) {
                if (instance == null) {
                    instance = new DBConnectionProvider();
                }
            }
        }
        return instance;

    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(dbUrl, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
