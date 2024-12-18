package account.service;

import account.dto.SuccessfulPassword;
import account.repositories.UserRepository;
import account.entities.Employee;
import account.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    /* Method to create a new user. It takes an employee email and password, here in this method we set the value of
    *  enabled to '1' and then save the user to the database. */
    @Override
    public void saveNewUser(Employee employee) {
        User tempUser = new User(employee);
        tempUser.setEnabled(1);
        this.userRepository.save(tempUser);
    }

    @Override
    public User findUserByUsername(String username) {
        for (User x : getAllUsers()) {
            if (x.getUsername().equals(username)) {
                return x;
            }
        }
        return null;
    }

    @Override
    public SuccessfulPassword changePassword(Employee employee) {
        saveNewUser(employee);
        return new SuccessfulPassword(employee.getEmail(), "The password has been updated successfully");
    }

    @Override
    public void updateUser(User user) {
        this.userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        for (User userOnList : getAllUsers()) {
            if (userOnList.getUsername().equalsIgnoreCase(username)) {
                this.userRepository.delete(userOnList);
            }
        }
    }

    @Override
    public void disableUser(Employee employee) {
        User tempUser = findUserByUsername(employee.getEmail());
        tempUser.setEnabled(0);
        updateUser(tempUser);
    }

    @Override
    public void enableUser(Employee employee) {
        User tempUser = findUserByUsername(employee.getEmail());
        tempUser.setEnabled(1);
        updateUser(tempUser);
    }

    @Override
    public void lockUser(Employee employee) {
        User tempUser = findUserByUsername(employee.getEmail());
//        tempUser.setAccountNonLocked(false);
        updateUser(tempUser);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
