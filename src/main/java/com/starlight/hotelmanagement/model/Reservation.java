package com.starlight.hotelmanagement.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Representa uma reserva de quarto.
 */
public class Reservation {
    private int id;
    private int userId;
    private int roomId;
    private String checkIn;
    private String checkOut;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status; 
    private int guestId;

    public Reservation() {
    }

    /**
     * Construtor para criar uma reserva com todos os detalhes.
     *
     * @param userId    ID do usuário que fez a reserva.
     * @param roomId    ID do quarto reservado.
     * @param checkIn   Data de check-in no formato ISO (yyyy-MM-dd).
     * @param checkOut  Data de check-out no formato ISO (yyyy-MM-dd).
     * @param guestId   ID do hóspede associado à reserva.
     */
    public Reservation(int userId, int roomId, String checkIn, String checkOut, int guestId) {
        this.userId = userId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guestId = guestId;
        this.status = "CONFIRMED"; // Status padrão
    }

    /**
     * Obtém o ID da reserva.
     *
     * @return ID da reserva.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID da reserva.
     *
     * @param id ID da reserva.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o ID do usuário que fez a reserva.
     *
     * @return ID do usuário.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Define o ID do usuário que fez a reserva.
     *
     * @param userId ID do usuário.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Obtém o ID do quarto reservado.
     *
     * @return ID do quarto.
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Define o ID do quarto reservado.
     *
     * @param roomId ID do quarto.
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Obtém a data de check-in.
     *
     * @return Data de check-in no formato ISO (yyyy-MM-dd).
     */
    public String getCheckIn() {
        return checkIn;
    }

    /**
     * Define a data de check-in.
     *
     * @param checkIn Data de check-in no formato ISO (yyyy-MM-dd).
     */
    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    /**
     * Obtém a data de check-out.
     *
     * @return Data de check-out no formato ISO (yyyy-MM-dd).
     */
    public String getCheckOut() {
        return checkOut;
    }

    /**
     * Define a data de check-out.
     *
     * @param checkOut Data de check-out no formato ISO (yyyy-MM-dd).
     */
    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    /**
     * Obtém o status da reserva.
     *
     * @return Status da reserva (e.g., "CONFIRMED", "CANCELLED").
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define o status da reserva.
     *
     * @param status Status da reserva (e.g., "CONFIRMED", "CANCELLED").
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Obtém a data de check-in como um objeto LocalDate.
     *
     * @return Data de check-in como LocalDate.
     */
    public LocalDate getCheckInDate() {
        return LocalDate.parse(checkIn, DateTimeFormatter.ISO_DATE);
    }

    /**
     * Obtém a data de check-out como um objeto LocalDate.
     *
     * @return Data de check-out como LocalDate.
     */
    public LocalDate getCheckOutDate() {
        return LocalDate.parse(checkOut, DateTimeFormatter.ISO_DATE);
    }

    /**
     * Obtém o ID do hóspede associado à reserva.
     *
     * @return ID do hóspede.
     */
    public int getGuestId() {
        return guestId;
    }

    /**
     * Define o ID do hóspede associado à reserva.
     *
     * @param guestId ID do hóspede.
     */
    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    /**
     * Retorna uma representação em string da reserva.
     *
     * @return String representando a reserva.
     */
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", userId=" + userId +
                ", roomId=" + roomId +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", status='" + status + '\'' +
                '}';
    }
}
