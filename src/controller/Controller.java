package controller;

import extras.KeyValuePair;
import model.Stock;
import model.Item;
import view.Checkout;

import java.util.ArrayList;
import java.util.List;

public class Controller extends AbstractController{

    private Stock stock;
    private Checkout checkout;

    @Override
    public void SetModelProperty(KeyValuePair data) {

    }

    @Override
    public void addItemToList(KeyValuePair data) {

    }

    @Override
    public void addItemToDatabase(Item data) {

    }

    @Override
    public boolean checkItemExists(KeyValuePair data) {
        return false;
    }


    @Override
    public void updateView(KeyValuePair data) {

    }

    @Override
    public ArrayList<Item> findItems(KeyValuePair data) {
        return null;
    }
}
