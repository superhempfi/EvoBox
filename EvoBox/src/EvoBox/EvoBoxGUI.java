package EvoBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class EvoBoxGUI extends JPanel {
    private JPanel mainPanel;
    private JPanel gamePanel;
    private JButton moveButton;
    private Timer globalTimer;

    private int anzFood = 0;
    private int maxFood = 10;
    private food[] allFood = new food[maxFood];

    private int anzSlimes = 0;
    private int currentSlimes = 0;
    private slime[] allSlimes = new slime[100];

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

        moveButton = new JButton("Move Slime");
        moveButton.setBounds(10, 10, 120, 30);
        moveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (anzSlimes > 0) {
                    int index = (int) (Math.random() * anzSlimes);
                    int newX = (int) (Math.random() * (gamePanel.getWidth() - 100));
                    int newY = (int) (Math.random() * (gamePanel.getHeight() - 100));
                    allSlimes[index].startMove(newX, newY, 2000);  // Move over 2 seconds
                }
            }
        });
        mainPanel.add(moveButton);


        globalTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < anzSlimes; i++) {
                    allSlimes[i].updatePosition();
                }
                gamePanel.repaint();
            }
        });

        globalTimer.start();







        // Fruit wird geladen
        // i = 0 ; i < 10 -> 10 mal geladen

        // Anzahl von spawns
        int toSpawn = 10;

        for (int i = 0; i < toSpawn; i++) {
            int x = (int) (Math.random() * (gamePanel.getWidth() - 75));
            int y = (int) (Math.random() * (gamePanel.getHeight() - 100));
            loadFruit(x, y, 32);
        }

        loadSlimes(50, 200, 64);

        System.out.println(Arrays.toString(allFood));

        System.out.println(allFood[0].getX());


        allSlimes[0].startMove(allFood[0].getX(), allFood[0].getY(), 2000);



    }


    private void loadFruit(int x, int y, int size) {


        int fruitWidth = size;
        int fruitHeight = fruitWidth;


        ImageIcon fruit = new ImageIcon(getClass().getClassLoader().getResource("EvoBox/images/apple.png"));
        Image scaledFruit = fruit.getImage().getScaledInstance(fruitWidth, fruitHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledFruitIcon = new ImageIcon(scaledFruit);

        int height = scaledFruitIcon.getIconHeight();
        int width = scaledFruitIcon.getIconWidth();

        food aFood = new food(x, y, width, height, gamePanel.getWidth(), gamePanel.getHeight(), scaledFruitIcon);

        allFood[anzFood] = aFood;
        gamePanel.add(aFood);
        anzFood++;

    }

    private void loadSlimes(int x, int y, int size) {

        int slimeWidth = size;
        int slimeHeight = slimeWidth;

        ImageIcon slime = new ImageIcon(getClass().getClassLoader().getResource("EvoBox/images/slime.png"));
        Image scaledSlime = slime.getImage().getScaledInstance(slimeWidth, slimeHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledSlimeIcon = new ImageIcon(scaledSlime);

        int height = scaledSlimeIcon.getIconHeight();
        int width = scaledSlimeIcon.getIconWidth();

        slime aSlime = new slime(x, y, width, height, gamePanel.getWidth(), gamePanel.getHeight(), scaledSlimeIcon);

        allSlimes[currentSlimes] = aSlime;
        gamePanel.add(aSlime);
        currentSlimes++;

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


}