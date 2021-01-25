package controller;

import extras.KeyValuePair;
import model.IModel;
import model.Item;
import view.AbstractView;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.*;
import java.util.ArrayList;

public class MultiModelController extends AbstractController{

    IModel[] models;
    AbstractView currentView;

    IModel currentModel;

    Method[] modelMethods;

    //Integer prevModelIndex;

    public MultiModelController (IModel[] models, AbstractView view){
        this.models = models;
        this.currentView = view;

        currentModel = models[0];
        view.SetController(this);

        modelMethods = currentModel.getClass().getDeclaredMethods();
    }

    @Override
    public void swapModel(int Index) {

        if (Index >= 0 && Index < models.length){
            if(currentModel != models[Index]){
                currentModel.remove(this);
                currentModel = models[Index];
                currentModel.add(this);

                modelMethods = currentModel.getClass().getDeclaredMethods();
            }
        }
    }

    @Override
    public void SetModelProperty(KeyValuePair data) {
        try {
            String methodName = "set" + data.key;
            for (Method method : modelMethods) {
                if (method.getName().equals(methodName)) {
                    method.invoke(currentModel,data.value);
                }
            }
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void addItemToList(KeyValuePair data) {

        ArrayList<Item> products = getItemArray();


        try{
            switch (data.key) {

                case AbstractController.ITEM:
                    products.add((Item)data.value);
                    updateView(new KeyValuePair("List", products));
                    break;

                case AbstractController.STOCK:
                    System.out.println("wow!");
                    Item currentItem = (Item)data.value;
                    String scanCode = currentItem.getScanCode();

                    for ( Item item : products)
                    {
                        if (item.getScanCode().equals(scanCode)){
                            System.out.println("Item Already Exists with this ScanCode. Please Change The Identifier");
                            return;
                        }
                    }

                    products.add(currentItem);
                    updateView(new KeyValuePair("List", products));
                    break;

                case AbstractController.SCAN_CODE:
                    try
                    {
                        Item result = findItem(new KeyValuePair(AbstractController.SCAN_CODE, data.value));
                        result.setQuantity(1);

                        swapModel(1);
                        ArrayList<Item> Database = getItemArray();
                        swapModel(0);

                        for (Item item: Database)
                        {
                            if (item.getScanCode().equals(result.getScanCode()))
                            {

                                if (item.getQuantity() > 0)
                                {
                                    if (!products.contains(result))
                                    {
                                        products.add(result);
                                    }
                                    else
                                    {
                                        for (Item scannedItem: products) {
                                            if (scannedItem.getScanCode().equals(result.getScanCode()))
                                            {
                                                scannedItem.setQuantity(scannedItem.getQuantity() + 1);
                                            }
                                        }
                                    }

                                    System.out.println("added to list");
                                    item.setQuantity(item.getQuantity() - 1);
                                    System.out.println(item.getQuantity() + " left in stock, the item is called " + item.getItemName());
                                    Integer AlertQuantity = (Integer)currentModel.getClass().getDeclaredMethod("getAlertQuantity").invoke(currentModel, null);
                                    System.out.println(AlertQuantity);
                                    if (item.getQuantity() <= AlertQuantity)
                                    {
                                        swapModel(2);
                                        System.out.println("Item is below alert quantity");
                                        ArrayList<Item> AlertStock = getItemArray();
                                        for (Item alertItem: AlertStock) {
                                            System.out.println(alertItem.getItemName());
                                        }
                                        if (!AlertStock.contains(item))
                                        {
                                            AlertStock.add(item);
                                            System.out.println("Item was added to alert stock");
                                        }
                                        swapModel(0);
                                     }
                                }
                                else
                                {
                                    System.out.println("Item is out of stock");
                                }
                            }
                        }


                    }
                    catch (Exception x)
                    {
                        System.out.println("Item with ScanCode could not be found");
                    }

                    updateView(new KeyValuePair("List" , products));
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addItemToDatabase(Item data) {
        swapModel(1);
        try {
            for (Method method : modelMethods) {
                if (method.getName() == "getProducts") {
                    ArrayList<Item> products = (ArrayList<Item>)method.invoke(currentModel, null);
                    if (products.contains(data)){
                        System.out.println("Item already exists in database");
                    }
                    else{
                        products.add(data);
                        System.out.println("Item Added to Database");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        swapModel(0);
    }

    public void WriteToDatabaseFile(KeyValuePair data){

    }


    @Override
    public boolean checkItemExists(KeyValuePair data) {
        swapModel(1);

        ArrayList<Item> products;
        try {
            for (Method method : modelMethods){
                if (method.getName() == "getProducts"){
                    products = (ArrayList<Item>)method.invoke(currentModel, null);
                    for (Item product : products) {
                        if( product.getScanCode().equals(data.value) ) {
                            swapModel(0);
                            return true;
                        }
                        else{
                            System.out.println("Did not Match Item: " + product.getScanCode());
                        }
                    }
                }
            }
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        swapModel(0);
        return false;

    }

    public void printAllItems(){

        ArrayList<Item> products;

        products = getItemArray();
        for(Item item : products){
            System.out.println( item.getScanCode() + " " + item.getItemName());
        }

    }


    @Override
    public void updateView(KeyValuePair data) {
        currentView.Update(data);
    }

    @Override
    public Item findItem(KeyValuePair data) {
        swapModel(1);

        ArrayList<Item> products = getItemArray();
        Item result = new Item();

        for (Item sample : products) {
            switch (data.key){
                case AbstractController.ITEM_NAME:
                    if (sample.getItemName().equals(data.value)){
                        result = sample;
                    }
                case AbstractController.DESCRIPTION:
                    if (sample.getDescription().equals(data.value)){
                        result = sample;
                    }
                case AbstractController.SCAN_CODE:
                    if (sample.getScanCode().equals(data.value)){
                        result = sample;
                    }
            }
        }
        swapModel(0);
        return result;
    }

    @Override
    public ArrayList<Item> findItems(KeyValuePair data) {

        ArrayList<Item> products = getItemArray();

        ArrayList<Item> results = new ArrayList<Item>();
        for (Item sample : products) {
            switch (data.key){
                case AbstractController.ITEM_NAME:
                    if (sample.getItemName().equals(data.value)){
                        results.add(sample);
                    }
                case AbstractController.DESCRIPTION:
                    if (sample.getDescription().equals(data.value)){
                        results.add(sample);
                    }
                case AbstractController.SCAN_CODE:
                    if (sample.getScanCode().equals(data.value)){
                        results.add(sample);
                    }
            }
        }
        return results;
    }


    public ArrayList<Item> getItemArray(){
        ArrayList<Item> products = new ArrayList<Item>();

        try {
            for (Method method : modelMethods) {
                if (method.getName() == "getProducts"){
                    products = (ArrayList<Item>)method.invoke(currentModel, null);
                }
            }

        }
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return products;
    }


}
