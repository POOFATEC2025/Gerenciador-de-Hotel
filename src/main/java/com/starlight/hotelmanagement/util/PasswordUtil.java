package com.starlight.hotelmanagement.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilitário para hash e verificação de senhas.
 */
public class PasswordUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    // private static final int ITERATIONS = 10000;
    // private static final int KEY_LENGTH = 256;
    
    /**
     * Gera um hash seguro para a senha usando PBKDF2WithHmacSHA256
     */
    public static String hashPassword(String password) {
        try {
            byte[] salt = new byte[16];
            RANDOM.nextBytes(salt);
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            
            return Base64.getEncoder().encodeToString(salt) + 
                   "$" + 
                   Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao criptografar senha", e);
        }
    }
    
    /**
     * Verifica se a senha corresponde ao hash armazenado
     */
    public static boolean verifyPassword(String password, String storedHash) {
        if (password == null || storedHash == null || !storedHash.contains("$")) {
            return false;
        }
        
        String[] parts = storedHash.split("\\$");
        if (parts.length != 2) return false;
        
        try {
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedPassword = Base64.getDecoder().decode(parts[1]);
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            
            return MessageDigest.isEqual(hashedPassword, storedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao verificar senha", e);
        }
    }
}