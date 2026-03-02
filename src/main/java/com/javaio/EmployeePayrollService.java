package com.javaio;

import java.util.Scanner;

/**
 * UC1 - Employee Payroll Service
 * Reads employee information from the console and writes it back to the console.
 */
public class EmployeePayrollService {

    /**
     * UC1: Reads employee payroll data from the console and prints it.
     */
    public void readAndWriteToConsole() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Employee Payroll Service - UC1 ===");
        System.out.print("Enter Employee ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

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

    public static void main(String[] args) {
        EmployeePayrollService service = new EmployeePayrollService();
        service.readAndWriteToConsole();
    }
}
