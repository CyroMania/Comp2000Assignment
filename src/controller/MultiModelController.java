package controller;

import extras.KeyValuePair;
import model.IModel;
import model.Item;
import view.AbstractView;

import javax.swing.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

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
    public void Login(String inUsername, char[] inPassword) throws FileNotFoundException {
        String username = inUsername;
        String password = "";
        for (char c: inPassword)
        {
            password += c;
        }

        Scanner admins = new Scanner(new File("admins.txt"));

        while (admins.hasNextLine())
        {
            if (admins.nextLine().equals(username + " " + password))
            {
                System.out.println("Logged in successfully");
                swapModel(2);
                ArrayList<Item> AlertStock = getItemArray();
                swapModel(1);

                String ItemList = "";
                for (Item item: AlertStock)
                {
                    ItemList += item.getItemName() + " " + item.getScanCode() + "\n";
                }

                JOptionPane.showMessageDialog(currentView, "The Following Items Need Replenishment:\n" + ItemList);

                updateView(new KeyValuePair(AbstractController.LOGIN, true));
                return;
            }
        }

        updateView(new KeyValuePair(AbstractController.LOGIN, false));

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
    public void loadList() throws IOException {
        ArrayList<Item> Items = getItemArray();

        writeToFile("products.txt", Items);

        updateView(new KeyValuePair(AbstractController.LIST, Items));
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


                    writeToFile("products.txt",products);


                    updateView(new KeyValuePair("List", products));
                    break;

                case AbstractController.SCAN_CODE:
                    try
                    {
                        Item result = findItem(new KeyValuePair(AbstractController.SCAN_CODE, data.value));
                        Integer AlertQuantity = (Integer)currentModel.getClass().getDeclaredMethod("getAlertQuantity").invoke(currentModel, null);



                        swapModel(2);
                        ArrayList<Item> AlertStock = getItemArray();
                        swapModel(0);

                        swapModel(1);
                        ArrayList<Item> Database = getItemArray();
                        swapModel(0);

                        for (Item item: Database)
                        {
                            if (item.getScanCode().equals(result.getScanCode()))
                            {
                                System.out.println(item.getQuantity());
                                if (item.getQuantity() > 0)
                                {
                                    item.setQuantity(item.getQuantity() - 1);
                                    Item newItem = new Item(item.getItemName(), item.getDescription(),item.getScanCode() ,1, item.getPrice());

                                    Boolean Exists = false;
                                    for (Item scannedItem: products)
                                    {
                                        if (scannedItem.getScanCode().equals(newItem.getScanCode()))
                                        {
                                            Exists = true;
                                        }
                                    }
                                    if (!Exists)
                                    {
                                        products.add(newItem);
                                        System.out.println("added to basket");
                                    }
                                    else
                                    {
                                        for (Item scannedItem: products)
                                        {
                                            if (scannedItem.getScanCode().equals(newItem.getScanCode()))
                                            {
                                                scannedItem.setQuantity(scannedItem.getQuantity() + 1);
                                                scannedItem.setPrice(scannedItem.getPrice() + item.getPrice());
                                            }
                                        }
                                    }

                                    System.out.println(item.getQuantity() + " left in stock, the item is called " + item.getItemName());

                                    System.out.println(AlertQuantity);
                                    if (item.getQuantity() <= AlertQuantity)
                                    {
                                        System.out.println("Item is below alert quantity");

                                        for (Item alertItem: AlertStock) {
                                            System.out.println(alertItem.getItemName());
                                        }

                                        if (!AlertStock.contains(item))
                                        {
                                            AlertStock.add(item);
                                            System.out.println("Item was added to alert stock");
                                        }
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

    @Override
    public void updateItemInList(KeyValuePair data) {

        ArrayList<Item> Products = getItemArray();
        Item itemToUpdate = (Item)data.value;
        try
        {
            for (int i = 0; i < Products.size(); i++) {
                if (Products.get(i).getScanCode().equals(itemToUpdate.getScanCode()))
                {
                    Products.set(i, itemToUpdate);
                }
                else if (Products.get(i).getItemName().equals(itemToUpdate.getItemName()) &&
                         Products.get(i).getDescription().equals(itemToUpdate.getDescription()) &&
                        Products.get(i).getQuantity().equals(itemToUpdate.getQuantity()) &&
                        Products.get(i).getPrice().equals(itemToUpdate.getPrice()))
                {
                    Products.set(i, itemToUpdate);
                }
            }

            writeToFile("products.txt",Products);

            updateView(new KeyValuePair(AbstractController.LIST, Products));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToFile(String fileName, ArrayList<Item> model) throws IOException {

        FileWriter file = new FileWriter(new File(fileName));
        for (int i = 0; i < model.size(); i++) {
            if (i < model.size() - 1)
            {
                file.write(model.get(i).getItemName() + " " +
                        model.get(i).getDescription() + " " +
                        model.get(i).getScanCode() + " " +
                        model.get(i).getQuantity() + " " +
                        model.get(i).getPrice() + "\n");
            }
            else
            {
                file.write(model.get(i).getItemName() + " " +
                        model.get(i).getDescription() + " " +
                        model.get(i).getScanCode() + " " +
                        model.get(i).getQuantity() + " " +
                        model.get(i).getPrice());
            }
        }

        file.close();
    }

    @Override
    public void removeItemFromList(KeyValuePair data) throws FileNotFoundException {

        ArrayList<Item> products = getItemArray();
        Item itemToRemove = (Item)data.value;
        try
        {
            products.remove(itemToRemove);

            FileWriter ProductFile = new FileWriter(new File("products.txt"));
            for (int i = 0; i < products.size(); i++) {
                if (i < products.size() - 1)
                {
                    ProductFile.write(products.get(i).getItemName() + " " +
                            products.get(i).getDescription() + " " +
                            products.get(i).getScanCode() + " " +
                            products.get(i).getQuantity() + " " +
                            products.get(i).getPrice() + "\n");
                }
                else
                {
                    ProductFile.write(products.get(i).getItemName() + " " +
                            products.get(i).getDescription() + " " +
                            products.get(i).getScanCode() + " " +
                            products.get(i).getQuantity() + " " +
                            products.get(i).getPrice());
                }
            }

            ProductFile.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        updateView(new KeyValuePair(AbstractController.LIST, products));

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
            System.out.println( item.getScanCode() + " " + item.getItemName() + " " + item.getQuantity());
        }

    }


    @Override
    public void updateView(KeyValuePair data) throws FileNotFoundException {
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
