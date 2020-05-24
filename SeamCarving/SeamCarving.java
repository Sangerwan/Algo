
public class SeamCarving {
    private static ImageProcessing img;
    public static void main(String[] args){// args[0]: nom_de_l'image 
                                                            //args[1]: % de reduction en x 
                                                            //args[2]: % de reduction en y 
        System.out.println(args[0]);
        img=new ImageProcessing(args[0]);
        //img.display_Img();
        int new_x=getInt(args[1]);
        int new_y=getInt(args[2]);
        // // //resize
        int nb_column=img.getWidth()*new_x/100;
        //int nb_column=0;
        int nb_row=img.getHeight() * new_y / 100;
        System.out.println(nb_column);
        System.out.println(nb_row);
        
        while(nb_column!=0 && nb_row!=0){
            int[][] M_vertical_seam=computeVerticalSeam();
            int[][] M_horizontal_seam=computeHorizontalSeam();
            if(minLastLine(M_vertical_seam)<=minLastColumn(M_horizontal_seam)){
                int[] vertical_seam=findLowestEnergyVerticalSeam(M_vertical_seam);
                img.removeVerticalSeam(vertical_seam);
                nb_column-=1;
            }
            else{
                int[] horizontal_seam=findLowestEnergyHorizontalSeam(M_horizontal_seam);
                img.removeHorizontalSeam(horizontal_seam);
                nb_row-=1;
            }
        }
        if(nb_column==0){
            while(nb_row!=0){
                int[][] M_horizontal_seam=computeHorizontalSeam();
                int[] horizontal_seam=findLowestEnergyHorizontalSeam(M_horizontal_seam);
                img.removeHorizontalSeam(horizontal_seam);
                nb_row-=1;
            }
        }
        else if(nb_row==0){
            while(nb_column!=0){
                int[][] M_vertical_seam=computeVerticalSeam();
                int[] vertical_seam=findLowestEnergyVerticalSeam(M_vertical_seam);
                img.removeVerticalSeam(vertical_seam);
                nb_column-=1;
            }
        }
        img.write_Img();
        System.out.println("done");
        return;
    }
    // VERTICAL 
    private static int[] findLowestEnergyVerticalSeam(int[][] m_vertical_seam) {
        int min=minLastLine(m_vertical_seam);
        int lastLine=m_vertical_seam.length-1;
        int pos_xmin=0;
        for(int i=0;i<m_vertical_seam[0].length;i++){
            if(m_vertical_seam[lastLine][i]==min){
                pos_xmin=i;
                break;
            }
        }
        return flevs(m_vertical_seam,lastLine,pos_xmin);
    }

    private static int[] flevs(int[][] m_vertical_seam, int line, int pos_xmin) {
        if(line==0)
            return new int[] {pos_xmin};
        int Ml,Mu;//,Mr;
        if(pos_xmin==0) 
            Ml=Integer.MAX_VALUE;
        else
            Ml=m_vertical_seam[line-1][pos_xmin-1]+verticalCostL(pos_xmin,line);
        Mu=m_vertical_seam[line-1][pos_xmin]+verticalCostU(pos_xmin,line);
        // if(x_min==img.getWidth()) Mr=Integer.MAX_VALUE;
        // else Mr=m_vertical_seam[x_min+1][y-1]+verticalCostR(x_min, y);
        if(m_vertical_seam[line][pos_xmin]==Ml) 
            return merge( flevs(m_vertical_seam,line-1,pos_xmin-1), new int[] {pos_xmin} );
        else if(m_vertical_seam[line][pos_xmin]==Mu)
            return merge( flevs(m_vertical_seam,line-1,pos_xmin), new int[] {pos_xmin} );
        else //else if(m_vertical_seam[x_min][y]==Mr)
            return merge(flevs(m_vertical_seam,line-1,pos_xmin+1), new int[] {pos_xmin});
    }
    
    private static int[] findLowestEnergyHorizontalSeam(int[][] m_horizontal_seam) {
        int min=minLastColumn(m_horizontal_seam);
        int lastColumn=m_horizontal_seam[0].length-1;
        int pos_ymin=0;
        for(int i=0;i<m_horizontal_seam.length;i++){
            if(m_horizontal_seam[i][lastColumn]==min){
                pos_ymin=i;
                break;
            }
        }
        return flehs(m_horizontal_seam,lastColumn,pos_ymin);
    }

	private static int[] flehs(int[][] m_horizontal_seam, int column, int pos_ymin) {
        if(column == 0)
            return new int[] {pos_ymin};
        int Ml,Mu;//,Mr;
        if(pos_ymin==0) 
            Mu=Integer.MAX_VALUE;
        else 
            Mu=m_horizontal_seam[pos_ymin-1][column-1]+verticalCostL(column,pos_ymin);
        Ml=m_horizontal_seam[pos_ymin][column-1]+verticalCostU(column,pos_ymin);
        if(m_horizontal_seam[pos_ymin][column]==Ml)
            return merge( flehs(m_horizontal_seam,column-1,pos_ymin) , new int[] {pos_ymin} );
        else if(m_horizontal_seam[pos_ymin][column]==Mu)
            return merge( flehs(m_horizontal_seam,column-1,pos_ymin-1) , new int[] {pos_ymin});
        else //else if(m_vertical_seam[x_min][y]==Mr)
            return merge( flehs(m_horizontal_seam,column-1,pos_ymin+1) , new int[] {pos_ymin});
    }

