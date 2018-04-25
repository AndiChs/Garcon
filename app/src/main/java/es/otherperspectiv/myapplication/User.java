package es.otherperspectiv.myapplication;

/**
 * Created by andichesoi on 25/04/2018.
 */

public class User {
    private String email;
    private String password;
    private boolean isAdmin;
    private boolean isManager;
    private String restaurantId;
    private String name;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.restaurantId = "0";
    }

    public User(String email, String password, boolean isAdmin, boolean isManager, String restaurantId) {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isManager = isManager;
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
