package account.service;

import account.entities.Authorities;
import account.entities.Employee;

import java.util.List;

public interface AuthoritiesService {
    void save(Employee employee);

    void saveNewAuthority(Authorities tempAuthority);

    Employee grantAuthority(Employee employee, String role);

    boolean checkForDuplicateRoles(Employee employee, String role);

    Employee deleteUserRole(Employee employee, String role);

    Authorities assignRoleNewUser(Employee employee);

    Employee assignCustomRole(Employee employee, String role);

//    void updateAuthorities(Employee employee, String role);

//    void deleteAuthority(String user);

    void deleteAuthority(Authorities authority);


//    Authorities assignCustomRole(Employee employee, String authority);

    boolean isThereOnlyOneUser();

    void deleteAllRoles(String username);

    boolean isUserAdmin(Employee employee);

    int howManyUsersWithAuthorities();

    List<Authorities> getAllAuthorities();
}
