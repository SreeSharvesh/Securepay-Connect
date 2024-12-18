package account.dto;

public class SuccessfulPassword {
    private String email;
    private String status;

    public SuccessfulPassword() {
    }

    public SuccessfulPassword(String email, String status) {
        this.email = email;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
