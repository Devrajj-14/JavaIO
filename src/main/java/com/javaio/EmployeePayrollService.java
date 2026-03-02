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
     * UC4: Count the number of entries (lines) in the payroll file.
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
    //  Main - UC4 Demo
    // =====================================================================

    public static void main(String[] args) throws IOException {
        EmployeePayrollService service = new EmployeePayrollService();

        // Populate test employee payroll objects (UC4)
        List<EmployeePayroll> employees = new ArrayList<>();
        employees.add(new EmployeePayroll(1, "Alice", 75000.00));
        employees.add(new EmployeePayroll(2, "Bob", 82000.00));
        employees.add(new EmployeePayroll(3, "Charlie", 68000.00));

        // Write to file and count entries (UC4)
        service.writePayrollToFile(employees);
    }
}
