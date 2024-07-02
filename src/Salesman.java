import java.io.Serializable;

public class Salesman implements Serializable {

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
        return  "===================================================\n" 
                + "================= SALESMAN INFORMATION =================\n"
                + "===================================================\n"
                +"Full Name: " + fullName + "\n"
                + "Staff Number: " + staffNumber + "\n"
                + "Date: " + monthYear + "\n"
                + "IC Number: " + icNumber + "\n"
                + "Bank Account Number: " + bankAccountNumber + "\n"
                + "Total Cars Sold: " + totalCarsSold + "\n"
                + "Total Amount Sold: RM " + totalAmountSold + "\n"
                + "---------------------------------------------------------\n"
                + "Basic Salary: RM " + basicSalary + "\n"
                + "Car Body Commission: RM " + carBodyCommission + "\n"
                + "Incentive Commission: RM " + incentiveCommission + "\n"
                + "Gross Salary: RM " + grossSalary + "\n"
                + "EPF: RM " + epf + "\n"
                + "Income Tax: RM " + incomeTax + "\n"
                + "Net Salary: RM " + netSalary + "\n"
                + "Status: " + status + "\n"
                + "===================================================\n";
    }
}