    private static int verticalCostR(int x, int y) {
        if(x==0)
            return 1000000;
        if(x==img.getWidth()-1)
            return 1000000;
        if(y==0)
            return 1000000;
        if(y==img.getHeight()-1)
            return 1000000;
        return dEnergy(x+1, y, x-1, y)+dEnergy(x, y-1, x+1, y);
    }

    private static int verticalCostL(int x, int y) {
        if(x==0)
            return 1000000;
        if(x==img.getWidth()-1)
            return 1000000;
        if(y==0)
            return 1000000;
        if(y==img.getHeight()-1)
            return 1000000;
        return dEnergy(x+1, y, x-1, y)+dEnergy(x, y-1, x-1, y);
    }

    private static int verticalCostU(int x, int y) {
        if(x==0)
            return 1000000;
        if(x==img.getWidth()-1)
            return 1000000;
        if(y==0)
            return 1000000;
        if(y==img.getHeight()-1)
            return 1000000;
        return dEnergy(x+1, y, x-1, y);
    }
    
    private static int[] merge(int[] a, int[] b){
        int[] mergedarray= new int[a.length+b.length];
        System.arraycopy(a, 0, mergedarray, 0, a.length);
        System.arraycopy(b, 0, mergedarray, a.length, b.length);
        return mergedarray;
    }
    private static int[][] computeVerticalSeam() {
        int L=img.getHeight();
        int C=img.getWidth();
        int[][] M=new int[L][C];
        //y=0
        for(int x=0;x<C;x++)
            M[0][x]=verticalCostU(x,0);
        //y[1:Y]
        for(int y=1;y<L;y++){// /!\ border
            M[y][0]=Math.min(M[y-1][0]+verticalCostU(0,y),M[y-1][0+1]+verticalCostR(0,y));
            for(int x=1;x<C-1;x++)
                M[y][x]=Math.min(M[y-1][x-1]+verticalCostL(x,y),Math.min(M[y-1][x]+verticalCostU(x,y),M[y-1][x+1]+verticalCostR(x,y)));
            M[y][C-1]=Math.min(M[y-1][C-1-1]+verticalCostL(C-1,y),M[y-1][C-1]+verticalCostU(C-1,y));
        }
        return M;
    }
    //pb
    private static int[][] computeHorizontalSeam() {
        int L=img.getHeight();
        int C=img.getWidth();
        int[][] M=new int[L][C];
        //x=0
        for(int y=0;y<L;y++)
            M[y][0]=horizontalCostL(0,y);
        for(int x=1;x<C;x++){
            M[0][x]=Math.min(M[0][x-1]+horizontalCostL(x,0),M[0+1][x-1]+horizontalCostD(x,0));
            for(int y=1;y<L-1;y++)
                M[y][x]=Math.min(M[y-1][x-1]+horizontalCostU(x,y),Math.min(M[y][x-1]+horizontalCostL(x,y),M[y+1][x-1]+horizontalCostD(x,y)));
            M[L-1][x]=Math.min(M[L-1-1][x-1]+horizontalCostU(x,L-1),M[L-1][x-1]+horizontalCostL(x,L-1));
        }
        return M;
    }
    
    private static int horizontalCostD(int x, int y) {
        if(x==0)
            return 1000000;
        if(x==img.getWidth()-1)
            return 1000000;
        if(y==0)
            return 1000000;
        if(y==img.getHeight()-1)
            return 1000000;
        return dEnergy(x+1, y, x, y-1)+dEnergy(x+1, y, x-1, y);
    }

    private static int horizontalCostU(int x, int y) {
        if(x==0)
            return 1000000;
        if(x==img.getWidth()-1)
            return 1000000;
        if(y==0)
            return 1000000;
        if(y==img.getHeight()-1)
            return 1000000;
        return dEnergy(x-1, y, x, y-1)+dEnergy(x+1, y, x-1, y);
    }

    private static int horizontalCostL(int x, int y) {
        if(x==0)
            return 1000000;
        if(x==img.getWidth()-1)
            return 1000000;
        if(y==0)
            return 1000000;
        if(y==img.getHeight()-1)
            return 1000000;
        return dEnergy(x+1, y, x-1, y);
    }
    private static int dEnergy(int x1,int y1,int x2,int y2){
        int r1=img.getRed(x1,y1);
        int g1=img.getGreen(x1,y1);
        int b1=img.getBlue(x1,y1);
        int r2=img.getRed(x2,y2);
        int g2=img.getGreen(x2,y2);
        int b2=img.getBlue(x2,y2);
        int I=(int) Math.pow(r1-r2,2)+(int) Math.pow(g1-g2,2)+(int) Math.pow(b1-b2,2);
        return I;
    }
    private static int minLastLine(int[][] tab){ //retourne le minimum de la dernière ligne d'un tableau non vide
    int last_row=tab.length-1;
    int min=tab[last_row][0];
    for(int i=1; i<tab[last_row].length;i++){
        if(tab[last_row][i]<min) min=tab[last_row][i];
    }
    return min;
    }
    private static int minLastColumn(int[][] tab){ //retourne le minimum de la dernière colonned'un tableau non vide
        int last_column=tab[0].length-1;
        int min=tab[0][last_column];
        for(int i=1; i<tab.length;i++){
            if(tab[i][last_column]<min) min=tab[i][last_column];
        }
        return min;
    }
    private static int getInt(String string) {
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