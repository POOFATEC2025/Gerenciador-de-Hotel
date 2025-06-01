package com.starlight.hotelmanagement.dao;

import com.starlight.hotelmanagement.model.Room;
import com.starlight.hotelmanagement.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelas operações de acesso a dados de quartos.
 */
public class RoomDAO {
    
    /**
     * Cria um novo quarto no banco de dados.
     * @param room Quarto a ser criado.
     * @return true se o quarto foi criado com sucesso, false caso contrário.
     */
    public boolean insert(Room room) {
        String sql = "INSERT INTO rooms (number, type, price, status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, room.getNumber());
            stmt.setString(2, room.getType());
            stmt.setDouble(3, room.getPrice());
            stmt.setString(4, room.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        room.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir quarto: " + e.getMessage());
        }
        return false;
    }

    /**
     * Atualiza as informações de um quarto no banco de dados.
     * @param room
     * @return true se o quarto foi atualizado com sucesso, false caso contrário.
     */
    public boolean update(Room room) {
        String sql = "UPDATE rooms SET number = ?, type = ?, price = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, room.getNumber());
            stmt.setString(2, room.getType());
            stmt.setDouble(3, room.getPrice());
            stmt.setString(4, room.getStatus());
            stmt.setInt(5, room.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar quarto: " + e.getMessage());
        }
        return false;
    }
    
    /**     
     * Deleta um quarto do banco de dados pelo ID.
     * @param roomId ID do quarto a ser deletado.
     * @return true se o quarto foi deletado com sucesso, false caso contrário.
     */
    public boolean delete(int roomId) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao deletar quarto: " + e.getMessage());
        }
        return false;
    }

    /**
     * Busca um quarto pelo ID.
     * @param roomId
     * @return Quarto encontrado ou null se não existir.
     */
    public Room findById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Room room = new Room(
                    rs.getInt("number"),
                    rs.getString("type"),
                    rs.getDouble("price"),
                    rs.getString("status")
                );

                room.setId(rs.getInt("id"));
                return room;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar quarto por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca um quarto pelo número.
     * @param roomNumber
     * @return Quarto encontrado ou null se não existir.
     */
    public Room findByNumber(int roomNumber) {
        String sql = "SELECT * FROM rooms WHERE number = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Room room = new Room(
                    rs.getInt("number"),
                    rs.getString("type"),
                    rs.getDouble("price"),
                    rs.getString("status")
                );

                room.setId(rs.getInt("id"));
                return room;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar quarto por número: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Lista todos os quartos do banco de dados.
     * @return Lista de quartos.
     */
    public List<Room> findAll() {
        String sql = "SELECT * FROM rooms";
        List<Room> rooms = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("number"),
                    rs.getString("type"),
                    rs.getDouble("price"),
                    rs.getString("status")
                );

                room.setId(rs.getInt("id"));
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar quartos: " + e.getMessage());
        }
        return rooms;
    }

    
}