package controller;

import model.Stock;
import model.Item;

import java.util.List;

public class Controller implements IController{

    private Stock stock;


    @Override
    public boolean CheckScanCode(String inScanCode) {
        boolean doesExist = this.stock.CheckItemExists(inScanCode);
        return doesExist;
    }

    @Override
    public void AddItemToList(String inScanCode) {
        List<Item> products = this.stock.getProducts();
        for (Item product : products) {
            if (product.getScanCode() == inScanCode) {
                String item = product.getName() + " £" + product.getPrice();
                UpdateCheckoutList(item);
            }
        }
    }

    @Override
    public void UpdateCheckoutList(String inData) {

    }
}
