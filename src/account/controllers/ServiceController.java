package account.controllers;

import account.dto.AssignUserAccess;
import account.dto.AssignUserRole;
import account.dto.MessageStatus;
import account.dto.StatusSuccessful;
import account.entities.Employee;
import account.entities.LogEntry;
import account.exceptions.CannotLockTheAdmin;
import account.exceptions.UserNotFound;
import account.service.AuthoritiesServiceImpl;
import account.service.EmployeeServiceImpl;
import account.service.LogServiceImpl;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
public class ServiceController {

    private EmployeeServiceImpl employeeService;
    private AuthoritiesServiceImpl authoritiesService;
    private LogServiceImpl logService;

    @Autowired
    public ServiceController(EmployeeServiceImpl employeeService,
                             AuthoritiesServiceImpl authoritiesService,
                             LogServiceImpl logService) {
        this.employeeService = employeeService;
        this.authoritiesService = authoritiesService;
        this.logService = logService;
    }

    @PutMapping("/user/role")
    public ResponseEntity<?> setUserRole(Authentication auth, @RequestBody AssignUserRole assignUserRole) {
        Employee tempEmployee = this.employeeService.getEmployeeByEmail(assignUserRole.getUsername());
        String username = assignUserRole.getUsername().toLowerCase();
        String role = assignUserRole.getRole();

        if (tempEmployee == null) {
            throw new UserNotFound();
        } else {
            String action = this.employeeService.removeOrGrant(assignUserRole);
            String actionPerformed = "";

            if (action.equalsIgnoreCase("GRANT_ROLE")) {
                actionPerformed = String.valueOf(new StringFormattedMessage("Grant role %s to %s", role, username));
            } else {
                actionPerformed = String.valueOf(new StringFormattedMessage("Remove role %s from %s", role, username));
            }


            this.logService.save(new LogEntry(auth.getName(), action, actionPerformed, "/api/admin/user/role"));
            return ResponseEntity.ok().body(tempEmployee);
        }
    }


    @DeleteMapping("/user/{username}")
    public ResponseEntity<?> deleteUser(Authentication auth, @PathVariable String username) {
        System.out.println(auth.getAuthorities());
        Employee tempEmployee = this.employeeService.getEmployeeByEmail(username);

        if (tempEmployee == null) {
            //throw exception
            throw new UserNotFound();
        } else {
            this.employeeService.deleteEmployee(tempEmployee);
            MessageStatus messageStatus = new MessageStatus(username, "Deleted successfully!");
            this.logService.save(new LogEntry(auth.getName(), "DELETE_USER", tempEmployee.getEmail(), "/api/admin/user"));
            return ResponseEntity.ok().body(messageStatus);
        }
    }


    @GetMapping("/user/")
    public ResponseEntity<?> getAllUsers() {
        if (this.authoritiesService.howManyUsersWithAuthorities() == 0) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().body(this.employeeService.getAllEmployees());
        }
    }

    @PutMapping("user/access")
    public ResponseEntity<?> controlUserAccess(Authentication auth, @RequestBody AssignUserAccess assignUserAccess) {
        Employee tempEmployee = this.employeeService.getEmployeeByEmail(assignUserAccess.getEmployeeUsername());
        String employeePerformedTheAction = auth.getName();
        String employeeToPerformTheAction = assignUserAccess.getEmployeeUsername().toLowerCase();

        if (tempEmployee == null) {
            throw new UserNotFound();
        } else if (this.authoritiesService.isUserAdmin(tempEmployee)) {
            throw new CannotLockTheAdmin();
        } else {
            String action = this.employeeService.lockOrUnlockUser(assignUserAccess);
            String lockOrUnlocked = "";
            String logStatus = "";

            if (action.equalsIgnoreCase("LOCK_USER")) {
                lockOrUnlocked = "locked!";
                logStatus = "Lock user ";
            } else {
                lockOrUnlocked = "unlocked!";
                logStatus = "Unlock user ";
            }

            String status = String.valueOf(new StringFormattedMessage("User %s %s", employeeToPerformTheAction, lockOrUnlocked));

            this.logService.save(new LogEntry(employeePerformedTheAction, action, logStatus + employeeToPerformTheAction, "/api/admin/user/access"));
            return ResponseEntity.ok().body(new StatusSuccessful(status));
        }
    }

}
