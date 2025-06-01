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
    private String status; // Pode ser "CONFIRMED", "CANCELLED", "COMPLETED"
    private int guestId;

    public Reservation() {
    }

    // public Reservation(int userId, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
    //     this.userId = userId;
    //     this.roomId = roomId;
    //     this.checkInDate = checkInDate;
    //     this.checkOutDate = checkOutDate;
    //     this.status = "CONFIRMED"; // Status padrão
    // }

    public Reservation(int userId, int roomId, String checkIn, String checkOut, int guestId) {
        this.userId = userId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guestId = guestId;
        this.status = "CONFIRMED"; // Status padrão
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCheckInDate() {
        return LocalDate.parse(checkIn, DateTimeFormatter.ISO_DATE);
    }

    public LocalDate getCheckOutDate() {
        return LocalDate.parse(checkOut, DateTimeFormatter.ISO_DATE);
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    // Método toString para facilitar a visualização
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
