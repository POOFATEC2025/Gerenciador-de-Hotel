package com.starlight.hotelmanagement.gui;

import com.starlight.hotelmanagement.model.*;
import com.starlight.hotelmanagement.dao.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.swing.table.*;
import javax.swing.text.MaskFormatter;

/**
 * Painel Swing para gerenciamento de reservas do usuário.
 */
public class ReservationFrame extends JPanel {
    private final User currentUser;
    private DefaultTableModel tableModel;
    private JTable reservationsTable;
    private final RoomDAO roomDAO = new RoomDAO();
    private final GuestDAO guestDAO = new GuestDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    public ReservationFrame(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        initComponents();
        loadReservations();
    }

    private void initComponents() {
        // Toolbar
        JToolBar toolBar = new JToolBar();
        JButton btnNewReservation = new JButton("Nova Reserva");
        JButton btnEdit = new JButton("Editar");
        JButton btnCancelReservation = new JButton("Cancelar Reserva");
        JButton btnRefresh = new JButton("Atualizar");

        btnNewReservation.addActionListener(e -> showNewReservationDialog());
        btnEdit.addActionListener(e -> editSelectedReservation());
        btnCancelReservation.addActionListener(e -> cancelSelectedReservation());
        btnRefresh.addActionListener(e -> loadReservations());

        toolBar.add(btnNewReservation);
        toolBar.add(btnEdit);
        toolBar.add(btnCancelReservation);
        toolBar.addSeparator();
        toolBar.add(btnRefresh);

        add(toolBar, BorderLayout.NORTH);

        // Tabela de reservas
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Quarto", "Check-in", "Check-out", "Noites", "Valor Total", "Status","Hóspede"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reservationsTable = new JTable(tableModel);
        reservationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(reservationsTable);

        add(scrollPane, BorderLayout.CENTER);

        // Status bar
        JLabel statusLabel = new JLabel(" Total de reservas: 0");
        add(statusLabel, BorderLayout.SOUTH);
    }

