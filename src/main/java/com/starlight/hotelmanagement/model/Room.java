package com.starlight.hotelmanagement.model;

/**
 * Representa um quarto do hotel.
 */
public class Room {

    private int id;
    private int number;
    private String type;
    private double price;
    private String status; 

    /**
     * Construtor para criar um quarto com todos os detalhes.
     *
     * @param number Número do quarto.
     * @param type Tipo do quarto (ex: "SINGLE", "DOUBLE", "SUITE").
     * @param price Preço do quarto.
     * @param status Status do quarto (ex: "AVAILABLE", "OCCUPIED", "MAINTENANCE").
     */
    public Room(int number, String type, double price, String status) {
        this.number = number;
        this.type = type;
        this.price = price;
        this.status = status;
    }

    /**
     * Construtor padrão para criar um quarto vazio.
     * Inicializa os atributos com valores padrão.
     */
    public Room(){
        this.number = 0;
        this.type = "";
        this.price = 0.0;
        this.status = "AVAILABLE"; // Default status

    }

    /**
     * Obtém o ID do quarto.
     *
     * @return ID do quarto.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID do quarto.
     *
     * @param id ID do quarto.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o número do quarto.
     *
     * @return Número do quarto.
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * Define o número do quarto.
     *
     * @param number Número do quarto.
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Obtém o tipo do quarto.
     *
     * @return Tipo do quarto.
     */
    public String getType() {
        return type;
    }

    /**
     * Define o tipo do quarto.
     *
     * @param type Tipo do quarto (ex: "SINGLE", "DOUBLE", "SUITE").
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Obtém o preço do quarto.
     *
     * @return Preço do quarto.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Define o preço do quarto.
     *
     * @param price Preço do quarto.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Obtém o status do quarto.
     *
     * @return Status do quarto (ex: "AVAILABLE", "OCCUPIED", "MAINTENANCE").
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define o status do quarto.
     *
     * @param status Status do quarto (ex: "AVAILABLE", "OCCUPIED", "MAINTENANCE").
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", number=" + number + ", type=" + type + ", price=" + price + ", status=" + status + '}';
    }
    
    
}
