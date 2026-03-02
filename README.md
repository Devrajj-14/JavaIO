# Java IO Project

A Java project demonstrating File IO operations and Employee Payroll Service using Git Flow workflow.

## Branching Strategy

| Branch | Purpose |
|--------|---------|
| `master` | Stable, production-ready code only |
| `develop` | Latest integrated code |
| `feature/uc*` | Individual use case implementation branches |

## Use Cases

| UC | Title |
|----|-------|
| UC1 | Employee Payroll Console Service |
| UC2 | File Operations JUnit Tests |
| UC3 | Java File Watch Service |
| UC4 | Employee Payroll File Storage |
| UC5 | Print Payrolls & Count Entries |
| UC6 | Read Payroll File for Analysis |

## Project Structure

```
src/
├── main/java/com/javaio/
│   ├── EmployeePayroll.java
│   ├── EmployeePayrollService.java
│   └── FileWatchService.java
└── test/java/com/javaio/
    ├── FileOperationsTest.java
    └── EmployeePayrollServiceTest.java
```