    /**
     * Exibe o diálogo para nova reserva.
     */
    private void showNewReservationDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Nova Reserva - Selecione Período");
        dialog.setModal(true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de seleção de datas
        JPanel datePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        JFormattedTextField txtCheckIn = createDateField();
        JFormattedTextField txtCheckOut = createDateField();
        
        JButton btnSearchRooms = new JButton("Buscar Quartos Disponíveis");

        datePanel.add(new JLabel("Check-in (yyyy-MM-dd):"));
        datePanel.add(txtCheckIn);
        datePanel.add(new JLabel("Check-out (yyyy-MM-dd):"));
        datePanel.add(txtCheckOut);
        datePanel.add(new JLabel(""));
        datePanel.add(btnSearchRooms);
        // JComboBox<Guest> cmbGuest = new JComboBox<>();
        // for (Guest guest : guestDAO.findAll()) {
        //     cmbGuest.addItem(guest);
        // }

        // Painel para exibir quartos disponíveis
        JPanel roomsPanel = new JPanel(new BorderLayout());
        DefaultTableModel roomsTableModel = new DefaultTableModel(
            new Object[]{"Número", "Tipo", "Preço Diária", "Valor Total"}, 0);
        JTable roomsTable = new JTable(roomsTableModel);
        roomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane roomsScrollPane = new JScrollPane(roomsTable);
        roomsPanel.add(roomsScrollPane, BorderLayout.CENTER);
        roomsPanel.setVisible(false);

        // Botão de reserva
        JButton btnReserve = new JButton("Reservar Quarto Selecionado");
        btnReserve.setEnabled(false);

        btnSearchRooms.addActionListener(e -> {
            try {

                System.out.println(LocalDate.parse(txtCheckIn.getText()));
                LocalDate checkIn = LocalDate.parse(txtCheckIn.getText());
                LocalDate checkOut = LocalDate.parse(txtCheckOut.getText());

                if (checkIn.isAfter(checkOut) || checkIn.isEqual(checkOut)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Check-in deve ser anterior ao check-out e com pelo menos 1 dia de diferença");
                    return;
                }

                List<Room> availableRooms = reservationDAO.getAvailableRooms(checkIn, checkOut);
                roomsTableModel.setRowCount(0);

                long days = ChronoUnit.DAYS.between(checkIn, checkOut);
                for (Room room : availableRooms) {
                    double totalValue = room.getPrice() * days;
                    roomsTableModel.addRow(new Object[]{
                        room.getNumber(),
                        room.getType(),
                        String.format("R$ %.2f", room.getPrice()),
                        String.format("R$ %.2f", totalValue)
                    });
                }

                roomsPanel.setVisible(true);
                dialog.pack();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Datas inválidas! Use o formato yyyy-MM-dd");
            }
        });

        btnReserve.addActionListener(e -> {
            int selectedRow = roomsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int roomNumber = (int) roomsTable.getValueAt(selectedRow, 0);
                LocalDate checkIn = LocalDate.parse(txtCheckIn.getText());
                LocalDate checkOut = LocalDate.parse(txtCheckOut.getText());

                Room selectedRoom = roomDAO.findByNumber(roomNumber);
                long days = ChronoUnit.DAYS.between(checkIn, checkOut);
                double totalValue = selectedRoom.getPrice() * days;

                // Novo: Diálogo para selecionar/cadastrar hóspede
                Guest selectedGuest = showGuestSelectionDialog();
                if (selectedGuest == null) {
                    JOptionPane.showMessageDialog(dialog, "Selecione ou cadastre um hóspede para continuar.");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Confirmar reserva do Quarto " + roomNumber + "?\n" +
                    "Período: " + checkIn + " a " + checkOut + " (" + days + " noites)\n" +
                    "Hóspede: " + selectedGuest.getName() + "\n" +
                    "Valor total: R$ " + String.format("%.2f", totalValue),
                    "Confirmar Reserva",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    Reservation newReservation = new Reservation(
                        currentUser.getId(),
                        selectedRoom.getId(),
                        checkIn.toString(),
                        checkOut.toString(),
                        selectedGuest.getId()
                    );

                    if (reservationDAO.createReservation(newReservation)) {
                        JOptionPane.showMessageDialog(dialog, "Reserva realizada com sucesso!");
                        loadReservations();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Erro ao realizar reserva. Tente novamente.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Selecione um quarto para reservar");
            }
        });

        roomsTable.getSelectionModel().addListSelectionListener(e -> {
            btnReserve.setEnabled(roomsTable.getSelectedRow() >= 0);
        });

        // Layout principal
        mainPanel.add(datePanel, BorderLayout.NORTH);
        mainPanel.add(roomsPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnReserve);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private JFormattedTextField createDateField() {
        try {
            MaskFormatter formatter = new MaskFormatter("####-##-##");
            formatter.setPlaceholderCharacter('_');
            return new JFormattedTextField(formatter);
        } catch (ParseException e) {
            return new JFormattedTextField();
        }
    }

    /**
     * Exibe o diálogo para editar reserva selecionada.
     */
    private void editSelectedReservation() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int reservationId = (int) reservationsTable.getValueAt(selectedRow, 0);
            Reservation reservation = reservationDAO.findById(reservationId);
            
            if (reservation != null) {
                if (reservation.getStatus().equals("CANCELLED")) {
                    JOptionPane.showMessageDialog(this, "Não é possível editar uma reserva cancelada");
                    return;
                }

                JDialog dialog = new JDialog();
                dialog.setTitle("Editar Reserva #" + reservationId);
                dialog.setModal(true);
                dialog.setSize(500, 300);
                dialog.setLocationRelativeTo(this);

                JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

                JLabel lblRoom = new JLabel("Quarto: " + reservation.getRoomId());
                JFormattedTextField txtCheckIn = createDateField();
                txtCheckIn.setText(reservation.getCheckIn().toString());
                JFormattedTextField txtCheckOut = createDateField();
                txtCheckOut.setText(reservation.getCheckOut().toString());
                
                
                Room room = roomDAO.findById(reservation.getRoomId());
                JLabel lblTotal = new JLabel(); // Inicializa vazio

                // Atualiza o valor total conforme as datas
                Runnable updateTotal = () -> {
                    try {
                        LocalDate checkIn = LocalDate.parse(txtCheckIn.getText());
                        LocalDate checkOut = LocalDate.parse(txtCheckOut.getText());
                        long newDays = ChronoUnit.DAYS.between(checkIn, checkOut);
                        if (newDays > 0 && room != null) {
                            double newTotalValue = room.getPrice() * newDays;
                            lblTotal.setText("Novo Valor: R$ " + String.format("%.2f", newTotalValue));
                        } else {
                            lblTotal.setText("Novo Valor: -");
                        }
                    } catch (Exception ex) {
                        lblTotal.setText("Novo Valor: -");
                    }
                };

                // Adiciona listeners para atualizar o valor ao digitar
                txtCheckIn.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                    public void insertUpdate(javax.swing.event.DocumentEvent e) { updateTotal.run(); }
                    public void removeUpdate(javax.swing.event.DocumentEvent e) { updateTotal.run(); }
                    public void changedUpdate(javax.swing.event.DocumentEvent e) { updateTotal.run(); }
                });
                txtCheckOut.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                    public void insertUpdate(javax.swing.event.DocumentEvent e) { updateTotal.run(); }
                    public void removeUpdate(javax.swing.event.DocumentEvent e) { updateTotal.run(); }
                    public void changedUpdate(javax.swing.event.DocumentEvent e) { updateTotal.run(); }
                });

                // Inicializa o valor ao abrir o diálogo
                updateTotal.run();

                formPanel.add(new JLabel("Quarto:"));
                formPanel.add(lblRoom);
                formPanel.add(new JLabel("Novo Check-in:"));
                formPanel.add(txtCheckIn);
                formPanel.add(new JLabel("Novo Check-out:"));
                formPanel.add(txtCheckOut);
                formPanel.add(new JLabel("Novo Valor:"));
                formPanel.add(lblTotal);

                JButton btnSave = new JButton("Salvar Alterações");
                JButton btnCancel = new JButton("Cancelar");

                btnSave.addActionListener(e -> {
                    try {
                        LocalDate newCheckIn = LocalDate.parse(txtCheckIn.getText());
                        LocalDate newCheckOut = LocalDate.parse(txtCheckOut.getText());

                        System.out.println("Check-in: " + newCheckIn.toString());
                        System.out.println("Check-out: " + newCheckOut.toString());

                        if (newCheckIn.isAfter(newCheckOut)) {
                            JOptionPane.showMessageDialog(dialog, "Check-in deve ser anterior ao check-out");
                            return;
                        }

                        reservation.setCheckIn(newCheckIn.toString());
                        reservation.setCheckOut(newCheckOut.toString());

                        boolean teste = reservationDAO.update(reservation);
                        System.out.println("Atualização: " + teste);

                        if (reservationDAO.isRoomAvailable(reservation.getRoomId(), newCheckIn, newCheckOut,reservation.getId())) {
                            reservation.setCheckIn(newCheckIn.toString());
                            reservation.setCheckOut(newCheckOut.toString());
                            
                            if (reservationDAO.update(reservation)) {
                                JOptionPane.showMessageDialog(dialog, "Reserva atualizada com sucesso!");
                                loadReservations();
                                dialog.dispose();
                            } else {
                                JOptionPane.showMessageDialog(dialog, "Erro ao atualizar reserva");
                            }
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Quarto não disponível para o novo período");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Datas inválidas! Use o formato yyyy-MM-dd");
                    }
                });

                btnCancel.addActionListener(e -> dialog.dispose());

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.add(btnCancel);
                buttonPanel.add(btnSave);

                mainPanel.add(formPanel, BorderLayout.CENTER);
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);

                dialog.add(mainPanel);
                dialog.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva para editar");
        }
    }

    /**
     * Cancela a reserva selecionada.
     */
    private void cancelSelectedReservation() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int reservationId = (int) reservationsTable.getValueAt(selectedRow, 0);
            String status = (String) reservationsTable.getValueAt(selectedRow, 6);
            
            if (status.equals("CANCELLED")) {
                JOptionPane.showMessageDialog(this, "Esta reserva já está cancelada");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente cancelar a reserva #" + reservationId + "?",
                "Confirmar Cancelamento",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (reservationDAO.cancelReservation(reservationId)) {
                    JOptionPane.showMessageDialog(this, "Reserva cancelada com sucesso");
                    loadReservations();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao cancelar reserva");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva para cancelar");
        }
    }

    /**
     * Carrega as reservas na tabela.
     */
    private void loadReservations() {
        tableModel.setRowCount(0);
        
        List<Reservation> reservations;
    // Verifica o papel do usuário
        if (currentUser.getRole().equalsIgnoreCase("ADMIN") || currentUser.getRole().equalsIgnoreCase("STAFF")) {
            // Busca todas as reservas
            reservations = reservationDAO.findAll();
        } else {
            // Busca apenas as reservas do usuário
            reservations = reservationDAO.getReservationsByUser(currentUser.getId());
        }
        
        // Ordena por data de check-in
        reservations.sort(Comparator.comparing(Reservation::getCheckIn));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Reservation reservation : reservations) {
            if (!reservation.getStatus().equals("CANCELLED")) {
                Room room = roomDAO.findById(reservation.getRoomId());
                Guest guest = guestDAO.findById(reservation.getGuestId()); // Busca o hóspede pelo guestId
                long days = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
                double totalValue = room.getPrice() * days;

                tableModel.addRow(new Object[]{
                    reservation.getId(),
                    room.getNumber(),
                    reservation.getCheckInDate().format(dateFormatter),
                    reservation.getCheckOutDate().format(dateFormatter),
                    days,
                    String.format("R$ %.2f", totalValue),
                    reservation.getStatus(),
                    guest != null ? guest.getName() : "N/A" // Exibe o nome do hóspede
                });
            }
        }

        // Atualiza status bar
        ((JLabel) getComponent(2)).setText(" Total de reservas: " + tableModel.getRowCount());
    }
    
    private Guest showGuestSelectionDialog() {
        JDialog guestDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Selecionar Hóspede", true);
        guestDialog.setSize(400, 250);
        guestDialog.setLocationRelativeTo(this);

        JComboBox<Guest> cmbGuest = new JComboBox<>();
        for (Guest guest : guestDAO.findAll()) {
            cmbGuest.addItem(guest);
        }

        JButton btnNewGuest = new JButton("Cadastrar Novo Hóspede");
        btnNewGuest.addActionListener(e -> {
            Guest newGuest = showGuestForm();
            if (newGuest != null) {
                cmbGuest.addItem(newGuest);
                cmbGuest.setSelectedItem(newGuest);
            }
        });

        JButton btnOk = new JButton("OK");
        final Guest[] selectedGuest = {null};
        btnOk.addActionListener(e -> {
            selectedGuest[0] = (Guest) cmbGuest.getSelectedItem();
            guestDialog.dispose();
        });

        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.add(cmbGuest);
        panel.add(btnNewGuest);
        panel.add(btnOk);

        guestDialog.add(panel);
        guestDialog.setVisible(true);

        return selectedGuest[0];
    }

    private Guest showGuestForm() {
        JTextField txtName = new JTextField();
        JTextField txtDocument = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Nome:"));
        panel.add(txtName);
        panel.add(new JLabel("Documento:"));
        panel.add(txtDocument);
        panel.add(new JLabel("Telefone:"));
        panel.add(txtPhone);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);

        int result = JOptionPane.showConfirmDialog(this, panel, "Novo Hóspede", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Guest guest = new Guest(txtName.getText(), txtDocument.getText(), txtPhone.getText(), txtEmail.getText());
            if (guestDAO.insert(guest)) {
                return guest;
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar hóspede.");
            }
        }
        return null;
    }
}