package test;

import model.Item;
import view.Checkout;

public class test {


    public static void main(String[] args){
        Checkout checkoutPage = new Checkout();
        checkoutPage.setVisible(true);


        //Admin adminPage = new Admin();
        //adminPage.setVisible(true);

        Item object = new Item("Tomato","Fruit", "Fx6432", 2.50f);


    }
}
