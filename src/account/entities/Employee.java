package account.entities;

//import account.entities.EmployeePayment;
//import account.dto.EmployeePayroll;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@JsonPropertyOrder({"id", "name", "lastname", "email", "roles"})
@Entity
@Table(name = "Employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty
    @JsonProperty(value = "name")
    @Column(name = "name")
    private String name;

    @NotEmpty
    @JsonProperty(value = "lastname")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty
    @JsonProperty(value = "email")
    @Email(regexp = "\\w+(@acme.com)$")
    @Column(name = "email", columnDefinition = "VARCHAR_IGNORECASE")
    private String email;

    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    @Size(min = 12)
    private String password;

    @Column(name = "ROLE")
    @JsonProperty(value = "roles")
    private List<String> role = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @Column(name = "Employee_Payroll")
    @ElementCollection
    @Embedded
    @JsonIgnore
    private List<EmployeePayroll> employeePayrollList = new ArrayList<>();

    @JsonIgnore
    private int failedLogins;

    public Employee() {
    }

    public Employee(@NotNull String name, @NotNull String lastName, @NotNull String email, @NotNull String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.failedLogins = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public List<Authorities> getRole() {
//        return role;
//    }
//
//    public void setRole(Authorities authority) {
//        this.role.add(authority);
//    }

    public List<String> getRole() {
        return this.role.stream().sorted().toList();
    }

    public void setRole(String role) {
        this.role.add("ROLE_" + role);
    }

    public void removeRole(String role) {
        this.role.remove(role);
    }

//        public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = "ROLE_" + role;
//    }

    @JsonIgnore
    public List<EmployeePayroll> getEmployeePaymentList() {
        employeePayrollList.sort(periodComparator);
        return employeePayrollList;
    }

    @JsonIgnore
    public void setEmployeePaymentList(List<EmployeePayroll> employeePayrollList) {
        this.employeePayrollList = employeePayrollList;
    }

    public int getFailedLogins() {
        return failedLogins;
    }

    public void setFailedLogins(int failedLogins) {
        this.failedLogins = failedLogins;
    }

    public void addEmployeePayroll(EmployeePayroll employeePayroll) {
        this.employeePayrollList.add(employeePayroll);
    }

    @Transient
    Comparator<EmployeePayroll> periodComparator = (o1, o2) -> {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM-yyyy", Locale.ENGLISH);
        try {
            Date date1 = dateFormat.parse(o1.getPeriod());
            Date date2 = dateFormat.parse(o2.getPeriod());
            return date2.compareTo(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    };


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
