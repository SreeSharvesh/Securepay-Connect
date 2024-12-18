package account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"user", "status"})
public class MessageStatus {

    @JsonProperty(value = "user")
    private String username;

    @JsonProperty(value = "status")
    private String status;

    public MessageStatus() {
    }

    public MessageStatus(String user, String status) {
        this.username = user;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MessageStatus{" +
                "username='" + username + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
