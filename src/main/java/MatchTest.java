/**
 * Created by vrenclouff on 08.12.16.
 */
public class MatchTest {

    public static void main(String [] args) {

        int [] count = {4, 8, 16, 36};
        int i,j,k;

        int layers = 0;
        for(i=1, k=1;k<=count[3];i++){
            if (i%2 == 0) continue;
            for(j=1;j<=i;j++, k++) {
                System.out.print(k+" ");
            }
            layers++;
            System.out.print("\n");
        }

        System.out.println("Cislo: "+count[0]+" Vrstev: "+Math.round(Math.sqrt(count[0])));
        System.out.println("Cislo: "+count[1]+" Vrstev: "+Math.round(Math.sqrt(count[1])));
        System.out.println("Cislo: "+count[2]+" Vrstev: "+Math.round(Math.sqrt(count[2])));
        System.out.println("Cislo: "+count[3]+" Vrstev: "+Math.round(Math.sqrt(count[3])));

    }
}
