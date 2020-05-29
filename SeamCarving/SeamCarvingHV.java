
public class SeamCarvingHV {
    /*
    Implémentaion de l'algorithme SeamCarving with forward energy
    qui retire d'abord les seam horizontal puis vertical
    */
    private static ImageProcessing img;
    public static void main(String[] args){//args[0]: nom_de_l'image 
                                           //args[1]: % de reduction en x 
                                           //args[2]: % de reduction en y 
        img=new ImageProcessing(args[0]);
        int new_x=getInt(args[1]);
        int new_y=getInt(args[2]);
        int nb_column=img.getWidth()*new_x/100;//nombre de colonnes à enlever
        int nb_row=img.getHeight()*new_y / 100;//nombre de lignes à enlever
        System.out.println("removing "+nb_column+" vertical seams");
        System.out.println("removing "+nb_row+" horizontal seams");
        long startTime = System.nanoTime();

        while(nb_row!=0){//on enlève le nombre de seam horizontal 
            int[][] M_horizontal_seam=computeHorizontalSeam();
            int[] horizontal_seam=findLowestEnergyHorizontalSeam(M_horizontal_seam);
            img.removeHorizontalSeam(horizontal_seam);
            nb_row-=1;
        }

        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("loop 1 "
                + elapsedTime/1000000000 + "s");
        
        while(nb_column!=0){//on enlève le nombre de seam vertical 
            int[][] M_vertical_seam=computeVerticalSeam();
            int[] vertical_seam=findLowestEnergyVerticalSeam(M_vertical_seam);
            img.removeVerticalSeam(vertical_seam);
            nb_column-=1;
        }
        
        elapsedTime = System.nanoTime() - startTime-elapsedTime;
        System.out.println("loop 2 "
                + elapsedTime/1000000000 + "s");
        
        img.write_Img(args[1],args[2]+"_HV");

        long finishtime=System.nanoTime()-startTime;
        System.out.println("done in "+ finishtime/1000000000 +"s" );

        return;
    }
   
    //VERTICAL
    private static int[][] computeVerticalSeam() {
        /*
        calcul le tableau M[0:L][0:C] de terme général M[l][c]=m(l,c),
        où m(l,c) est l'energie verticale minimum cumulée d'une case de la ligne 0 jusqu'à (l,c) ;
        */
        int L=img.getHeight();
        int C=img.getWidth();
        int[][] M=new int[L][C];
        /*
        Initialisation pour la ligne 0
        */
        //y=0
        for(int x=0;x<C;x++)//on initialise la première ligne en choisissant le coût vertical haut
            M[0][x]=verticalCostU(x,0);
        //Cas général : y[1:Y]
        for(int y=1;y<L;y++){// /!\ border
            /*
            Calcul du coût vertical cumulatif minimum,
            m(l,c) est le minimum des 3 déplacement possibles( gauche haut bas)
            */
            //Cas particulier x=0, le coût gauche vertical n'existe pas
            M[y][0]=Math.min(M[y-1][0]+verticalCostU(0,y),M[y-1][0+1]+verticalCostR(0,y));
            for(int x=1;x<C-1;x++)
                M[y][x]=Math.min(M[y-1][x-1]+verticalCostL(x,y),Math.min(M[y-1][x]+verticalCostU(x,y),M[y-1][x+1]+verticalCostR(x,y)));
            //Cas particulier x=C-1, le coût droit vertical n'existe pas
            M[y][C-1]=Math.min(M[y-1][C-1-1]+verticalCostL(C-1,y),M[y-1][C-1]+verticalCostU(C-1,y));
        }
        return M;
    }

     private static int[] findLowestEnergyVerticalSeam(int[][] m_vertical_seam){
        /*
        Calcul du seam vertical d'énergie minimum, retourne un tableau avec ses positions en x
        */
        int min=minLastLine(m_vertical_seam);//calcul du minimum de la dernière ligne
        int lastLine=m_vertical_seam.length-1;
        int pos_xmin=0;
        for(int i=0;i<m_vertical_seam[0].length;i++){//calcul de l'arg du minimum
            if(m_vertical_seam[lastLine][i]==min){
                pos_xmin=i;
                break;
            }
        }
        return flevs(m_vertical_seam,lastLine,pos_xmin);//appel de la fonction récursive qui calcule le tableau
    }

