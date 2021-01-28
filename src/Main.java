import controller.AbstractController;
import controller.MultiModelController;
import extras.KeyValuePair;
import model.IModel;
import model.Item;
import model.Stock;
import view.AbstractView;
import view.Checkout;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        IModel[] models = new IModel[3];

        IModel StockDatabase = new Stock();
        IModel Basket = new Stock();
        IModel LowQuantityStock = new Stock();

        models[0] = Basket;
        models[1] = StockDatabase;
        models[2] = LowQuantityStock;


        AbstractView checkoutPage = new Checkout();
        AbstractController MainController = new MultiModelController(models, checkoutPage);
        checkoutPage.SetController(MainController);

        //Admin adminPage = new Admin();
        //adminPage.setVisible(true);

        Scanner ProductFile = new Scanner(new File("products.txt"));
        while (ProductFile.hasNextLine())
        {
            String itemName = ProductFile.next();
            System.out.println(itemName);
            String description = ProductFile.next();
            String scanCode = ProductFile.next();
            Integer quantity = Integer.parseInt(ProductFile.next());
            Float price = Float.parseFloat(ProductFile.next());
            Item currentItem = new Item(itemName, description, scanCode, quantity, price);
            MainController.addItemToDatabase(currentItem);
        }
        ProductFile.close();

        //Item tomato = new Item("Tomato","Fruit", "Fx6432", 10, 2.50f);
        //Item carrot = new Item("Carrot","Veg", "Re6122",5, 0.60f);

        //MainController.swapModel(1);
        //MainController.addItemToDatabase(tomato);
        //MainController.addItemToDatabase(carrot);



        //FileWriter admins = new FileWriter(new File("admins.txt"));
        //admins.write("GaryJon password" + "\n");
        //admins.write("MaryFun 12345" + "\n");
        //admins.close();
    }
}
