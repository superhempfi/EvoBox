package EvoBox;

import javax.swing.*;

public class slime extends JLabel {

    protected int x, y;
    protected int fw, fh;
    protected int w, h;
    private int startX, startY;
    private int targetX, targetY;
    private long startTime;
    private long duration;
    private boolean isMoving;

    // Attribute
    protected double energy;
    protected double size;
    protected double perception;
    protected food foodTarget;
    protected int foodSelection;

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
        this.perception = perception;

        this.setBounds(x, y, w, h);
    }

    public void setSize(double newSize){
        this.size = newSize;
    }


    public void startMove(int targetX, int targetY, long duration, int targetSize, food aFood, int foodSelected) {
        this.startX = this.x;
        this.startY = this.y;

        this.foodTarget = aFood;
        this.foodSelection = foodSelected;

        this.targetX = targetX + (targetSize / 2) - (this.w / 2);
        this.targetY = targetY + (targetSize / 2) - (this.h / 2);

        this.duration = duration;
        this.startTime = System.currentTimeMillis();
        this.isMoving = true;
    }

    public void updatePosition() {
        if (!isMoving) return;


        long elapsedTime = System.currentTimeMillis() - startTime;
        double fraction = (double) elapsedTime / duration;
        if (fraction >= 1.0) {
            fraction = 1.0;
            isMoving = false;
            this.foodTarget.remove(this.foodSelection);
        }




        int currentX = (int) (startX + fraction * (targetX - startX));
        int currentY = (int) (startY + fraction * (targetY - startY));
        this.setBounds(currentX, currentY, w, h);
        this.x = currentX;
        this.y = currentY;


    }
}
