package model;

import controller.AbstractController;

import java.util.*;

public class Stock implements IModel{

        private ArrayList<Item> products;
        private Integer AlertQuantity = 5;



        public Stock() {
                products = new ArrayList<Item>();
        }


        public void AddItem(String itemName, String Description, String ScanCode, Integer Quantity, Float Price) {

                Item product = new Item(itemName, Description, ScanCode, Quantity, Price);

                products.add(product);
        }

        public ArrayList<Item> getProducts() {
                return products;
        }

        public Integer getAlertQuantity() {
                return AlertQuantity;
        }

        public void setAlertQuantity(Integer alertQuantity) {
                AlertQuantity = alertQuantity;
        }

        @Override
        public void add(AbstractController observer) {

        }

        @Override
        public void remove(AbstractController observer) {

        }
}
