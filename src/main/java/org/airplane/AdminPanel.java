package org.airplane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

import static org.airplane.DataStore.*;

/**
 * Admin panel to view and manage all bookings in memory.
 */
public class AdminPanel {
    private JFrame adminFrame;
    private JTable bookingTable;
    private JButton refreshButton;
    private JButton deleteButton;

    public AdminPanel() {
        // (Optional) Set system look-and-feel + bigger font
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Font font = new Font("Arial", Font.PLAIN, 16);
            UIManager.put("Label.font", font);
            UIManager.put("TextField.font", font);
            UIManager.put("Button.font", font);
            UIManager.put("Table.font", font);
            UIManager.put("TableHeader.font", font);
            UIManager.put("TextArea.font", font);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1) Create admin window
        adminFrame = new JFrame("Admin Panel - Manage Bookings");
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // or EXIT_ON_CLOSE

        // 2) Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 3) Create table
        bookingTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 4) Create button panel
        JPanel buttonPanel = new JPanel();
        refreshButton = new JButton("REFRESH");
        deleteButton = new JButton("DELETE");
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 5) Final setup
        adminFrame.setContentPane(mainPanel);
        adminFrame.setSize(800, 400);
        adminFrame.setLocationRelativeTo(null);
        adminFrame.setVisible(true);

        // 6) Add listeners
        refreshButton.addActionListener(e -> refreshTable());
        deleteButton.addActionListener(e -> deleteBooking());

        // Load initial data
        refreshTable();
    }

    // -----------------------------------------------------------------------
    // REFRESH TABLE with all bookings
    // -----------------------------------------------------------------------
    private void refreshTable() {
        bookingTable.setModel(buildBookingTableModel());
    }

    // -----------------------------------------------------------------------
    // DELETE BOOKING (admin selects a row, we remove from DataStore)
    // -----------------------------------------------------------------------
    private void deleteBooking() {
        int row = bookingTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(adminFrame, "Please select a booking to delete.");
            return;
        }
        // Remove from DataStore
        DataStore.removeBooking(row);
        // Refresh the table
        refreshTable();
    }

    // -----------------------------------------------------------------------
    // BUILD TABLE MODEL from DataStore bookings
    // -----------------------------------------------------------------------
    private DefaultTableModel buildBookingTableModel() {
        // Columns: Name, FlightNo, Source, Destination, Date, Time, TotalFare
        Vector<String> columns = new Vector<>();
        columns.add("NAME");
        columns.add("FLIGHT_NO");
        columns.add("SOURCE");
        columns.add("DESTINATION");
        columns.add("DATE");
        columns.add("TIME");
        columns.add("TOTAL_FARE");

        Vector<Vector<Object>> data = new Vector<>();
        for (Booking b : DataStore.getBookings()) {
            Vector<Object> row = new Vector<>();
            row.add(b.userName());
            row.add(b.flightNo());
            row.add(b.source());
            row.add(b.destination());
            row.add(b.date());
            row.add(b.time());
            row.add(b.totalFare());
            data.add(row);
        }
        return new DefaultTableModel(data, columns);
    }
}
