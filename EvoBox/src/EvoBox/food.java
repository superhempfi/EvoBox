package EvoBox;

import javax.swing.*;
import java.util.ArrayList;

public class food extends JLabel {

    protected int x, y;
    protected int fw, fh;
    protected int w, h;

    private JPanel Parent;

    private boolean isTargeted = false;
    int index;
    public ArrayList<food> allFood;

    public food(int x, int y, int w, int h, int fw, int fh, ImageIcon fruit, JPanel Parent,ArrayList<food> allFood){
        super();
        this.Parent = Parent;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.fw = fw;
        this.fh = fh;
        this.setIcon(fruit);
        this.setBounds(x, y, w, h);
        this.allFood = allFood;


    }

    public void addIndex(int index){
        this.index = index;
    }

    public void delete(){
        allFood.remove(index);
        Parent.remove(this);


        for(int i = 0; i < allFood.size(); i++){

            allFood.get(i).addIndex(i);

        }

    }

    public  boolean getIfTargeted(){
        return isTargeted;
    }

    public void setIfTargeted(boolean isTargeted){
        this.isTargeted = isTargeted;
    }




}
