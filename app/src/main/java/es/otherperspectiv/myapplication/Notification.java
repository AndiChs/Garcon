package es.otherperspectiv.myapplication;

public class Notification {
    private String description;
    private String createdAt;

    public Notification(String description, String createdAt) {
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
