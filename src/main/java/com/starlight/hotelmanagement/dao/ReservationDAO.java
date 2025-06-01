package com.starlight.hotelmanagement.dao;

import com.starlight.hotelmanagement.model.Reservation;
import com.starlight.hotelmanagement.model.Room;
// import com.starlight.hotelmanagement.model.Room;
import com.starlight.hotelmanagement.util.DatabaseUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelas operações de acesso a dados de reservas.
 */
public class ReservationDAO {

    /**
     * Cria uma nova reserva no banco de dados.
     * @param reservation Reserva a ser criada.
     * @return true se a reserva foi criada com sucesso, false caso contrário.
     */
    public boolean createReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (user_id, room_id, check_in, check_out, guest_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getRoomId());
            stmt.setString(3, reservation.getCheckIn());
            stmt.setString(4, reservation.getCheckOut());
            stmt.setInt(5, reservation.getGuestId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        reservation.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao criar reserva: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cancela uma reserva pelo ID.
     * @param reservationId ID da reserva a ser cancelada.
     * @return true se a reserva foi cancelada com sucesso, false caso contrário.
     */
    public boolean cancelReservation(int reservationId) {
        String sql = "UPDATE reservations SET status = 'CANCELLED' WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao cancelar reserva: " + e.getMessage());
        }
        return false;
    }

    /**
     * Busca todas as reservas de um usuário.
     * @param userId ID do usuário.
     * @return Lista de reservas do usuário.
     */
    public List<Reservation> getReservationsByUser(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setUserId(rs.getInt("user_id"));
                reservation.setRoomId(rs.getInt("room_id"));
                reservation.setCheckIn(rs.getString("check_in"));
                reservation.setCheckOut(rs.getString("check_out"));
                reservation.setStatus(rs.getString("status"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar reservas: " + e.getMessage());
        }
        return reservations;
    }

    /**
     * Verifica se um quarto está disponível para um determinado período.
     * @param roomId ID do quarto.
     * @param checkIn Data de check-in.
     * @param checkOut Data de check-out.
     * @param reservationIdToIgnore ID da reserva a ser ignorada (útil para edição), pode ser null.
     * @return true se o quarto está disponível, false caso contrário.
     */
    public boolean isRoomAvailable(int roomId, LocalDate checkIn, LocalDate checkOut, Integer reservationIdToIgnore) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE room_id = ? AND status != 'CANCELLED' " +
                    "AND (check_in < ? AND check_out > ?)";
        if (reservationIdToIgnore != null) {
            sql += " AND id != ?";
        }
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            stmt.setString(2, Date.valueOf(checkOut).toString());
            stmt.setString(3, Date.valueOf(checkIn).toString());
            if (reservationIdToIgnore != null) {
                stmt.setInt(4, reservationIdToIgnore);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar disponibilidade: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Busca todas as reservas do sistema.
     * @return Lista de todas as reservas.
     */
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservations";
        List<Reservation> reservations = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Reservation reservation = new Reservation(
                    rs.getInt("user_id"),
                    rs.getInt("room_id"),
                    rs.getString("check_in"),
                    rs.getString("check_out"),
                    rs.getInt("guest_id")
                );

                reservation.setId(rs.getInt("id"));
                reservation.setStatus(rs.getString("status"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar quartos: " + e.getMessage());
        }
        return reservations;
    }
    
    /**
     * Atualiza uma reserva existente.
     * @param reservation Reserva com dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean update(Reservation reservation) {
    String sql = "UPDATE reservations SET user_id = ?, room_id = ?, check_in = ?, check_out = ? WHERE id = ?";

    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, reservation.getUserId());
        stmt.setInt(2, reservation.getRoomId());
        stmt.setString(3, reservation.getCheckIn());
        stmt.setString(4, reservation.getCheckOut());
        stmt.setInt(5, reservation.getId());

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Erro ao atualizar a reserva: " + e.getMessage());
    }
    return false;
    }
    
    /**
     * Busca uma reserva pelo ID.
     * @param reservationId ID da reserva.
     * @return Reserva encontrada ou null.
     */
    public Reservation findById(int reservationId) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Reservation reservation = new Reservation(
                    rs.getInt("user_id"),
                    rs.getInt("room_id"),
                    rs.getString("check_in"),
                    rs.getString("check_out"),
                    rs.getInt("guest_id")
                );

                reservation.setId(rs.getInt("id"));
                return reservation;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar reserva por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retorna uma lista de quartos disponíveis para um período.
     * @param checkIn Data de check-in.
     * @param checkOut Data de check-out.
     * @return Lista de quartos disponíveis.
     */
    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
    // Quartos ocupados no período desejado
    String occupiedRoomsSql = "SELECT DISTINCT room_id FROM reservations WHERE status != 'CANCELLED' " +
                              "AND (check_in < ? AND check_out > ?)";
    // Seleciona quartos que NÃO estão ocupados
    String availableRoomsSql = "SELECT * FROM rooms WHERE id NOT IN (" + occupiedRoomsSql + ")";

    List<Room> availableRooms = new ArrayList<>();

    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(availableRoomsSql)) {

        stmt.setString(1, Date.valueOf(checkOut).toString()); // check_in < checkOut
        stmt.setString(2, Date.valueOf(checkIn).toString());  // check_out > checkIn

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Room room = new Room();
            room.setId(rs.getInt("id"));
            room.setNumber(rs.getInt("number"));
            room.setType(rs.getString("type"));
            room.setPrice(rs.getDouble("price"));
            availableRooms.add(room);
        }
    } catch (SQLException e) {
        System.err.println("Erro ao buscar quartos disponíveis: " + e.getMessage());
        e.printStackTrace();
    }

    return availableRooms;
}


    
}