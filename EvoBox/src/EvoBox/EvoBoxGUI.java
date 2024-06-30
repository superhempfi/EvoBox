package EvoBox;



import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EvoBoxGUI extends JPanel {

    // Alle GUI Elemente
    private final JPanel mainPanel;
    private final JPanel gamePanel;


    private Timer globalTimer;

    // Momentare Anzahl von Food und Maximal zu spawnende

    public ArrayList<food> allFood = new ArrayList<>();

    public ArrayList<slime> allSlimes = new ArrayList<>();






    public boolean isDay;


    public int toSpawnFood = 10;   // Anzahl von zu spawnenden Früchten
    public int toSpawnSlime = 1;   // Anzhal von zu spawnenden Slimes
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
        int buttonYDif = buttonH + 20;


        JButton moveButton = new JButton("Move Slime");
        moveButton.setBounds(buttonX, buttonY, buttonW, buttonH);
        moveButton.addActionListener(e -> {
            if (!allSlimes.isEmpty() && !allFood.isEmpty()) {

                for(int i = 0; i != allSlimes.size(); i++){
                    startSlimeMovement(i);
                }

            }
        });
        gamePanel.add(moveButton);

        JButton spawnFood = new JButton("Spawn Food");
        spawnFood.setBounds(buttonX + buttonXDif, buttonY, buttonW, buttonH);
        spawnFood.addActionListener(e -> loadFruit(foodSize));
        gamePanel.add(spawnFood);

        JButton spawnFood5 = new JButton("Spawn Food 5x");
        spawnFood5.setBounds(buttonX + (buttonXDif * 2), buttonY, buttonW, buttonH);
        spawnFood5.addActionListener(e -> {
            for (int i = 0; i < 5; i++) {
                loadFruit(foodSize);
            }
        });
        gamePanel.add(spawnFood5);

        JButton spawnFood500 = new JButton("Spawn Food 500x");
        spawnFood500.setBounds(buttonX + (buttonXDif * 3), buttonY, buttonW + 20, buttonH);
        spawnFood500.addActionListener(e -> {
            for (int i = 0; i < 500; i++) {
                loadFruit(foodSize);
            }
        });
        gamePanel.add(spawnFood500);

        JButton spawnSlime = new JButton("Spawn Slime");
        spawnSlime.setBounds(buttonX, buttonY + buttonYDif, buttonW, buttonH);
        spawnSlime.addActionListener(e -> {
            int x = (int) (Math.random() * (gamePanel.getWidth() - 75));
            int y = (int) (Math.random() * (gamePanel.getHeight() - 100));
            double size = 0.5 + (Math.random() * 0.5);
            double perception = Math.random();      // To-Do
            double energy = 0;          // To-Do
            loadSlimes(x, y, slimeSize, size, perception, energy);
        });
        gamePanel.add(spawnSlime);

        JButton killSlime = new JButton("Kill SLime");
        killSlime.setBounds(buttonX + buttonXDif, buttonY + buttonYDif, buttonW, buttonH);
        killSlime.addActionListener(e -> {
            if (!allSlimes.isEmpty()){
                slime latestSlimes = allSlimes.get(allSlimes.size() - 1);
                latestSlimes.delete();
                gamePanel.remove(allSlimes.get(allSlimes.size() - 1));
                allSlimes.remove(allSlimes.size() - 1);


            }
        });
        gamePanel.add(killSlime);

        JButton doOneDay = new JButton("Do one Day");
        doOneDay.setBounds(buttonX + (2 * buttonXDif), buttonY + buttonYDif, buttonW, buttonH);
        doOneDay.addActionListener(e -> {
            doOneDay();
            isDay = true;
        });
        gamePanel.add(doOneDay);



        firstSlimeSpawns();


        globalTimer = new Timer(10, e -> {
            for (int i = 0; i < allSlimes.size(); i++) {

                slime currentSlime = allSlimes.get(i);
                currentSlime.updatePosition(allFood,EvoBoxGUI.this);

                // Überprüft ob der Slime bei seinem Ziel angelangt ist
                if (currentSlime.hasArrived()) {
                    food targetFood = currentSlime.foodTarget;

                    // Überprüft ob das Ziel noch existiert
                    if (targetFood != null && allFood.contains(targetFood)) {

                        // löscht das Ziel / Frisst es
                        allFood.remove(targetFood);
                        gamePanel.remove(targetFood);
                        currentSlime.foodTarget = null;

                        // Gibt dem Slime Energie für das Fressen
                        currentSlime.changeEnergy(1);

                        // Falls es noch Essen auf dem Feld gibt
                        if (!allFood.isEmpty()) {
                            startSlimeMovement(i);
                        }
                    }
                }
            }
            // Das Ende des Tages
            if (allFood.isEmpty() && isDay) {
                for (int i = 0; i < allSlimes.size(); i++) {
                    slime currentSlime = allSlimes.get(i);

                    // löscht den Slime wenn dessen Energie leer ist
                    if (currentSlime.getEnergy() <= 0) {
                        currentSlime.delete();
                        gamePanel.remove(currentSlime);
                        allSlimes.remove(i);
                        i--;

                    } else if (currentSlime.getEnergy() >= 2) {

                        int x;
                        int y = currentSlime.getY();
                        double size;
                        double perception;
                        double energy = 1;

                        if (currentSlime.getX() > (gamePanel.getWidth() / 2)) {
                            x = currentSlime.getX() - 30;
                        } else {
                            x = currentSlime.getX() + 30;
                        }

                        size = currentSlime.getSlimeSize() * (0.85 + (Math.random() * 0.30));
                        if (size < 0.4) {
                            size = 0.4;
                        } else if (size > 1.1) {
                            size = 1.1;
                        }
                        perception = currentSlime.getPerception() * (0.85 + (Math.random() * 0.30));

                        loadSlimes(x, y, slimeSize, size, perception, energy);
                        currentSlime.setEnergy(0);

                    } else if (currentSlime.getEnergy() == 1) {
                        currentSlime.setEnergy(0);
                    }
                }
                isDay = false;
            }

            gamePanel.repaint();
        });
        globalTimer.start();



    }


    private void firstSlimeSpawns(){

        for (int i = 0; i < toSpawnSlime; i++) {
            double size = 0.5 + (Math.random() * 0.5);
            double perception = Math.random();      // To-Do
            double energy = 0;          // To-Do
            int x = (int) (Math.random() * (gamePanel.getWidth() - 75));
            int y = (int) (Math.random() * (gamePanel.getHeight() - 100));
            loadSlimes(x, y, slimeSize, size, perception, energy);
        }
    }


    public void doOneDay() {
        // Spawn new food
        for (int i = 0; i < toSpawnFood; i++) {
            loadFruit(foodSize);
        }

        // Initiate slime movement if there is food and slimes
        if (!allSlimes.isEmpty() && !allFood.isEmpty()) {
            for (int i = 0; i < allSlimes.size(); i++) {
                startSlimeMovement(i);
            }
        }
    }


    private void loadFruit(int size) {


        ImageIcon fruit = new ImageIcon(getClass().getClassLoader().getResource("EvoBox/images/apple.png"));
        Image scaledFruit = fruit.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        ImageIcon scaledFruitIcon = new ImageIcon(scaledFruit);

        int sizeScaled = scaledFruitIcon.getIconHeight();


        int x = (int) (Math.random() * (gamePanel.getWidth() - 75));
        int y = (int) (Math.random() * (gamePanel.getHeight() - 100));


        food aFood = new food(x, y, sizeScaled, sizeScaled, gamePanel.getWidth(), gamePanel.getHeight(), scaledFruitIcon, gamePanel, allFood);

        allFood.add(aFood);
        gamePanel.add(aFood);
        aFood.addIndex(allFood.size() - 1);

    }


    private void loadSlimes(int x, int y, int scale, double size, double perception, double energy) {



        int scaledSize = (int) (size * scale);

        ImageIcon slime = new ImageIcon(getClass().getClassLoader().getResource("EvoBox/images/slime.png"));
        Image scaledSlime = slime.getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
        ImageIcon scaledSlimeIcon = new ImageIcon(scaledSlime);

        int height = scaledSlimeIcon.getIconHeight();
        int width = scaledSlimeIcon.getIconWidth();

        slime aSlime = new slime(x, y, width, height, gamePanel.getWidth(), gamePanel.getHeight(), scaledSlimeIcon, energy, size, perception, this);

        System.out.println("My perception is " + perception);

        allSlimes.add(aSlime);
        gamePanel.add(aSlime);



        for(int i = 0; i<allFood.size(); i++){
            aSlime.addFood(allFood.get(i));
        }
        aSlime.sortClosestFood();

    }


    public void startSlimeMovement(int index) {
        slime currentSlime = allSlimes.get(index);
        currentSlime.closestFood.clear();

        for (food aFood : allFood) {
            currentSlime.addFood(aFood);
        }

        currentSlime.sortClosestFood();      // Sortiert die innere Liste von dem Slime nach der Entfernung

        for (food closestFood : currentSlime.closestFood){
            double chance = Math.random() * 0.5;
            if (chance < currentSlime.getPerception() || closestFood == currentSlime.closestFood.get(currentSlime.closestFood.size() - 1)){
                moveSlimeTowardsFood(currentSlime, closestFood);
                break;
            }
        }




/*
        if (currentSlime.foodTarget == null && !currentSlime.closestFood.isEmpty()){
            moveSlimeTowardsFood(currentSlime, currentSlime.closestFood.get(0));
        }
*/

    }









    /*
    private void startSlimeMovement(int index){

        slime currentSlime = allSlimes.get(index);
        currentSlime.closestFood.clear();

        for (food aFood : allFood){
            currentSlime.addFood(aFood);
        }

        allSlimes.get(index).sortClosestFood();
        food closestFood = allSlimes.get(index).closestFood.get(0);

        if (!closestFood.getIfTargeted()){
            closestFood.setIfTargeted(true);
            moveSlimeTowardsFood(currentSlime, closestFood);
        }

    }


    */
    private void moveSlimeTowardsFood(slime aSlime, food aFood) {
        int targetX = aFood.getX();
        int targetY = aFood.getY();
        int targetSize = aFood.getHeight();
        aSlime.startMove(targetX, targetY, targetSize, aFood);

    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("EvoBox.0.2");     // Titel vom Fenster
        EvoBoxGUI evoBoxGUI = new EvoBoxGUI();
        frame.setContentPane(evoBoxGUI.mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);                   // Die Größe des Fensters
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);                              // Fenster hat eine Feste Größe
        frame.setVisible(true);
    }


}