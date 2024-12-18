package account.service;

import account.dto.UploadPayroll;
import account.entities.Employee;
import account.entities.EmployeePayroll;
import account.exceptions.BadRequest;
import account.repositories.EmployeePayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeePayrollServiceImpl implements EmployeePayrollService {

    private EmployeePayrollRepository employeePayrollRepository;

    @Autowired
    public EmployeePayrollServiceImpl(EmployeePayrollRepository employeePayrollRepository) {
        this.employeePayrollRepository = employeePayrollRepository;
    }


    @Override
    public void save(EmployeePayroll employeePayroll) {
        this.employeePayrollRepository.save(employeePayroll);
    }

    @Override
    public String dateYearMonthFormatter(String period) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(period, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMMM-yyyy");

        period = yearMonth.format(outputFormatter);
        return period;
    }
}
