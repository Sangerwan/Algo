
public class SeamCarving {

    public static void main(String[] args){// args[0]: nom_de_l'image 
                                                            //args[1]: % de reduction en x 
                                                            //args[2]: % de reduction en y 
        System.out.println(args[0]);
        ImageProcessing img =new ImageProcessing(args[0]);
        img.display_Img();

        int[] M=new int[img.getWidth()];
        System.out.println(M.length);
        System.out.println(img.getWidth());
        int new_x=getInt(args[1]);
        int new_y=getInt(args[2]);
        // // //resize
        int nb_column=img.getWidth()-img.getWidth()*new_x/100;
        int nb_row=img.getHeight() - img.getHeight() * new_y / 100;
        System.out.println(img.getRGB(0, 0));
        img.setRGB(0, 0,img.getRGB(0, 0));
        System.out.println(img.getRGB(0, 0));
        while(nb_column!=0 || nb_row!=0){
            int[][] M_vertical_seam=computeVerticalSeam(img);
            int[][] M_horizontal_seam=computeHorizontalSeam(img);
            if(M_vertical_seam[img.getWidth()][img.getHeight()]<=M_horizontal_seam[img.getWidth()][img.getHeight()]){
                int[] vertical_seam=findLowestEnergyVerticalSeam(img,M_vertical_seam);
                img.removeVerticalSeam(vertical_seam);
                nb_column-=1;
            }
            else{
                int[] horizontal_seam=findLowestEnergyHorizontalSeam(img,M_horizontal_seam);
                img.removeHorizontalSeam(horizontal_seam);
                nb_row-=1;
            }
        }
        if(nb_column==0){
            while(nb_row!=0){
                int[][] M_horizontal_seam=computeHorizontalSeam(img);
                int[] horizontal_seam=findLowestEnergyHorizontalSeam(img,M_horizontal_seam);
                img.removeHorizontalSeam(horizontal_seam);
                nb_row-=1;
            }
        }
        else if(nb_row==0){
            while(nb_column!=0){
                int[][] M_vertical_seam=computeVerticalSeam(img);
                int[] vertical_seam=findLowestEnergyVerticalSeam(img,M_vertical_seam);
                img.removeVerticalSeam(vertical_seam);
                nb_column-=1;
            }
        }
    }
    private static int[] findLowestEnergyHorizontalSeam(ImageProcessing img, int[][] m_horizontal_seam) {
        return null;
    }

    private static int[] findLowestEnergyVerticalSeam(ImageProcessing img, int[][] m_vertical_seam) {
        return null;
    }

	private static int verticalCostR(int x, int y, ImageProcessing img) {
        int r1=img.getRed(x,y-1);
        int g1=img.getGreen(x,y-1);
        int b1=img.getBlue(x,y-1);
        int r2=img.getRed(x+1,y);
        int g2=img.getGreen(x+1,y);
        int b2=img.getBlue(x+1,y);
        int r3=img.getRed(x-1,y);
        int g3=img.getGreen(x-1,y);
        int b3=img.getBlue(x-1,y);
        int I1=(int) Math.pow(r2-r3,2) +(int) Math.pow(g2-g3,2)+(int) Math.pow(b2-b3,2);
        int I2=(int) Math.pow(r1-r2,2) +(int) Math.pow(g1-g2,2)+(int) Math.pow(b1-b2,2);
        return I1+I2;
    }

    private static int verticalCostL(int x, int y, ImageProcessing img) {
        int r1=img.getRed(x+1,y);
        int g1=img.getGreen(x+1,y);
        int b1=img.getBlue(x+1,y);
        int r2=img.getRed(x-1,y);
        int g2=img.getGreen(x-1,y);
        int b2=img.getBlue(x-1,y);
        int r3=img.getRed(x,y-1);
        int g3=img.getGreen(x,y-1);
        int b3=img.getBlue(x,y-1);
        int I1=(int) Math.pow(r1-r2,2) +(int) Math.pow(g1-g2,2)+(int) Math.pow(b1-b2,2);
        int I2=(int) Math.pow(r3-r2,2) +(int) Math.pow(g3-g2,2)+(int) Math.pow(b3-b2,2);
        return I1+I2;
    }

