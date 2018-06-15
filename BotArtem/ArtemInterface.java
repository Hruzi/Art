package BotArtem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ArtemInterface extends JFrame {
    private JButton exit_butt = new JButton("SLEEP");

    public ArtemInterface() {

        Frame frame = new Frame("TelegramBot");
        frame.setLayout(new BorderLayout());
        frame.setBounds(600, 200, 200, 120);
        frame.add(exit_butt, BorderLayout.CENTER);
        exit_butt.addActionListener(new ArtemInterface.ExitButton());
        frame.setVisible(true);
    }

    class ExitButton implements ActionListener {
        public void actionPerformed(ActionEvent e){
            System.exit(1);
        }
    }
}
