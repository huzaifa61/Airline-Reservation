package org.airlineReservationSystem;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.airlineReservationSystem.DataStore.*;

public class Airplane {
    private JFrame planeF;
    private JPanel airPanel;

    // Top panel fields
    private JTextField nameData;
    private JTextField sourceData;
    private JTextField destination;
    private JSpinner spinner1;
    private JDateChooser dateChooser;

    // Center panel fields
    private JTable table1;

    // Bottom panel fields
    private JLabel fare;
    private JTextArea boarding;
    private JButton SEARCHButton;
    private JButton BOOKButton;
    private JButton RESETButton;

    public Airplane() {
        // (Optional) Set system look-and-feel + bigger font for visibility
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Font font = new Font("Arial", Font.PLAIN, 16);
            UIManager.put("Label.font", font);
            UIManager.put("TextField.font", font);
            UIManager.put("Button.font", font);
            UIManager.put("Table.font", font);
            UIManager.put("TableHeader.font", font);
            UIManager.put("TextArea.font", font);
            UIManager.put("Spinner.font", font);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1) Create main window
        planeF = new JFrame("User Booking Page");
        planeF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // or EXIT_ON_CLOSE

        // 2) Create a main panel with BorderLayout
        airPanel = new JPanel(new BorderLayout());

        // -------------------------------------------------------------------
        // TOP PANEL (Name, Source, Destination, Passengers)
        // -------------------------------------------------------------------
        JPanel topPanel = new JPanel(new GridLayout(2, 4, 5, 5));

        nameData = new JTextField();
        sourceData = new JTextField();
        destination = new JTextField();
        spinner1 = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        dateChooser = new JDateChooser();

        topPanel.add(new JLabel("Name: "));
        topPanel.add(nameData);
        topPanel.add(new JLabel("Source: "));
        topPanel.add(sourceData);
        topPanel.add(new JLabel("Destination: "));
        topPanel.add(destination);
        topPanel.add(new JLabel("Passengers: "));
        topPanel.add(spinner1);

        // -------------------------------------------------------------------
        // CENTER PANEL (Date chooser + Table)
        // -------------------------------------------------------------------
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Date sub-panel
        JPanel datePanel = new JPanel();
        datePanel.add(new JLabel("Date: "));
        datePanel.add(dateChooser);
        centerPanel.add(datePanel, BorderLayout.NORTH);

        // Table
        table1 = new JTable();
        JScrollPane scrollPane = new JScrollPane(table1);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // -------------------------------------------------------------------
        // BOTTOM PANEL (Fare, Boarding Pass, Buttons)
        // -------------------------------------------------------------------
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Fare row
        JPanel farePanel = new JPanel();
        farePanel.add(new JLabel("Fare: "));
        fare = new JLabel("0");
        farePanel.add(fare);

        // Boarding pass area
        boarding = new JTextArea(8, 30);
        JScrollPane boardingScroll = new JScrollPane(boarding);

        // Buttons row
        JPanel buttonPanel = new JPanel();
        SEARCHButton = new JButton("SEARCH");
        BOOKButton = new JButton("BOOK");
        RESETButton = new JButton("RESET");

        buttonPanel.add(SEARCHButton);
        buttonPanel.add(BOOKButton);
        buttonPanel.add(RESETButton);

        bottomPanel.add(farePanel, BorderLayout.NORTH);
        bottomPanel.add(boardingScroll, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // -------------------------------------------------------------------
        // Add panels to the main panel
        // -------------------------------------------------------------------
        airPanel.add(topPanel, BorderLayout.NORTH);
        airPanel.add(centerPanel, BorderLayout.CENTER);
        airPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 3) Final setup of JFrame
        planeF.setContentPane(airPanel);
        planeF.pack();
        planeF.setLocationRelativeTo(null);
        planeF.setVisible(true);

