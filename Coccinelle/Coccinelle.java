import java.util.Arrays;
public class Coccinelle {
    static final int[][] V={{2,4,3,9,6},        // Tableau des pucerons déterminé en fonction de Table 1
                            {1,10,15,1,2},
                            {2,4,11,26,66},
                            {36,34,1,13,30},
                            {46,2,8,7,15},
                            {89,27,10,12,3},
                            {1,72,3,6,6},
                            {3,1,2,4,500000}};

    static int L= V.length;                     //Nombres de lignes du tableau
    static int C= V[0].length;                  //Nombres de colones du tableau
    static int[][] M= new int[L][C];            //Tableau de terme général m(l,c)
    static String[] Chemin= new String[L];      //String qui permet de stocker le chemin maximum
    static void calculerM(){// calcul le tableau M[0:L][0:C] de terme général M[l][c]=m(l,c),
                            // où m(l,c) est le nombre maximum de pucerons que la coccinelle peut manger sur un chemin allant d’une case de la ligne l = 0 jusqu’`a la case (l,c) ;
            //Initialisation, cas de base m(0,c)=V[0][c]
            for(int i=0;i<C;i++)
                M[0][i]=V[0][i];
            //Cas général 1<=l<L, 0<=c<C
            for(int l=1;l<L;l++)
                for(int c=0;c<C;c++){//calcul du nombres max de pucerons mangé de la case (l,c)
                    int V1,V2,V3;   //V1 correspond au nombres max de pucerons si le déplacement est Nord Est
                                    //V1 correspond au nombres max de pucerons si le déplacement est Nord 
                                    //V1 correspond au nombres max de pucerons si le déplacement est Nord West
                    if(c==0)V1=Integer.MIN_VALUE;// le déplacement n'est pas Nord Est car en dehors de la grille
                    else V1=M[l-1][c-1]+V[l][c];
                    V2=M[l-1][c]+V[l][c];
                    if(c==C-1)V3=Integer.MIN_VALUE;// le déplacement n'est pas Nord West car en dehors de la grille
                    else V3=M[l-1][c+1]+V[l][c];
                    M[l][c]=Math.max(V1,Math.max(V2,V3));
                }
    }
    static int max_tab(int[] tab){ //retourne le maximum d'un tableau en supposant ce tableau non vide
        int Max=tab[0];
        for(int i=1; i<tab.length;i++){
            if(tab[i]>Max) Max=tab[i];
        }
        return Max;
    }
    static void afficherMatrice(int[][] G){//Affiche une matrice 2D
        for(int i=G.length-1;i>=0;i--){
            for(int j=0;j<G[0].length;j++)
                System.out.print(G[i][j]+"\t");
            System.out.println();
        }
        System.out.println('\n');
    }
    static void calculerCheminMax(){//Calcul le chemin de la coccinelle 
        int max_M=max_tab(M[L-1]);
        int c=0;
        for(int i=0;i<M[L-1].length;i++){//Calcul l'argument du maximum de M[L-1] pour connaître la case de l'interview
            if(M[L-1][i]==max_M){
                c=i;
                break;
            }
        }
        ccm(L-1,c);
    }
    static void ccm(int l, int c){//Calcul par récurence du chemin maximum pour arriver en case (l,c)
        if(l==0){Chemin[l]="(0,"+c+")"; return;}//si l=0, alors on se trouve sur la case de départ
        int Mnw,Mn,Mne;//Mnw est la valeur maximum si le dernier déplacement est
        if(c==0)Mne=Integer.MIN_VALUE;//si c=0, alors le déplacement Nord Est n'existe pas
        else Mne=M[l-1][c-1]+V[l][c];
        Mn=M[l-1][c]+V[l][c];
        if(c==C-1)Mnw=Integer.MIN_VALUE;//si c=C-1, alors le déplacement Nord Est n'existe pas
        else Mnw=M[l-1][c+1]+V[l][c];
        if(M[l][c]== Mnw) ccm(l-1,c+1);//si le dernier déplacement est Nord West, calcul du chemin max jusqu'en case (l-1,c+1)
        else if(M[l][c]== Mn) ccm(l-1,c);//si le dernier déplacement est Nord, calcul du chemin max jusqu'en case (l-1,c)
        else if(M[l][c]== Mne) ccm(l-1,c-1);//si le dernier déplacement est Nord Est, calcul du chemin max jusqu'en case (l-1,c-1)
        Chemin[l]="("+l+","+c+")";  //On ajoute la case actuelle au chemin maximum
    }
    public static void main(String[] args){
        calculerM();//calcul de la matrice de terme général M[l][c]=m(l,c)
        System.out.println("Grille des pucerons :");
        afficherMatrice(V);//affiche la grille de valeur
        System.out.println("Grille M[L][C] de terme général M[l][c]=m(l,c) :");
        afficherMatrice(M);//afficher la grille de terme général M[l][c]=m(l,c)
        System.out.println("La coccinelle a mangé "+max_tab(M[L-1])+" pucerons lors de son périple.");//affichage de la valeur max de M[L-1]
        calculerCheminMax();//calcul du chemin maximum
        System.out.println("La coccinelle a attéri en case "+ Chemin[0]+".");//affichage de la case de départ
        System.out.println("La coccinelle a été interviewé en case "+ Chemin[L-1]+".");//affichage de la case d'arrivée
        System.out.print("Le chemin de la coccinelle est ");
        //for(String S : Chemin ) System.out.print(S);
        Arrays.stream(Chemin).forEach(System.out::print);//affichage du chemin
        System.out.print(".");
    }
}
/* 
Compilation:

javac Coccinelle.java
java Coccinelle

Grille des pucerons :
3       1       2       4       5       
1       72      3       6       6
89      27      10      12      3
46      2       8       7       15
36      34      1       13      30
2       4       11      26      66
1       10      15      1       2
2       4       3       9       6


Grille M[L][C] de terme général M[l][c]=m(l,c) :
279     277     278     149     145     
205     276     145     140     140
204     142     124     134     125
115     71      98      114     122
64      69      51      90      107
16      28      35      50      77
5       14      24      10      11
2       4       3       9       6


La coccinelle a mangé 279 pucerons lors de son périple.
La coccinelle a attéri en case (0,3).
La coccinelle a été interviewé en case (7,0).
Le chemin de la coccinelle est (0,3)(1,2)(2,2)(3,1)(4,0)(5,0)(6,1)(7,0).
*/
