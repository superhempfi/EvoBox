package EvoBox;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;

public class slime extends JLabel {

    protected int x, y;
    protected int fw, fh;
    protected int w, h;
    private int startX, startY;
    private int targetX, targetY;
    private long startTime;
    private long duration;
    private boolean isMoving;
    private boolean hasArrived = false;
    private JPanel Parent;
    public ArrayList<food> closestFood = new ArrayList<>();

    // Attribute
    protected double energy;
    protected double size, speed;
    protected double perception;
    protected food foodTarget;






    public slime(int x, int y, int w, int h, int fw, int fh, ImageIcon slime, double energy, double size, double perception, JPanel Parent) {
        super();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.fw = fw;
        this.fh = fh;
        this.setIcon(slime);
        this.Parent = Parent;


        this.energy = energy;

        this.size = size;
        speed = 1 / this.size;      // Desto höher der erste Faktor desto geringer die Geschwindigkeit (eine Art scale)

        this.perception = perception;



        this.setBounds(x, y, w, h);
    }




    public void startMove(int targetX, int targetY, int targetSize, food target) {
        this.startX = this.x;
        this.startY = this.y;

        this.hasArrived = false;
        this.foodTarget = target;





        this.targetX = targetX + (targetSize / 2) - (this.w / 2);
        this.targetY = targetY + (targetSize / 2) - (this.h / 2);

        double distanceX = this.targetX - startX;
        double distanceY = this.targetY - startY;
        int distanceTotal = (int) Math.sqrt(distanceX * distanceX +distanceY*distanceY);       // Satz des Pythagoras



        this.duration = (long) (distanceTotal * size * 2);
        this.startTime = System.currentTimeMillis();
        this.isMoving = true;
    }

    public void updatePosition(ArrayList<food> allFood, EvoBoxGUI gui) {
        if (!isMoving) {
            this.hasArrived = true;
            return;
        }


        if (foodTarget == null || !allFood.contains(foodTarget)) {
            gui.startSlimeMovement(gui.allSlimes.indexOf(this));
            return;
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        double fraction = (double) elapsedTime / duration;
        if (fraction >= 1.0) {
            fraction = 1.0;
            isMoving = false;

        }

        int currentX = (int) (startX + fraction * (targetX - startX));
        int currentY = (int) (startY + fraction * (targetY - startY));
        this.setBounds(currentX, currentY, w, h);
        this.x = currentX;
        this.y = currentY;

    }


    public void sortClosestFood() {
        closestFood.sort((aFood, bFood) -> {

            int xDistanceA = x - aFood.getX();
            int yDistanceA = y - aFood.getY();
            double totalDistanceA = Math.sqrt(xDistanceA * xDistanceA + yDistanceA * yDistanceA);


            int xDistanceB = x - bFood.getX();
            int yDistanceB = y - bFood.getY();
            double totalDistanceB = Math.sqrt(xDistanceB * xDistanceB + yDistanceB * yDistanceB);


            return Double.compare(totalDistanceA, totalDistanceB);
        });
    }


    public void delete() {
        Parent.remove(this);
    }

    public boolean hasArrived() {return hasArrived;}

    public void addFood(food newFood){closestFood.add(newFood);}

    public void setEnergy(double newEnergy) {this.energy = newEnergy;}
    public void changeEnergy(double newEnergy) {this.energy = this.energy + newEnergy;}

    public double getEnergy() {return energy;}
    public double getSlimeSize() {return size;}
    public double getPerception() {return perception;}


}
