package Oblig3;
/**
 * <h1>ObligSBinTre</h>
 *
 * Fredrik Pedersen - s306631
 * Victor K. Pishva - s325871
 * Nikita Petrovs - s325918
 * Athisaiyan Suresh - s325855
 *
 * @since 16.10.2018
 * @author Fredrik Pedersen
 *
 *
 * Definisjon 5.2.1:  For hver node p gjelder: 1) hvis p har et ikke-tomt venstre subtre,
 * er alle verdiene der mindre enn verdien i p og 2) hvis p har et ikke-tomt høyre subtre,
 * er alle verdiene der større enn eller lik verdien i p. Et tomt tre og et tre med kun én
 * node er per definisjon et binært søketre.
 *
 */

import java.util.*;
public class ObligSBinTre<T> implements Beholder<T> {

    private static final class Node<T> { // en indre nodeklasse
        private T verdi; // nodens verdi
        private Node<T> venstre, høyre; // venstre og høyre barn
        private Node<T> forelder; // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder) { // konstruktør

            this(verdi, null, null, forelder);
        }

        @Override
        public String toString(){
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot; // peker til rotnoden
    private int antall; // antall noder
    private int endringer; // antall endringer
    private final Comparator<? super T> comp; // komparator

    public ObligSBinTre(Comparator<? super T> c) { // konstruktør

        rot = null;
        antall = 0;
        comp = c;
    }

    @Override
    public final boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot;
        Node<T> q = null;
        int cmp = 0;

        while (p != null) { //fortsetter til p er ute av treet

            q = p;                                 //setter q lik p før p oppdateres så q alltid er forelderen til p
            cmp = comp.compare(verdi, p.verdi);
            p = cmp < 0 ? p.venstre : p.høyre;     //flytter p. Flyttes til venstre når verdi er mindre enn nodeverdien p er på, til høyre når den er større.
        }

        //p er nå null, dvs. ute av treet, q er den siste vi passerte
        p = new Node<>(verdi, q); //oppretter en ny node med q som forelder.

        if (q == null)
            rot = p; //p blir rotnode hvis treet er tomt

        else if (cmp < 0)
            q.venstre = p;

        else
            q.høyre = p;

        antall++;
        endringer++;
        return true; //vellykket innlegging
    }

