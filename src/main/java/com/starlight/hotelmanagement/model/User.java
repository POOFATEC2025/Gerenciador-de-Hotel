package com.starlight.hotelmanagement.model;

/**
 * Representa um usuário do sistema.
 */
public class User {
    
    private int id;
    private String name;
    private String email;
    private String password;
    private String role; // Papel do usuário (ADMIN, STAFF, etc)

    /**
     * Construtor padrão.
     */
    public User() { }

    /**
     * Construtor com parâmetros.
     * @param name Nome do usuário.
     * @param email E-mail do usuário.
     * @param password Senha do usuário.
     * @param role Papel do usuário (ADMIN, STAFF, etc).
     */
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Retorna o ID do usuário.
     * @return ID do usuário.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID do usuário.
     * @param id Novo ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna o nome do usuário.
     * @return Nome do usuário.
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do usuário.
     * @param name Novo nome.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna o e-mail do usuário.
     * @return E-mail do usuário.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o e-mail do usuário.
     * @param email Novo e-mail.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna a senha do usuário.
     * @return Senha do usuário.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define a senha do usuário.
     * @param password Nova senha.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retorna o papel do usuário.
     * @return Papel do usuário.
     */
    public String getRole() {
        return role;
    }

    /**
     * Define o papel do usuário.
     * @param role Novo papel.
     */
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role + '}';
    }
}