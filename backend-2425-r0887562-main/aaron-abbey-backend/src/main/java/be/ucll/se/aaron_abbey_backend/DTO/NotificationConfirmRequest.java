package be.ucll.se.aaron_abbey_backend.DTO;

public class NotificationConfirmRequest {

    private boolean confirmed;

    public NotificationConfirmRequest() {
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
    
}
