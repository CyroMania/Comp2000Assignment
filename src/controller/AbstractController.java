package controller;

import extras.KeyValuePair;
import model.Item;

import javax.swing.*;
import java.util.ArrayList;

public abstract class AbstractController {
    public static final String ITEM_NAME = "ItemName";
    public static final String DESCRIPTION = "Description";
    public static final String SCAN_CODE = "ScanCode";
    public static final String QUANTITY = "Quantity";
    public static final String PRICE = "Price";
    public static final String LIST  = "List";
    public static final String ITEM  = "Item";
    //public static final String STOCK_DATABASE = "StockDatabase";
    //public static final String BASKET = "Shopping";

    public abstract void SetModelProperty(KeyValuePair data);
    public abstract void addItemToList(KeyValuePair data);
    public abstract void addItemToDatabase(Item data);
    public abstract boolean checkItemExists(KeyValuePair data);
    public abstract void updateView(KeyValuePair data);
    public abstract Item findItem(KeyValuePair data);
    public abstract ArrayList<Item> findItems(KeyValuePair data);

    public void printAllItems() {}

    public void swapModel(int Index){};



}
