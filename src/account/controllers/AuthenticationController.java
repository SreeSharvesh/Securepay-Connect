package account.controllers;

import account.AuthenticationFailureEvent;
import account.BreachedPasswords;
import account.entities.LogEntry;
import account.dto.PasswordChange;
import account.dto.SuccessfulPassword;
import account.entities.Employee;
import account.exceptions.BreachedPassword;
import account.exceptions.SamePassword;
import account.exceptions.UserExistException;
import account.service.AuthoritiesServiceImpl;
import account.service.EmployeeServiceImpl;
import account.service.LogServiceImpl;
import account.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthenticationController {

    private EmployeeServiceImpl employeeService;
    private UserServiceImpl userService;
    private AuthoritiesServiceImpl authoritiesService;
    private BreachedPasswords breachedPasswords;
    private AuthenticationFailureEvent authenticationFailureEvent;
    private LogServiceImpl logService;

    @Autowired
    public AuthenticationController(EmployeeServiceImpl employeeService,
                                    UserServiceImpl userService,
                                    AuthoritiesServiceImpl authoritiesService,
                                    BreachedPasswords breachedPasswords,
                                    AuthenticationFailureEvent authenticationFailureEvent,
                                    LogServiceImpl logService) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.authoritiesService = authoritiesService;
        this.breachedPasswords = breachedPasswords;
        this.authenticationFailureEvent = authenticationFailureEvent;
        this.logService = logService;
    }


//    public AuthenticationController(EmployeeServiceImpl employeeService, UserServiceImpl userService, AuthoritiesServiceImpl authoritiesService, BreachedPasswords breachedPasswords) {
//        this.employeeService = employeeService;
//        this.userService = userService;
//        this.authoritiesService = authoritiesService;
//        this.breachedPasswords = breachedPasswords;
//    }


    @PostMapping("/signup")
    public ResponseEntity<?> signUser(@Valid @RequestBody Employee employee) {
        boolean userExists = this.employeeService.doUserExists(employee.getEmail());
        employee.setEmail(employee.getEmail().toLowerCase());

        if (userExists) {
            throw new UserExistException();
        } else {
            this.employeeService.saveNewEmployee(employee);
            this.logService.save(new LogEntry("Anonymous", "CREATE_USER", employee.getEmail(), "/api/auth/signup"));
            return ResponseEntity.ok().body(employee);
        }
    }

    @PostMapping("/changepass")
    public ResponseEntity<?> changePassword(Authentication auth, @Valid @RequestBody PasswordChange newPassword) {
        String username = auth.getName();
        boolean breachedPassword = this.employeeService.breachedPassword(newPassword.getNewPassword());
        boolean samePassword = this.employeeService.samePassword(username, newPassword.getNewPassword());


        if (breachedPassword) {
            throw new BreachedPassword();
        } else if (samePassword) {
            throw new SamePassword();
        } else {
            //Consider doing this inside the employee service
            Employee tempEmployee = this.employeeService.changePassword(username, newPassword.getNewPassword());
            SuccessfulPassword successfulPassword = this.userService.changePassword(tempEmployee);
            this.logService.save(new LogEntry(auth.getName(), "CHANGE_PASSWORD", tempEmployee.getEmail(), "/api/auth/changepass"));
            return ResponseEntity.ok().body(successfulPassword);
        }

    }

    @GetMapping("/block")
    public void lockUser(Authentication auth) {
        Employee tempEmployee = this.employeeService.getEmployeeByEmail(auth.getName());

        this.userService.lockUser(tempEmployee);
    }
}
