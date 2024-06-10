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

    public slime(int x, int y, int w, int h, int fw, int fh, ImageIcon slime) {
        super();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.fw = fw;
        this.fh = fh;
        this.setIcon(slime);
        this.setBounds(x, y, w, h);
    }

    public void startMove(int targetX, int targetY, long duration, int targetSize) {
        this.startX = this.x;
        this.startY = this.y;

        this.targetX = targetX + (targetSize / 2) - (this.w / 2);
        this.targetY = targetY + (targetSize / 2) - (this.h / 2);

        this.duration = duration;
        this.startTime = System.currentTimeMillis();
        this.isMoving = true;
        System.out.println("Slime move started");
    }

    public void updatePosition() {
        if (!isMoving) return;

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
}
