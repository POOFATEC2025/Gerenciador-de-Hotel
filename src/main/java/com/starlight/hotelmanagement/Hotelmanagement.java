package com.starlight.hotelmanagement;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.starlight.hotelmanagement.gui.LoginFrame;
import java.text.ParseException;
import javax.swing.*;

/**
 * Classe principal do sistema de gerenciamento de hotel.
 * Responsável por iniciar a aplicação e configurar o tema visual.
 */
public class Hotelmanagement {
    /**
     * Método principal. Inicia a aplicação Swing.
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) throws ParseException {

        // DatabaseUtil.initializeDB();
        
        FlatIntelliJLaf.setup();
        
                try {
                    UIManager.setLookAndFeel(new FlatIntelliJLaf());
                    SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }
    }

