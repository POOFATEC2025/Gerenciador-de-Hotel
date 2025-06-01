package com.starlight.hotelmanagement.model;



/**
 *
 * @author bruna
 */
public class Room {

    private int id;
    private int number;
    private String type;
    private double price;
    private String status; 

    // Construtor
    public Room(int number, String type, double price, String status) {
        this.number = number;
        this.type = type;
        this.price = price;
        this.status = status;
    }

    public Room(){
        this.number = 0;
        this.type = "";
        this.price = 0.0;
        this.status = "AVAILABLE"; // Default status

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", number=" + number + ", type=" + type + ", price=" + price + ", status=" + status + '}';
    }
    
    
}
