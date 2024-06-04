package EvoBox;

import javax.swing.*;

public class slime extends JLabel {

    protected int x, y;
    protected int fw, fh;
    protected int w, h;

    public slime(int x, int y, int w, int h, int fw, int fh, ImageIcon slime){
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


}
