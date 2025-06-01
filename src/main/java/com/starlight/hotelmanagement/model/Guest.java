package com.starlight.hotelmanagement.model;

/**
 * Representa um hóspede do hotel.
 */
public class Guest {
    private int id;
    private String name;
    private String document; // CPF, RG, passaporte, etc.
    private String phone;
    private String email;

    public Guest() {}

    public Guest(String name, String document, String phone, String email) {
        this.name = name;
        this.document = document;
        this.phone = phone;
        this.email = email;
    }

    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return name + " - " + document;
    }
}