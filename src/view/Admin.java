package view;

import javax.swing.*;
import java.awt.*;

public class Admin extends JFrame{
    private JPanel mainPanel;

    public Admin(){
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(960, 540));
        pack();
    }
}
