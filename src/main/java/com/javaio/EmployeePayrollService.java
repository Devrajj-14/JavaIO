package com.javaio;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Employee Payroll Service
 *
 * UC1: Reads employee payroll data from the console and writes it back to the console.
 * UC4: Stores employee payroll into a file using File IO and counts entries.
 * UC5: Prints employee payroll lines from file and shows number of entries.
 */
public class EmployeePayrollService {

    // =====================================================================
    //  UC1 - Read and Write Employee Payroll to Console
    // =====================================================================

    /**
     * UC1: Reads employee payroll data from the console and prints it.
     */
    public void readAndWriteToConsole() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Employee Payroll Service - UC1 ===");
        System.out.print("Enter Employee ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Employee Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Employee Salary: ");
        double salary = scanner.nextDouble();

        EmployeePayroll employee = new EmployeePayroll(id, name, salary);

        System.out.println("\n--- Employee Payroll Written to Console ---");
        System.out.println("Employee Payroll Record:");
        System.out.println("  ID     : " + employee.getId());
        System.out.println("  Name   : " + employee.getName());
        System.out.println("  Salary : " + employee.getSalary());
        System.out.println("------------------------------------------");

        scanner.close();
    }

    // =====================================================================
    //  UC4 - Store Employee Payroll into a File
    // =====================================================================

    private static final String PAYROLL_FILE = "employee_payroll.txt";

    /**
     * UC4: Writes a list of EmployeePayroll objects to a file using File IO.
     * Each employee is written as a comma-separated line: id,name,salary
     *
     * @param employees List of EmployeePayroll objects to write
     * @throws IOException if file writing fails
     */
    public void writePayrollToFile(List<EmployeePayroll> employees) throws IOException {
        System.out.println("\n=== UC4 - Writing Employee Payroll to File ===");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYROLL_FILE))) {
            for (EmployeePayroll emp : employees) {
                String line = emp.getId() + "," + emp.getName() + "," + emp.getSalary();
                writer.write(line);
                writer.newLine();
                System.out.println("Written to file: " + line);
            }
        }

        System.out.println("Payroll file written: " + PAYROLL_FILE);
        System.out.println("Total entries written: " + countEntriesInFile());
    }

    /**
     * UC4 & UC5: Count the number of entries (lines) in the payroll file.
     *
     * @return number of lines in the payroll file
     * @throws IOException if file reading fails
     */
    public long countEntriesInFile() throws IOException {
        Path filePath = Paths.get(PAYROLL_FILE);
        if (!Files.exists(filePath)) {
            System.out.println("Payroll file does not exist yet.");
            return 0;
        }
        long count = Files.lines(filePath).count();
        System.out.println("Number of entries in payroll file: " + count);
        return count;
    }

    // =====================================================================
    //  UC5 - Print Employee Payrolls from File and Show Number of Entries
    // =====================================================================

    /**
     * UC5: Reads the payroll file line by line and prints each employee payroll.
     * Also shows the total number of entries to verify the operation worked.
     *
     * @throws IOException if file reading fails
     */
    public void printPayrollLinesFromFile() throws IOException {
        System.out.println("\n=== UC5 - Printing Employee Payrolls from File ===");

        Path filePath = Paths.get(PAYROLL_FILE);
        if (!Files.exists(filePath)) {
            System.out.println("Payroll file not found. Please run UC4 first to write payroll data.");
            return;
        }

        System.out.println("--- Payroll Lines ---");
        Files.lines(filePath).forEach(line -> {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                System.out.println("  ID: " + parts[0]
                        + " | Name: " + parts[1]
                        + " | Salary: " + parts[2]);
            }
        });
        System.out.println("---------------------");

        // Count entries to ensure the print operation worked
        long count = countEntriesInFile();
        System.out.println("UC5 - Total payroll entries displayed: " + count);
    }

    // =====================================================================
    //  Main - UC5 Demo (runs UC4 first, then UC5)
    // =====================================================================

    public static void main(String[] args) throws IOException {
        EmployeePayrollService service = new EmployeePayrollService();

        // UC4: Populate and write to file
        List<EmployeePayroll> employees = new ArrayList<>();
        employees.add(new EmployeePayroll(1, "Alice", 75000.00));
        employees.add(new EmployeePayroll(2, "Bob", 82000.00));
        employees.add(new EmployeePayroll(3, "Charlie", 68000.00));
        service.writePayrollToFile(employees);

        // UC5: Print payroll lines and show count
        service.printPayrollLinesFromFile();
    }
}