        // 4) Add event listeners
        SEARCHButton.addActionListener(e -> searchFlights());
        BOOKButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "FIND YOUR BOARDING PASS ATTACHED");
            printPass();
            // Also store booking in DataStore
            storeBooking();
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateFare();
            }
        });
        RESETButton.addActionListener(e -> {
            planeF.dispose();
            new Airplane();
        });
    }

    // -----------------------------------------------------------------------
    // 1) SEARCH FLIGHTS (in-memory)
    // -----------------------------------------------------------------------
    private void searchFlights() {
        if (destination.getText().isEmpty() || sourceData.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill SOURCE & DESTINATION to search.");
            return;
        }

        String src = sourceData.getText().trim().toUpperCase();
        String dst = destination.getText().trim().toUpperCase();

        // Filter flights by source/destination
        List<Flight> results = new ArrayList<>();
        for (Flight f : DataStore.FLIGHTS) {
            if (f.source().equalsIgnoreCase(src) && f.destination().equalsIgnoreCase(dst)) {
                results.add(f);
            }
        }

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(null, "NO FLIGHTS AVAILABLE");
            table1.setModel(new DefaultTableModel()); // Clear table
        } else {
            table1.setModel(buildTableModelFromFlights(results));
        }
    }

    // -----------------------------------------------------------------------
    // 2) UPDATE FARE (when user clicks a row in the table)
    // -----------------------------------------------------------------------
    private void updateFare() {
        DefaultTableModel dm = (DefaultTableModel) table1.getModel();
        int selectedRow = table1.getSelectedRow();
        if (selectedRow >= 0) {
            // Price is column 4
            int pricePerSeat = (int) dm.getValueAt(selectedRow, 4);
            int totalFare = pricePerSeat * (int) spinner1.getValue();
            fare.setText(String.valueOf(totalFare));
        }
    }

    // -----------------------------------------------------------------------
    // 3) PRINT BOARDING PASS
    // -----------------------------------------------------------------------
    private void printPass() {
        DefaultTableModel dms = (DefaultTableModel) table1.getModel();
        int selectedRow = table1.getSelectedRow();

        // Validate selection & date
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a flight from the table.");
            return;
        }
        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Please select a valid date.");
            return;
        }

        // Gather flight & user data
        String date = DateFormat.getDateInstance().format(dateChooser.getDate());
        String flightNo = dms.getValueAt(selectedRow, 0).toString();
        String time = dms.getValueAt(selectedRow, 3).toString();
        String totalAmount = fare.getText();

        boarding.setText(String.format("""
                BOARDING PASS
                ------***------

                NAME: %s
                FLIGHT NO: %s
                SOURCE: %s
                DESTINATION: %s
                DATE OF JOURNEY: %s
                TIME: %s
                TOTAL AMOUNT: ₹%s
                """,
                nameData.getText(),
                flightNo,
                sourceData.getText(),
                destination.getText(),
                date,
                time,
                totalAmount
        ));
    }

    // -----------------------------------------------------------------------
    // 4) STORE BOOKING in DataStore
    // -----------------------------------------------------------------------
    private void storeBooking() {
        DefaultTableModel dm = (DefaultTableModel) table1.getModel();
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1 || dateChooser.getDate() == null) {
            return; // skip storing if no valid selection
        }

        String date = DateFormat.getDateInstance().format(dateChooser.getDate());
        String flightNo = dm.getValueAt(selectedRow, 0).toString();
        String time = dm.getValueAt(selectedRow, 3).toString();
        int totalAmount = Integer.parseInt(fare.getText());

        DataStore.Booking booking = new DataStore.Booking(
                nameData.getText(),
                flightNo,
                sourceData.getText(),
                destination.getText(),
                date,
                time,
                totalAmount
        );

        DataStore.addBooking(booking);
    }

    // -----------------------------------------------------------------------
    // 5) BUILD TABLE MODEL FROM A LIST OF FLIGHTS
    // -----------------------------------------------------------------------
    private DefaultTableModel buildTableModelFromFlights(List<Flight> flights) {
        // Columns: 0=FLIGHT_NO, 1=SOURCE, 2=DESTINATION, 3=TIME, 4=PRICE
        Vector<String> columnNames = new Vector<>();
        columnNames.add("FLIGHT_NO");
        columnNames.add("SOURCE");
        columnNames.add("DESTINATION");
        columnNames.add("TIME");
        columnNames.add("PRICE");

        Vector<Vector<Object>> data = new Vector<>();
        for (Flight f : flights) {
            Vector<Object> row = new Vector<>();
            row.add(f.flightNo());
            row.add(f.source());
            row.add(f.destination());
            row.add(f.time());
            row.add(f.price());
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }
}
