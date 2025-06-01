package com.starlight.hotelmanagement.dao;
import com.starlight.hotelmanagement.model.User;
import com.starlight.hotelmanagement.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelas operações de acesso a dados de usuários.
 */
public class UserDAO {

    /**
     * Insere um novo usuário no banco de dados.
     * @param user Usuário a ser inserido.
     * @return true se o usuário foi inserido com sucesso, false caso contrário.
     */
    public boolean insert(User user) {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            
            int affectedRows = stmt.executeUpdate();
            
            // Recupera o ID gerado pelo banco de dados
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
        }
        return false;
    }

    /**
     * Atualiza os dados de um usuário existente.
     * @param user Usuário com dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean update(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setInt(5, user.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
        }
        return false;
    }

    /**
     * Remove um usuário pelo ID.
     * @param userId ID do usuário a ser removido.
     * @return true se o usuário foi removido com sucesso, false caso contrário.
     */
    public boolean delete(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
        }
        return false;
    }

    /**
     * Busca um usuário pelo ID.
     * @param userId ID do usuário.
     * @return Usuário encontrado ou null.
     */
    public User findById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                );
                user.setId(rs.getInt("id"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca um usuário pelo e-mail.
     * @param email E-mail do usuário.
     * @return Usuário encontrado ou null.
     */
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
             if (rs.next()) {
                User user = new User(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                );
                user.setId(rs.getInt("id"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retorna todos os usuários cadastrados.
     * @return Lista de usuários.
     */
    public List<User> findAll() {
    String sql = "SELECT * FROM users";
    List<User> users = new ArrayList<>();
    
    try (Connection conn = DatabaseUtil.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            User user = new User(
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role")
            );
            user.setId(rs.getInt("id"));
            users.add(user);
        }
    } catch (SQLException e) {
        System.err.println("Erro ao listar usuários: " + e.getMessage());
    }
    return users;
    }

    /**
     * Verifica se um e-mail já está cadastrado no sistema.
     * @param email E-mail a ser verificado.
     * @return true se o e-mail já existir, false caso contrário.
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar email: " + e.getMessage());
        }
        return false;
    }
}