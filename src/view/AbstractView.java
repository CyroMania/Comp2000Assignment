package view;

import controller.AbstractController;
import extras.KeyValuePair;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public abstract class AbstractView extends JFrame{

    private int defWindowSize = 500;
    protected AbstractController controller;

    protected void Initialise(JPanel mainPanel){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(defWindowSize, defWindowSize));
        this.setContentPane(mainPanel);
        this.pack();

    }

    public abstract void Update(KeyValuePair data) throws FileNotFoundException;

    public void SetController(AbstractController inController){
        this.controller = inController;
    }

    public AbstractController getController() {
        return controller;
    }
}