    private static int[] flevs(int[][] m_vertical_seam, int line, int pos_xmin) {
        if(line==0)//on se trouve sur la 1ere ligne, on retourne donc sa position en x
            return new int[] {pos_xmin};
        int Ml,Mu;//,Mr;
        //calcul de la valeur précédente de M[l][c] si le "déplacement" est gauche
        if(pos_xmin==0) 
            Ml=Integer.MAX_VALUE;
        else
            Ml=m_vertical_seam[line-1][pos_xmin-1]+verticalCostL(pos_xmin,line);
        //calcul de la valeur précédente de M[l][c] si le "déplacement" est haut
        Mu=m_vertical_seam[line-1][pos_xmin]+verticalCostU(pos_xmin,line);
        if(m_vertical_seam[line][pos_xmin]==Ml)//si le déplacement précédent est gauche calcul du seam minimum jusqu'en case (l-1,c-1)
            return merge( flevs(m_vertical_seam,line-1,pos_xmin-1), new int[] {pos_xmin});       
        else if(m_vertical_seam[line][pos_xmin]==Mu)//si le déplacement précédent est gauche calcul du seam minimum jusqu'en case (l-1,c)
            return merge( flevs(m_vertical_seam,line-1,pos_xmin), new int[] {pos_xmin});       
        else//si le déplacement précédent n'est pas gauche et haut alors le déplacement précédent est droite, calcul du seam minimum jusqu'en case (l-1,c+1)
            return merge(flevs(m_vertical_seam,line-1,pos_xmin+1), new int[] {pos_xmin});
        
    }

    private static int verticalCostR(int x, int y) {
        /*
        Coût vertical droit
        */
        if(y==0)
            return 0;      
        if(x==0)
            return dEnergy(x+1, y, x, y)+dEnergy(x, y-1, x+1, y);      
        return dEnergy(x+1, y, x-1, y)+dEnergy(x, y-1, x+1, y);
    }

    private static int verticalCostL(int x, int y) {
        /*
        Coût vertical gauche
        */
        if(y==0)
            return 0;
        if(x==img.getWidth()-1)
            return dEnergy(x, y, x-1, y)+dEnergy(x, y-1, x-1, y);
        return dEnergy(x+1, y, x-1, y)+dEnergy(x, y-1, x-1, y);
    }

    private static int verticalCostU(int x, int y) {
        /*
        Coût vertical haut
        */
        if(x==0)
            return dEnergy(x+1, y, x, y);
        if(x==img.getWidth()-1)
            return dEnergy(x, y, x-1, y);
        return dEnergy(x+1, y, x-1, y);
    }


    //HORIZONTAL
    private static int[][] computeHorizontalSeam() {
        /*
        calcul le tableau M[0:L][0:C] de terme général M[l][c]=m(l,c),
        où m(l,c) est l'energie horizontale minimum cumulée d'une case de la colonne 0 jusqu'à (l,c) ;
        */
        int L=img.getHeight();
        int C=img.getWidth();
        int[][] M=new int[L][C];
        /*
        Initialisation pour la colonne 0
        */
        //x=0
        for(int y=0;y<L;y++)//on initialise la première ligne en choisissant le coût horizontal gauche
            M[y][0]=horizontalCostL(0,y);
        //Cas général: x[1:C]
        for(int x=1;x<C;x++){
            /*
            Calcul du coût horizontal cumulatif minimum,
            m(l,c) est le minimum des 3 déplacement possibles( haut gauche bas)
            */
            //Cas particulier y=0, le coût haut horizontal n'existe pas
            M[0][x]=Math.min(M[0][x-1]+horizontalCostL(x,0),M[0+1][x-1]+horizontalCostD(x,0));
            for(int y=1;y<L-1;y++)
                M[y][x]=Math.min(M[y-1][x-1]+horizontalCostU(x,y),Math.min(M[y][x-1]+horizontalCostL(x,y),M[y+1][x-1]+horizontalCostD(x,y)));
            //Cas particulier y=L-1, le coût bas horizontal n'existe pas 
            M[L-1][x]=Math.min(M[L-1-1][x-1]+horizontalCostU(x,L-1),M[L-1][x-1]+horizontalCostL(x,L-1));
        }
        return M;
    }

    private static int[] findLowestEnergyHorizontalSeam(int[][] m_horizontal_seam) {
        /*
        Calcul du seam vertical d'énergie minimum, retourne un tableau avec ses positions en y
        */
        int min=minLastColumn(m_horizontal_seam);//calcul du minimum de la dernière colonne
        int lastColumn=m_horizontal_seam[0].length-1;
        int pos_ymin=0;
        for(int i=0;i<m_horizontal_seam.length;i++){//calcul de l'arg du minimum
            if(m_horizontal_seam[i][lastColumn]==min){
                pos_ymin=i;
                break;
            }
        }
        return flehs(m_horizontal_seam,lastColumn,pos_ymin);//appel de la fonction récursive qui calcule le tableau
    }

