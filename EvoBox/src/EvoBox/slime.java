package EvoBox;

import javax.swing.*;

public class slime extends JLabel {

    protected int x, y;
    protected int fw, fh;
    protected int w, h;
    private int startX, startY;
    private int targetX, targetY;
    private double distanceX, distanceY;
    private int distanceTotal;
    private long startTime;
    private long duration;
    private boolean isMoving;
    private boolean hasArrived = false;

    // Attribute
    protected double energy;
    protected double size, speed;
    protected double perception;
    protected food foodTarget;
    protected int foodSelected;





    public slime(int x, int y, int w, int h, int fw, int fh, ImageIcon slime, double energy, double size, double perception) {
        super();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.fw = fw;
        this.fh = fh;
        this.setIcon(slime);

        this.energy = energy;

        this.size = size;
        speed = 1 / this.size;      // Desto höher der erste Faktor desto geringer die Geschwindigkeit (eine Art scale)

        this.perception = perception;



        this.setBounds(x, y, w, h);
    }

    public void setSize(double newSize){
        this.size = newSize;
    }


    public void startMove(int targetX, int targetY, int targetSize, int foodSelected, food target) {
        this.startX = this.x;
        this.startY = this.y;

        this.hasArrived = false;
        this.foodTarget = target;





        this.targetX = targetX + (targetSize / 2) - (this.w / 2);
        this.targetY = targetY + (targetSize / 2) - (this.h / 2);

        distanceX = this.targetX - startX;
        distanceY = this.targetY - startY;
        distanceTotal = (int) Math.sqrt(distanceX*distanceX+distanceY*distanceY);       // Satz des Pythagoras



        this.duration = (long) (distanceTotal * speed);


        this.startTime = System.currentTimeMillis();
        this.isMoving = true;
    }

    public void updatePosition() {
        if (!isMoving) {
            this.hasArrived = true;
            return;
        }


        long elapsedTime = System.currentTimeMillis() - startTime;
        double fraction = (double) elapsedTime / duration;
        if (fraction >= 1.0) {
            fraction = 1.0;
            isMoving = false;
            foodTarget.delete();

        }


        int currentX = (int) (startX + fraction * (targetX - startX));
        int currentY = (int) (startY + fraction * (targetY - startY));
        this.setBounds(currentX, currentY, w, h);
        this.x = currentX;
        this.y = currentY;

    }

    public int returnTarget(){
        return foodSelected;
    }

    public void setTarget(int newTarget){
        foodSelected = newTarget;
    }

    public boolean hasArrived(){
        return hasArrived;
    }


}
