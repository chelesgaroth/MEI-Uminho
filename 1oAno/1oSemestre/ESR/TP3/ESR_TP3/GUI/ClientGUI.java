package GUI;

import javax.swing.*;
import java.awt.*;

public class ClientGUI extends JFrame {

    public JButton setupButton;
    public JButton playButton;
    public JButton pauseButton;
    public JButton tearButton;
    JPanel mainPanel;
    JPanel buttonPanel;

    JLabel iconLabel;
    ImageIcon icon;

    Timer cTimer;

    public ClientGUI() {

        setTitle("TebinaFlix");
        setSize(610, 450);

        // Create JButton and JPanel
        setupButton = new JButton("Ping");
        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        tearButton = new JButton("Teardown");
        mainPanel = new JPanel();
        buttonPanel = new JPanel();

        iconLabel = new JLabel();
        icon = new ImageIcon();
        ;

        /*
         * ImageIcon img = new ImageIcon("play.png");
         * Image image = img.getImage();
         * Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
         * // scale it the smooth way
         * ImageIcon play = new ImageIcon(newimg); // transform it back
         * 
         * 
         * playButton.setIcon(play);
         */
        // Add button to JPanel
        buttonPanel.setLayout(new GridLayout(1, 0));
        buttonPanel.add(setupButton);
        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(tearButton);

        // Image Display
        iconLabel.setIcon(icon);

        mainPanel.setLayout(null);
        mainPanel.add(iconLabel);
        mainPanel.add(buttonPanel);
        iconLabel.setBounds(110, 40, 380, 280);
        buttonPanel.setBounds(0, 362, 600, 50);

        // And JPanel needs to be added to the JFrame itself!
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public void displayVideo(byte[] imageBytes) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.createImage(imageBytes, 0, imageBytes.length);
        icon = new ImageIcon(image);
        iconLabel.setIcon(icon);
    }

}