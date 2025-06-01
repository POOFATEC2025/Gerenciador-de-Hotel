package com.starlight.hotelmanagement.gui;

import com.starlight.hotelmanagement.dao.GuestDAO;
import com.starlight.hotelmanagement.model.Guest;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Painel para cadastro e listagem de hóspedes.
 */
public class GuestManagementPanel extends JPanel {
    private final GuestDAO guestDAO = new GuestDAO();
    private final DefaultTableModel tableModel;
    private final JTable guestsTable;

    /**
     * Construtor do painel de gerenciamento de hóspedes.
     * Configura a tabela, botões e layout.
     */
    public GuestManagementPanel() {
        setLayout(new BorderLayout());

        // Tabela de hóspedes
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Documento", "Telefone", "Email"}, 0
        ) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        guestsTable = new JTable(tableModel);
        guestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(guestsTable);

        // Botão para novo hóspede
        JButton btnNew = new JButton("Novo Hóspede");
        btnNew.addActionListener(e -> showGuestForm(null));

        // Painel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(btnNew);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Status bar
        JLabel statusLabel = new JLabel(" Total de hóspedes: 0");
        add(statusLabel, BorderLayout.SOUTH);

        loadGuests();
    }

    /**
     * Carrega os hóspedes na tabela.
     */
    private void loadGuests() {
        tableModel.setRowCount(0);
        List<Guest> guests = guestDAO.findAll();
        for (Guest guest : guests) {
            tableModel.addRow(new Object[]{
                guest.getId(),
                guest.getName(),
                guest.getDocument(),
                guest.getPhone(),
                guest.getEmail()
            });
        }
        ((JLabel) getComponent(2)).setText(" Total de hóspedes: " + guests.size());
    }

    /**
     * Exibe o formulário para cadastro/edição de hóspede.
     */
    private void showGuestForm(Guest guest) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            guest == null ? "Novo Hóspede" : "Editar Hóspede", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField txtName = new JTextField();
        JTextField txtDocument = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();

        if (guest != null) {
            txtName.setText(guest.getName());
            txtDocument.setText(guest.getDocument());
            txtPhone.setText(guest.getPhone());
            txtEmail.setText(guest.getEmail());
        }

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Documento:"));
        formPanel.add(txtDocument);
        formPanel.add(new JLabel("Telefone:"));
        formPanel.add(txtPhone);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);

        JButton btnSave = new JButton("Salvar");
        btnSave.addActionListener(e -> {
            String name = txtName.getText().trim();
            String document = txtDocument.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();

            if (name.isEmpty() || document.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nome e documento são obrigatórios!");
                return;
            }

            if (guest == null) {
                Guest newGuest = new Guest(name, document, phone, email);
                if (guestDAO.insert(newGuest)) {
                    JOptionPane.showMessageDialog(dialog, "Hóspede cadastrado com sucesso!");
                }
            } else {
                guest.setName(name);
                guest.setDocument(document);
                guest.setPhone(phone);
                guest.setEmail(email);
                // guestDAO.update(guest); // Implemente se desejar edição
            }
            loadGuests();
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
