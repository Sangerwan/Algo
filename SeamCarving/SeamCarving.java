import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class SeamCarving {


    public static void ForwardEnergy(BufferedImage img, int x, int y){
        img.getRGB(x, y);
    }
    public static void main(String[] args) throws IOException{
        System.out.println(args[0]);
        test_img(args[0]);
        //Initialisation
        //Calcul de l'energie de la première ligne M(x,0)=E(x,0)

    }

    static public void test_img(String path){
        BufferedImage img = null;
        try {
            File file = new File(path);
            img = ImageIO.read(file);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        JFrame frame= new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel=new JPanel();
        JLabel label=new JLabel(new ImageIcon(img));
        panel.add(label);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
}