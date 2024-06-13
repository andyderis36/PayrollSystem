
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollSystemGUI extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PayrollSystemGUI().setVisible(true);
            }
        });
    }

    private JTextField fullNameField, staffNumberField, monthYearField, icNumberField, bankAccountNumberField, totalCarsSoldField, totalAmountSoldField;
    private JTextArea displayArea;
    private List<Salesman> salesmen;

    public PayrollSystemGUI() {
        salesmen = new ArrayList<>();
        setTitle("Salesman Payroll System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Container container = getContentPane();
        container.setLayout(new GridLayout(13, 2)); // Adjusted layout rows for buttons and display area

        container.add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        container.add(fullNameField);

        container.add(new JLabel("Staff Number:"));
        staffNumberField = new JTextField();
        container.add(staffNumberField);

        container.add(new JLabel("Month/Year:"));
        monthYearField = new JTextField();
        container.add(monthYearField);

        container.add(new JLabel("IC Number:"));
        icNumberField = new JTextField();
        container.add(icNumberField);

        container.add(new JLabel("Bank Account Number:"));
        bankAccountNumberField = new JTextField();
        container.add(bankAccountNumberField);

        container.add(new JLabel("Total Cars Sold:"));
        totalCarsSoldField = new JTextField();
        container.add(totalCarsSoldField);

        container.add(new JLabel("Total Amount Sold:"));
        totalAmountSoldField = new JTextField();
        container.add(totalAmountSoldField);

        JButton calculateButton = new JButton("Calculate and Save");
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateAndSaveData();
            }
        });
        container.add(calculateButton);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchSalesman();
            }
        });
        container.add(searchButton);

        JButton displayAllButton = new JButton("Display All");
        displayAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAllSalesmen();
            }
        });
        container.add(displayAllButton);

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editSalesman();
            }
        });
        container.add(editButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSalesman();
            }
        });
        container.add(deleteButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });
        container.add(resetButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        container.add(exitButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        container.add(scrollPane);

        loadSalesmen();
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

            displayArea.setText("Salesman data saved successfully.\n" + salesman.toString());
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
        for (Salesman salesman : salesmen) {
            allSalesmen.append(salesman.toString()).append("\n");
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
}
