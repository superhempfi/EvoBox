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

    public void startMove(int targetX, int targetY, long duration) {
        this.startX = this.x;
        this.startY = this.y;
        this.targetX = targetX;
        this.targetY = targetY;
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
        }

        int currentX = (int) (startX + fraction * (targetX - startX));
        int currentY = (int) (startY + fraction * (targetY - startY));
        this.setBounds(currentX, currentY, w, h);
        this.x = currentX;
        this.y = currentY;
    }
}
