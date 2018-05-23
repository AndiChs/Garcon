package es.otherperspectiv.myapplication;

/**
 * Created by andichesoi on 18/05/2018.
 */

public class Item {
    private String name;
    private String description;
    private int price;

    public Item(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

     public int getPrice() {
        return price;
    }
}
