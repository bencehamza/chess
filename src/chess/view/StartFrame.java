package chess.view;

import chess.model.gameplay.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartFrame extends JFrame implements ActionListener {
    private final JButton playButton;
    private final JTextField userName1;
    private final JTextField userName2;

    public StartFrame() {
        setTitle("Chess");

        setIconImage(new ImageIcon("src/resources/welcome.jpg").getImage());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setResizable(false);

        JPanel greeting = new JPanel();
        greeting.setBackground(Color.black);
        greeting.setLayout(null);

        playButton = new JButton("Play");
        playButton.addActionListener(this);
        playButton.setFocusable(false);
        playButton.setBackground(Color.white);
        playButton.setForeground(Color.black);

        JLabel label = new JLabel("Welcome to Chess Game!");
        JLabel userLabel1 = new JLabel("Player 1");
        JLabel userLabel2 = new JLabel("Player 2");

        label.setForeground(Color.white);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.TOP);
        label.setIcon(new ImageIcon("src/resources/welcome.jpg"));
        label.setFont(new Font("Times New Roman", Font.BOLD, 30));
        label.setIconTextGap(30);
        userLabel1.setForeground(Color.white);
        userLabel2.setForeground(Color.white);

        userName1 = new JTextField("Player1");
        userName2 = new JTextField("Player2");

        playButton.setBounds(200, 500, 100, 50);
        label.setBounds(80, 0, 400, 400);
        userLabel1.setBounds(150, 420, 70, 15);
        userLabel2.setBounds(150, 460, 70, 15);
        userName1.setBounds(220, 410, 100, 35);
        userName2.setBounds(220, 450, 100, 35);

        greeting.add(label);
        greeting.add(userLabel1);
        greeting.add(userName1);
        greeting.add(userLabel2);
        greeting.add(userName2);
        greeting.add(playButton);
        add(greeting);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == playButton) {
            dispose();
            Board board = new Board(userName1.getText(), userName2.getText());
            new GameFrame(board);
        }
    }
}
