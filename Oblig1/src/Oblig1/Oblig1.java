package Oblig1;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * <h1>Oblig 1</h1>
 * <p>Hovedklassen som det skal jobbes i.</p>
 *
 *Fredrik Pedersen - s306631
 *Victor K. Pishva - s325871
 *Nikita Petrovs - s325918
 *Athisaiyan Suresh - s325855
 *
 * @author Fredrik Pedersen
 * @since 03. september 2018
 */
public class Oblig1 {

    /**
     * <h1>Oppgave 1</h1>
     *
     * Antar at tabellen inneholder en tilfeldig permutasjon av tallene fra 1 til n.
     * Det blir flest ombyttinger (n-1) når den storste verdien er plassert i a[0].
     * Det blir fearrest ombyttinger (0) når den storste verdien er plassert i a[n].
     * Ved kjoring av metoden gjennomsnitt får en at ved 100 forskjellige permutasjoner av en tabell med tallene 1 til 10, så vil det i gjennomsnitt bli tilnaermet 7 ombyttinger.
     *
     * Med gjennomsnittlig sju ombyttinger kan en uten aa gjore noen videre beregninger anta at det vi har gjort her er mindre effektivt enn
     * maksmetodene vi har sett på tidligere, da de ikke utforer noen ombyttinger.
     *
     * @author Nikita Petrovs, Fredrik Pedersen
     * @version 1.5
     * @since 10. september 2018
     */
    public static int maks(int[] a) {
        tomKontroll(a); //kontrollerer at tabellen har 1 eller flere verdier
        for(int i = 0; i < a.length-1; ++i) {
            if(a[i] > a[i+1]) {
                bytt(a,i,i+1); //sjekker om a[i] er storre enn den neste verdien i tabellen. Bytter plass paa dem hvis det stemmer.
            }
        }
        return a[a.length-1]; //returnerer maksverdien, som naa er plassert sist i tabellen.
    }

    public static int ombyttinger(int[] a) {
        int ombyttinger = 0;
        for(int i = 0; i < a.length-1; ++i) {
            if(a[i] > a[i+1]) {
                bytt(a,i,i+1); //sjekker om a[i] er storre enn den neste verdien i tabellen. Bytter plass paa dem hvis det stemmer.
                ombyttinger++;
            }
        }
        return ombyttinger;
    }

    public static double gjennomsnitt() {
        double sum = 0;

        for (int i = 0; i < 100; i++) {
            int[] a = randPerm(10);
            sum+= ombyttinger(a);
        }
        double gjennomsnitt = sum/100;
        return  gjennomsnitt;
    }

    /**
     * <h1>Oppgave 2</h1>
     *
     * @author Fredrik Pedersen
     * @version 1.0
     * @since 10. september 2018
     */
    public static int antallUlikeSortert(int[] a) {

        if (!erSortert(a)) {
            throw new IllegalStateException("Tabellen er ikke sortert!");
        }

        int antallForskjellige = 1;

        for (int i = 0; i < a.length-1; i++) {
            if (a[i] != a[i+1]) {
                antallForskjellige += 1;
            }
        }
        if (a.length == 0) {
            antallForskjellige = 0;
        }
        return antallForskjellige;
    }


    /**
     * <h1>Oppgave 3</h1>
     *
     * @author Fredrik Pedersen
     * @version 1.0
     * @since 10. september 2018
     */
    public static int antallUlikeUsortert(int[] a) {

        int antallForskjellige = 1;

        if (a.length < 2) { //sjekker om tabellen har en lengde paa to eller mer, for aa slippe aa gaa inn i for-loopen naar det er unodvendig.
            antallForskjellige = a.length;
        }

        for (int i =1; i < a.length; i++) {
            int j;
            for (j = 0; j < i; j++) {
                if (a[j] == a[i])
                    break;
            }

            if (j == i)
                antallForskjellige++;
        }
        return antallForskjellige;
    }


    /**
     * <h1>Oppgave 4</h1>
     *
     * @author Nikita Petrovs, Fredrik Pedersen
     * @version 1.1
     * @since 11. september 2018
     */
    public static void delsortering(int[] a) {
        int v = 0;
        int h = a.length-1;

        while (v < a.length && (a[v] & 1) != 0) v++;
        while (h >= 0 && ((a[h] & 1) == 0)) h--;

        while (true) {

            while (v < a.length && (a[v] & 1) != 0) v++;
            while (h >= 0 && ((a[h] & 1) == 0)) h--;
            if(v < h) {
                bytt(a, v, h);
            } else {
                break;
            }

        }
        Arrays.sort(a,0,v);
        Arrays.sort(a,v,a.length);
    }

    /**
     * <h1>Oppgave 5</h1>
     *
     * @author Nikita Petrovs, Fredrik Pedersen
     * @version 1.1
     * @since 11. september 2018
     */
    public static void rotasjon(char[] a) {

        if (a.length < 2) {
            return;
        }

        for(int i = a.length-1; i > 0; i--) {
            byttChar(a,i-1,i);
        }
    }

