package com.starlight.hotelmanagement.dao;

import com.starlight.hotelmanagement.model.Room;
import com.starlight.hotelmanagement.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    
    // Método para inserir um novo quarto
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

    // Método para atualizar um quarto
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
    
      // Cancela uma reserva
    public boolean updateRoomStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, roomId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atulizar status do quarto: " + e.getMessage());
        }
        return false;
    }

    // Método para deletar um quarto
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

    // Método para buscar quarto por ID
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

    // Método para buscar quarto por número
    public Integer findIdByNumber(int roomNumber) {
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
                return room.getId();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar quarto por número: " + e.getMessage());
        }
        return null;
    }

    // Método para buscar quarto por número
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
    
    // Método para buscar preço por número do quarto
    public Double findPriceByNumber(int roomNumber) {
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
                return room.getPrice();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar preço do quarto por número: " + e.getMessage());
        }
        return null;
    }

    // Método para listar todos os quartos
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

    // Método para listar quartos por status
    public List<Integer> findRoomNumbersByStatus(String status) {
    String sql = "SELECT number FROM rooms WHERE status = ?";
    List<Integer> roomNumbers = new ArrayList<>();
    
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, status);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            roomNumbers.add(rs.getInt("number"));
        }
    } catch (SQLException e) {
        System.err.println("Erro ao buscar números de quartos por status: " + e.getMessage());
    }
    return roomNumbers;
}

    // Método para verificar se um número de quarto já existe
    public boolean roomNumberExists(int roomNumber) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE number = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar número do quarto: " + e.getMessage());
        }
        return false;
    }
    
    // Método para buscar quarto por ID
    public List<Room> findByStatus(String status) {
        String sql = "SELECT * FROM rooms WHERE status = ?";
        List<Room> rooms = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
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
            System.err.println("Erro ao buscar quarto por Status: " + e.getMessage());
        }
        return null;
    }
}