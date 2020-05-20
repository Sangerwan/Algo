import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class SeamCarving {

    static public void write_Img(BufferedImage img){
        try{
        
        File outputfile = new File("saved.png");
        ImageIO.write(img, "png", outputfile);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    static public BufferedImage load_Img(String path){
        BufferedImage img = null;
        try {
            File file = new File(path);
            img = ImageIO.read(file);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return img;
    }
    static public void display_Img(BufferedImage img){
        JFrame frame= new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel=new JPanel();
        JLabel label=new JLabel(new ImageIcon(img));
        panel.add(label);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void ForwardEnergy(BufferedImage img, int x, int y){
        img.getRGB(x, y);
        return;
    }
    public static void main(String[] args) throws IOException{
        System.out.println(args[0]);
        BufferedImage img =load_Img(args[0]);
        //display_Img(img);
        write_Img(img);
        // img.setRGB(1, 0, 255);
        // System.out.println(img.getRGB(0,0));
        // System.out.println(img.getRGB(1,0));
        // System.out.println(img.getRGB(2,0));
        //Initialisation
        //Calcul de l'energie de la première ligne MX(x,0)=E(x,0)
        //Calcul de l'energie de la première colonne MY(0,y)=E(0,y)
        //enlever le plus faible
        //calcul energie diff

        // for(int i=0; i<img.getWidth();i++){
        //     if(i==0){
        //         Ex[i]=(int) Math.pow((img.getRGB(i,0)-img.getRGB(i+1,0)),2);
        //     }
        //     else if(i==img.getWidth()-1){
        //         Ex[i]=(int) Math.pow((img.getRGB(i,0)-img.getRGB(i-1,0)),2);
        //     }
        //     else Ex[i]=(int) Math.pow((img.getRGB(i-1,0)-img.getRGB(i+1,0)),2);
        // } 
        // for(int j=0; j<img.getHeight();j++){
        //     if(j==0){
        //         Ey[j]=(int) Math.pow((img.getRGB(0,j)-img.getRGB(0,j+1)),2);
        //     }
        //     else if(j==img.getHeight()-1){
        //         Ey[j]=(int) Math.pow((img.getRGB(0,j)-img.getRGB(0,j-1)),2);
        //     }
        //     else Ey[j]=(int) Math.pow((img.getRGB(0,j-1)-img.getRGB(0,j+1)),2);
        // } 
        
        
        System.out.println("1");
    }
    public static int calculerE(int x, int y, BufferedImage img){
        switch(x){
            case 0: 
                if(y==0)
                if(y==img.getHeight())
                return (int) Math.pow((img.getRGB(x,y)-img.getRGB(x+1,y)),2);
        }
        // for(int x=0; x<img.getWidth();x++){
        //     if(x==0){
        //         Ex[i]=(int) Math.pow((img.getRGB(i,0)-img.getRGB(i+1,0)),2);
        //     }
        //     else if(i==img.getWidth()-1){
        //         Ex[i]=(int) Math.pow((img.getRGB(i,0)-img.getRGB(i-1,0)),2);
        //     }
        //     else Ex[i]=(int) Math.pow((img.getRGB(i-1,0)-img.getRGB(i+1,0)),2);
        // } 
        // for(int j=0; j<img.getHeight();j++){
        //     if(j==0){
        //         Ey[j]=(int) Math.pow((img.getRGB(0,j)-img.getRGB(0,j+1)),2);
        //     }
        //     else if(j==img.getHeight()-1){
        //         Ey[j]=(int) Math.pow((img.getRGB(0,j)-img.getRGB(0,j-1)),2);
        //     }
        //     else Ey[j]=(int) Math.pow((img.getRGB(0,j-1)-img.getRGB(0,j+1)),2);
        // } 
    }

    public static int[][] calculerMx(BufferedImage img){
        int[][] Mx = new int[img.getWidth()][img.getHeight()];
        for(int x=0; x<img.getWidth();x++) M[x][0]=calculerE(x,0, img);
        for(int y=0; y<img.getHeight();y++) M[0][y]=calculerE(0,y, img);

        return Mx;
    }

}