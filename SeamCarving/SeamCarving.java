
public class SeamCarving {
    private static ImageProcessing img;
    public static void main(String[] args){//args[0]: nom_de_l'image 
                                           //args[1]: % de reduction en x 
                                           //args[2]: % de reduction en y 
        img=new ImageProcessing(args[0]);
        int new_x=getInt(args[1]);
        int new_y=getInt(args[2]);
        // // //resize
        int nb_column=img.getWidth()*new_x/100;//nombre de colonnes à enlever
        int nb_row=img.getHeight()*new_y / 100;//nombre de lignes à enlever
        System.out.println("removing "+nb_column+" vertical seams");
        System.out.println("removing "+nb_row+" horizontal seams");
        long startTime = System.nanoTime();
        while(nb_column!=0 && nb_row!=0){/*tant qu'il reste des lignes ET des colonnes a supprimer on calcule
                                            M_vertical_seam[0:L][0:C] et M_horizontal_seam[0:L][0:C] de terme général 
                                            M[l][c]=m(l,c), l'énergie cumulative minimum verticale/horizontale
                                            pour une image de taille (l,c)
                                            */
            int[][] M_vertical_seam=computeVerticalSeam();
            int[][] M_horizontal_seam=computeHorizontalSeam();
            System.out.println("Vertical");
            afficherMatrice(M_vertical_seam);
            System.out.println("Verticalmin");
            System.out.println(minLastLine(M_vertical_seam));
            int[] v=findLowestEnergyVerticalSeam(M_vertical_seam);
            for(int i=0; i<v.length;i++)
                System.out.println(v[i]);
            System.out.println("Horizontal");
            afficherMatrice(M_horizontal_seam);
            System.out.println("Horizontalmin");
            System.out.println(minLastColumn(M_horizontal_seam));
            int[] h=findLowestEnergyHorizontalSeam(M_horizontal_seam);
            for(int i=0; i<h.length;i++)
                System.out.println(h[i]);
            if(minLastLine(M_vertical_seam)<=minLastColumn(M_horizontal_seam)){//on prend l'energie cumulée minimum entre les 2 matrices
            int[] vertical_seam=findLowestEnergyVerticalSeam(M_vertical_seam);//Calcul de la seam verticale minimum
                img.removeVerticalSeam(vertical_seam);//on enleve la seam verticale min de l'image
                nb_column-=1;
            }
            else{
                int[] horizontal_seam=findLowestEnergyHorizontalSeam(M_horizontal_seam);//Calcul de la seam horizontale minimum
                img.removeHorizontalSeam(horizontal_seam);//on enleve la seam horizontale min de l'image
                nb_row-=1;
            }
        }
        //1ere étape terminée, il reste seulement des lignes ou colonnes a supprimer
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("loop 1 "
                + elapsedTime/1000000000 + "s");

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
        elapsedTime = System.nanoTime() - startTime-elapsedTime;
        System.out.println("loop 2 "
                + elapsedTime/1000000000 + "s");
        img.write_Img(args[1],args[2]);
        double finishtime=System.nanoTime()-startTime;
        System.out.println("done in "+ finishtime/1000000000 +"s" );
        return;
    }
    // VERTICAL 
    private static int[] findLowestEnergyVerticalSeam(int[][] m_vertical_seam){
        
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
            return merge( flevs(m_vertical_seam,line-1,pos_xmin-1), new int[] {pos_xmin});       
        else if(m_vertical_seam[line][pos_xmin]==Mu)
            return merge( flevs(m_vertical_seam,line-1,pos_xmin), new int[] {pos_xmin});       
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
            Mu=m_horizontal_seam[pos_ymin-1][column-1]+horizontalCostU(column,pos_ymin);
        Ml=m_horizontal_seam[pos_ymin][column-1]+horizontalCostL(column,pos_ymin);
        if(m_horizontal_seam[pos_ymin][column]==Ml)
            return merge( flehs(m_horizontal_seam,column-1,pos_ymin) , new int[] {pos_ymin} );
        else if(m_horizontal_seam[pos_ymin][column]==Mu)
            return merge( flehs(m_horizontal_seam,column-1,pos_ymin-1) , new int[] {pos_ymin});
        else
            return merge( flehs(m_horizontal_seam,column-1,pos_ymin+1) , new int[] {pos_ymin});
    }

    private static int verticalCostR(int x, int y) {
        if(y==0)
            return 0;      
        if(x==0)
            return dEnergy(x+1, y, x, y)+dEnergy(x, y-1, x+1, y);      
        return dEnergy(x+1, y, x-1, y)+dEnergy(x, y-1, x+1, y);
    }

    private static int verticalCostL(int x, int y) {
        if(y==0)
            return 0;
        if(x==img.getWidth()-1)
            return dEnergy(x, y, x-1, y)+dEnergy(x, y-1, x-1, y);
        return dEnergy(x+1, y, x-1, y)+dEnergy(x, y-1, x-1, y);
    }

    private static int verticalCostU(int x, int y) {
        if(x==0)
            return dEnergy(x+1, y, x, y);
        if(x==img.getWidth()-1)
            return dEnergy(x, y, x-1, y);
        return dEnergy(x+1, y, x-1, y);
    }
    
    private static int[] merge(int[] a, int[] b){
        int[] mergedarray= new int[a.length+b.length];
        System.arraycopy(a, 0, mergedarray, 0, a.length);
        System.arraycopy(b, 0, mergedarray, a.length, b.length);
        return mergedarray;
    }
    private static int[][] computeVerticalSeam() {
        /*
        calcul le tableau M[0:L][0:C] de terme général M[l][c]=m(l,c),
        où m(l,c) est l'energie verticale minimum cumulée d'une case de la ligne 0 jusqu'à (l,c) ;
        */
        int L=img.getHeight();
        int C=img.getWidth();
        int[][] M=new int[L][C];
        /*
        Initialisation
        pour la ligne 0 on initialise a une valeur arbitraire fixé
        */
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
            return 0;
        if(y==0)
            return dEnergy(x, y+1, x, y)+dEnergy(x, y+1, x-1, y);
        return dEnergy(x, y+1, x, y-1)+dEnergy(x, y+1, x-1, y);
    }

    private static int horizontalCostU(int x, int y) {
        if(x==0)
            return 0;
        if(y==img.getHeight()-1)
            return dEnergy(x, y-1, x-1, y)+dEnergy(x, y, x, y-1);
        return dEnergy(x, y-1, x-1, y)+dEnergy(x, y+1, x, y-1);
    }

    private static int horizontalCostL(int x, int y) {
        if(y==0)
            return dEnergy(x, y+1, x, y);;
        if(y==img.getHeight()-1)
            return dEnergy(x, y, x, y-1);;
        return dEnergy(x, y+1, x, y-1);
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
    private static void afficherMatrice(int[][] G){//Affiche une matrice 2D
        for(int i=0;i<G.length;i++){
            for(int j=0;j<G[0].length;j++)
                System.out.print(G[i][j]+"\t");
            System.out.println();
        }
        System.out.println('\n');
    }
    

}
/*
./SeamCarving/4.jpg
280
200
boucle1 1972s
boucle2 527s
done in 2500.2177132s
*/

/*
./SeamCarving/938044.jpg
1920
1080
boucle1 39050s
boucle2 2156s
done in 41208.3654361s
*/