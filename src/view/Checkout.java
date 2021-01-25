package view;

import controller.AbstractController;
import controller.MultiModelController;
import extras.KeyValuePair;
import model.Item;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

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

    private List<String> ScannedItems;

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


        setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(600, 600));
        this.pack();

        ShoppingTA.setText("");
        this.setVisible(true);

        //Initialise(3,5);

        ServiceTPn.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = ServiceTPn.getSelectedIndex();
                controller.swapModel(index);
                System.out.println(index);


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
                    Float Price = Float.parseFloat(PriceTf.getText());
                    Item newProduct = new Item(itemName, Description, scanCode, Quantity, Price);
                    controller.addItemToList(new KeyValuePair(AbstractController.STOCK, newProduct));
                }
                catch (Exception x)
                {
                    System.out.println("ITEM FAILED TO ADD");
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
    }

    @Override
    public void Update(KeyValuePair data) {
        Integer selectedTab = ServiceTPn.getSelectedIndex();

        if (selectedTab == 0) {
            switch (data.key) {
                case AbstractController.LIST:
                    ShoppingTA.setText("");
                    String ItemList = ShoppingTA.getText();
                    for (Item item: (ArrayList<Item>)data.value)
                    {
                        ItemList += item.getItemName();
                        ItemList += "\n";
                    }
                    ShoppingTA.setText(ItemList);
            }
        }
        else if (selectedTab == 1) {
            switch (data.key) {
                case AbstractController.LIST:
                    StockDatabaseTA.setText("");
                    String ItemList = StockDatabaseTA.getText();
                    for (Item item: (ArrayList<Item>)data.value)
                    {
                        ItemList += item.getItemName();
                        ItemList += "\n";
                    }
                    StockDatabaseTA.setText(ItemList);
            }
        }
    }
}
