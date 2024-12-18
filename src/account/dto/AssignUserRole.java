package account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class AssignUserRole {
    @JsonProperty(value = "user")
    private String username;
    @JsonProperty(value = "role")
    private String role;
    @JsonProperty(value = "operation")
    private String operation;

    public AssignUserRole() {
    }

    public AssignUserRole(String username, String role, String operation) {
        this.username = username;
        this.role = role;
        this.operation = operation;
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
        this.role = role;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "AssignUserRole{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }
}
