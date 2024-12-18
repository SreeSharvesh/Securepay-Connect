package account.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "authorities")
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authority_id;

    @Column(name = "username", columnDefinition = "VARCHAR_IGNORECASE")
    private String username;

    @Column(name = "authority")
    private String role;

    public Authorities() {
    }

    public Authorities(Employee employee) {
        this.username = employee.getEmail();
        this.role = employee.getEmail();
    }

    public Authorities(Employee employee, String role) {
        this.username = employee.getEmail();
        this.role = "ROLE_" + role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = "ROLE_" + role;
    }
}
