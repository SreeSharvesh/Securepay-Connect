package account.service;

import account.entities.EmployeePayroll;

public interface EmployeePayrollService {

    void save(EmployeePayroll employeePayroll);
    String dateYearMonthFormatter(String period);
}
