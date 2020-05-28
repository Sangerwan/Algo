import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.swing.*;
import java.io.File;

public class ImageProcessing {
    /*
    Classe qui contient les fonctions nécessaire au travail sur l'image
    */
    private BufferedImage img;
    private JFrame frame;
    private String file_name;

    public ImageProcessing(String path){
        /*
        lecture d'une image
        */
        img = null;
        try {
            File file = new File(path);
            img = ImageIO.read(file);
            file_name=path.substring(path.lastIndexOf('/')+1);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    public void display_Img(){
        /*
        affiche l'image img
        */
        frame= new JFrame(file_name);
        JLabel label=new JLabel(new ImageIcon(img));
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void write_Img(String xred,String yred){
        /*
        utilisé pour enregistre l'image retouché sous la forme 
        nomdufichier_resized_%reductionx_%reductiony.extensiondufichier
        */
        try{
        File outputfile = new File(file_name.substring(0,file_name.lastIndexOf('.'))+"_"+"resized"+"_"+xred+"_"+yred+file_name.substring(file_name.lastIndexOf('.')));
        ImageIO.write(img, file_name.substring(file_name.lastIndexOf('.')+1), outputfile);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public String getFileName(){
        /*
        retourne le nom de l'image
        */
        return file_name;
    }
    public int getWidth(){
        /*
        retourne la longueur de l'image
        */
        return img.getWidth();
    }
    public int getHeight(){
        /*
        retourne la hauteur de l'image
        */
        return img.getHeight();
    }
    public int getRGB(int x, int y){
        /*
        retourne la valeur TYPE_INT_ARGB du pixel (x,y)
        */
        return img.getRGB(x, y);
    }
    public int getRed(int x,int y){
        /*
        retourne la valeur rouge du pixel (x,y)
        */
        return (img.getRGB(x,y)>>16) & 0xFF;
    }
    public int getGreen(int x,int y){
        /*
        retourne la valeur verte du pixel (x,y)
        */
        return (img.getRGB(x,y)>>8) & 0xFF;
    }
    public int getBlue(int x,int y){
        /*
        retourne la valeur bleue du pixel (x,y)
        */
        return (img.getRGB(x,y)) & 0xFF;
    }
    
    public void setRGB(int x, int y,int rgb){
        /*
        met la valeur rgb du pixel (x,y) à 'rgb'
        */
        img.setRGB(x, y, rgb);
    }
	public void removeVerticalSeam(int[] vertical_seam) {
        /*
        enlève la seam verticale de l'image en ayant ses coordonnées x
        */
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
        /*
        enlève la seam verticale de l'image en ayant ses coordonnées y
        */
        int width=img.getWidth();
        int height=img.getHeight();
        BufferedImage updated_img=new BufferedImage(width,height-1,img.getType());
        for(int i=0;i<horizontal_seam.length;i++){
            for(int c1=0;c1<horizontal_seam[i];c1++)  
                updated_img.setRGB(i,c1, img.getRGB(i,c1));
            for(int c2=horizontal_seam[i]+1;c2<height;c2++) 
                updated_img.setRGB(i,c2-1, img.getRGB(i,c2));
        }
        img=updated_img;
	}
}