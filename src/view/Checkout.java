package view;

import controller.AbstractController;
import extras.KeyValuePair;
import model.Item;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Checkout extends AbstractView {


    private JPanel mainPanel;
    private JTextField ScanTf;
    private JButton ScanBtn;
    private JTextField ScanCodeTf;
    private JTextField ItemNameTf;
    private JTextField DescTf;
    private JTextField PriceTf;
    private JPanel addStockPanel;
    private JButton AddBtn;
    private JTextField QuantityTf;
    private JTabbedPane ServiceTPn;
    private JTextArea ShoppingTA;
    private JTextArea StockDatabaseTA;
    private JButton CashBtn;
    private JButton CardBtn;
    private JLabel PaymentLbl;
    private JTextPane QuantityTA;
    private JTextArea PriceTA;
    private JButton UpdateBtn;
    private JButton DeleteBtn;
    private JList StockDatabaseLt;
    private JTextField UsernameTf;
    private JPasswordField PasswordTf;
    private JButton LoginBtn;

    private DefaultListModel<Item> StockDatabase;

    public Checkout(){


        Initialise(mainPanel);

        StockDatabase = new DefaultListModel<>();
        StockDatabaseLt.setModel(StockDatabase);
        StockDatabaseLt.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        StockDatabaseLt.setVisibleRowCount(5);

        PasswordTf.setEchoChar((char) 0);
        ShoppingTA.setText("");

        this.setVisible(true);


        ServiceTPn.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = ServiceTPn.getSelectedIndex();
                controller.swapModel(index);
                System.out.println(index);


                try {
                    controller.loadList();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }


                if (index != 1)
                {
                    UpdateBtn.setEnabled(false);
                    DeleteBtn.setEnabled(false);
                }
                //Debug Code

                controller.printAllItems();
            }
        });

        ScanBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ScanTf.getText() != "") {
                    if ( controller.checkItemExists(new KeyValuePair(AbstractController.SCAN_CODE, ScanTf.getText())) == true ){

                        System.out.println("it exists.");

                        controller.addItemToList(new KeyValuePair(AbstractController.SCAN_CODE, ScanTf.getText()));

                        ScanTf.setText("");
                    }
                    else{
                        System.out.println("ERROR OBJECT DID NOT SCAN CORRECTLY");
                    }

                }

            }
        });



        AddBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                try
                {
                    String itemName = ItemNameTf.getText();
                    String scanCode = ScanCodeTf.getText();
                    String Description = DescTf.getText();
                    Integer Quantity = Integer.parseInt(QuantityTf.getText());
                    Float Price = Float.parseFloat(PriceTf.getText().substring(PriceTf.getText().lastIndexOf("£") + 1));
                    Item newProduct = new Item(itemName, Description, scanCode, Quantity, Price);
                    controller.addItemToList(new KeyValuePair(AbstractController.STOCK, newProduct));
                }
                catch (Exception x)
                {
                    System.out.println("ITEM FAILED TO ADD");
                }

            }
        });

        DeleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Item itemToDelete = StockDatabase.elementAt(StockDatabaseLt.getSelectedIndex());
                try {
                    controller.removeItemFromList(new KeyValuePair(AbstractController.ITEM, itemToDelete));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });

        StockDatabaseLt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {

                try
                {

                    Integer selectedIndex = StockDatabaseLt.getSelectedIndex();
                    if (selectedIndex != -1)
                    {
                        UpdateBtn.setEnabled(true);
                        DeleteBtn.setEnabled(true);
                        ItemNameTf.setText(StockDatabase.get(selectedIndex).getItemName());
                        DescTf.setText(StockDatabase.get(selectedIndex).getDescription());
                        ScanCodeTf.setText(StockDatabase.get(selectedIndex).getScanCode());
                        QuantityTf.setText(String.valueOf(StockDatabase.get(selectedIndex).getQuantity()));
                        DecimalFormat Df = new DecimalFormat("£#0.00");
                        PriceTf.setText(Df.format(StockDatabase.get(selectedIndex).getPrice()));
                    }
                    else
                    {
                        ItemNameTf.setText("#" + AbstractController.ITEM_NAME);
                        DescTf.setText("#" + AbstractController.DESCRIPTION);
                        ScanCodeTf.setText("#" + AbstractController.SCAN_CODE);
                        QuantityTf.setText("#" + AbstractController.QUANTITY);
                        PriceTf.setText("#" + AbstractController.PRICE);
                    }
                }
                catch (IndexOutOfBoundsException v)
                {
                    v.printStackTrace();
                    System.out.println("We went out of bounds");
                }

            }
        });

        ScanCodeTf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (ScanCodeTf.getText().equals("#" + AbstractController.SCAN_CODE)){
                    ScanCodeTf.setText("");
                }
            }
        });
        ItemNameTf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (ItemNameTf.getText().equals("#" + AbstractController.ITEM_NAME)){
                    ItemNameTf.setText("");
                }
            }
        });
        DescTf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (DescTf.getText().equals("#" + AbstractController.DESCRIPTION)){
                    DescTf.setText("");
                }
            }
        });
        QuantityTf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (QuantityTf.getText().equals("#" + AbstractController.QUANTITY)){
                    QuantityTf.setText("");
                }
            }
        });
        PriceTf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (PriceTf.getText().equals("#" + AbstractController.PRICE)){
                    PriceTf.setText("");
                }
            }
        });


        UpdateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    Integer selectedIndex = StockDatabaseLt.getSelectedIndex();
                    String itemName = ItemNameTf.getText();
                    String scanCode = ScanCodeTf.getText();
                    String Description = DescTf.getText();
                    Integer Quantity = Integer.parseInt(QuantityTf.getText());
                    Float Price = Float.parseFloat(PriceTf.getText().substring(PriceTf.getText().lastIndexOf("£") + 1));
                    Item updatedProduct = new Item(itemName, Description, scanCode, Quantity, Price);

                    System.out.println(StockDatabase.get(StockDatabaseLt.getSelectedIndex()).getScanCode());
                    System.out.println(updatedProduct.getScanCode());

                    if (!StockDatabase.get(StockDatabaseLt.getSelectedIndex()).getScanCode().equals(updatedProduct.getScanCode())) {
                        JOptionPane.showMessageDialog(mainPanel, "SCANCODES DO NOT NEED TO BE UPDATED");
                    } else {
                        controller.updateItemInList(new KeyValuePair(AbstractController.STOCK, updatedProduct));
                    }

                }
                catch (Exception x)
                {
                    System.out.println("ITEM FAILED TO UPDATE");
                }
            }
        });


        LoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = UsernameTf.getText();
                char[] password = PasswordTf.getPassword();

                try {
                    controller.Login(username, password);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        UsernameTf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (UsernameTf.getText().equals("#Username"))
                {
                    UsernameTf.setText("");
                }
            }
        });
        PasswordTf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String password = "#Password";
                Boolean isDefault = true;

                for (int i = 0; i < password.length(); i++) {
                    if (PasswordTf.getPassword()[i] != password.charAt(i))
                    {
                        isDefault = false;
                        break;
                    }
                }

                if (isDefault)
                {
                    PasswordTf.setText("");
                }


                PasswordTf.setEchoChar('*');
            }
        });
    }

    @Override
    public void Update(KeyValuePair data) throws FileNotFoundException {
        Integer selectedTab = ServiceTPn.getSelectedIndex();


        if (selectedTab == 0) {
            ArrayList<Item> Basket = (ArrayList<Item>)data.value;


            switch (data.key) {
                case AbstractController.LIST:

                    ShoppingTA.setText("");
                    QuantityTA.setText("");
                    PriceTA.setText("");

                    String ItemList = ShoppingTA.getText();
                    String Quantities = QuantityTA.getText();
                    String PriceList = PriceTA.getText();

                    for (Item item: Basket)
                    {
                        ItemList += item.getItemName() + "\n";
                        Quantities += item.getQuantity() + "\n";
                        DecimalFormat Df = new DecimalFormat("£#0.00");
                        PriceList += Df.format(item.getPrice()) + "\n";
                    }

                    ShoppingTA.setText(ItemList);
                    QuantityTA.setText(Quantities);
                    PriceTA.setText(PriceList);

                    break;
            }
        }
        else if (selectedTab == 1) {

            switch (data.key) {
                case AbstractController.LIST:
                    ArrayList<Item> Products = (ArrayList<Item>)data.value;
                    System.out.println("We're updating");

                    StockDatabaseTA.setText("");
                    StockDatabase.clear();

                    Scanner ProductFile = new Scanner(new File("products.txt"));

                    while(ProductFile.hasNextLine())
                    {
                        StockDatabaseTA.append(ProductFile.next() + "\n");
                        ProductFile.nextLine();
                    }

                    ProductFile.close();


                    for (int i = 0; i < Products.size(); i++) {
                        StockDatabase.addElement(Products.get(i));
                    }

                    break;

                case AbstractController.LOGIN:

                    if ((Boolean)data.value == true)
                    {
                        JOptionPane.showMessageDialog(mainPanel, "Login Successful!");


                        UsernameTf.setEnabled(false);
                        PasswordTf.setEnabled(false);
                        LoginBtn.setEnabled(false);

                        StockDatabaseTA.setEnabled(true);
                        StockDatabaseLt.setEnabled(true);

                        addStockPanel.setEnabled(true);
                        AddBtn.setEnabled(true);
                        ItemNameTf.setEnabled(true);
                        DescTf.setEnabled(true);
                        ScanCodeTf.setEnabled(true);
                        QuantityTf.setEnabled(true);
                        PriceTf.setEnabled(true);

                    }
                    else
                    {
                        JOptionPane.showMessageDialog(mainPanel,"No Username or Password Matched, Try Again");
                    }


                    break;


                case AbstractController.ITEM:

                    StockDatabase.addElement((Item)data.value);
                    String ItemList2 = StockDatabaseTA.getText();
                    ItemList2 += ((Item)data.value).getItemName() + "\n";
                    StockDatabaseTA.setText(ItemList2);
                    break;
            }
        }
    }
}
