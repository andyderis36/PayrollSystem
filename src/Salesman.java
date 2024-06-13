public class Salesman {
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
        return "Salesman{" +
                "fullName='" + fullName + '\'' +
                ", staffNumber='" + staffNumber + '\'' +
                ", monthYear='" + monthYear + '\'' +
                ", icNumber='" + icNumber + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", totalCarsSold=" + totalCarsSold +
                ", totalAmountSold=" + totalAmountSold +
                ", carBodyCommission=" + carBodyCommission +
                ", incentiveCommission=" + incentiveCommission +
                ", grossSalary=" + grossSalary +
                ", epf=" + epf +
                ", incomeTax=" + incomeTax +
                ", netSalary=" + netSalary +
                '}';
    }
}
