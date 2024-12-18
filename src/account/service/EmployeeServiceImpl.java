package account.service;

import account.BreachedPasswords;
import account.dto.AssignUserAccess;
import account.dto.AssignUserRole;
import account.dto.UploadPayroll;
import account.entities.EmployeePayroll;
import account.exceptions.BadRequest;
import account.exceptions.BreachedPassword;
import account.exceptions.CannotCombineRoles;
import account.exceptions.CannotRemoveAdministrator;
import account.repositories.EmployeeRepository;
import account.entities.Employee;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private AuthoritiesServiceImpl authoritiesService;
    private UserServiceImpl userService;
    private PasswordEncoder encoder;
    private BreachedPasswords breachedPasswords;
    private EmployeePayrollServiceImpl employeePayrollService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               AuthoritiesServiceImpl authoritiesService,
                               UserServiceImpl userService,
                               PasswordEncoder encoder,
                               BreachedPasswords breachedPasswords,
                               EmployeePayrollServiceImpl employeePayrollService) {
        this.employeeRepository = employeeRepository;
        this.authoritiesService = authoritiesService;
        this.userService = userService;
        this.encoder = encoder;
        this.breachedPasswords = breachedPasswords;
        this.employeePayrollService = employeePayrollService;
    }

    /* This method adds a new Employee to the database, before storing the employee it checks if the password is
     *  breached, and then encode the password using Bcrypt with a strength of 13 */
    @Override
    public void saveNewEmployee(Employee employee) {
        this.authoritiesService.save(employee);

        //Assigning a bcrypt password to the employee
        breachedPassword(employee.getPassword());
        employee.setPassword(encoder.encode(employee.getPassword()));

        this.userService.saveNewUser(employee);

        //Persisting the employee to the 'Employees' table
        this.employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void updateEmployee(Employee employee) {
        this.employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        for (Employee x : getAllEmployees()) {
            if (x.getEmail().equalsIgnoreCase(email)) {
                return x;
            }
        }
        return null;
    }

    /* Method that receives an employee and a list of UploadPayroll objects to convert it to EmployeePayroll objects
     * which are used to save this info on the database and adds the values to the current employee in their List<EmployeePayroll> list.
     *  Then it proceeds to update the employee with this new info to persist it on the database.
     *
     * If the employee is different from the original that was passed on the method then it changes employee based
     * on their values, a user must be registered on the database, otherwise an exception will be thrown
     *
     * An employee payroll period should be unique, this means that it cannot be paid twice or more on the same period. */
    @Override
    @Transactional
    public void setEmployeePayment(Employee employee, List<UploadPayroll> uploadPayrollList) {
//        List<EmployeePayroll> employeePayrollList = new ArrayList<>();

        //Convert the data from UploadPayroll Object to EmployeePayroll
        for (UploadPayroll x : uploadPayrollList) {
            Employee tempEmployee = getEmployeeByEmail(x.getEmail());

            if (tempEmployee == null) {
                throw new BadRequest();
            } else if (!tempEmployee.equals(employee)) {
                employee = tempEmployee;
            }

            EmployeePayroll employeePayrollInfo = new EmployeePayroll(tempEmployee);
            employeePayrollInfo.setPeriod(x.getPeriod());
            employeePayrollInfo.setSalary(x.getSalary());

            //This is to check if there's repeated periods
            for (EmployeePayroll payroll : tempEmployee.getEmployeePaymentList()) {
                if (payroll.getPeriod().equalsIgnoreCase(employeePayrollInfo.getPeriod())) {
                    throw new BadRequest();
                }
            }

            //Saving the employeePayrollInfo to the table on the database and adding the payroll to the respective employee
            this.employeePayrollService.save(employeePayrollInfo);
            tempEmployee.addEmployeePayroll(employeePayrollInfo);
            updateEmployee(tempEmployee);
        }
    }

    /* */

    @Override
    @Transactional
    public void updatePayrolls(Employee employee, List<UploadPayroll> uploadPayrollList) {
        List<EmployeePayroll> employeePayrollList = new ArrayList<>();

        //Convert the data from UploadPayroll Object to EmployeePayroll
        for (UploadPayroll x : uploadPayrollList) {
            if (employee.getEmail().equalsIgnoreCase(x.getEmail())) {
                EmployeePayroll employeePayrollInfo = new EmployeePayroll(employee);
                employeePayrollInfo.setPeriod(x.getPeriod());
                employeePayrollInfo.setSalary(x.getSalary());
                employeePayrollList.add(employeePayrollInfo);
            }
        }
        updateOldPayrollWithNewPayroll(employee, employeePayrollList);
        updateEmployee(employee);
    }

    public void updateOldPayrollWithNewPayroll(Employee employee, List<EmployeePayroll> employeePayrollList) {
        for (EmployeePayroll newPayrollInfo : employeePayrollList) {
            for (EmployeePayroll oldPayrollInfo : employee.getEmployeePaymentList()) {
                if (newPayrollInfo.getPeriod().equals(oldPayrollInfo.getPeriod())) {
                    oldPayrollInfo.setSalary(newPayrollInfo.getSalary());
                }
            }
        }
    }

    @Override
    public boolean doUserExists(String email) {
        for (Employee x : getAllEmployees()) {
            if (x.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public EmployeePayroll getSinglePayroll(Employee employee, String period) {
        period = formatMonthNumberToMonthName(period);

        for (EmployeePayroll x : employee.getEmployeePaymentList()) {
            if (x.getPeriod().equals(period)) {
                return x;
            }
        }
        throw new BadRequest();
    }

    /* Formatting the period from "numberOfMonth-yyyy" to "nameOfMonth-yyyy" and checking if it matches the regex
     *  if it doesn't a new BadRequest exception is going to be thrown */
    public String formatMonthNumberToMonthName(String period) {
        Pattern pattern = Pattern.compile("([0][1-9]|[1][0-2])(-{1})(\\d{1,})");
        Matcher patternPeriodMatcher = pattern.matcher(period);
        if (!patternPeriodMatcher.matches()) {
            throw new BadRequest();
        }
        period = this.employeePayrollService.dateYearMonthFormatter(period);
        return period;
    }

//    @Override
//    @Transactional
//    public void updateEmployeeRole(Employee employee, String role) {
////        this.authoritiesService.assignCustomRole(employee, role);
//            updateEmployee(this.authoritiesService.updateAuthorities(employee, role));
//
//    }

//    @Override
//    @Transactional
//    public void updateEmployeeRole(AssignUserRole assignUserRole) {
//
//
////        this.authoritiesService.removeOrGrant(operation);
////        this.authoritiesService.assignCustomRole(employee, role);
////        updateEmployee(this.authoritiesService.grantAuthority(employee, role));
//
//    }


    @Override
    public boolean samePassword(String email, String newPassword) {
        for (Employee x : getAllEmployees()) {
            if (x.getEmail().equals(email)) {
                return encoder.matches(newPassword, x.getPassword());
            }
        }
        return false;
    }

    @Override
    public boolean breachedPassword(String newPassword) {
        for (String x : this.breachedPasswords.listOfBreachedPasswords()) {
            if (x.equals(newPassword)) {
                throw new BreachedPassword();
            }
        }
        return false;
    }

    @Override
    public Employee changePassword(String username, String newPassword) {
        Employee tempEmployee = getEmployeeByEmail(username);
        tempEmployee.setPassword(this.encoder.encode(newPassword));
        updateEmployee(tempEmployee);

        return tempEmployee;
    }


    @Transactional
    @Override
    public String removeOrGrant(AssignUserRole assignUserRole) {
        Employee employee = getEmployeeByEmail(assignUserRole.getUsername());
        String role = assignUserRole.getRole();
        String operation = assignUserRole.getOperation();


        switch (operation.toUpperCase()) {
            case "GRANT" -> {
                if (!this.authoritiesService.isUserAdmin(employee) && role.equalsIgnoreCase("ADMINISTRATOR")) {
                    //Admin cannot have roles
                    throw new CannotCombineRoles();
                }
                updateEmployee(this.authoritiesService.grantAuthority(employee, role));
                return "GRANT_ROLE";
            }
            case "REMOVE" -> {
                if (this.authoritiesService.isUserAdmin(employee)) {
                    //throw exception user is admin, cannot have roles
                    throw new CannotRemoveAdministrator();
                }
                updateEmployee(this.authoritiesService.deleteUserRole(employee, role));
                return "REMOVE_ROLE";
            }
            default -> {
                //throw exception here
                throw new BadRequest();
            }
        }
    }

    @Override
    @Transactional
    public void deleteEmployee(Employee employee) {
        String username = employee.getEmail();

        this.authoritiesService.deleteAllRoles(username);
        this.userService.deleteUser(username);
        this.employeeRepository.delete(employee);
    }

    @Override
    public void failedLogin(Employee employee) {
        employee.setFailedLogins(employee.getFailedLogins() + 1);
        updateEmployee(employee);
    }

    @Override
    public void disableEmployee(Employee employee) {
        this.userService.disableUser(employee);
    }

//    @Override
//    public void enableEmployee(Employee employee) {
//        this.userService.enableUser(employee);
//        resetLoginAttempts(employee);
//    }

    @Override
    public void maxOutLoginAttempts(Employee employee) {
        employee.setFailedLogins(12);
        updateEmployee(employee);
    }

    @Transactional
    @Override
    public void resetLoginAttempts(Employee employee) {
        employee.setFailedLogins(0);
        updateEmployee(employee);
    }

    @Override
    public String lockOrUnlockUser(AssignUserAccess assignUserAccess) {
        Employee tempEmployee = getEmployeeByEmail(assignUserAccess.getEmployeeUsername());
        String operation = assignUserAccess.getOperation();

        switch (operation.toUpperCase()) {
            case "LOCK" -> {
                this.userService.disableUser(tempEmployee);
                maxOutLoginAttempts(tempEmployee);
                return "LOCK_USER";
            }
            case "UNLOCK" -> {
                this.userService.enableUser(tempEmployee);
                resetLoginAttempts(tempEmployee);
                return "UNLOCK_USER";
            }
            default -> {
                throw new BadRequest();
            }
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        return this.employeeRepository.findAll();
    }
}
