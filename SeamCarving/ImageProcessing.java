import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.swing.*;
import java.io.File;

public class ImageProcessing {
    private BufferedImage img;
    private JFrame frame;
    public ImageProcessing(int width, int height){
        img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    }
    public ImageProcessing(String path){
        img = null;
        try {
            File file = new File(path);
            img = ImageIO.read(file);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    public void display_Img(){
        frame= new JFrame("test");
        JLabel label=new JLabel(new ImageIcon(img));
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void write_Img(){
        //save image in saved.png
        try{
        File outputfile = new File("saved.png");
        ImageIO.write(img, "png", outputfile);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public int getWidth(){
        return img.getWidth();
    }
    public int getHeight(){
        return img.getHeight();
    }
    public int getRGB(int x, int y){
        return img.getRGB(x, y);
    }
    public int getRed(int x,int y){
        return (img.getRGB(x,y)>>16) & 0xFF;
    }
    public int getGreen(int x,int y){
        return (img.getRGB(x,y)>>8) & 0xFF;
    }
    public int getBlue(int x,int y){
        return (img.getRGB(x,y)) & 0xFF;
    }
    
    public void setRGB(int x, int y,int rgb){
        img.setRGB(x, y, rgb);
    }
	public void removeVerticalSeam(int[] vertical_seam) {
        BufferedImage updated_img=new BufferedImage(img.getWidth()-1, img.getHeight(),BufferedImage.TYPE_INT_ARGB);
        img=updated_img;
	}
	public void removeHorizontalSeam(int[] horizontal_seam) {
        BufferedImage updated_img=new BufferedImage(img.getWidth(), img.getHeight()-1,BufferedImage.TYPE_INT_ARGB);
        img=updated_img;
	}
}