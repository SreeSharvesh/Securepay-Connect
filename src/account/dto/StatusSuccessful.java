package account.dto;

public class StatusSuccessful {
    private String status;

    public StatusSuccessful() {
    }

    public StatusSuccessful(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
