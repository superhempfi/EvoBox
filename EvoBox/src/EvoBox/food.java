package EvoBox;

import javax.swing.*;

public class food extends JLabel {

    protected int x, y;
    protected int fw, fh;
    protected int w, h;

    public food(int x, int y, int w, int h, int fw, int fh, ImageIcon fruit){
        super();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.fw = fw;
        this.fh = fh;
        this.setIcon(fruit);
        this.setBounds(x, y, w, h);

    }






}
