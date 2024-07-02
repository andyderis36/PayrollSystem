
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.w3c.dom.Text;

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
        searchPanel.add(new JLabel("Search Name or Staff Number:"));
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

        // Add form panel and button panel to the container
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

        // Add label above display area
        JLabel displayLabel = new JLabel("Search Results:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.gridwidth = 2;
        container.add(displayLabel, gbc);

        // Display area
        displayArea = new JTextArea(20, 30);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        gbc.gridx = 0;
        gbc.gridy = 5;
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

            JOptionPane.showMessageDialog(this, "Salesman data saved successfully.", "Calculate and Save", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Please enter valid String or Integer!", "Calculate and Save", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void searchSalesman() {
        String searchValue = searchField.getText().trim().toLowerCase();

        if (searchValue.isEmpty()) {
            //displayArea.setText("Search field is empty. Please enter a search value.");
            JOptionPane.showMessageDialog(this, "Search field is empty. Please enter a search value.", "Search", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

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
            //displayArea.setText("Salesman not found.");
            JOptionPane.showMessageDialog(this, "Salesman not found.", "Search", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void displayAllSalesmen() {
        if (salesmen.isEmpty()) {
            //displayArea.setText("No salesman data available.");
            JOptionPane.showMessageDialog(this, "No salesmen data available.", "Display", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder allSalesmen = new StringBuilder();
            for (int i = 0; i < salesmen.size(); i++) {
                allSalesmen.append("[ SALESMAN - ").append(i + 1).append(" ] \n");
                allSalesmen.append(salesmen.get(i).toString()).append("\n");
            }
            displayArea.setText(allSalesmen.toString());
        }
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
                    //displayArea.setText("Salesman data updated successfully.\n" + salesman.toString());
                    JOptionPane.showMessageDialog(this, "Salesman data updated successfully.", "Edit", JOptionPane.WARNING_MESSAGE);
                } catch (NumberFormatException e) {
                    displayArea.setText("Error: Please enter valid numbers for total cars sold and total amount sold.");
                    JOptionPane.showMessageDialog(this, "Error: Please enter valid String or Integer!", "Edit", JOptionPane.WARNING_MESSAGE);
                }
                return;
            }
        }
        displayArea.setText("Salesman not found.");
    }

    private void deleteSalesman() {
        String fullName = fullNameField.getText().trim();
    
        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Salesman Full Name is empty. Please enter a Full Name to delete.", "Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
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
            JOptionPane.showMessageDialog(this, "Salesman data deleted successfully.", "Delete", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Salesman not found.", "Delete", JOptionPane.INFORMATION_MESSAGE);
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

        // Display a message dialog
        JOptionPane.showMessageDialog(this, "All fields have been reset.", "Reset", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveSalesmen() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("salesmen.dat"))) {
            oos.writeObject(salesmen);
        } catch (IOException e) {
            //displayArea.setText("Error: Could not save salesmen data.");
            JOptionPane.showMessageDialog(this, "Error: Could not save salesmen data.", "Save", JOptionPane.INFORMATION_MESSAGE);
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
    private double totalAmountSold, carBodyCommission, incentiveCommission, basicSalary, grossSalary, epf, incomeTax, netSalary;

    public Salesman(String fullName, String staffNumber, String monthYear, String icNumber, String bankAccountNumber, int totalCarsSold, double totalAmountSold, String status) {
        this.fullName = fullName;
        this.staffNumber = staffNumber;
        this.monthYear = monthYear;
        this.icNumber = icNumber;
        this.bankAccountNumber = bankAccountNumber;
        this.totalCarsSold = totalCarsSold;
        this.totalAmountSold = totalAmountSold;
        this.status = status;
        this.basicSalary = 1500;
        calculateCommissions();
        calculateSalaries();
    }

    public void calculateCommissions() {
        this.carBodyCommission = 0.01 * this.totalAmountSold;

        if (totalCarsSold >= 5 && totalCarsSold <= 9) {
            this.incentiveCommission = totalCarsSold * 200;
        } else if (totalCarsSold >= 10 && totalCarsSold <= 14) {
            this.incentiveCommission = totalCarsSold * 400;
        } else if (totalCarsSold > 14) {
            this.incentiveCommission = totalCarsSold * 600;
        } else {
            this.incentiveCommission = 0;
        }
    }

    public void calculateSalaries() {
        double basicSalary = 1500;
        this.grossSalary = basicSalary + carBodyCommission + incentiveCommission;
        this.epf = 0.11 * grossSalary;
        this.incomeTax = calculateIncomeTax(grossSalary);
        this.netSalary = grossSalary - epf - incomeTax;
    }

    private double calculateIncomeTax(double grossSalary) {
        double tax = 0;
        if (grossSalary <= 416.67) {
            tax = 0;
        } else if (grossSalary <= 1666.67) {
            tax = (grossSalary - 416.67) * 0.01;
        } else if (grossSalary <= 2916.67) {
            tax = 12.5 + (grossSalary - 1666.67) * 0.03;
        } else if (grossSalary <= 4166.67) {
            tax = 50 + (grossSalary - 2916.67) * 0.08;
        } else if (grossSalary <= 5833.33) {
            tax = 150 + (grossSalary - 4166.67) * 0.13;
        } else if (grossSalary <= 8333.33) {
            tax = 362.5 + (grossSalary - 5833.33) * 0.21;
        } else if (grossSalary <= 20833.33) {
            tax = 887.5 + (grossSalary - 8333.33) * 0.24;
        } else if (grossSalary <= 33333.33) {
            tax = 4000 + (grossSalary - 20833.33) * 0.245;
        } else if (grossSalary <= 50000.00) {
            tax = 7075 + (grossSalary - 33333.33) * 0.25;
        } else if (grossSalary <= 83333.33) {
            tax = 11000 + (grossSalary - 50000) * 0.26;
        } else if (grossSalary <= 166666.67) {
            tax = 20433.33 + (grossSalary - 83333.33) * 0.28;
        } else {
            tax = 43716.67 + (grossSalary - 166666.67) * 0.30;
        }
        return tax;
    }

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public int getTotalCarsSold() {
        return totalCarsSold;
    }

    public void setTotalCarsSold(int totalCarsSold) {
        this.totalCarsSold = totalCarsSold;
    }

    public double getTotalAmountSold() {
        return totalAmountSold;
    }

    public void setTotalAmountSold(double totalAmountSold) {
        this.totalAmountSold = totalAmountSold;
    }

    public double getCarBodyCommission() {
        return carBodyCommission;
    }

    public void setCarBodyCommission(double carBodyCommission) {
        this.carBodyCommission = carBodyCommission;
    }

    public double getIncentiveCommission() {
        return incentiveCommission;
    }

    public void setIncentiveCommission(double incentiveCommission) {
        this.incentiveCommission = incentiveCommission;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public double getEpf() {
        return epf;
    }

    public void setEpf(double epf) {
        this.epf = epf;
    }

    public double getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(double incomeTax) {
        this.incomeTax = incomeTax;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "===================================================\n"
                + "================= SALESMAN INFORMATION =================\n"
                + "===================================================\n"
                + "Full Name: " + fullName + "\n"
                + "Staff Number: " + staffNumber + "\n"
                + "Date: " + monthYear + "\n"
                + "IC Number: " + icNumber + "\n"
                + "Bank Account Number: " + bankAccountNumber + "\n"
                + "Total Cars Sold: " + totalCarsSold + "\n"
                + "Total Amount Sold: RM " + totalAmountSold + "\n"
                + "---------------------------------------------------------\n"
                + "Basic Salary: RM " + basicSalary + "/Month" + "\n"
                + "Car Body Commission: RM " + carBodyCommission + "\n"
                + "Incentive Commission: RM " + incentiveCommission + "\n"
                + "Gross Salary: RM " + grossSalary + "/Month" + "\n"
                + "EPF: RM " + epf + "\n"
                + "Income Tax: RM " + incomeTax + "\n"
                + "Net Salary: RM " + netSalary + "/Month" + "\n"
                + "Status: " + status + "\n"
                + "===================================================\n";
    }
}
