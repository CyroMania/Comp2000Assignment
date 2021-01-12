package view;

import javax.swing.*;
import java.awt.*;

public abstract class View extends JFrame{
        private int defWindowSize = 900;

    protected void Initialise(int rows, int cols){
        this.setLayout(new GridLayout(rows, cols));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(defWindowSize, defWindowSize));
        this.pack();

        this.setVisible(true);
    }



    void UpdateView(){

    }
}