    /**
     * <h1>Oppgave 6</h1>
     * Med unntak av lengdesjekken saa er denne metoden hentet fra kompendiet
     *
     * @author Fredrik Pedersen
     * @version 1.0
     * @since 11. september 2018
     */
    public static void rotasjon(char[] a, int k) {
        if (a.length < 1)
            return; //kontrollerer om tabellen har faerre enn ett element, avslutter metoden hvis den er tom.

        int n = a.length;

        if ((k %= n) < 0)
            k += n; //motsatt vei

        char[] b = Arrays.copyOfRange(a, n -k, n); //hjelpetabell
        for (int i = n - 1; i >= k; i--)
            a[i] = a[i-k]; //forskyver

        System.arraycopy(b,0,a,0,k); //kopierer
    }
	
	/**
     * <h1>Oppgave 7a</h1>
     *
     * Dette ligger i kompendiet , men det som maa legges merke til er at metoden egentlig var for int
     * Det som har blitt gjort her at jeg har gjort dette om til String som oppgaven har bedt oss om aa gjore
     * StringBuilder og append er to viktige maater aa faa gjort om fra int til String i en metode.
     *
     * @author Athisaiyan Suresh
     * @version 1.0
     * @since 11. september 2018
     */
    public static String flett (String s, String t) {
        StringBuilder sb = new StringBuilder();
        int i = 0, j = 0, k = 0;                 // lokkevariabler

        while (i < s.length() - 1 && j < t.length()) {
            sb.append(s.charAt(i++));      // forst en verdi fra s
            sb.append(t.charAt(j++));       // saa en verdi fra t
        }
        // vi maa ta med resten

        if (i < s.length()) sb.append(s.substring(i));
        if (j < t.length()) sb.append(t.substring(j));

        return sb.toString();

        //append = legge til
        //charAt sjekker char paa index
    }

    /**
     *  <h1>Oppgave 7b</h1>
     *
     *  Vi antar at den runden vi er paa er den siste. Det vi gjor da er at vi gaar gjennom alle strings
     *  og sjekker om den har flere char. Hvis det er flere saa vil den bli satt inn.
     *  Hvis ikke vil den iterere paa nytt.
     *
     * @author Athisaiyan Suresh
     * @version 1.0
     * @since 13. september 2018
     *
     */
    public static String flett(String... s){

        StringBuilder sb2 = new StringBuilder();

        boolean done = false;
        int i = 0;
        while (!done) {
            done = true;                       //Antar at den runden vi er paa er den siste

            for (int j = 0; j < s.length; j++) { //gaar gjennom alle stings
                if (s[j].length() > i) {        //sjekker om den har flere char
                    sb2.append(s[j].charAt(i)); //Hvis det er flere saa vil den sette den inn

                    done = false;              //Hvis ikke saa vil den iterere paa nytt.
                }
            }
            i++;

        }
        return sb2.toString();
    }

    /**
     * <h1>Oppgave 8</h1>
     *
     * Innsettingssortering tatt fra kompendiet og formatert for aa passe til oppgaven
     *
     * @author Fredrik Pedersen
     * @version 1.0
     * @since 11 september 2018
     */
    public static int[] indekssortering(int[] a) {
        int[] indeks = new int[a.length]; //oppretter en ny tabell med samme lengde som parametertabellen

        for(int i = 0; i < a.length; i++){
            indeks[i] = i; //Fyller indeks-tabellen med verdier fra 0 til a.length
        }

        int temp;
        int j;
        for (int i = 1; i < a.length; i++) {
            for (temp = indeks[i], j = i-1; j >=0 && a[temp] < a[indeks[j]]; j--) {
                //Setter temp lik verdiene i indeks, og j lik i-1.
                //Så lenge j er mindre eller lik 0, og a[temp] er mindre enn a[indeks[j]], saa skal j dekrementeres.
                indeks[j+1] = indeks[j];
            }
            indeks[j+1] = temp;
        }
    return indeks;
    }

    /**
     * <h1>Oppgave 9</h1>
     *
     * @author Victor K. Pishva
     * @version 3.0
     * @since 12 september 2018
     */
    public static int[] tredjeMin(int[] a) {

        if (a.length < 3) {
            throw new NoSuchElementException("a.length (" + a.length + ") < 3");//Feilmelding kastes ved mindre enn tre verdier i arrayet
        }

        int [] firstThree= {a[0], a[1], a[2]};
        int [] sortert = indekssortering(firstThree); // Bruker indekssortering iht oppgaveteksten for aa sortere de tre forste.

        int m = sortert[0]; //minst
        int nm = sortert[1];//nest minst
        int tm = sortert[2];//tredjeminst

        int minst = a[m]; //indeks til minste tall
        int nestminst = a[nm];// indeks til nest minste tall
        int tredjeminst = a[tm];//indeks til tredje minste tall.


        for (int i = 3; i < a.length; i++){
            if (a[i] < tredjeminst){
                if (a[i] < nestminst){
                    if (a[i] < minst){
                        tm = nm;
                        tredjeminst = nestminst;

                        nm = m;
                        nestminst = minst;

                        m = i;
                        minst = a[i];
                    }else{
                        tm = nm;
                        tredjeminst = nestminst;

                        nm = i;
                        nestminst = a[i];
                    }
                }else{
                    tm = i;
                    tredjeminst = a[i];
                }
            }
        }
        return new int[] {m,nm,tm}; //returnerer array med verdiene
    }

