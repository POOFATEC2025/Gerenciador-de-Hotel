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

    public Guest() {
    }

    /**
     * Construtor para criar um hóspede com todos os detalhes.
     *
     * @param name     Nome do hóspede.
     * @param document Documento de identificação (CPF, RG, passaporte, etc.).
     * @param phone    Telefone de contato.
     * @param email    Email de contato.
     */
    public Guest(String name, String document, String phone, String email) {
        this.name = name;
        this.document = document;
        this.phone = phone;
        this.email = email;
    }

    /**
     * Obtém o ID do hóspede.
     *
     * @param id ID do hóspede.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID do hóspede.
     *
     * @param id ID do hóspede.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o nome do hóspede.
     *
     * @return Nome do hóspede.
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do hóspede.
     *
     * @return Nome do hóspede.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtém o documento de identificação do hóspede.
     *
     * @return Documento de identificação (CPF, RG, passaporte, etc.).
     */
    public String getDocument() {
        return document;
    }

    /**
     * Define o documento de identificação do hóspede.
     *
     * @return Documento de identificação (CPF, RG, passaporte, etc.).
     */
    public void setDocument(String document) {
        this.document = document;
    }

    /**
     * Obtém o telefone do hóspede.
     *
     * @return Telefone de contato do hóspede.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Define o telefone do hóspede.
     *
     * @return Telefone de contato do hóspede.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Obtém o email do hóspede.
     *
     * @return Email de contato do hóspede.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o email do hóspede.
     *
     * @param email Email de contato do hóspede.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    
    @Override
    public String toString() {
        return name + " - " + document;
    }
}