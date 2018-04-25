package es.otherperspectiv.myapplication;

/**
 * Created by andichesoi on 24/04/2018.
 */

public class Restaurant {
    private String id;
    private String CVR;
    private String name;
    private String address;

    public Restaurant() {

    }

    public Restaurant(String id, String CVR, String name, String address) {
        this.id = id;
        this.CVR = CVR;
        this.name = name;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCVR() {
        return CVR;
    }

    public void setCVR(String CVR) {
        this.CVR = CVR;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
