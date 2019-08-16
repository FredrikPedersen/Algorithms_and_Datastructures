package Oblig2;

/**
 * <h1>Main</h1>
 *
 * Husk at det BARE er DobbletLenkeListe som skal leveres inn!
 * Denne klassen skal brukes til å teste metoder fra DobbeltLenketListe
 *
 */

public class Main {

    public static void main(String[] args) {

        String[] s = {"Athi", null, "Victor", "Fredrik", "Nikita", null};

        Liste<String> liste = new DobbeltLenketListe<>(s);
        System.out.println(liste.antall() + " " + liste.tom());


    }
}