	private static int[] flehs(int[][] m_horizontal_seam, int column, int pos_ymin) {
        if(column == 0)//on se trouve sur la 1ere colonne, on retourne donc sa position en y
            return new int[] {pos_ymin};
        int Ml,Mu;
        //calcul de la valeur précédente de M[l][c] si le "déplacement" est haut
        if(pos_ymin==0) 
            Mu=Integer.MAX_VALUE;
        else 
            Mu=m_horizontal_seam[pos_ymin-1][column-1]+horizontalCostU(column,pos_ymin);
        //calcul de la valeur précédente de M[l][c] si le "déplacement" est gauche
        Ml=m_horizontal_seam[pos_ymin][column-1]+horizontalCostL(column,pos_ymin);
        if(m_horizontal_seam[pos_ymin][column]==Ml)//si le déplacement précédent est gauche calcul du seam minimum jusqu'en case (l,c-1)
            return merge( flehs(m_horizontal_seam,column-1,pos_ymin) , new int[] {pos_ymin} );
        else if(m_horizontal_seam[pos_ymin][column]==Mu)//si le déplacement précédent est haut calcul du seam minimum jusqu'en case (l-1,c-1)
            return merge( flehs(m_horizontal_seam,column-1,pos_ymin-1) , new int[] {pos_ymin});
        else//si le déplacement précédent n'est pas gauche et haut alors le déplacement précédent est bas, calcul du seam minimum jusqu'en case (l+1,c-1)
            return merge( flehs(m_horizontal_seam,column-1,pos_ymin+1) , new int[] {pos_ymin});
    }

    private static int horizontalCostD(int x, int y) {
        /*
        Coût horizontal bas
        */
        if(x==0)
            return 0;
        if(y==0)
            return dEnergy(x, y+1, x, y)+dEnergy(x, y+1, x-1, y);
        return dEnergy(x, y+1, x, y-1)+dEnergy(x, y+1, x-1, y);
    }
    private static int horizontalCostL(int x, int y) {
        /*
        Coût horizontal gauche
        */
        if(y==0)
            return dEnergy(x, y+1, x, y);;
        if(y==img.getHeight()-1)
            return dEnergy(x, y, x, y-1);;
        return dEnergy(x, y+1, x, y-1);
    }
    private static int horizontalCostU(int x, int y) {
        /*
        Coût horizontal haut
        */
        if(x==0)
            return 0;
        if(y==img.getHeight()-1)
            return dEnergy(x, y-1, x-1, y)+dEnergy(x, y, x, y-1);
        return dEnergy(x, y-1, x-1, y)+dEnergy(x, y+1, x, y-1);
    }


    private static int dEnergy(int x1,int y1,int x2,int y2){
        /*
        calcul de la différence "absolue" des valeurs R/G/B entre 2 pixels
        */
        int r1=img.getRed(x1,y1);
        int g1=img.getGreen(x1,y1);
        int b1=img.getBlue(x1,y1);
        int r2=img.getRed(x2,y2);
        int g2=img.getGreen(x2,y2);
        int b2=img.getBlue(x2,y2);
        int I=(int) Math.pow(r1-r2,2)+(int) Math.pow(g1-g2,2)+(int) Math.pow(b1-b2,2);
        return I;
    }

    private static int minLastColumn(int[][] tab){
        /*
        retourne le minimum de la dernière colonne d'un tableau 2D
        */
        int last_column=tab[0].length-1;
        int min=tab[0][last_column];
        for(int i=1; i<tab.length;i++){
            if(tab[i][last_column]<min) min=tab[i][last_column];
        }
        return min;
    }

    private static int minLastLine(int[][] tab){ 
        /*
        retourne le minimum de la dernière ligne d'un tableau 2D
        */
        int last_row=tab.length-1;
        int min=tab[last_row][0];
        for(int i=1; i<tab[last_row].length;i++){
            if(tab[last_row][i]<min) min=tab[last_row][i];
        }
        return min;
    }
    
    private static int[] merge(int[] a, int[] b){
        /*
        Fusionne 2 tableau de int
        ex a={1,2,3} b={4,5,6} merge(a,b)={1,2,3,4,5,6}
        */
        int[] mergedarray= new int[a.length+b.length];
        System.arraycopy(a, 0, mergedarray, 0, a.length);
        System.arraycopy(b, 0, mergedarray, a.length, b.length);
        return mergedarray;
    }

    private static int getInt(String string) {
        /*
        retourne la valeur int d'un string
        */
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