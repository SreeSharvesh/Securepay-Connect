package account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AssignUserAccess {

    @JsonProperty(value = "user")
    @NotEmpty
    private String employeeUsername;

    @NotEmpty
    private String operation;

    public AssignUserAccess() {
    }

    public AssignUserAccess(@NotNull String employeeUsername, @NotNull String operation) {
        this.employeeUsername = employeeUsername;
        this.operation = operation;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
