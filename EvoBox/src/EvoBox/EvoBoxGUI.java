package EvoBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EvoBoxGUI extends JPanel {
    private JPanel mainPanel;
    private JPanel gamePanel;

    private int anzFood = 0;
    private int maxFood = 10;
    private food[] allFood = new food[maxFood];

    public EvoBoxGUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 1200, 800);
        mainPanel.setPreferredSize(new Dimension(1200, 800));

        gamePanel = new JPanel();
        gamePanel.setBounds(0, 0, 1200, 800);
        gamePanel.setLayout(null);
        gamePanel.setPreferredSize(new Dimension(1200,800));
        mainPanel.add(gamePanel);


        // Fruit wird geladen
        loadFruit();


    }


    private void loadFruit() {

        ImageIcon fruit = new ImageIcon(getClass().getClassLoader().getResource("EvoBox/images/apple.png"));

        if (fruit.getIconWidth() == -1 || fruit.getIconHeight() == -1) {
            System.out.println("Failed to load image: src/images/apple.png");
            return;
        }



        int height = fruit.getIconHeight();
        int width = fruit.getIconWidth();

        food aFood = new food(100, 200, width, height, gamePanel.getWidth(), gamePanel.getHeight(), fruit);

        allFood[anzFood] = aFood;
        gamePanel.add(aFood);
        gamePanel.repaint();

        System.out.println("Jawoll ja");
        System.out.println(aFood);

    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("EvoBox am Grinden");
        EvoBoxGUI evoBoxGUI = new EvoBoxGUI();
        frame.setContentPane(evoBoxGUI.getMainPanel());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false); // Prevent the user from resizing the window
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}