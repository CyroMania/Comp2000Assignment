package view;

import model.Item;
import model.Stock;
import controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Checkout extends View{
    protected IController controller;

    private JPanel mainPanel;
    private JList ShoppingLst;
    private JTextField ScanTxt;
    private JButton ScanBtn;

    private List<String> ScannedItems;

    public Checkout(){

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 600));
        pack();


        ScanBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (ScanTxt.getText() != ""){
                    boolean doesExist = controller.CheckScanCode(ScanTxt.getText());

                    if (doesExist){
                        controller.AddItemToList(ScanTxt.getText());
                    }

                }
            }
        });
    }

}
