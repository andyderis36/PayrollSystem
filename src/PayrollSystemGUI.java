import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class PayrollSystemGUI extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PayrollSystemGUI().setVisible(true));
    }

    private JTextField fullNameField, staffNumberField, icNumberField, bankAccountNumberField, totalCarsSoldField, totalAmountSoldField, searchField;
    private JTextArea displayArea;
    private JComboBox<String> monthComboBox, yearComboBox;
    private JRadioButton contractRadioButton, permanentRadioButton;
    private ButtonGroup statusGroup;

    private List<Salesman> salesmen;

    public PayrollSystemGUI() {
        setTitle("Salesman Payroll System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use GridBagLayout for main content layout
        Container container = getContentPane();
        container.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchPanel.add(new JLabel("Search by Full Name or Staff Number:"));
        searchPanel.add(searchField);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        container.add(searchPanel, gbc);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(5, 5, 5, 5);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formGbc.gridx = 0;
        formGbc.gridy = 1;

        addLabelAndField(formPanel, formGbc, "Full Name:", fullNameField = new JTextField(), 0);
        addLabelAndField(formPanel, formGbc, "Staff Number:", staffNumberField = new JTextField(), 1);

        // ComboBox for Month
        formGbc.gridx = 0;
        formGbc.gridy = 2;
        formPanel.add(new JLabel("Month:"), formGbc);
        formGbc.gridx = 1;
        monthComboBox = new JComboBox<>(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        formPanel.add(monthComboBox, formGbc);

        // ComboBox for Year
        formGbc.gridx = 0;
        formGbc.gridy = 3;
        formPanel.add(new JLabel("Year:"), formGbc);
        formGbc.gridx = 1;
        yearComboBox = new JComboBox<>(new String[]{"2024", "2025", "2026"}); // Update with relevant years
        formPanel.add(yearComboBox, formGbc);

        addLabelAndField(formPanel, formGbc, "IC Number:", icNumberField = new JTextField(), 4);
        addLabelAndField(formPanel, formGbc, "Bank Account Number:", bankAccountNumberField = new JTextField(), 5);
        addLabelAndField(formPanel, formGbc, "Total Cars Sold:", totalCarsSoldField = new JTextField(), 6);
        addLabelAndField(formPanel, formGbc, "Total Amount Sold:", totalAmountSoldField = new JTextField(), 7);

        // Radio buttons for Employment Status
        formGbc.gridx = 0;
        formGbc.gridy = 8;
        formPanel.add(new JLabel("Employment Status:"), formGbc);
        formGbc.gridx = 1;
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contractRadioButton = new JRadioButton("Contract");
        permanentRadioButton = new JRadioButton("Permanent");
        statusGroup = new ButtonGroup();
        statusGroup.add(contractRadioButton);
        statusGroup.add(permanentRadioButton);
        statusPanel.add(contractRadioButton);
        statusPanel.add(permanentRadioButton);
        formPanel.add(statusPanel, formGbc);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addButton(buttonPanel, "Calculate and Save", e -> calculateAndSaveData());
        addButton(buttonPanel, "Search", e -> searchSalesman());
        addButton(buttonPanel, "Display All", e -> displayAllSalesmen());
        addButton(buttonPanel, "Edit", e -> editSalesman());
        addButton(buttonPanel, "Delete", e -> deleteSalesman());
        addButton(buttonPanel, "Reset", e -> resetFields());
        addButton(buttonPanel, "Exit", e -> System.exit(0));

        displayArea = new JTextArea(20, 30);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        container.add(formPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        container.add(buttonPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        container.add(scrollPane, gbc);

        pack(); // Pack components to set the size based on preferred sizes
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);

        loadSalesmen();
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        field.setColumns(20);
        field.setToolTipText(label);
        panel.add(field, gbc);
    }

    private void addButton(JPanel panel, String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setToolTipText(text);
        panel.add(button);
    }

    private void calculateAndSaveData() {
        try {
            String fullName = fullNameField.getText();
            String staffNumber = staffNumberField.getText();
            String month = (String) monthComboBox.getSelectedItem();
            String year = (String) yearComboBox.getSelectedItem();
            String monthYear = month + " " + year;
            String icNumber = icNumberField.getText();
            String bankAccountNumber = bankAccountNumberField.getText();
            int totalCarsSold = Integer.parseInt(totalCarsSoldField.getText());
            double totalAmountSold = Double.parseDouble(totalAmountSoldField.getText());
            String status = contractRadioButton.isSelected() ? "Contract" : "Permanent";

            Salesman salesman = new Salesman(fullName, staffNumber, monthYear, icNumber, bankAccountNumber, totalCarsSold, totalAmountSold, status);
            salesmen.add(salesman);
            saveSalesmen();

            displayArea.setText("Salesman data saved successfully.\n \n" + salesman.toString());
        } catch (NumberFormatException e) {
            displayArea.setText("Error: Please enter valid numbers for total cars sold and total amount sold.");
        }
    }

    private void searchSalesman() {
        String searchValue = searchField.getText().trim().toLowerCase();
        boolean found = false;
        StringBuilder result = new StringBuilder();

        for (Salesman salesman : salesmen) {
            if (salesman.getFullName().toLowerCase().contains(searchValue) || salesman.getStaffNumber().equalsIgnoreCase(searchValue)) {
                result.append(salesman.toString()).append("\n\n");
                found = true;
            }
        }

        if (found) {
            displayArea.setText(result.toString());
        } else {
            displayArea.setText("Salesman not found.");
        }
    }

    private void displayAllSalesmen() {
        StringBuilder allSalesmen = new StringBuilder();
        for (int i = 0; i < salesmen.size(); i++) {
            allSalesmen.append("[ SALESMAN - ").append(i + 1).append(" ] \n");
            allSalesmen.append(salesmen.get(i).toString()).append("\n");
        }
        displayArea.setText(allSalesmen.toString());
    }

    private void editSalesman() {
        String fullName = fullNameField.getText();
        for (Salesman salesman : salesmen) {
            if (salesman.getFullName().equalsIgnoreCase(fullName)) {
                try {
                    salesman.setStaffNumber(staffNumberField.getText());
                    String month = (String) monthComboBox.getSelectedItem();
                    String year = (String) yearComboBox.getSelectedItem();
                    salesman.setMonthYear(month + " " + year);
                    salesman.setIcNumber(icNumberField.getText());
                    salesman.setBankAccountNumber(bankAccountNumberField.getText());
                    salesman.setTotalCarsSold(Integer.parseInt(totalCarsSoldField.getText()));
                    salesman.setTotalAmountSold(Double.parseDouble(totalAmountSoldField.getText()));
                    salesman.setStatus(contractRadioButton.isSelected() ? "Contract" : "Permanent");
                    salesman.calculateCommissions();
                    salesman.calculateSalaries();
                    saveSalesmen();
                    displayArea.setText("Salesman data updated successfully.\n" + salesman.toString());
                } catch (NumberFormatException e) {
                    displayArea.setText("Error: Please enter valid numbers for total cars sold and total amount sold.");
                }
                return;
            }
        }
        displayArea.setText("Salesman not found.");
    }

    private void deleteSalesman() {
        String fullName = fullNameField.getText();
        Salesman toRemove = null;
        for (Salesman salesman : salesmen) {
            if (salesman.getFullName().equalsIgnoreCase(fullName)) {
                toRemove = salesman;
                break;
            }
        }
        if (toRemove != null) {
            salesmen.remove(toRemove);
            saveSalesmen();
            displayArea.setText("Salesman data deleted successfully.");
        } else {
            displayArea.setText("Salesman not found.");
        }
    }

    private void resetFields() {
        fullNameField.setText("");
        staffNumberField.setText("");
        monthComboBox.setSelectedIndex(0);
        yearComboBox.setSelectedIndex(0);
        icNumberField.setText("");
        bankAccountNumberField.setText("");
        totalCarsSoldField.setText("");
        totalAmountSoldField.setText("");
        statusGroup.clearSelection();
        displayArea.setText("");
        searchField.setText("");
    }

    private void saveSalesmen() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("salesmen.dat"))) {
            oos.writeObject(salesmen);
        } catch (IOException e) {
            displayArea.setText("Error: Could not save salesmen data.");
        }
    }

    private void loadSalesmen() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("salesmen.dat"))) {
            salesmen = (List<Salesman>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            salesmen = new ArrayList<>();
        }
    }
}

