package EvoBox;

import sun.security.util.ArrayUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class EvoBoxGUI extends JPanel {

    // Alle GUI Elemente
    private JPanel mainPanel;
    private JPanel gamePanel;
    private JButton moveButton;
    private JButton spawnFood;
    private JButton spawnFood5;
    private Timer globalTimer;

    // Momentare Anzahl von Food und Maximal zu spawnende

    private ArrayList<food> allFood = new ArrayList<food>();


    private int anzFood = 0;
    private int maxFood = 10;
    //private food[] allFood = new food[maxFood];

    // Momentare Anzahl von Slimes
    private int anzSlimes = 0;
    private int currentSlimes = 0;
    private slime[] allSlimes = new slime[100];

    // neuer random Seed
    public double seed = Math.random();
    public Random rand = new Random((long) seed);


    public int toSpawnFood = 20;   // Anzahl von zu spawnenden Früchten
    public int toSpawnSlime = 5;   // Anzhal von zu spawnenden Slimes
    public int foodSize = 32;      // Größe von Früchten als Faktor    Base : 16
    public int slimeSize = 64;     // Größe von Slimes als Faktor      Base : 16

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


        int buttonX = 10;       // Erster Button Y-Koordinate
        int buttonY = 10;       // Erster Button X-Koordinate
        int buttonW = 120;      // Button Breite
        int buttonH = 30;       // Button Höhe
        int buttonXDif = buttonW + 30;  // Button X-Entfernung


        moveButton = new JButton("Move Slime");
        moveButton.setBounds(buttonX, buttonY, buttonW, buttonH);
        moveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentSlimes > 0 && anzFood > 0) {
                    for(int i = 0; i!=currentSlimes; i++){
                        int foodSelected = (int) (Math.random()*allFood.size());                        // Nimmt ein zufälliges Element aus allFood
                        moveSlimeTowardsFood(allSlimes[i], allFood.get(foodSelected), 100);    // Duration: die Länge um zum Food zu kommen in Millisekunden
                        gamePanel.remove(allFood.get(foodSelected));                                    // löscht das gewählte Element vom gamePanel
                        allFood.remove(foodSelected);                                                   // löscht das gewählte Element aus allFood
                        anzFood--;
                    }
                }
            }
        });
        gamePanel.add(moveButton);


        spawnFood = new JButton("Spawn Food");
        spawnFood.setBounds(buttonX + buttonXDif, buttonY, buttonW, buttonH);
        spawnFood.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = (int) (Math.random() * (gamePanel.getWidth() - 75));
                int y = (int) (Math.random() * (gamePanel.getHeight() - 100));
                loadFruit(x, y, foodSize);
            }
        });
        gamePanel.add(spawnFood);


        spawnFood5 = new JButton("Spawn Food 5x");
        spawnFood5.setBounds(buttonX + (buttonXDif * 2), buttonY, buttonW, buttonH);
        spawnFood5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 5; i++) {
                    int x = (int) (Math.random() * (gamePanel.getWidth() - 75));
                    int y = (int) (Math.random() * (gamePanel.getHeight() - 100));
                    loadFruit(x, y, foodSize);
                }
            }
        });
        gamePanel.add(spawnFood5);



        globalTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < currentSlimes; i++) {
                    allSlimes[i].updatePosition();
                }
                gamePanel.repaint();
            }
        });

        globalTimer.start();





            for (int i = 0; i < toSpawnFood; i++) {
                int x = (int) (Math.random() * (gamePanel.getWidth() - 75));
                int y = (int) (Math.random() * (gamePanel.getHeight() - 100));
                loadFruit(x, y, foodSize);
            }

            for (int i = 0; i < toSpawnSlime; i++) {
                int x = (int) (Math.random() * (gamePanel.getWidth() - 75));
                int y = (int) (Math.random() * (gamePanel.getHeight() - 100));
                loadSlimes(x, y, slimeSize);
            }
    }


    private void loadFruit(int x, int y, int size) {


        ImageIcon fruit = new ImageIcon(getClass().getClassLoader().getResource("EvoBox/images/apple.png"));
        Image scaledFruit = fruit.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        ImageIcon scaledFruitIcon = new ImageIcon(scaledFruit);

        int sizeScaled = scaledFruitIcon.getIconHeight();

        food aFood = new food(x, y, sizeScaled, sizeScaled, gamePanel.getWidth(), gamePanel.getHeight(), scaledFruitIcon);

        allFood.add(anzFood, aFood);
        gamePanel.add(aFood);
        anzFood++;

    }


    private void loadSlimes(int x, int y, int scale) {

        int randomInt = rand.nextInt(100-50) + 50;
        double size = (double) randomInt / 100;

        System.out.println(size);


        double perception = Math.random();      // To-Do
        double energy = Math.random();          // To-Do

        int scaledSize = (int) (size * scale);

        ImageIcon slime = new ImageIcon(getClass().getClassLoader().getResource("EvoBox/images/slime.png"));
        Image scaledSlime = slime.getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
        ImageIcon scaledSlimeIcon = new ImageIcon(scaledSlime);

        int height = scaledSlimeIcon.getIconHeight();
        int width = scaledSlimeIcon.getIconWidth();

        slime aSlime = new slime(x, y, width, height, gamePanel.getWidth(), gamePanel.getHeight(), scaledSlimeIcon, energy, size, perception);

        allSlimes[currentSlimes] = aSlime;
        gamePanel.add(aSlime);
        currentSlimes++;

    }


    private void moveSlimeTowardsFood(slime aSlime, food aFood, long duration) {
        int targetX = aFood.getX();
        int targetY = aFood.getY();
        int targetSize = aFood.getHeight();
        aSlime.startMove(targetX, targetY, duration, targetSize);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("EvoBox.0.1");     // Titel vom Fenster
        EvoBoxGUI evoBoxGUI = new EvoBoxGUI();
        frame.setContentPane(evoBoxGUI.mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);                   // Die Größe des Fensters
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);                              // Fenster hat eine Feste Größe
        frame.setVisible(true);
    }


}