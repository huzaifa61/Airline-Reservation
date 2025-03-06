package org.airplane;

import javax.swing.*;
import java.awt.*;

/**
 * Controller page to navigate between the User Booking page (Airplane)
 * and the Admin Panel (AdminPanel).
 */
public class Controller {
    private JFrame controllerFrame;
    private JButton userButton;
    private JButton adminButton;

    public Controller() {
        // (Optional) Set system look-and-feel + bigger font
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Font font = new Font("Arial", Font.PLAIN, 16);
            UIManager.put("Label.font", font);
            UIManager.put("Button.font", font);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1) Create controller window
        controllerFrame = new JFrame("Controller Page");
        controllerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 2) Create a simple panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // 3) Create buttons
        userButton = new JButton("Open User Booking Page");
        adminButton = new JButton("Open Admin Panel");

        // 4) Add listeners
        userButton.addActionListener(e -> new Airplane());
        adminButton.addActionListener(e -> new AdminPanel());

        // 5) Add buttons to panel
        panel.add(userButton);
        panel.add(adminButton);

        // 6) Final setup
        controllerFrame.add(panel);
        controllerFrame.pack();
        controllerFrame.setLocationRelativeTo(null);
        controllerFrame.setVisible(true);
    }
}

