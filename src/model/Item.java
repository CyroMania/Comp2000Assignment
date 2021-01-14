package model;

public class Item {

    private String itemName;
    private String description;
    private String scanCode;
    private Integer quantity;
    private Float price;

    public Item(String inName, String inDescription, String inScanCode, Integer inQuantity, Float inPrice){
        itemName = inName;
        description = inDescription;
        scanCode = inScanCode;
        quantity = inQuantity;
        price = inPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getScanCode() {
        return scanCode;
    }

    public Float getPrice() {
        return price;
    }

    public void setName(String name) {
        this.itemName = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
