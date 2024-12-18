package account.controllers;

import account.dto.StatusSuccessful;
import account.dto.UploadPayroll;
import account.entities.Employee;
import account.entities.EmployeePayroll;
import account.exceptions.BadRequest;
import account.service.AuthoritiesServiceImpl;
import account.service.EmployeeServiceImpl;
import account.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
public class BusinessLogicController {

    private EmployeeServiceImpl employeeService;
    private UserServiceImpl userService;
    private AuthoritiesServiceImpl authoritiesService;

    @Autowired
    public BusinessLogicController(EmployeeServiceImpl employeeService, UserServiceImpl userService, AuthoritiesServiceImpl authoritiesService) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.authoritiesService = authoritiesService;
    }

    @PostMapping("/api/acct/payments")
    public ResponseEntity<?> uploadPayrolls(@RequestBody @Valid List<UploadPayroll> uploadPayroll) {
        List<UploadPayroll> testEmployeePayment = new ArrayList<>(uploadPayroll);
        Employee tempEmployee = this.employeeService.getEmployeeByEmail(testEmployeePayment.get(0).getEmail());

        if (tempEmployee == null) {
            throw new BadRequest();
        } else {
            this.employeeService.setEmployeePayment(tempEmployee, testEmployeePayment);
            return ResponseEntity.ok().body(new StatusSuccessful("Added successfully!"));
        }

    }

    @PutMapping("/api/acct/payments")
    public ResponseEntity<?> changeSalary(@RequestBody @Valid List<UploadPayroll> uploadPayroll) {
        List<UploadPayroll> testEmployeePayment = new ArrayList<>(uploadPayroll);
        Employee tempEmployee = this.employeeService.getEmployeeByEmail(testEmployeePayment.get(0).getEmail());

        if (tempEmployee == null) {
            throw new BadRequest();
        } else {
            this.employeeService.updatePayrolls(tempEmployee, testEmployeePayment);
            return ResponseEntity.ok().body(new StatusSuccessful("Updated successfully!"));
        }
    }

    /* Method that takes return an Employee payroll, it returns an entire Employee payroll if no period is on the input
       that is if the period is == null.

       If a period is on the parameter then it's going to return the employee payroll from that specific period. */
    @GetMapping("/api/empl/payment")
    public ResponseEntity<?> getEmployeePayroll(Authentication auth, @RequestParam(required = false) String period) {
        String user = auth.getName().toLowerCase();
        Employee testEmployee = this.employeeService.getEmployeeByEmail(user);

        if (testEmployee == null) {
            return ResponseEntity.badRequest().body("User doesnt exist");
        }

        if (period == null) {
            return ResponseEntity.ok().body(testEmployee.getEmployeePaymentList());
        } else {
            EmployeePayroll singleEmployeePayroll = this.employeeService.getSinglePayroll(testEmployee, period);
            return ResponseEntity.ok().body(singleEmployeePayroll);
        }
    }


}
