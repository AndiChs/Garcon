package es.otherperspectiv.myapplication;

/**
 * Created by andichesoi on 25/04/2018.
 */

public class RequestRestaurant {
    private String id;
    private String userId;
    private String restaurantId;

    public RequestRestaurant(String id, String userId, String restaurantId) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
