package es.otherperspectiv.myapplication;


public class Order {
    private String price;
    private String description;
    private String readyAt;
    private String id;
    private String tableId;

    public Order(String price, String description, String readyAt, String id, String table_id) {
        this.price = price;
        this.description = description;
        this.readyAt = readyAt;
        this.id = id;
        this.tableId = table_id;
    }


    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getReadyAt() {
        return readyAt;
    }

    public String getId() { return id; }

    public String getTableId() { return tableId; }
}
