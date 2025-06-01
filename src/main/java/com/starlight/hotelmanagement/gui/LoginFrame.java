package com.starlight.hotelmanagement.gui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.starlight.hotelmanagement.service.AuthService;
import javax.swing.*;
import java.awt.*;
import com.starlight.hotelmanagement.model.User;

/**
 * Tela de login do sistema.
 */
public class LoginFrame extends JFrame {
    private final AuthService authService = new AuthService();
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    /**
     * Construtor da tela de login.
     */
    public LoginFrame() {
        setTitle("Hotel Management - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }
    /**
     * Inicializa os componentes da tela de login.
     */
    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        panel.add(txtPassword, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> performLogin());
        panel.add(btnLogin, gbc);
        gbc.gridy = 3;
        btnRegister = new JButton("Registrar Novo Usuário");
        btnRegister.addActionListener(e -> openRegisterFrame());
        panel.add(btnRegister, gbc);

        add(panel);
    }

    /**
     * Realiza o login do usuário no sistema.
     */
    private void performLogin() {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        try {
            User user = authService.login(email, password);
            System.out.println("Usuário: " + user);
            System.out.println("Senha: " + password);
            if (user != null) {
                dispose(); // Fecha a tela de login
                new MainMenuFrame(user).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Credenciais inválidas!");
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Isso mostrará o stack trace completo no console
            JOptionPane.showMessageDialog(this, "Erro ao realizar login: " + ex.getMessage());
        }
    }

    /**
     * Abre a tela de registro de novo usuário.
     */
    private void openRegisterFrame() {
        new RegisterFrame().setVisible(true);
    }

}