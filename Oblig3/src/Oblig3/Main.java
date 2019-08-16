package Oblig3;

import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {

        ObligSBinTre<Character> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        char[] verdier = "IATBHJCRSOFELKGDMPQN".toCharArray();
        for (char c : verdier)
            tre.leggInn(c);

       /* System.out.println("Treets høyre gren: " + tre.høyreGren());
        System.out.println("Treets lengste gren: " + tre.lengstGren() + "\n");


        String[] s = tre.grener();

        System.out.println("Treets grener sortert fra venstre mot høyre: ");
        for (String gren : s )
            System.out.println(gren);
        System.out.println();

        System.out.println("Treet sortert i postorder: " + tre.postString()); */

     for (Character c : tre)
         System.out.print(c + " ");


    }
}
