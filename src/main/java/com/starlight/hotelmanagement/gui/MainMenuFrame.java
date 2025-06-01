package com.starlight.hotelmanagement.gui;

import com.starlight.hotelmanagement.model.User;
import javax.swing.*;

/**
 * Tela principal do sistema, que exibe o menu de navegação.
 * Dependendo do tipo de usuário, diferentes abas são exibidas.
 */
public class MainMenuFrame extends JFrame {
    private final User currentUser;

    /**
     * Construtor da tela principal.
     * @param user Usuário logado no sistema.
     */
    public MainMenuFrame(User user) {
        this.currentUser = user;
        setTitle("Hotel Management - Bem-vindo, " + user.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    /**
     * Inicializa os componentes da tela principal.
     * Cria as abas de reservas, quartos, hóspedes e usuários conforme o tipo de usuário.
     */
    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Aba de Reservas
        tabbedPane.addTab("Reservas", new ReservationFrame(currentUser));

        // Aba de Quartos (disponível apenas para staff/admin)
        if (currentUser.getRole().equals("STAFF") || currentUser.getRole().equals("ADMIN")) {
            tabbedPane.addTab("Gerenciar Quartos", new RoomManagementPanel());
            tabbedPane.addTab("Gerenciar Hóspedes", new GuestManagementPanel());
        }

        // Aba de Usuários (apenas para admin)
        if (currentUser.getRole().equals("ADMIN")) {
            tabbedPane.addTab("Gerenciar Usuários", new UserManagementPanel());
        }

        add(tabbedPane);
    }
}