package gui;

import casino.Stavka;
import javafx.scene.control.RadioButton;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Interface extends JFrame {

    int balance;

    private JButton press = new JButton("OK");
    private JLabel redLab = new JLabel("Красный");
    private JLabel blackLab = new JLabel("Чёрный");
    private JLabel greenLab = new JLabel("Зелёный");
    private JRadioButton redRad = new JRadioButton();
    private JRadioButton blackRad = new JRadioButton();
    private JRadioButton greenRad = new JRadioButton();
    private JLabel stavLab = new JLabel("Ставка:");
    private JTextField stavField = new JTextField("", 5);
    private JLabel balLab = new JLabel("Баланс: " + balance + "$");
    private JTextField balField = new JTextField("", 5);
    private JButton balButt = new JButton("Пополнить");
    private TextArea log = new TextArea(10, 50);


    public Interface() {
        Frame frame = new Frame("Roulette");
        frame.setLayout(new BorderLayout());

        frame.setBounds(600, 200, 400, 280);

        Panel cont = new Panel();
        cont.setLayout(new GridLayout(4, 3, 2, 2));

        cont.add(redLab);
        redLab.setForeground(Color.red);

        cont.add(blackLab);
        blackLab.setForeground(Color.black);

        cont.add(greenLab);
        greenLab.setForeground(Color.green);

        ButtonGroup rad = new ButtonGroup();
        rad.add(redRad);
        rad.add(blackRad);
        rad.add(greenRad);

        cont.add(redRad);
        cont.add(blackRad);
        cont.add(greenRad);
        cont.add(stavLab);
        cont.add(stavField);
        cont.add(press);
        cont.add(balLab);
        cont.add(balField);
        cont.add(balButt);
        press.addActionListener(new Button1());
        balButt.addActionListener(new Button2());

        frame.add(cont, BorderLayout.CENTER);
        frame.add(log, BorderLayout.SOUTH);
        frame.addWindowListener(new CloseWin());
        frame.pack();
        frame.setVisible(true);
    }

    class Button2 implements ActionListener {
        int ball;

        public void actionPerformed(ActionEvent e) {

            ball = Integer.parseInt(balField.getText());
            balField.setText("");

            balance = ball + balance;

            if (ball <= 0) {
                JOptionPane.showMessageDialog(null, "Внос не может быть меньше или равна 0!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Ваш баланс пополнен на " + ball + "$.", "Пополнение баланса", JOptionPane.PLAIN_MESSAGE);
                balLab.setText("Баланс: " + balance + "$");
            }
        }
    }

    class Button1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            int deng = Integer.parseInt(stavField.getText());
            stavField.setText("");

            if (deng > balance) {
                JOptionPane.showMessageDialog(null, "У вас не хватает $!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            } else {

                char colour = 0;

                if (redRad.isSelected()) {
                    colour = 'r';
                }
                if (blackRad.isSelected()) {
                    colour = 'b';
                }
                if (greenRad.isSelected()) {
                    colour = 'z';
                }

                int prog = Stavka.rand(0);

                char prog1 = 0;
                int stav = deng;
                String Col = "";

                if (prog % 2 == 0) {
                    prog1 = 'r';
                    Col = "Красный";
                }
                if (prog % 2 == 1) {
                    prog1 = 'b';
                    Col = "Черный";
                }
                if (prog == 0) {
                    prog1 = 'z';
                    Col = "Зелёный";
                    stav = deng * 14;
                }

                String rez = "Выпадает: " + prog + " (" + Col + ")";

                if (prog1 == colour) {
                    balance = balance + stav;
                    JOptionPane.showMessageDialog(null, rez + "\nПобеда!", "Результат", JOptionPane.PLAIN_MESSAGE);
                } else {
                    balance = balance - deng;
                    JOptionPane.showMessageDialog(null, rez + "\nПроигрыш!", "Результат", JOptionPane.PLAIN_MESSAGE);
                }
                balLab.setText("Баланс: " + balance + "$");
            }
        }
    }
    private class CloseWin extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}