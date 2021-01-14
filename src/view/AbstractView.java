package view;

import controller.AbstractController;
import controller.Controller;
import extras.KeyValuePair;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractView extends JFrame{

    private int defWindowSize = 900;
    protected AbstractController controller;

    protected void Initialise(int rows, int cols){
        this.setLayout(new GridLayout(rows, cols));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(defWindowSize, defWindowSize));
        this.pack();

        this.setVisible(true);
    }

    public abstract void Update(KeyValuePair data);

    public void SetController(AbstractController inController){
        this.controller = inController;
    }

    public AbstractController getController() {
        return controller;
    }
}
