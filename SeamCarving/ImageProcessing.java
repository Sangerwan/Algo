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
        int width=img.getWidth();
        int height=img.getHeight();
        BufferedImage updated_img=new BufferedImage(width-1,height,img.getType());
        for(int i=0;i<vertical_seam.length;i++){
            for(int l1=0;l1<vertical_seam[i];l1++)  
                updated_img.setRGB(l1, i, img.getRGB(l1, i));
            for(int l2=vertical_seam[i]+1;l2<width;l2++) 
                updated_img.setRGB(l2-1, i, img.getRGB(l2, i));
        }
        img=updated_img;
	}
	public void removeHorizontalSeam(int[] horizontal_seam) {
        int width=img.getWidth();
        int height=img.getHeight();
        BufferedImage updated_img=new BufferedImage(width,height-1,img.getType());
        for(int i=0;i<horizontal_seam.length;i++){
            for(int c1=0;c1<horizontal_seam[i];c1++)  
                updated_img.setRGB(i,c1, img.getRGB(i,c1));
            for(int c2=horizontal_seam[i]+1;c2<width;c2++) 
                updated_img.setRGB(i,c2-1, img.getRGB(i,c2));
        }
        img=updated_img;
	}
}