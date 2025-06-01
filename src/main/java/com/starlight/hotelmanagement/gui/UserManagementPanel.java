package com.starlight.hotelmanagement.gui;

import com.starlight.hotelmanagement.dao.UserDAO;
import com.starlight.hotelmanagement.model.User;
import com.starlight.hotelmanagement.service.AuthService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Painel Swing para gerenciamento de usuários do sistema.
 */
public class UserManagementPanel extends JPanel {
    private final UserDAO userDAO = new UserDAO();
    private final AuthService authService = new AuthService();
    private JTable usersTable;
    private DefaultTableModel tableModel;

    public UserManagementPanel() {
        setLayout(new BorderLayout());
        initComponents();
        loadUsers();
    }

    private void initComponents() {
        // Toolbar
        JToolBar toolBar = new JToolBar();
        JButton btnAdd = new JButton("Novo Usuário");
        JButton btnEdit = new JButton("Editar");
        JButton btnDelete = new JButton("Remover");
        JButton btnRefresh = new JButton("Atualizar");

        btnAdd.addActionListener(e -> showUserForm(null));
        btnEdit.addActionListener(e -> editSelectedUser());
        btnDelete.addActionListener(e -> deleteSelectedUser());
        btnRefresh.addActionListener(e -> loadUsers());

        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.addSeparator();
        toolBar.add(btnRefresh);

        add(toolBar, BorderLayout.NORTH);

        // Tabela de usuários
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Email", "Tipo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        usersTable = new JTable(tableModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(usersTable);

        add(scrollPane, BorderLayout.CENTER);

        // Status bar
        JLabel statusLabel = new JLabel(" Total de usuários: 0");
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userDAO.findAll();
        
        for (User user : users) {
            tableModel.addRow(new Object[]{
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
            });
        }
        
        ((JLabel) getComponent(2)).setText(" Total de usuários: " + users.size());
    }

    private void showUserForm(User user) {
        JDialog dialog = new JDialog();
        dialog.setTitle(user == null ? "Novo Usuário" : "Editar Usuário");
        dialog.setModal(true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        // Campos do formulário
        JTextField txtName = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JPasswordField txtConfirmPassword = new JPasswordField();
        JComboBox<String> cmbRole = new JComboBox<>(
            new String[]{"ADMIN", "STAFF"});

        // Preencher campos se for edição
        if (user != null) {
            txtName.setText(user.getName());
            txtEmail.setText(user.getEmail());
            cmbRole.setSelectedItem(user.getRole());
        }

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Senha:"));
        formPanel.add(txtPassword);
        formPanel.add(new JLabel("Confirmar Senha:"));
        formPanel.add(txtConfirmPassword);
        formPanel.add(new JLabel("Tipo:"));
        formPanel.add(cmbRole);

        JButton btnSave = new JButton("Salvar");
        btnSave.addActionListener(e -> {
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
            
            if (!password.isEmpty() && !password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, "As senhas não coincidem!");
                return;
            }

            try {
                if (user == null) {
                    // Cadastro de novo usuário usando AuthService
                    boolean success = authService.register(
                        txtName.getText(),
                        txtEmail.getText(),
                        password,
                        cmbRole.getSelectedItem().toString()
                    );
                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Usuário criado com sucesso!");
                    }
                } else {
                    // Edição de usuário existente
                    User updatedUser = new User(
                        txtName.getText(),
                        txtEmail.getText(),
                        password.isEmpty() ? user.getPassword() : password,
                        cmbRole.getSelectedItem().toString()
                    );
                    updatedUser.setId(user.getId());
                    userDAO.update(updatedUser);
                }

                loadUsers();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage());
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void editSelectedUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) usersTable.getValueAt(selectedRow, 0);
            User user = userDAO.findById(id);
            showUserForm(user);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar");
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) usersTable.getValueAt(selectedRow, 0);
            String email = usersTable.getValueAt(selectedRow, 2).toString();
            
            if (email.equals("admin@hotel.com")) {
                JOptionPane.showMessageDialog(this, "Não é possível remover o usuário admin principal!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Tem certeza que deseja remover este usuário?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                userDAO.delete(id);
                loadUsers();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para remover");
        }
    }
}