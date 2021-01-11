import javax.swing.*;
import java.awt.*;

public class Checkout extends JFrame {

    private JPanel mainPanel;

    public Checkout(){
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(960, 540));
        pack();
    }

    public static void main(String[] args){
        Checkout checkout = new Checkout();
        checkout.setVisible(true);
    }
}
