package com.starlight.hotelmanagement.dao;

import com.starlight.hotelmanagement.model.Guest;
import com.starlight.hotelmanagement.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com hóspedes.
 */
public class GuestDAO {

    /**
     * Insere um novo hóspede no banco de dados.
     *
     * @param guest O hóspede a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean insert(Guest guest) {
        String sql = "INSERT INTO guests (name, document, phone, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getDocument());
            stmt.setString(3, guest.getPhone());
            stmt.setString(4, guest.getEmail());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) guest.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir hóspede: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lista todos os hóspedes cadastrados no banco de dados.
     * @return Lista de hóspedes.
     */
    public List<Guest> findAll() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Guest guest = new Guest();
                guest.setId(rs.getInt("id"));
                guest.setName(rs.getString("name"));
                guest.setDocument(rs.getString("document"));
                guest.setPhone(rs.getString("phone"));
                guest.setEmail(rs.getString("email"));
                guests.add(guest);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar hóspedes: " + e.getMessage());
        }
        return guests;
    }

    /**
     * Encontra hóspede pelo ID.
     * @param id
     * @return Objeto Guest ou null se não encontrado.
     */
    public Guest findById(int id) {
        String sql = "SELECT * FROM guests WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Guest guest = new Guest();
                guest.setId(rs.getInt("id"));
                guest.setName(rs.getString("name"));
                guest.setDocument(rs.getString("document"));
                guest.setPhone(rs.getString("phone"));
                guest.setEmail(rs.getString("email"));
                return guest;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar hóspede: " + e.getMessage());
        }
        return null;
    }
}