package EvoBox;



import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class EvoBoxGUI extends JPanel {

    // Alle GUI Elemente
    private final JPanel mainPanel;
    private final JPanel gamePanel;


    private Timer globalTimer;

    // Momentare Anzahl von Food und Maximal zu spawnende

    public ArrayList<food> allFood = new ArrayList<>();

    public ArrayList<slime> allSlimes = new ArrayList<>();






    public boolean isDay;
    private boolean allSlimesAtBorder = false;

    public int toSpawnFood = 50;   // Anzahl von zu spawnenden Früchten
    public int toSpawnSlime = 1;   // Anzhal von zu spawnenden Slimes

    public int toSpawnFoodButton = 50;
    public int toSpawnSlimeButton = 1;
    public int daysToRun = 1;

    public int foodSize = 32;      // Größe von Früchten als Faktor    Base : 16
    public int slimeSize = 64;     // Größe von Slimes als Faktor      Base : 16

    public double mutationEffect = 0.15;
    public double minStat = 0.4;
    public double maxStat = 1.1;

    private JButton doDays;
    private JSlider initialFoodSlider;
    private JSlider mutationSlider;
    private JSlider minStatSlider;
    private JSlider maxStatSlider;

    public EvoBoxGUI() {


        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 1200, 800);
        mainPanel.setPreferredSize(new Dimension(1200, 800));

        setLayout(new BorderLayout());
        gamePanel = new JPanel();
        gamePanel.setBounds(0, 0, 1200, 800);
        gamePanel.setLayout(null);
        gamePanel.setPreferredSize(new Dimension(1200,800));
        gamePanel.setBackground(new Color(40, 143, 45));
        mainPanel.add(gamePanel, BorderLayout.CENTER);



        firstSlimeSpawns();



        globalTimer = new Timer(10, e -> {

            long currentTime = System.currentTimeMillis();
            boolean allSlimesArrived = true;

            for (int i = 0; i < allSlimes.size(); i++) {

                slime currentSlime = allSlimes.get(i);

                if (!currentSlime.isDead()) {
                    currentSlime.updatePosition(allFood, EvoBoxGUI.this);

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
                    } else {
                        allSlimesArrived = false;
                    }
                } else {
                    if (currentTime - currentSlime.getDeathTime() > 5000) {
                        currentSlime.delete();
                        gamePanel.remove(currentSlime);
                        allSlimes.remove(i);
                        i--;
                    }
                }
            }
                // Das Ende des Tages
                if (allFood.isEmpty() && isDay) {

                    for (int i = 0; i < allSlimes.size(); i++) {
                        slime currentSlime = allSlimes.get(i);

                        // löscht den Slime wenn dessen Energie leer ist
                        if (!currentSlime.isDead()){
                            if (currentSlime.getEnergy() <= 0) {
                                currentSlime.markAsDead(currentTime);
                            } else if (currentSlime.getEnergy() >= 2) {

                                int x = currentSlime.getX() > (gamePanel.getWidth() / 2) ? currentSlime.getX() - 30 : currentSlime.getX() + 30;
                                int y = currentSlime.getY();

                                double size = currentSlime.getSlimeSize() * (1.0 + (Math.random() * mutationEffect * 2 - mutationEffect));
                                size = Math.max(minStat, Math.min(size, maxStat));

                                double perception = currentSlime.getPerception() * (1.0 + (Math.random() * mutationEffect * 2 - mutationEffect));
                                perception = Math.max(minStat, Math.min(perception, maxStat));

                                loadSlimes(x, y, slimeSize, size, perception, 1);
                                currentSlime.setEnergy(0);

                            } else if (currentSlime.getEnergy() == 1) {
                                currentSlime.setEnergy(0);
                            }
                        }

                    }
                    moveSlimesToBorder();
                    isDay = false;
                    allSlimesAtBorder = true;
                }

                if (allSlimesAtBorder && allSlimesArrived) {
                    allSlimesAtBorder = false;
                    if (daysToRun > 0) {
                        daysToRun--;
                        if (daysToRun > 0) {
                            doOneDay();
                            isDay = true;
                        } else {
                            doDays.setEnabled(true);
                        }
                    }
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


        allSlimes.add(aSlime);
        gamePanel.add(aSlime);



        for(int i = 0; i<allFood.size(); i++){
            aSlime.addFood(allFood.get(i));
        }
        aSlime.sortClosestFood();

    }


    public void startSlimeMovement(int index) {
        slime currentSlime = allSlimes.get(index);
        if (currentSlime.isDead()) return;

        currentSlime.closestFood.clear();

        for (food aFood : allFood) {
            currentSlime.addFood(aFood);
        }

        currentSlime.sortClosestFood();      // Sortiert die innere Liste von dem Slime nach der Entfernung

        Random random = new Random();
        double perception = currentSlime.getPerception();


        if (currentSlime.closestFood.isEmpty()){
            return;
        }


        int choice;

        if (perception >= 0.9){
            choice = 0;                        // Nähestest Food
        } else if (perception >= 0.7) {
            choice = random.nextDouble() < (0.9 - perception) ? 1 : 0;
        } else {
            double rand  = random.nextDouble();
            if (rand < 0.5) {
                choice = 0;
            } else if (rand < 0.75) {
                choice = 1;
            } else {
                choice = 2;
            }
        }


        if (choice >= currentSlime.closestFood.size()) {
            choice = currentSlime.closestFood.size() - 1;
        }


        food selectedFood = currentSlime.closestFood.get(choice);
        if (!selectedFood.getIfTargeted() || resolveConflict(currentSlime, selectedFood)) {
            selectedFood.setIfTargeted(true);
            moveSlimeTowardsFood(currentSlime,selectedFood);
        } else {
            for (food closestFood : currentSlime.closestFood) {
                if (!closestFood.getIfTargeted() || resolveConflict(currentSlime, selectedFood)) {
                    closestFood.setIfTargeted(true);
                    moveSlimeTowardsFood(currentSlime, closestFood);
                    break;
                }
            }
        }
    }


    private void moveSlimeTowardsFood(slime aSlime, food aFood) {
        int targetX = aFood.getX();
        int targetY = aFood.getY();
        int targetSize = aFood.getHeight();
        aSlime.startMove(targetX, targetY, targetSize, aFood);

    }


    private boolean resolveConflict(slime currentSlime, food targetFood) {
        for (slime otherSlime : allSlimes){
            if (otherSlime != currentSlime && otherSlime.foodTarget == targetFood){
                double currentDistance = currentSlime.distanceToFood(targetFood);
                double otherDistance = otherSlime.distanceToFood(targetFood);

                if (currentDistance < otherDistance){
                    otherSlime.foodTarget = null;
                    return true;
                } else {
                    return false;
                }

            }
        }
        return true;
    }


    private void moveSlimesToBorder() {
        int panelWidth = gamePanel.getWidth();
        int panelHeight = gamePanel.getHeight();


        for (slime currentSlime : allSlimes) {
            int x = 0;
            int y = 0;
            Random random = new Random();
            int leeway = random.nextInt(50);
            int border = random.nextInt(4);

            switch (border) {
                case 0: // Oben
                    x = random.nextInt(panelWidth) - currentSlime.getWidth();
                    y = leeway;
                    break;
                case 1: // Unten
                    x = random.nextInt(panelWidth) - currentSlime.getWidth();
                    y = panelHeight - 100 - leeway;
                    break;
                case 2: // Links
                    x = leeway;
                    y = random.nextInt(panelHeight) - 100;
                    break;
                case 3: // Rechts
                    x = panelWidth - 100 - leeway;
                    y = random.nextInt(panelHeight) - 100;
                    break;
            }

            currentSlime.startMove(x, y, currentSlime.getHeight(), null);
        }
    }


    private void createControlPanel() {
        JFrame controlFrame = new JFrame("Control Panel");
        controlFrame.setSize(350, 800);
        controlFrame.setLocation(mainPanel.getLocationOnScreen().x + 1178, mainPanel.getLocationOnScreen().y - 30);
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlFrame.add(controlPanel);

        addButtonsToControlPanel(controlPanel);

        controlPanel.setBackground(new Color(0, 7, 19));
        controlFrame.setVisible(true);
    }

    private void addButtonsToControlPanel(JPanel controlPanel) {
        Dimension buttonSize = new Dimension(150, 30);
        Dimension sliderSize = new Dimension(260, 40);

        JButton moveButton = createButton("Move Slimes", buttonSize);
        moveButton.setPreferredSize(new Dimension(buttonSize));
        moveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        moveButton.addActionListener(e -> {
            if (!allSlimes.isEmpty() && !allFood.isEmpty()) {
                for (int i = 0; i < allSlimes.size(); i++) {
                    startSlimeMovement(i);
                }
            }
        });

        controlPanel.add(Box.createVerticalStrut(20));

        controlPanel.add(moveButton);

        controlPanel.add(Box.createVerticalStrut(20));

        JButton killSlime = createButton("Kill Slimes", buttonSize);
        killSlime.setPreferredSize(new Dimension(buttonSize));
        killSlime.setAlignmentX(Component.CENTER_ALIGNMENT);
        killSlime.addActionListener(e -> {
            for (int i = allSlimes.size() - 1; i >= 0; i--){
                if (!allSlimes.isEmpty()){
                    slime latestSlime = allSlimes.get(i);
                    latestSlime.delete();
                    gamePanel.remove(latestSlime);
                    allSlimes.remove(i);


                }
            }
        });
        controlPanel.add(killSlime);

        controlPanel.add(Box.createVerticalStrut(20));

        JButton spawnFood = createButton("Spawn Food", buttonSize);
        spawnFood.setPreferredSize(new Dimension(buttonSize));
        spawnFood.setAlignmentX(Component.CENTER_ALIGNMENT);
        spawnFood.addActionListener(e -> {
            for (int i = 0; i < toSpawnFoodButton; i++) {
                loadFruit(foodSize);
            }
        });
        controlPanel.add(spawnFood);

        controlPanel.add(Box.createVerticalStrut(10));

        JSlider foodSlider = createSlider(1, 500, 50, sliderSize);
        foodSlider.addChangeListener(e -> toSpawnFoodButton = foodSlider.getValue());
        controlPanel.add(foodSlider);

        controlPanel.add(Box.createVerticalStrut(20));

        JButton spawnSlime = createButton("Spawn Slime", buttonSize);
        spawnSlime.setPreferredSize(new Dimension(buttonSize));
        spawnSlime.setAlignmentX(Component.CENTER_ALIGNMENT);
        spawnSlime.addActionListener(e -> {
            for (int i = 0; i < toSpawnSlimeButton; i++){
                int x = (int) (Math.random() * (gamePanel.getWidth() - 75));
                int y = (int) (Math.random() * (gamePanel.getHeight() - 100));
                double size = 0.5 + (Math.random() * 0.5);
                double perception = Math.random();
                double energy = 0;
                loadSlimes(x, y, slimeSize, size, perception, energy);
            }

        });
        controlPanel.add(spawnSlime);

        controlPanel.add(Box.createVerticalStrut(10));

        JSlider slimeSlider = createSlider(1, 10, 1, sliderSize);
        slimeSlider.addChangeListener(e -> toSpawnSlimeButton = slimeSlider.getValue());
        controlPanel.add(slimeSlider);

        controlPanel.add(Box.createVerticalStrut(20));

        JSlider daysSlider = createSlider(1, 10, 1, sliderSize);
        daysSlider.addChangeListener(e -> daysToRun = daysSlider.getValue());

        doDays = createButton("Do day(s)", buttonSize);
        doDays.setPreferredSize(new Dimension(buttonSize));
        doDays.setAlignmentX(Component.CENTER_ALIGNMENT);
        doDays.addActionListener(e -> {
            if (!isDay) {
                daysToRun = daysSlider.getValue();
                if (daysToRun > 0){
                    isDay = true;
                    doDays.setEnabled(false);
                    doOneDay();
                }
            }
        });
        controlPanel.add(doDays);

        controlPanel.add(Box.createVerticalStrut(10));

        controlPanel.add(daysSlider);

        controlPanel.add(Box.createVerticalStrut(20));

        JLabel initialFoodLabel = new JLabel("Food to generate");
        initialFoodLabel.setForeground(Color.WHITE);
        initialFoodLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(initialFoodLabel);

        controlPanel.add(Box.createVerticalStrut(10));

        initialFoodSlider = createSlider(1, 500, 50, sliderSize);
        initialFoodSlider.addChangeListener(e -> toSpawnFood = initialFoodSlider.getValue());
        controlPanel.add(initialFoodSlider);

        controlPanel.add(Box.createVerticalStrut(20));

        JLabel mutationLabel = new JLabel("Mutation factor");
        mutationLabel.setForeground(Color.WHITE);
        mutationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(mutationLabel);

        controlPanel.add(Box.createVerticalStrut(10));

        mutationSlider = createSlider(1, 100, 15, sliderSize);
        mutationSlider.addChangeListener(e -> mutationEffect = mutationSlider.getValue());
        controlPanel.add(mutationSlider);

        controlPanel.add(Box.createVerticalStrut(20));

        JLabel minMaxStatLabel = new JLabel("Min/Max stats");
        minMaxStatLabel.setForeground(Color.WHITE);
        minMaxStatLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(minMaxStatLabel);

        controlPanel.add(Box.createVerticalStrut(10));

        JPanel statRangePanel = new JPanel();
        statRangePanel.setLayout(new BoxLayout(statRangePanel, BoxLayout.Y_AXIS));

        minStatSlider = createSlider(1, 100, 40, sliderSize);
        minStatSlider.addChangeListener(e -> minStat = minStatSlider.getValue() / 100.0);
        statRangePanel.add(minStatSlider);

        maxStatSlider = createSlider(100, 200, 110, sliderSize);
        maxStatSlider.addChangeListener(e -> maxStat = maxStatSlider.getValue() / 100.0);
        statRangePanel.add(maxStatSlider);

        controlPanel.add(statRangePanel);
        controlPanel.add(Box.createVerticalStrut(20));

    }

    private JButton createButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        return button;
    }

    private JSlider createSlider(int min, int max, int value, Dimension size) {
        JSlider slider = new JSlider(min, max, value);
        slider.setMajorTickSpacing((max - min) / 2);
        slider.setMinorTickSpacing((max - min) / 10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider.setPreferredSize(size);
        slider.setMaximumSize(size);
        slider.setMinimumSize(size);
        return slider;
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("EvoBox");     // Titel vom Fenster
        EvoBoxGUI evoBoxGUI = new EvoBoxGUI();
        frame.setContentPane(evoBoxGUI.mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);                   // Die Größe des Fensters
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);                              // Fenster hat eine Feste Größe
        frame.setVisible(true);

        evoBoxGUI.createControlPanel();
    }


}