    @Override
    public boolean inneholder(T verdi) {
        if (verdi == null)
            return false;

        Node<T> p = rot;
        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0)
                p = p.venstre;
            else if (cmp > 0)
                p = p.høyre;
            else
                return true;
        }
        return false;
    }

    @Override
    public boolean fjern(T verdi) {
        if (verdi == null)
            return false;  // treet har ingen nullverdier

        Node<T> p = rot;
        Node<T> q = null;   // q skal være forelder til p

        while (p != null){ // leter etter verdi

            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) {
                q = p;
                p = p.venstre;  // går til venstre
            }

            else if (cmp > 0) {
                q = p;
                p = p.høyre;  // går til høyre
            }

            else
                break;    // den søkte verdien ligger i p
        }

        if (p == null)
            return false; // finner ikke verdien i treet

        if (p.venstre == null || p.høyre == null) {  //p har null eller ett barn

            //b for barn. Har p et venstrebarn settes b til p.venstre, ellers til p.høyre. Denne har en svakhet: hvis p.høyre = null. Dette håndteres under.
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;

            if (b != null)
                b.forelder = q; //håndterer svakheten nevnt ovenfor

            if (p == rot) //er p rotnoden? Isåfall er barnet til p den nye rotnoden.
                rot = b;

            else if (p == q.venstre) { //hvis p er venstrebarnet til q, så settes q.venstre til b
                q.venstre = b;
            }

            else { //hvis p er høyrebarnet til q, så settes q.høyre til b
                q.høyre = b;
                }
            }

        else {  //p har to barn
            Node<T> s = p;
            Node<T> r = p.høyre;   // finner neste i inorden

            while (r.venstre != null) {
                s = r;    // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (r.høyre != null)
                r.høyre.forelder = s;

            if (s != p)
                s.venstre = r.høyre;
            else
                s.høyre = r.høyre;
        }

        antall--;   // det er nå én node mindre i treet
        endringer++;
        return true;
        }

    public int fjernAlle(T verdi){

        if(tom()) return 0;
        int fjernet = 0;

        while(inneholder(verdi)) {
            fjern(verdi);
            fjernet++;
        }
        return fjernet;
    }

    @Override
    public int antall() {
        return antall;
    }

    public int antall(T verdi) {
        int antallForekomster = 0;

        if (inneholder(verdi)) {
            Node<T> p = rot;

            while(p != null) {
                int cmp = comp.compare(verdi, p.verdi);

                if (cmp < 0)
                    p = p.venstre;
                else {
                    if (cmp == 0)
                        antallForekomster++;
                    p = p.høyre;
                }
            }
        }
        return antallForekomster;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public void nullstill() {
        if (!tom()) {
            Node<T> p = rot;
            nullstill(p);
            rot = null;
            endringer++;
        }
    }

    private void nullstill(Node<T> p) {
        /* metode som traverserer treet i post-orden helt ned til bunnen av treet, og nullstiller
        sub-trærne til en node før selve noden nullstilles.
         */
        if (p.venstre != null) {
            nullstill(p.venstre);
            p.venstre = null;
        }

        if (p.høyre != null) {
            nullstill(p.høyre);
            p.høyre = null;
        }

        p.verdi = null;
        antall--; //vi kan spare noen operasjoner på å sette antall = 0 i nullstill(), men skulle det bli behov for å nullstille enkeltnoder senere beholder jeg denne her
    }


    private static <T> Node<T> nesteInorden(Node<T> p) {

        //Hvis p har et høyre subtre, så er den neste helt til venstre i det subtreet
        if (p.høyre != null) {
            p = p.høyre;
            while (p.venstre != null)
                p = p.venstre;
        }

        //p har ikke et høyre subtre, den neste befinner seg dermed over p i treet
        else {

            //hvis p er det høyre barnet til foreldren sin, er det besteforeldren som er den neste
            while (p.forelder != null && p.forelder.høyre == p) {
                p = p.forelder;
            }
                    //hvis p er det venstre barnet til foreldren sin, er det foreldren som er den neste.
                    //siden høyrebarna blir satt til p.forelder i while-løkken over, så tilsvarer dette å sette p = p.forelder.forelder for høyrebarna
                    p = p.forelder;
        }

        return p;
    }

    @Override
    public String toString() {
        if (tom())
            return "[]";

        StringJoiner inOrderString = new StringJoiner(", ", "[", "]");

        //sender p helt ned til venstre i treet, der den første noden i inorden er
        Node<T> p = rot;
        while (p.venstre != null)
            p = p.venstre;

        //iterer gjennom hele treet og legger verdiene til strengen i inorden rekkefølge
        while (p != null) {
            inOrderString.add(p.verdi.toString());
            p = nesteInorden(p);
        }
        return inOrderString.toString();
    }

    public String omvendtString() {
        if (tom())
            return "[]";

        Deque<Node<T>> stack = new ArrayDeque<>();
        Node<T> p = rot;
        StringJoiner stackString = new StringJoiner(", ", "[", "]");

        while (p.høyre != null) { //går helt ned til høyre
            stack.addFirst(p); //legger til verdiene som passeres på veien, den siste verdien legges ikke på stacken
            p = p.høyre;
        }

        stackString.add(p.verdi.toString()); //legger til den siste verdien i Stringen

        /*I det vi starter loopen er p lik den siste verdien. Her sjekker vi om p har
        et venstrebarn. Hvis nei, så fjernes p fra stacken og legges til i strengen.

        Hvis ja, så settes p lik det venstre barnet, og en sjekker om det er et høyrebarn.
        Hvis nei, så fjernes p fra stacken og legges til i strengen, hvis ja, så legges p
        til på stacken og høyrebarnet er den nye p.

        På denne måten traverseres det til bunnen av ethvert høyre subtre først, så venstre.

        Når stacken tilslutt er tom, så brytes while-løkka*/

        while (true) {
            if (p.venstre != null) {

                p = p.venstre;

                while (p.høyre != null) {
                    stack.addFirst(p);
                    p = p.høyre;
                }
            }

            else if(!stack.isEmpty()) {
                p = stack.removeFirst();
            }
            else
                break;

            stackString.add(p.verdi.toString());
        }

        return stackString.toString();
    }

    public String høyreGren() {
        if (tom())
            return "[]";

        StringJoiner høyreString = new StringJoiner(", ", "[", "]");

        //sender p helt ned til høyre i treet
        Node<T> p = rot;
        while(true) {

            høyreString.add(p.verdi.toString());

            if (p.høyre != null)
                p = p.høyre;

            else if (p.venstre != null)
                p = p.venstre;

            else
                break; //det er ikke flere barn
        }

        return høyreString.toString();
    }

    public String lengstGren() {
        //henter mye av denne metoden fra kompendiet. Programkode 5.1.6 a)
        if (tom())
            return "[]";


        Deque<Node<T>> kø = new ArrayDeque<>();
        kø.addLast(rot);

        Node<T> p = null;

        while (!kø.isEmpty()) { //while-loop som itererer gjennom treet i nivåorden. Når loopen stopper er p lik den siste verdien i treet.

            p = kø.removeFirst();

            if (p.høyre != null)
                kø.addLast(p.høyre);
            if (p.venstre != null)
                kø.addLast(p.venstre);
        }

        return gren(p); //kaller hjelpemetoden gren for å få grenen som en streng.
    }

    public String[] grener() {
        if (tom())
            return new String[0];

        List<String> grenListe = new ArrayList<>(); //oppretter listen som brukes til å oppbevare alle grenene

        grener(rot, grenListe); //finner alle grenene og lagrer de i hver sin String

        String[] grener = new String[grenListe.size()]; //lager en String tabell med like mange plasser som vi har verdier i listen

        for (int i = 0; i < grener.length; i++) { //setter verdien i grener[i] til å være lik listens tilsvarende indeks
            grener[i] = grenListe.get(i);
        }

        return grener;
    }

    //rekursiv hjelpemetode som brukes til å finne alle grener i subtrærne til p og legge de til i en liste
    private void grener(Node<T> p, List<String> grenListe) {

        //er p en bladnode? Hvis ja, legg hele grenen til i listen.
        if (p.venstre == null && p.høyre == null)
            grenListe.add(gren(p));

        //har p et venstrebarn? Hvis ja, kall metoden på nytt med venstrebarnet som p
        if (p.venstre != null)
            grener(p.venstre, grenListe);

        //har p et høyrebarn? Hvis ja, kall metoden på nytt med høyrebarnet som p
        if (p.høyre != null)
            grener(p.høyre, grenListe);
    }

    //hjelpemetode som brukes til å få ut en enkelt gren som en String. Originalt kodet inne i høyreGren(), men ble flyttet ut grunnet gjennbruk av kode i grener()
    private <T> String gren(Node<T> p) {

        Stack<T> stackA = new Stack<>();
        Stack<T> stackB = new Stack<>();

        while (p != null) { //legger p og alle dens foreldrenoder opp til rotnoden (altså hele grenen) på stack A
            stackA.push(p.verdi);
            p = p.forelder;
        }

        while (!stackA.isEmpty()) { //flytter grenen til Stack B, og dermed snur rekkefølgen. Ønskes det å skrive ut grenen fra bunn til topp, fjern denne.
            stackB.push(stackA.pop());
        }

        return stackB.toString(); //returnerer stack B som en String.
    }

    public String bladnodeverdier() {
        if (tom())
            return "[]";

        List<T> bladnodeListe = new ArrayList<>(); //oppretter listen som brukes til å oppbevare alle bladnodene

        bladnoder(rot, bladnodeListe); //finner alle bladnodene og lagrer de i hver sin String

        StringJoiner bladnoder = new StringJoiner(", ", "[", "]");

        for (T verdi : bladnodeListe)
            bladnoder.add(verdi.toString()); //setter itererer gjennom listen og legger verdiene sammen i en String

        return bladnoder.toString();
    }

    //hjelpemetode som brukes til å finne bladnodene i alle subtrærne til p og legger de til i en liste
    private void bladnoder(Node<T> p, List<T> bladnodeListe) {
        //er p en bladnode? Hvis ja, legg til noden i listen.
        if (p.venstre == null && p.høyre == null)
            bladnodeListe.add(p.verdi);

        //har p et venstrebarn? Hvis ja, kall metoden på nytt med venstrebarnet som p
        if (p.venstre != null)
            bladnoder(p.venstre, bladnodeListe);

        //har p et høyrebarn? Hvis ja, kall metoden på nytt med høyrebarnet som p
        if (p.høyre != null)
            bladnoder(p.høyre, bladnodeListe);
    }

    public String postString() {
        if (tom())
            return "[]";

        Node<T> p = rot;

        StringJoiner postString = new StringJoiner(", ", "[", "]");
        p = førsteBladNode(p); //setter p lik den første bladnoden i postorder

        postString.add(p.verdi.toString()); //legger til den første bladnoden i Stringen

       while (p.forelder != null) { //stopper løkken når vi kommer til roten
            p = nestePostOrden(p); //setter p lik den neste noden i PostOrder
            postString.add(p.verdi.toString());
       }

        return postString.toString();
    }

    //hjelpemetode for å finne den neste noden i postorder
    private Node<T> nestePostOrden(Node<T> p) {

        Node<T> q = p.forelder; //hjelpenode

        if (p == q.høyre || q.høyre == null) //er p høyrebarnet til q eller q ikke har et høyrebarn så er q den neste
            p = q;

        else //hvis p er venstrebarnet til q og det eksisterer et høyre subtre for q er den første bladnoden der den neste
            p = førsteBladNode(q.høyre);

        return p;
    }

    private Node<T> førsteBladNode(Node<T> p) {
        while (true) {
            if (p.venstre != null) //traverserer så langt til venstre som det er mulig
                p = p.venstre;

            else if (p.høyre != null) //når det er tomt for venstrebarn traverserer vi en gang mot høyre
                p = p.høyre;

            else //ingen flere barn, rotnode funnet
                break;
        }
        return p;
    }

    //hjelpemetoden som finner den neste bladnoden
    private Node<T> nesteBladNode(Node<T> p) {
        Node<T> q = p.forelder;
        while (q != null && (p == q.høyre || q.høyre == null)) {
            p = q;
            q = q.forelder;
        }

        if (q == null)
            return null;

        else
            return førsteBladNode(q.høyre);
    }

    @Override
    public Iterator<T> iterator() {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T> {
        private Node<T> p = rot;
        private Node<T> q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator() { // konstruktør som setter p lik roten og nullstiller alle verdier i iteratoren
            if (tom())
                return;

            p = førsteBladNode(rot);
            q = null;
            removeOK = false;
            iteratorendringer = endringer;
        }

        @Override
        public boolean hasNext() {
            return p != null; // Denne skal ikke endres!
        }

        @Override
        public T next() {
            if (endringer != iteratorendringer)
                throw new ConcurrentModificationException("Endringer og iteratorendringer er forskjellige!");

            if (!hasNext())
                throw new NoSuchElementException("Det er ikke flere bladnoder!");

            removeOK = true;

            q = p;
            p = nesteBladNode(p);
            return q.verdi;
        }

        @Override
        public void remove() {
            if (!removeOK)
                throw new IllegalStateException("Ikke lov å fjerne verdier!");

            if (endringer != iteratorendringer)
                throw new ConcurrentModificationException("Endringer og iteratorendringer er forksjellige!");

            removeOK = false;

            Node<T> r = q.forelder;
            if (r == null)
                rot = null;

            else if (q == r.venstre)
                r.venstre = null;

            else
                r.høyre = null;

            antall--;
            endringer++;
            iteratorendringer++;
        }
    } // BladnodeIterator
} // ObligSBinTre
