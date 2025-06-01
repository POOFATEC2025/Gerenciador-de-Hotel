package com.starlight.hotelmanagement.gui;

import com.starlight.hotelmanagement.dao.RoomDAO;
import com.starlight.hotelmanagement.model.Room;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Painel Swing para gerenciamento de quartos.
 */
public class RoomManagementPanel extends JPanel {
    private final RoomDAO roomDAO = new RoomDAO();
    private JTable roomsTable;
    private DefaultTableModel tableModel;

    /**
     * Construtor do painel de gerenciamento de quartos.
     * Configura o layout, inicializa os componentes e carrega os quartos.
     */
    public RoomManagementPanel() {
        setLayout(new BorderLayout());
        initComponents();
        loadRooms();
    }

    /**
     * Inicializa os componentes do painel.
     * Cria a barra de ferramentas, a tabela de quartos e a barra de status.
     */
    private void initComponents() {
        // Toolbar com botões
        JToolBar toolBar = new JToolBar();
        JButton btnAdd = new JButton("Adicionar Quarto");
        JButton btnEdit = new JButton("Editar");
        JButton btnDelete = new JButton("Remover");
        JButton btnRefresh = new JButton("Atualizar");

        btnAdd.addActionListener(e -> showRoomForm(null));
        btnEdit.addActionListener(e -> editSelectedRoom());
        btnDelete.addActionListener(e -> deleteSelectedRoom());
        btnRefresh.addActionListener(e -> loadRooms());

        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.addSeparator();
        toolBar.add(btnRefresh);

        add(toolBar, BorderLayout.NORTH);

        // Tabela de quartos
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Número", "Tipo", "Preço", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomsTable = new JTable(tableModel);
        roomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(roomsTable);

        add(scrollPane, BorderLayout.CENTER);

        // Status bar
        JLabel statusLabel = new JLabel(" Total de quartos: 0");
        add(statusLabel, BorderLayout.SOUTH);
    }

    /**
     * Carrega os quartos do banco de dados e atualiza a tabela.
     * Limpa a tabela antes de adicionar os novos dados.
     */
    private void loadRooms() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomDAO.findAll();
        
        for (Room room : rooms) {
            tableModel.addRow(new Object[]{
                room.getId(),
                room.getNumber(),
                room.getType(),
                String.format("R$ %.2f", room.getPrice()),
                room.getStatus()
            });
        }
        
        ((JLabel) getComponent(2)).setText(" Total de quartos: " + rooms.size());
    }

    /**
     * Exibe um formulário para adicionar ou editar um quarto.
     * Se o quarto for null, é um novo quarto; caso contrário, é uma edição.
     * @param room Quarto a ser editado ou null para novo quarto.
     */
    private void showRoomForm(Room room) {
        
        JDialog dialog = new JDialog();
        dialog.setTitle(room == null ? "Novo Quarto" : "Editar Quarto");
        dialog.setModal(true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        
        // Campos do formulário
        JTextField txtNumber = new JTextField();
        JComboBox<String> cmbType = new JComboBox<>(new String[]{"Standard", "Deluxe", "Suite"});
        JTextField txtPrice = new JTextField();
        JComboBox<String> cmbStatus = new JComboBox<>(
            new String[]{"AVAILABLE", "OCCUPIED", "MAINTENANCE"});

        // Preencher campos se for edição
        if (room != null) {
            txtNumber.setText(String.valueOf(room.getNumber()));
            cmbType.setSelectedItem(room.getType());
            txtPrice.setText(String.valueOf(room.getPrice()));
            cmbStatus.setSelectedItem(room.getStatus());
        }

        formPanel.add(new JLabel("Número:"));
        formPanel.add(txtNumber);
        formPanel.add(new JLabel("Tipo:"));
        formPanel.add(cmbType);
        formPanel.add(new JLabel("Preço:"));
        formPanel.add(txtPrice);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(cmbStatus);

        JButton btnSave = new JButton("Salvar");
        btnSave.addActionListener(e -> {
            try {
                Room updatedRoom = new Room(
                    Integer.parseInt(txtNumber.getText()),
                    cmbType.getSelectedItem().toString(),
                    Double.parseDouble(txtPrice.getText()),
                    cmbStatus.getSelectedItem().toString()
                );

                if (room == null) {
                    roomDAO.insert(updatedRoom);
                } else {
                    updatedRoom.setId(room.getId());
                    roomDAO.update(updatedRoom);
                }

                loadRooms();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Valores inválidos!");
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Edita o quarto selecionado na tabela.
     * Se nenhum quarto estiver selecionado, exibe uma mensagem de erro.
     */
    private void editSelectedRoom() {
        int selectedRow = roomsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) roomsTable.getValueAt(selectedRow, 0);
            Room room = roomDAO.findById(id);
            showRoomForm(room);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um quarto para editar");
        }
    }

    /**
     * Remove o quarto selecionado na tabela.
     * Se nenhum quarto estiver selecionado, exibe uma mensagem de erro.
     * Solicita confirmação antes de remover o quarto.
     */
    private void deleteSelectedRoom() {
        int selectedRow = roomsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) roomsTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Tem certeza que deseja remover este quarto?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                roomDAO.delete(id);
                loadRooms();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um quarto para remover");
        }
    }
}