    private static int verticalCostU(int x, int y, ImageProcessing img) {
        int r1=img.getRed(x+1,y);
        int g1=img.getGreen(x+1,y);
        int b1=img.getBlue(x+1,y);
        int r2=img.getRed(x-1,y);
        int g2=img.getGreen(x-1,y);
        int b2=img.getBlue(x-1,y);
        int I=(int) Math.pow(r1-r2,2)+(int) Math.pow(g1-g2,2)+(int) Math.pow(b1-b2,2);
        return I;
    }
    
    
    private static int[][] computeVerticalSeam(ImageProcessing img) {
        int X=img.getWidth();
        int Y=img.getHeight();
        int[][] M=new int[X][Y];
        //y=0
        for(int x=0;x<X;x++)
            M[x][0]=verticalCostU(x,0,img);
        //y[1:Y]
        for(int y=1;y<Y-1;y++){// /!\ border
            M[0][y]=Math.min(M[0][y-1]+verticalCostU(0,y,img),M[0+1][y-1]+verticalCostR(0,y,img));
            for(int x=1;x<X-1;x++)
                M[x][y]=Math.min(M[x-1][y-1]+verticalCostL(x,y,img),Math.min(M[x][y-1]+verticalCostU(x,y,img),M[x+1][y-1]+verticalCostR(x,y,img)));
            M[X-1][y]=Math.min(M[X-1][y-1]+verticalCostL(X-1,y,img),M[X][y-1]+verticalCostU(X-1,y,img));
        }
        return M;
    }
    private static int[][] computeHorizontalSeam(ImageProcessing img) {
        int X=img.getWidth();
        int Y=img.getHeight();
        int[][] M=new int[X][Y];
        //x=0
        for(int y=0;y<Y;y++)
            M[0][y]=horizontalCostL(0,y,img);
        for(int x=1;x<X-1;x++){
            M[x][0]=Math.min(M[x-1][0]+horizontalCostL(x,0,img),M[x-1][0+1]+horizontalCostD(x,0,img));
            for(int y=1;y<Y-1;y++)
                M[x][y]=Math.min(M[x-1][y-1]+horizontalCostU(x,y,img),Math.min(M[x-1][y]+horizontalCostL(x,y,img),M[x-1][y+1]+horizontalCostD(x,y,img)));
            M[x][Y-1]=Math.min(M[x-1][Y-1-1]+horizontalCostU(x,Y-1,img),M[x-1][Y-1]+horizontalCostL(x,Y-1,img));
        }
        return M;
    }
    
    private static int horizontalCostD(int x, int y, ImageProcessing img) {
        int r1=img.getRed(x+1,y);
        int g1=img.getGreen(x+1,y);
        int b1=img.getBlue(x+1,y);
        int r2=img.getRed(x+1,y-1);
        int g2=img.getGreen(x+1,y-1);
        int b2=img.getBlue(x+1,y-1);
        int r3=img.getRed(x-1,y);
        int g3=img.getGreen(x-1,y);
        int b3=img.getBlue(x-1,y);
        int I1=(int) Math.pow(r1-r2,2) +(int) Math.pow(g1-g2,2)+(int) Math.pow(b1-b2,2);
        int I2=(int) Math.pow(r1-r3,2) +(int) Math.pow(g1-g3,2)+(int) Math.pow(b1-b3,2);       
        return I1+I2;
    }

    private static int horizontalCostU(int x, int y, ImageProcessing img) {
        int r1=img.getRed(x-1,y);
        int g1=img.getGreen(x-1,y);
        int b1=img.getBlue(x-1,y);
        int r2=img.getRed(x,y-1);
        int g2=img.getGreen(x,y-1);
        int b2=img.getBlue(x,y-1);
        int r3=img.getRed(x+1,y);
        int g3=img.getGreen(x+1,y);
        int b3=img.getBlue(x+1,y);
        int I1=(int) Math.pow(r1-r2,2) +(int) Math.pow(g1-g2,2)+(int) Math.pow(b1-b2,2);
        int I2=(int) Math.pow(r3-r1,2) +(int) Math.pow(g3-g1,2)+(int) Math.pow(b3-b1,2);
        return I1+I2;
    }

    private static int horizontalCostL(int x, int y, ImageProcessing img) {
        int r1=img.getRed(x+1,y);
        int g1=img.getGreen(x+1,y);
        int b1=img.getBlue(x+1,y);
        int r2=img.getRed(x-1,y);
        int g2=img.getGreen(x-1,y);
        int b2=img.getBlue(x-1,y);
        int I=(int) Math.pow(r1-r2,2)+(int) Math.pow(g1-g2,2)+(int) Math.pow(b1-b2,2);
        return I;
    }

    public static int getInt(String string) {
        int get_number;
        try{
            get_number=Integer.parseInt(string);
        }
        catch(NumberFormatException e){
            System.out.println(e);
            return 0;
        }
        return get_number;
    }

    

}