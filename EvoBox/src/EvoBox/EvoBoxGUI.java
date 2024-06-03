package EvoBox;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class EvoBoxGUI {
    private JPanel mainPanel;
    private JPanel gamePanel;

    //private Timer myTimer;

    private int anzFood = 0;
    private int maxFood = 10;
    private food[] allFood = new food[maxFood];



    public EvoBoxGUI(){
        gamePanel.setBounds(0, 0, 1200, 800);
        gamePanel.setLayout(null);



        // Fruit wird geladen

        ImageIcon fruit = new ImageIcon("src/images/apple.png");
        int height = fruit.getIconHeight();
        int width = fruit.getIconWidth();
        //food aFood = new food(Math.round(Math.random()*gamePanel.getWidth()), Math.round(Math.random()*gamePanel.getHeight()), fruit);

        food aFood = new food(100, 200, width, height, gamePanel.getWidth(), gamePanel.getHeight(), fruit);
        allFood[anzFood] = aFood;
        gamePanel.add(aFood);

        System.out.println("Jawoll ja");
        System.out.println(aFood);




    }


    public static void main(String[] args) {


        ImageIcon playField = new ImageIcon("src/EvoBox/images/field.png");
        ImageIcon foodApple = new ImageIcon("src/EvoBox/images/apple.png");
        JLabel gameBG = new JLabel(playField);
        JLabel food = new JLabel(foodApple);

        JFrame frame = new JFrame("EvoBox am Grinden");
        frame.setContentPane(new EvoBoxGUI().mainPanel);
        frame.add(gameBG);
        frame.add(food);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setIconImage(playField.getImage());

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
