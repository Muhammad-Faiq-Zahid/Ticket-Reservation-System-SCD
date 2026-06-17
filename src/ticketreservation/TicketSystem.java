package ticketreservation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import ticketreservation.exception.ReservationException;
import ticketreservation.model.ReservationManager;
import ticketreservation.model.Seat;
import ticketreservation.model.SeatCategory;

public class TicketSystem extends JFrame {
    private final ReservationManager reservationManager = new ReservationManager();

    private final JComboBox<SeatCategory> categoryComboBox = new JComboBox<>();
    private final JTextField seatNumberField = new JTextField(16);
    private final JTextField customerNameField = new JTextField(16);
    private final JTextArea outputArea = new JTextArea(7, 32);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[] { "Seat No", "Category", "Customer", "Price", "Status" }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public TicketSystem() {
        super("Ticket Reservation System");
        initializeLookAndFeel();
        buildInterface();
        refreshSeatTable();
    }

    public void bookSeat(String category, String seatNumber, String customerName) {
        try {
            Seat seat = reservationManager.bookSeat(category, seatNumber, customerName);
            outputArea.setText("Booking successful.\n\n" + seat.getDisplayText());
            clearInputFields();
            refreshSeatTable();
        } catch (ReservationException | IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    public void cancelSeat(String seatNumber) {
        try {
            Seat seat = reservationManager.cancelSeat(seatNumber);
            outputArea.setText("Cancellation successful.\n\nCancelled seat: " + seat.getSeatNumber());
            clearInputFields();
            refreshSeatTable();
        } catch (ReservationException ex) {
            showError(ex.getMessage());
        }
    }

    public void undoBooking() {
        try {
            outputArea.setText(reservationManager.undoBooking());
            refreshSeatTable();
        } catch (ReservationException ex) {
            showError(ex.getMessage());
        }
    }

    public void undoCancellation() {
        try {
            outputArea.setText(reservationManager.undoCancellation());
            refreshSeatTable();
        } catch (ReservationException ex) {
            showError(ex.getMessage());
        }
    }

    public void displaySeatInfo(String seatNumber) {
        try {
            Seat seat = reservationManager.getSeat(seatNumber);
            if (seat == null) {
                outputArea.setText("Seat is available or has not been booked yet.");
            } else {
                outputArea.setText(seat.getDisplayText());
            }
        } catch (ReservationException ex) {
            showError(ex.getMessage());
        }
    }

    public void clearInputFields() {
        seatNumberField.setText("");
        customerNameField.setText("");
        categoryComboBox.setSelectedIndex(0);
        seatNumberField.requestFocusInWindow();
    }

    private void buildInterface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(860, 560));

        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        root.setBackground(new Color(245, 247, 250));

        root.add(createHeaderPanel(), BorderLayout.NORTH);
        root.add(createFormPanel(), BorderLayout.WEST);
        root.add(createTablePanel(), BorderLayout.CENTER);
        root.add(createOutputPanel(), BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("Ticket Reservation System");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(30, 45, 65));

        JLabel subtitle = new JLabel("Book, cancel, undo, and view seat reservations");
        subtitle.setForeground(new Color(85, 95, 110));

        panel.add(title, BorderLayout.NORTH);
        panel.add(subtitle, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(285, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 220, 228)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        categoryComboBox.setModel(new DefaultComboBoxModel<>(SeatCategory.values()));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 0, 7, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        addLabeledField(panel, gbc, "Category", categoryComboBox);
        addLabeledField(panel, gbc, "Seat Number", seatNumberField);
        addLabeledField(panel, gbc, "Customer Name", customerNameField);

        JButton bookButton = new JButton("Book Seat");
        JButton cancelButton = new JButton("Cancel Booking");
        JButton viewButton = new JButton("View Seat Info");
        JButton undoBookingButton = new JButton("Undo Booking");
        JButton undoCancellationButton = new JButton("Undo Cancellation");
        JButton clearButton = new JButton("Clear");

        bookButton.addActionListener(event -> bookSeat(
                categoryComboBox.getSelectedItem().toString(),
                seatNumberField.getText(),
                customerNameField.getText()));
        cancelButton.addActionListener(event -> cancelSeat(seatNumberField.getText()));
        viewButton.addActionListener(event -> displaySeatInfo(seatNumberField.getText()));
        undoBookingButton.addActionListener(event -> undoBooking());
        undoCancellationButton.addActionListener(event -> undoCancellation());
        clearButton.addActionListener(event -> clearInputFields());

        addButton(panel, gbc, bookButton);
        addButton(panel, gbc, cancelButton);
        addButton(panel, gbc, viewButton);
        addButton(panel, gbc, undoBookingButton);
        addButton(panel, gbc, undoCancellationButton);
        addButton(panel, gbc, clearButton);

        gbc.weighty = 1;
        panel.add(new JLabel(), gbc);
        return panel;
    }

    private void addLabeledField(JPanel panel, GridBagConstraints gbc, String labelText,
            java.awt.Component field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridy++;
        panel.add(label, gbc);
        gbc.gridy++;
        panel.add(field, gbc);
    }

    private void addButton(JPanel panel, GridBagConstraints gbc, JButton button) {
        gbc.gridy++;
        button.setFocusPainted(false);
        panel.add(button, gbc);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JLabel label = new JLabel("Current Reservations");
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(new Color(30, 45, 65));

        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);

        table.getSelectionModel().addListSelectionListener(event -> {
            int row = table.getSelectedRow();
            if (!event.getValueIsAdjusting() && row >= 0) {
                seatNumberField.setText(tableModel.getValueAt(row, 0).toString());
            }
        });

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JLabel label = new JLabel("Result");
        label.setFont(new Font("SansSerif", Font.BOLD, 14));

        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        return panel;
    }

    private void refreshSeatTable() {
        tableModel.setRowCount(0);
        Collection<Seat> seats = reservationManager.getAllSeats();
        for (Seat seat : seats) {
            tableModel.addRow(new Object[] {
                    seat.getSeatNumber(),
                    seat.getCategory(),
                    seat.getCustomerName(),
                    "Rs. " + seat.getCategory().getPrice(),
                    seat.isBooked() ? "Booked" : "Available"
            });
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Reservation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void initializeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            System.err.println("Could not load system look and feel: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicketSystem().setVisible(true));
    }
}

