package account.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Employee_Payroll")
public class EmployeePayroll implements Comparable<EmployeePayroll>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payroll")
    private long payrollId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Employee employee;

    private String name;

    private String lastname;

    private String period;

    private String salary;

    public EmployeePayroll() {
    }

    public EmployeePayroll(Employee employee) {
        this.employee = employee;
        this.name = employee.getName();
        this.lastname = employee.getLastName();
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(period, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMMM-yyyy");

        this.period = yearMonth.format(outputFormatter);
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = String.format("%d dollar(s) %d cent(s)", salary / 100, salary % 100);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayroll that = (EmployeePayroll) o;
        return Objects.equals(name, that.name) && Objects.equals(lastname, that.lastname) && Objects.equals(period, that.period) && Objects.equals(salary, that.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastname, period, salary);
    }


    @Override
    public String toString() {
        return "EmployeePayment{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", period='" + period + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }

    @Override
    public int compareTo(EmployeePayroll o) {
        return this.period.compareTo(o.getPeriod());
    }
}
