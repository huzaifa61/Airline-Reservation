package org.airplane;

import javax.swing.SwingUtilities;

/**
 * Main entry point. Launches the Controller page.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Controller::new);
    }
}
