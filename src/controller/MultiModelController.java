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

        ArrayList<Item> products;
        try{
            for (Method method: modelMethods) {
                if(method.getName() == "getProducts"){
                    products = (ArrayList<Item>)method.invoke(currentModel, null);
                    switch (data.key) {
                        case AbstractController.SCAN_CODE:
                            ArrayList<Item> result = findItems(new KeyValuePair(AbstractController.SCAN_CODE, data.value));
                            for (Item item: result) {
                                products.add(item);
                                System.out.println(item.getItemName());
                                System.out.println("added");
                            }
                            updateView(new KeyValuePair("List" , products));
                        case AbstractController.ITEM:
                            products.add((Item)data.value);
                            System.out.println(((Item) data.value).getItemName());
                            updateView(new KeyValuePair("List" , products));
                    }


                    System.out.println("updated");
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
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

        return false;


    }

    public void printAllItems(){
        //swapModel(1);
        ArrayList<Item> products;

        products = getItemArray();
        for (Item product: products) {
            System.out.println(product.getItemName());
        }

    }


    @Override
    public void updateView(KeyValuePair data) {
        currentView.Update(data);
    }

    @Override
    public ArrayList<Item> findItems(KeyValuePair data) {
        swapModel(1);
        currentModel.getClass().getName();
        ArrayList<Item> products = new ArrayList<Item>();
        try {
            for (Method method : modelMethods) {

            }
        }
        catch {

        }

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
                        System.out.println(sample.getItemName() + "exists");
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
