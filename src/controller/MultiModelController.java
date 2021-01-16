package controller;

import extras.KeyValuePair;
import model.IModel;
import model.Item;
import view.AbstractView;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addItemToList(KeyValuePair data) {

        swapModel(1);
        ArrayList<Item> products = getItemArray();
        swapModel(0);

        try{
            switch (data.key) {

                case AbstractController.SCAN_CODE:
                    Item result = findItem(new KeyValuePair(AbstractController.SCAN_CODE, data.value));
                    products.add(result);
                    System.out.println(result.getItemName());
                    System.out.println("added");

            }

            updateView(new KeyValuePair("List" , products));
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
                    products.add(data);
                    System.out.println("Item Added to Database");
                }
                else {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        swapModel(0);
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
                            return true;
                        }
                        else{
                            System.out.println("Did not Match Item: " + product.getScanCode());
                            System.out.println(data.value);
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
        swapModel(1);
        ArrayList<Item> products;

        products = getItemArray();
        for (Item product: products) {
            System.out.println(product.getItemName());
        }
        swapModel(0);
    }


    @Override
    public void updateView(KeyValuePair data) {
        currentView.Update(data);
    }

    @Override
    public Item findItem(KeyValuePair data) {
        swapModel(1);

        ArrayList<Item> products = getItemArray();
        System.out.println("we're finding this item");
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
                        System.out.println("Found item");
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
