import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WebsiteInput extends Frame implements ActionListener {
    JTextField tf; static JLabel l; JButton b; boolean buttonPressed = false;
    WebsiteInput(){
        boolean buttonPressed = false;
        tf = new JTextField();
        tf.setBounds(50,50,150,20);

        l = new JLabel();
        l.setBounds(50,100,500,20);

        b = new JButton("Please enter a url");
        b.setBounds(50,150,200,30);

        b.addActionListener(this);

        add(b); add(tf); add(l);

        setSize(500,225);
        setLayout(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try{
            buttonPressed = true;
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }
}