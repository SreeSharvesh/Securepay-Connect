package account.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.util.Objects;


@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class UploadPayroll {

    @NotEmpty
    @JsonProperty(value = "employee")
    private String email;

    @NotEmpty
    @JsonProperty(value = "period")
    @Pattern(regexp = "([0][1-9]|[1][0-2])(-{1})(\\d{1,})")
    private String period;

    @Min(value = 0L, message = "Salary can't be negative")
    private Long salary;

    public UploadPayroll() {
    }

    public UploadPayroll(@NotNull String email, @NotNull String period, @NotNull Long salary) {
        this.email = email;
        this.period = period;
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UploadPayroll that = (UploadPayroll) o;
        return Objects.equals(email, that.email) && Objects.equals(period, that.period) && Objects.equals(salary, that.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, period, salary);
    }

    @Override
    public String toString() {
        return "EmployeePayroll{" +
                "email='" + email + '\'' +
                ", yearMonth='" + period + '\'' +
                ", salary=" + salary +
                '}';
    }
}