    /**
     * <h1>Oppgave 10</h1>
     *
     * Inspirert av oppgavesvar funnet i en gammel eksamensfasit
     *
     * @author Nikita Petrovs
     * @version 1.0
     * @since 13. september 2018
     */
    public static boolean inneholdt(String a, String b) {
        char[] charA, charB;

        charA = a.toCharArray();
        charB = b.toCharArray();
        //Lager int tabeller med lengedn av ascii tabellen
        int[] antalA = new int[256];
        int[] antalB = new int[256];

        //Teller opp antall forekomster an alle tegn
        //for loopen git ut et tegn fra stringen(char tabellen)
        //dette tegnet blir brukt som index i antall tabellen
        //når vi caster char til int faar vi ascii verdien til char-en
        for(char aa: charA) antalA[aa]++;
        for(char bb: charB) antalB[bb]++;


        //Sjekker om antall forekomster av et tegn i B er storre en antallet i A
        //Med tanke paa at A maa inneholde B, saa returnerer vi false hvis
        //B har flere forekomster av et tegn enn i A
        for(int i = 0; i < 256; i++) {
            if (antalB[i] < antalA[i]) {
                return false;
            }
        }
        return true;
    }

    /* ----------------- Metoder fra kompendiet --------------------------------- */

    public static int helMin (int[] a) { //bruker hele tabellen
        return minIntervall(a, 0, a.length);
    }

    public static int minIntervall(int a[], int fra, int til) {

        //kontrollerer at vi har et gyldig intervall
        fratilKontroll(a.length, fra, til);

        int m = fra; //indeks til minste verdi i a[fra:til>
        int minverdi = a[fra]; //minste verdi i a[fra:til>

        for (int i = fra + 1; i < til; i++) {
            if (a[i] < minverdi) {
                m = i; //indeks til minste verdi oppdatere
                minverdi = a[m]; //minste verdi oppdateres
            }
        }
        return m; //returnerer posisjonen til minste verdi i a[fra:til>
    }

    public static void fratilKontroll(int tablengde, int fra, int til) {
        if (fra < 0)    //fra er negativ
            throw new ArrayIndexOutOfBoundsException("fra(" + fra + ") er negativ!");

        if (til > tablengde)    //til er utenfor tabellen
            throw new ArrayIndexOutOfBoundsException("til(" + til + ") > tablengde(" + tablengde + ")");

        if (fra > til)  //fra er større enn til
            throw new IllegalArgumentException("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }
    public static int[] randPerm(int n) {
        Random r = new Random(); //en randomgenerator
        int[] a = new int[n]; //en tabell med plass til n tall

        Arrays.setAll(a, i -> i + 1); //Legger inn tallene 1, 2, n

        for (int k = n - 1; k > 0; k--) { //lokke som går n-1 ganger
            int i = r.nextInt(k + 1); //et tilfeldig tall fra 0 til k
            bytt(a, k, i); //bytter om
        }
        return a; //permutasjon returneres
    }

    public static void bytt(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j]= temp;
    }

    public static void byttChar(char[] a, int i, int j) {
        char temp = a[i];
        a[i] = a[j];
        a[j]= temp;
    }
    /* -----------------Hjelpemetoder -----------------------------*/


    /**
     * <h1>tomKontroll</h1>
     *
     * Brukes i oppgave 1 til aa kontrollere at arrayet ikke er tomt
     * @author Fredrik Pedersen, Victor K. Pishva
     * @version 1.0
     * @since 10. september 2018
     */
    public static void tomKontroll(int[] a) {
        if (a.length == 0)
            throw new NoSuchElementException("Parametertabellen er tom!");
    }

    /**
     * <h1>erSortert</h1>
     *
     * Brukes i oppgave 2 til aa kontrollere om en tabell er sortert
     *
     * @author Fredrik Pedersen
     * @version 1.0
     * @since 10. september 2018
     */
    public static boolean erSortert(int[] a) {
        for (int i = 0; i < a.length-1; i++) {
            if (a[i] > a[i+1]) { //sjekker om en verdi er storre enn den som kommer etter
                return false; //tabellen er IKKE sortert
            }
        }
        return true; //fant ingen forekomster at a[i] er storre enn a[i+1], tabellen er sortert.
    }

}
