package com.starlight.hotelmanagement.service;

import com.starlight.hotelmanagement.dao.UserDAO;
import com.starlight.hotelmanagement.model.User;
import com.starlight.hotelmanagement.util.PasswordUtil;
// import java.sql.SQLException;

/**
 * Serviço responsável pela autenticação e registro de usuários.
 */
public class AuthService {
    private final UserDAO userDAO;
    
    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Realiza o login de um usuário
     * @param email Email do usuário
     * @param password Senha não criptografada
     * @return Objeto User se login for bem-sucedido, null caso contrário
     */
    public User login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return null;
        }

        User user = userDAO.findByEmail(email.trim());
        
        if (user != null && PasswordUtil.verifyPassword(password, user.getPassword())) {
            return user;
        }
        
        return null;
    }

    /**
     * Registra um novo usuário
     * @param name Nome completo
     * @param email Email válido
     * @param password Senha não criptografada
     * @param role Papel do usuário ( STAFF, ADMIN)
     * @return true se o registro foi bem-sucedido
     * @throws IllegalArgumentException Se os dados forem inválidos
     */
    public boolean register(String name, String email, String password, String role) {
        // Validações básicas
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome não pode estar vazio");
        }
        
        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
        }

        // Verifica se email já existe
        if (userDAO.emailExists(email)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        // Cria e salva o usuário
        User newUser = new User(
            name.trim(),
            email.trim().toLowerCase(),
            PasswordUtil.hashPassword(password), // Criptografa a senha
            role.toUpperCase()
        );

        return userDAO.insert(newUser);
    }

    /**
     * Verifica se um usuário tem permissão de administrador
     * @param user Usuario
     * @return true ou false
     */
    public boolean isAdmin(User user) {
        return user != null && "ADMIN".equals(user.getRole());
    }

    /**
     * Verifica se um usuário tem permissão de staff (funcionário)
     * @param user Usuario
     * @return true ou false
     */
    public boolean isStaff(User user) {
        return user != null && ("STAFF".equals(user.getRole()) || "ADMIN".equals(user.getRole()));
    }
}
