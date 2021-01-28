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
import java.text.DecimalFormat;
import java.util.ArrayList;

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

    private DefaultListModel<Item> StockDatabase;

    public Checkout(){

      //  mainPanel = new JPanel();

      //  ShoppingLst = new JList();
      //  ScanTf = new JTextField();
      //  ScanBtn = new JButton();
      //  addStockPanel = new JPanel();
      //  ScanCodeTf = new JTextField();
      //  ItemNameTf = new JTextField();
      //  DescTf = new JTextField();
      //  QuantityTf = new JTextField();
      //  PriceTf = new JTextField();
      //  AddBtn = new JButton();



        //mainPanel.add(ShoppingLst);
        //mainPanel.add(ScanTf);
        //mainPanel.add(ScanBtn);
        //mainPanel.add(addStockPanel);
        //mainPanel.add(ScanCodeTf);
        //mainPanel.add(ItemNameTf);
        //mainPanel.add(DescTf);
        //mainPanel.add(QuantityTf);
        //mainPanel.add(PriceTf);
        //mainPanel.add(AddBtn);


        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 800));
        this.pack();

        StockDatabase = new DefaultListModel<>();
        StockDatabaseLt.setModel(StockDatabase);
        StockDatabaseLt.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        StockDatabaseLt.setVisibleRowCount(5);

        ShoppingTA.setText("");

        this.setVisible(true);

        //Initialise(3,5);

        ServiceTPn.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = ServiceTPn.getSelectedIndex();
                controller.swapModel(index);
                System.out.println(index);


                controller.loadList();


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
                controller.removeItemFromList(new KeyValuePair(AbstractController.ITEM, itemToDelete));
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
    }

    @Override
    public void Update(KeyValuePair data) {
        Integer selectedTab = ServiceTPn.getSelectedIndex();
        ArrayList<Item> Products = (ArrayList<Item>)data.value;

        if (selectedTab == 0) {
            switch (data.key) {
                case AbstractController.LIST:

                    ShoppingTA.setText("");
                    QuantityTA.setText("");
                    PriceTA.setText("");

                    String ItemList = ShoppingTA.getText();
                    String Quantities = QuantityTA.getText();
                    String PriceList = PriceTA.getText();

                    for (Item item: Products)
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
                    System.out.println("We're updating");

                    StockDatabaseTA.setText("");
                    StockDatabase.clear();

                    String ItemList = StockDatabaseTA.getText();

                    for (int i = 0; i < Products.size(); i++) {
                        ItemList += Products.get(i).getItemName() + "\n";
                        StockDatabase.addElement(Products.get(i));
                    }

                    StockDatabaseTA.setText(ItemList);
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