class Salesman implements Serializable {

    private String fullName, staffNumber, monthYear, icNumber, bankAccountNumber, status;
    private int totalCarsSold;
    private double totalAmountSold, commissions, salaries;

    public Salesman(String fullName, String staffNumber, String monthYear, String icNumber, String bankAccountNumber, int totalCarsSold, double totalAmountSold, String status) {
        this.fullName = fullName;
        this.staffNumber = staffNumber;
        this.monthYear = monthYear;
        this.icNumber = icNumber;
        this.bankAccountNumber = bankAccountNumber;
        this.totalCarsSold = totalCarsSold;
        this.totalAmountSold = totalAmountSold;
        this.status = status;
        calculateCommissions();
        calculateSalaries();
    }

    public String getFullName() {
        return fullName;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setTotalCarsSold(int totalCarsSold) {
        this.totalCarsSold = totalCarsSold;
    }

    public void setTotalAmountSold(double totalAmountSold) {
        this.totalAmountSold = totalAmountSold;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void calculateCommissions() {
        this.commissions = totalAmountSold * 0.02;
    }

    public void calculateSalaries() {
        this.salaries = 2000 + commissions;
    }

    @Override
    public String toString() {
        return "Full Name: " + fullName +
                "\nStaff Number: " + staffNumber +
                "\nMonth/Year: " + monthYear +
                "\nIC Number: " + icNumber +
                "\nBank Account Number: " + bankAccountNumber +
                "\nTotal Cars Sold: " + totalCarsSold +
                "\nTotal Amount Sold: " + totalAmountSold +
                "\nEmployment Status: " + status +
                "\nCommissions: " + commissions +
                "\nSalaries: " + salaries;
    }
}
