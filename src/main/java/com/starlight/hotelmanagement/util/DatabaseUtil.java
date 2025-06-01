package com.starlight.hotelmanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utilitário para conexão e inicialização do banco de dados.
 */
public class DatabaseUtil {
    // Método para obter conexão com o SQLite
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/src/main/resources/db/hotel.db";
        // String url = "jdbc:sqlite:src/main/resources/db/hotel.db";
        return DriverManager.getConnection(url);
    }

    // Método para criar tabelas (initializeDB)
   public static void initializeDB() {
    String sqlUsers = """
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            email TEXT UNIQUE NOT NULL,
            password TEXT NOT NULL,
            role TEXT CHECK(role IN ('ADMIN', 'STAFF')))
        """;  // Parêntese fechado corretamente

    String sqlRooms = """
        CREATE TABLE IF NOT EXISTS rooms (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            number INTEGER UNIQUE NOT NULL,
            type TEXT NOT NULL,
            price REAL NOT NULL,
            status TEXT CHECK(status IN ('AVAILABLE', 'OCCUPIED', 'MAINTENANCE')))
        """;  // Parêntese extra removido

    String sqlReservations = """
        CREATE TABLE IF NOT EXISTS reservations (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER NOT NULL,
            room_id INTEGER NOT NULL,
            check_in TEXT NOT NULL,  -- Alterado para TEXT
            check_out TEXT NOT NULL, -- Alterado para TEXT
            status TEXT DEFAULT 'CONFIRMED',
            FOREIGN KEY (user_id) REFERENCES users(id),
            FOREIGN KEY (room_id) REFERENCES rooms(id))
        """;  // Adicionado IF NOT EXISTS

    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
        stmt.execute(sqlUsers);
        stmt.execute(sqlRooms);
        stmt.execute(sqlReservations);
        System.out.println("Tabelas criadas com sucesso!");
    } catch (SQLException e) {
        System.err.println("Erro ao criar tabelas: " + e.getMessage());
    }
}
}
