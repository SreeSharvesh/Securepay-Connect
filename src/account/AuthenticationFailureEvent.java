package account;

import account.entities.Employee;
import account.entities.LogEntry;
import account.exceptions.ControllerExceptionHandler;
import account.exceptions.UserIsLocked;
import account.service.AuthoritiesServiceImpl;
import account.service.EmployeeServiceImpl;
import account.service.LogServiceImpl;
import account.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureEvent implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private HttpServletRequest request;
    //    private HttpServletResponse response;
    private LogServiceImpl logService;
    private EmployeeServiceImpl employeeService;
    private UserServiceImpl userService;
    private AuthoritiesServiceImpl authoritiesService;


    public AuthenticationFailureEvent() {
    }

    @Autowired
    public AuthenticationFailureEvent(HttpServletRequest request,
//                                      HttpServletResponse response,
//                                AuthenticationException exception,
                                      LogServiceImpl logService,
                                      EmployeeServiceImpl employeeService,
                                      UserServiceImpl userService,
                                      AuthoritiesServiceImpl authoritiesService
    ) {
        this.request = request;
//        this.response = response;
//        this.exception = exception;
        this.logService = logService;
        this.employeeService = employeeService;
        this.userService = userService;
        this.authoritiesService = authoritiesService;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        String personAuth = success.getAuthentication().getName();
        String endpoint = request.getRequestURI();

        Employee tempEmployee = this.employeeService.getEmployeeByEmail(personAuth);

        if (tempEmployee == null) {

        } else {
            this.employeeService.resetLoginAttempts(tempEmployee);
        }
    }

//    @EventListener
//    public void onFailure(AbstractAuthenticationEvent failures) {
//        String personAuth = failures.getAuthentication().getName();
//        String endpoint = request.getRequestURI();
//
//        Employee tempEmployee = this.employeeService.getEmployeeByEmail(personAuth);
//
//        if (tempEmployee == null) {
//            //dont do anything
//        } else if (!this.authoritiesService.isUserAdmin(tempEmployee) && failures.getAuthentication().isAuthenticated() && ) {
//            this.logService.save(new LogEntry(personAuth, "ACCESS_DENIED", endpoint, endpoint));
//        } else if (this.authoritiesService.isUserAdmin(tempEmployee) && !failures.getAuthentication().isAuthenticated()) {
//            this.logService.save(new LogEntry(personAuth, "LOGIN_FAILED", endpoint, endpoint));
//        } else {
//            if (tempEmployee.getFailedLogins() >= 5) {
//                // dont do anything, may aswell remove this
//            } else if (tempEmployee.getFailedLogins() == 4) {
//                this.logService.save(new LogEntry(personAuth, "BRUTE_FORCE", endpoint, endpoint));
//                this.logService.save(new LogEntry(personAuth, "LOCK_USER", "Lock employee " + personAuth, endpoint));
//                tempEmployee.setFailedLogins(tempEmployee.getFailedLogins() + 1);
//
//                this.employeeService.updateEmployee(tempEmployee);
//                this.employeeService.disableEmployee(tempEmployee);
//            } else {
//                this.logService.save(new LogEntry(personAuth, "LOGIN_FAILED", endpoint, endpoint));
//                this.employeeService.failedLogin(tempEmployee);
//            }
//        }
//    }

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        String personAuth = event.getAuthentication().getName();
        String endpoint = request.getRequestURI();

        Employee tempEmployee = this.employeeService.getEmployeeByEmail(personAuth);

        if (tempEmployee == null && personAuth == null) {
            this.logService.save(new LogEntry("Anonymous", "LOGIN_FAILED", endpoint, endpoint));
        } else if (personAuth != null && tempEmployee == null) {
            this.logService.save(new LogEntry(personAuth, "LOGIN_FAILED", endpoint, endpoint));
        } else if (!this.authoritiesService.isUserAdmin(tempEmployee) && event.getAuthentication().isAuthenticated()) {
            this.logService.save(new LogEntry(personAuth, "ACCESS_DENIED", endpoint, endpoint));
        } else if (this.authoritiesService.isUserAdmin(tempEmployee) && !event.getAuthentication().isAuthenticated()) {
            this.logService.save(new LogEntry(personAuth, "LOGIN_FAILED", endpoint, endpoint));
        } else {
            if (tempEmployee.getFailedLogins() >= 7) {
                throw new UserIsLocked("User account is locked");
            } else if (tempEmployee.getFailedLogins() > 3) {
                this.logService.save(new LogEntry(personAuth, "LOGIN_FAILED", endpoint, endpoint));
                this.logService.save(new LogEntry(personAuth, "BRUTE_FORCE", endpoint, endpoint));
                this.logService.save(new LogEntry(personAuth, "LOCK_USER", "Lock user " + personAuth, "/api/admin/user/access"));
                tempEmployee.setFailedLogins(tempEmployee.getFailedLogins() + 9);

                this.employeeService.updateEmployee(tempEmployee);
                this.employeeService.disableEmployee(tempEmployee);
            } else {
                this.logService.save(new LogEntry(personAuth, "LOGIN_FAILED", endpoint, endpoint));
                this.employeeService.failedLogin(tempEmployee);
            }
        }
    }
//        System.out.println(event.getException().toString());
//        System.out.println(event.getException().getMessage());
//        System.out.println();
//        System.out.println(event.getAuthentication().getCredentials().toString());


}

//    @EventListener
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

//
//        System.out.println(request.getUserPrincipal());
//    }

//    public void logEvent(String username, String action, String object, String path) {
//        LogEntry logEntry = new LogEntry(username, action, object, path);
//
//        this.logEntries.add(logEntry);
//    }

//    public String defineAction(String path) {
//        String finald = "";
//        switch (path) {
//            case "/api/auth/changepass" -> finald = "CHANGE_PASSWORD";
//        }
//        return finald;
//    }

//    public List<LogEntry> getLogEntries() {
//        return logEntries;
//    }

//
//    @Override
//    public String toString() {
//        return "AuthenticationEvents{" +
//                "logEntries=" + logEntries +
//                '}';
//    }


