
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
        Container container = getContentPane();
        container.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        addHeading(container, gbc);
        addSearchPanel(container, gbc);
        addFormPanel(container, gbc);
        addButtonPanel(container, gbc);
        addDisplayArea(container, gbc);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        loadSalesmen();
    }

    private void addHeading(Container container, GridBagConstraints gbc) {
        JLabel headingLabel = new JLabel("Maju Auto Sales Sdn. Bhd");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        container.add(headingLabel, gbc);
    }

    private void addSearchPanel(Container container, GridBagConstraints gbc) {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        searchPanel.add(new JLabel("Search Name or Staff Number:"));
        searchPanel.add(searchField);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        container.add(searchPanel, gbc);
    }

    private void addFormPanel(Container container, GridBagConstraints gbc) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(5, 5, 5, 5);
        formGbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addLabelAndField(formPanel, formGbc, "Full Name:", fullNameField = new JTextField(), row++);
        addLabelAndField(formPanel, formGbc, "Staff Number:", staffNumberField = new JTextField(), row++);
        addLabelAndField(formPanel, formGbc, "Month:", monthComboBox = new JComboBox<>(getMonths()), row++);
        addLabelAndField(formPanel, formGbc, "Year:", yearComboBox = new JComboBox<>(getYears()), row++);
        addLabelAndField(formPanel, formGbc, "IC Number:", icNumberField = new JTextField(), row++);
        addLabelAndField(formPanel, formGbc, "Bank Account Number:", bankAccountNumberField = new JTextField(), row++);
        addLabelAndField(formPanel, formGbc, "Total Cars Sold:", totalCarsSoldField = new JTextField(), row++);
        addLabelAndField(formPanel, formGbc, "Total Amount Sold:", totalAmountSoldField = new JTextField(), row++);
        addEmploymentStatus(formPanel, formGbc, row++);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        container.add(formPanel, gbc);
    }

    private void addButtonPanel(Container container, GridBagConstraints gbc) {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        addButton(buttonPanel, "Calculate and Save", e -> calculateAndSaveData());
        addButton(buttonPanel, "Search", e -> searchSalesman());
        addButton(buttonPanel, "Display All", e -> displayAllSalesmen());
        addButton(buttonPanel, "Edit", e -> editSalesman());
        addButton(buttonPanel, "Delete", e -> deleteSalesman());
        addButton(buttonPanel, "Reset", e -> resetFields());
        addButton(buttonPanel, "Exit", e -> System.exit(0));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        container.add(buttonPanel, gbc);
    }

    private void addDisplayArea(Container container, GridBagConstraints gbc) {
        JLabel displayLabel = new JLabel("Output Results:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        container.add(displayLabel, gbc);

        displayArea = new JTextArea(20, 50);
        displayArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(displayArea);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.gridwidth = 1;
        container.add(scrollPane, gbc);
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        field.setPreferredSize(new Dimension(200, field.getPreferredSize().height));
        panel.add(field, gbc);
    }

    private void addEmploymentStatus(JPanel panel, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Employment Status:"), gbc);

        contractRadioButton = new JRadioButton("Contract");
        permanentRadioButton = new JRadioButton("Permanent");

        statusGroup = new ButtonGroup();
        statusGroup.add(contractRadioButton);
        statusGroup.add(permanentRadioButton);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(contractRadioButton);
        statusPanel.add(permanentRadioButton);

        gbc.gridx = 1;
        panel.add(statusPanel, gbc);
    }

    private void addButton(JPanel panel, String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setToolTipText(text);
        panel.add(button);
    }

    private String[] getMonths() {
        return new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    }

    private String[] getYears() {
        int startYear = 2020;
        int endYear = 2045;
        int numYears = endYear - startYear + 1;
        String[] years = new String[numYears];

        for (int i = 0; i < numYears; i++) {
            years[i] = String.valueOf(startYear + i);
        }

        return years;
    }

    private void calculateAndSaveData() {
        try {
            Salesman salesman = createSalesmanFromFields();
            salesmen.add(salesman);
            saveSalesmen();
            saveSalesmanToTextFile(salesman);

            displayArea.setText(salesman.toString());
            JOptionPane.showMessageDialog(this, "Salesman data saved successfully.", "Calculate and Save", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Please enter valid numbers for total cars sold and total amount sold.", "Calculate and Save", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveSalesmanToTextFile(Salesman salesman) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("salesmen.txt", true))) {
            writer.write(salesman.toString());
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: Could not save salesman data to text file.", "Save to Text File", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Salesman createSalesmanFromFields() throws NumberFormatException {
        String fullName = fullNameField.getText();
        String staffNumber = staffNumberField.getText();
        String monthYear = monthComboBox.getSelectedItem() + " " + yearComboBox.getSelectedItem();
        String icNumber = icNumberField.getText();
        String bankAccountNumber = bankAccountNumberField.getText();
        int totalCarsSold = Integer.parseInt(totalCarsSoldField.getText());
        double totalAmountSold = Double.parseDouble(totalAmountSoldField.getText());
        String status = contractRadioButton.isSelected() ? "Contract" : "Permanent";

        return new Salesman(fullName, staffNumber, monthYear, icNumber, bankAccountNumber, totalCarsSold, totalAmountSold, status);
    }

    private void searchSalesman() {
        String searchValue = searchField.getText().trim().toLowerCase();

        if (searchValue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Search field is empty. Please enter a search value.", "Search", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Salesman salesman : salesmen) {
            if (salesman.getFullName().toLowerCase().contains(searchValue) || salesman.getStaffNumber().equalsIgnoreCase(searchValue)) {
                setFieldsFromSalesman(salesman);
                displayArea.setText(salesman.toString());
                JOptionPane.showMessageDialog(this, "Salesman found.", "Search", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Salesman not found.", "Search", JOptionPane.ERROR_MESSAGE);
    }

    private void setFieldsFromSalesman(Salesman salesman) {
        fullNameField.setText(salesman.getFullName());
        staffNumberField.setText(salesman.getStaffNumber());
        String[] monthYear = salesman.getMonthYear().split(" ");
        monthComboBox.setSelectedItem(monthYear[0]);
        yearComboBox.setSelectedItem(monthYear[1]);
        icNumberField.setText(salesman.getIcNumber());
        bankAccountNumberField.setText(salesman.getBankAccountNumber());
        totalCarsSoldField.setText(String.valueOf(salesman.getTotalCarsSold()));
        totalAmountSoldField.setText(String.valueOf(salesman.getTotalAmountSold()));
        if (salesman.getStatus().equalsIgnoreCase("Contract")) {
            contractRadioButton.setSelected(true);
        } else {
            permanentRadioButton.setSelected(true);
        }
    }

    private void displayAllSalesmen() {
        if (salesmen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No salesmen data available.", "Display", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder allSalesmen = new StringBuilder();
        for (int i = 0; i < salesmen.size(); i++) {
            allSalesmen.append("[ SALESMAN - ").append(i + 1).append(" ]\n");
            allSalesmen.append(salesmen.get(i).toString()).append("\n");
        }

        displayArea.setText(allSalesmen.toString());
        JOptionPane.showMessageDialog(this, "Salesmen data displayed.", "Display", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editSalesman() {
        String fullName = fullNameField.getText();
        for (Salesman salesman : salesmen) {
            if (salesman.getFullName().equalsIgnoreCase(fullName)) {
                try {
                    updateSalesmanFromFields(salesman);
                    saveSalesmen();
                    displayArea.setText(salesman.toString());
                    JOptionPane.showMessageDialog(this, "Salesman data updated successfully.", "Edit", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Error: Please enter valid numbers for total cars sold and total amount sold.", "Edit", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Salesman not found.", "Edit", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateSalesmanFromFields(Salesman salesman) throws NumberFormatException {
        salesman.setStaffNumber(staffNumberField.getText());
        salesman.setMonthYear(monthComboBox.getSelectedItem() + " " + yearComboBox.getSelectedItem());
        salesman.setIcNumber(icNumberField.getText());
        salesman.setBankAccountNumber(bankAccountNumberField.getText());
        salesman.setTotalCarsSold(Integer.parseInt(totalCarsSoldField.getText()));
        salesman.setTotalAmountSold(Double.parseDouble(totalAmountSoldField.getText()));
        salesman.setStatus(contractRadioButton.isSelected() ? "Contract" : "Permanent");
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

        JOptionPane.showMessageDialog(this, "All fields have been reset.", "Reset", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveSalesmen() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("salesmen.dat"))) {
            oos.writeObject(salesmen);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: Could not save salesmen data.", "Save", JOptionPane.ERROR_MESSAGE);
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
