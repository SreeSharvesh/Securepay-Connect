package account.service;

import account.entities.Authorities;
import account.entities.Employee;
import account.exceptions.*;
import account.repositories.AuthoritiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.management.relation.Role;
import java.util.List;

@Service
@Transactional
public class AuthoritiesServiceImpl implements AuthoritiesService {

    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    public AuthoritiesServiceImpl(AuthoritiesRepository authoritiesRepository) {
        this.authoritiesRepository = authoritiesRepository;
    }

    /* Pretty self-explanatory method of creating a new Authority, if there's no user in the database then the first
       user will always be an admin, the rest will be normal users. */
    @Override
    public void save(Employee employee) {
        Authorities tempAuthority = assignRoleNewUser(employee);
        this.authoritiesRepository.save(tempAuthority);
    }

    @Override
    public void saveNewAuthority(Authorities tempAuthority) {
        this.authoritiesRepository.save(tempAuthority);
    }


    @Override
    public Employee grantAuthority(Employee employee, String role) {
        if (isUserAdmin(employee)) {
            //throw new exception for an admin cannot have additional roles
            throw new CannotCombineRoles();
        } else if (!checkForDuplicateRoles(employee, role)) {
            return assignCustomRole(employee, role);
        } else {
            throw new UserNotFound();
//            return null;
        }
    }

    @Override
    public void deleteAuthority(Authorities authority) {
        for (Authorities userAuthority : getAllAuthorities()) {
            if (userAuthority.getRole().equalsIgnoreCase(authority.getRole()) && userAuthority.getUsername().equalsIgnoreCase(authority.getUsername())) {
                this.authoritiesRepository.delete(userAuthority);
            }
        }
    }

    @Override
    public boolean checkForDuplicateRoles(Employee employee, String role) {
        for (String employeeRoles : employee.getRole()) {
            if (employeeRoles.equalsIgnoreCase("ROLE_" + role)) {
                //Throw an exception here
                throw new BadRequest();
            }
        }
        return false;
    }

    @Override
    public Employee deleteUserRole(Employee employee, String role) {
        Authorities tempAuthority = new Authorities(employee, role);

        for (String employeeRoles : employee.getRole()) {
            if (employeeRoles.equalsIgnoreCase("ROLE_" + role)) {

                if (employeeRoles.equalsIgnoreCase("ROLE_ADMINISTRATOR")) {
                    throw new CannotRemoveAdministrator();
                } else if (employee.getRole().size() == 1) {
                    throw new UserMustHaveOneRole();
                }

                employee.removeRole("ROLE_" + role);
                deleteAuthority(tempAuthority);
                return employee;
            }
        }

        throw new UserRoleDoesntExist();
    }

    @Override
    public Authorities assignRoleNewUser(Employee employee) {
        Authorities tempAuthority = new Authorities(employee);
        tempAuthority.setUsername(employee.getEmail());

        if (!isThereOnlyOneUser()) {
            tempAuthority.setRole("USER");
            employee.setRole("USER");
        } else {
            tempAuthority.setRole("ADMINISTRATOR");
            employee.setRole("ADMINISTRATOR");
        }

        return tempAuthority;
    }

    @Override
    public Employee assignCustomRole(Employee employee, String role) {
        if (!role.equalsIgnoreCase("ACCOUNTANT") && !role.equalsIgnoreCase("USER") && !role.equalsIgnoreCase("AUDITOR")) {
            throw new RoleNotFound();
        }

        Authorities tempAuthority = new Authorities(employee, role);
        saveNewAuthority(tempAuthority);

        employee.setRole(role);
        return employee;
    }

    @Override
    public boolean isThereOnlyOneUser() {
        return howManyUsersWithAuthorities() == 0;
    }

    @Override
    public void deleteAllRoles(String username) {
        for (Authorities x : getAllAuthorities()) {
            if (x.getUsername().equalsIgnoreCase(username)) {
                if (x.getRole().equalsIgnoreCase("ROLE_ADMINISTRATOR")) {
                    //throw cannot delete admin
                    throw new CannotRemoveAdministrator();
                }
                deleteAuthority(x);
            }
        }
    }

    @Override
    public boolean isUserAdmin(Employee employee) {
        for (String role : employee.getRole()) {
            if (role.equalsIgnoreCase("ROLE_ADMINISTRATOR")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int howManyUsersWithAuthorities() {
        return this.authoritiesRepository.findAll().size();
    }

    @Override
    public List<Authorities> getAllAuthorities() {
        return this.authoritiesRepository.findAll();
    }
}
