import controller.AbstractController;
import controller.MultiModelController;
import extras.KeyValuePair;
import model.IModel;
import model.Item;
import model.Stock;
import view.AbstractView;
import view.Checkout;

import java.security.Key;

public class Main {

    public static void main(String[] args){
        IModel[] models = new IModel[2];
        IModel StockDatabase = new Stock();
        IModel Basket = new Stock();

        models[0] = Basket;
        models[1] = StockDatabase;


        AbstractView checkoutPage = new Checkout();
        AbstractController MainController = new MultiModelController(models, checkoutPage);
        checkoutPage.SetController(MainController);

        //Admin adminPage = new Admin();
        //adminPage.setVisible(true);

        Item tomato = new Item("Tomato","Fruit", "Fx6432", 10, 2.50f);
        Item carrot = new Item("Carrot","Veg", "Re6122",5, 0.60f);

        //MainController.swapModel(1);
        //MainController.addItemToDatabase(tomato);
        MainController.addItemToDatabase(carrot);

        MainController.printAllItems();

    }
}
