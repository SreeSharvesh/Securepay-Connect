package account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


public class PasswordChange {

    @NotEmpty
    @Size(min = 12)
    @JsonProperty(value = "new_password")
    private String newPassword;

    public PasswordChange() {
    }

    public PasswordChange(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
