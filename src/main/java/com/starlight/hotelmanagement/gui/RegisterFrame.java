package com.starlight.hotelmanagement.gui;

import com.starlight.hotelmanagement.service.AuthService;
import javax.swing.*;
import java.awt.*;

/**
 * Tela de registro de novos usuários.
 */
public class RegisterFrame extends JFrame {
    private final AuthService authService = new AuthService();
    
    private JTextField txtName, txtEmail;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JButton btnRegister;

    /**
     * Construtor da tela de registro.
     */
    public RegisterFrame() {
        setTitle("Registro de Novo Usuário");
        setSize(400, 350);
        setLocationRelativeTo(null);
        initComponents();
    }

    /**
     * Inicializa os componentes da tela de registro.
     * Cria os campos de entrada e o botão de registro.
     */
    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campos do formulário
        addField(panel, gbc, 0, "Nome Completo:", txtName = new JTextField(20));
        addField(panel, gbc, 1, "Email:", txtEmail = new JTextField(20));
        addField(panel, gbc, 2, "Senha:", txtPassword = new JPasswordField(20));
        addField(panel, gbc, 3, "Confirmar Senha:", txtConfirmPassword = new JPasswordField(20));

        // Botão de registro
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        btnRegister = new JButton("Registrar");
        btnRegister.addActionListener(e -> performRegistration());
        panel.add(btnRegister, gbc);

        add(panel);
    }

    /**
     * Adiciona um campo de entrada ao painel com o rótulo correspondente.
     * @param panel O painel onde o campo será adicionado.
     * @param gbc As restrições de layout do GridBag.
     * @param row A linha onde o campo será adicionado.
     * @param label O rótulo do campo.
     * @param field O componente de entrada (JTextField ou JPasswordField).
     */
    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    /**
     * Realiza o registro do novo usuário.
     * Valida os campos e chama o serviço de autenticação para registrar o usuário.
     */
    private void performRegistration() {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "As senhas não coincidem!");
            return;
        }

        try {
            boolean success = authService.register(name, email, password, "STAFF");
            if (success) {
                JOptionPane.showMessageDialog(this, "Registro realizado com sucesso!");
                dispose();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}