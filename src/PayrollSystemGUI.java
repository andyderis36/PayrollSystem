
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

    private JTextField fullNameField, staffNumberField, monthYearField, icNumberField, bankAccountNumberField, totalCarsSoldField, totalAmountSoldField;
    private JTextArea displayArea;
    private List<Salesman> salesmen;

    public PayrollSystemGUI() {
        setTitle("Salesman Payroll System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Container container = getContentPane();
        container.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addLabelAndField(formPanel, gbc, "Full Name:", fullNameField = new JTextField(), 0);
        addLabelAndField(formPanel, gbc, "Staff Number:", staffNumberField = new JTextField(), 1);
        addLabelAndField(formPanel, gbc, "Month/Year:", monthYearField = new JTextField(), 2);
        addLabelAndField(formPanel, gbc, "IC Number:", icNumberField = new JTextField(), 3);
        addLabelAndField(formPanel, gbc, "Bank Account Number:", bankAccountNumberField = new JTextField(), 4);
        addLabelAndField(formPanel, gbc, "Total Cars Sold:", totalCarsSoldField = new JTextField(), 5);
        addLabelAndField(formPanel, gbc, "Total Amount Sold:", totalAmountSoldField = new JTextField(), 6);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addButton(buttonPanel, "Calculate and Save", e -> calculateAndSaveData());
        addButton(buttonPanel, "Search", e -> searchSalesman());
        addButton(buttonPanel, "Display All", e -> displayAllSalesmen());
        addButton(buttonPanel, "Edit", e -> editSalesman());
        addButton(buttonPanel, "Delete", e -> deleteSalesman());
        addButton(buttonPanel, "Reset", e -> resetFields());
        addButton(buttonPanel, "Exit", e -> System.exit(0));

        displayArea = new JTextArea(10, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        container.add(formPanel, BorderLayout.NORTH);
        container.add(buttonPanel, BorderLayout.CENTER);
        container.add(scrollPane, BorderLayout.SOUTH);

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
            String monthYear = monthYearField.getText();
            String icNumber = icNumberField.getText();
            String bankAccountNumber = bankAccountNumberField.getText();
            int totalCarsSold = Integer.parseInt(totalCarsSoldField.getText());
            double totalAmountSold = Double.parseDouble(totalAmountSoldField.getText());

            Salesman salesman = new Salesman(fullName, staffNumber, monthYear, icNumber, bankAccountNumber, totalCarsSold, totalAmountSold);
            salesmen.add(salesman);
            saveSalesmen();

            displayArea.setText("Salesman data saved successfully.\n \n" + salesman.toString());
        } catch (NumberFormatException e) {
            displayArea.setText("Error: Please enter valid numbers for total cars sold and total amount sold.");
        }
    }

    private void searchSalesman() {
        String fullName = fullNameField.getText();
        for (Salesman salesman : salesmen) {
            if (salesman.getFullName().equalsIgnoreCase(fullName)) {
                displayArea.setText(salesman.toString());
                return;
            }
        }
        displayArea.setText("Salesman not found.");
    }

    private void displayAllSalesmen() {
        StringBuilder allSalesmen = new StringBuilder();
        for (int i = 0; i < salesmen.size(); i++) {
            allSalesmen.append("*** SALESMAN - ").append(i + 1).append(" *** \n \n");
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
                    salesman.setMonthYear(monthYearField.getText());
                    salesman.setIcNumber(icNumberField.getText());
                    salesman.setBankAccountNumber(bankAccountNumberField.getText());
                    salesman.setTotalCarsSold(Integer.parseInt(totalCarsSoldField.getText()));
                    salesman.setTotalAmountSold(Double.parseDouble(totalAmountSoldField.getText()));
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
        monthYearField.setText("");
        icNumberField.setText("");
        bankAccountNumberField.setText("");
        totalCarsSoldField.setText("");
        totalAmountSoldField.setText("");
        displayArea.setText("");
    }

    private void saveSalesmen() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("salesmen.dat"))) {
            oos.writeObject(salesmen);
        } catch (IOException e) {
            displayArea.setText("Error: Unable to save salesmen data.");
        }
    }

    private void loadSalesmen() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("salesmen.dat"))) {
            salesmen = (List<Salesman>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            salesmen = new ArrayList<>();
        }
    }

    public static class Salesman implements Serializable {

        private static final long serialVersionUID = 1L;
        private String fullName;
        private String staffNumber;
        private String monthYear;
        private String icNumber;
        private String bankAccountNumber;
        private int totalCarsSold;
        private double totalAmountSold;
        private double carBodyCommission;
        private double incentiveCommission;
        private double grossSalary;
        private double epf;
        private double incomeTax;
        private double netSalary;

        public Salesman(String fullName, String staffNumber, String monthYear, String icNumber, String bankAccountNumber, int totalCarsSold, double totalAmountSold) {
            this.fullName = fullName;
            this.staffNumber = staffNumber;
            this.monthYear = monthYear;
            this.icNumber = icNumber;
            this.bankAccountNumber = bankAccountNumber;
            this.totalCarsSold = totalCarsSold;
            this.totalAmountSold = totalAmountSold;
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

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("┌─────────────────────┐\n");
            sb.append(String.format("│ %-19s │\n", "Salesman Data"));
            sb.append("├─────────────────────┤\n");
            sb.append(String.format("│ %-18s: %-14s │\n", "Full Name", fullName));
            sb.append(String.format("│ %-18s: %-14s │\n", "Staff Number", staffNumber));
            sb.append(String.format("│ %-18s: %-14s │\n", "Month/Year", monthYear));
            sb.append(String.format("│ %-18s: %-14s │\n", "IC Number", icNumber));
            sb.append(String.format("│ %-18s: %-14s │\n", "Bank Account", bankAccountNumber));
            sb.append(String.format("│ %-18s: %-14d │\n", "Total Cars Sold", totalCarsSold));
            sb.append(String.format("│ %-18s: %-14.2f │\n", "Total Amount Sold", totalAmountSold));
            sb.append(String.format("│ %-18s: %-14.2f │\n", "Car Body Commission", carBodyCommission));
            sb.append(String.format("│ %-18s: %-14.2f │\n", "Incentive Commission", incentiveCommission));
            sb.append(String.format("│ %-18s: %-14.2f │\n", "Gross Salary", grossSalary));
            sb.append(String.format("│ %-18s: %-14.2f │\n", "EPF", epf));
            sb.append(String.format("│ %-18s: %-14.2f │\n", "Income Tax", incomeTax));
            sb.append(String.format("│ %-18s: %-14.2f │\n", "Net Salary", netSalary));
            sb.append("└─────────────────────┘\n");

            return sb.toString();
        }

    }
}
