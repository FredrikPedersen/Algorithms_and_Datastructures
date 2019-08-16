package Oblig2;

/**
 * <h1>DobbeltLenketListe</h1>
 *
 * Fredrik Pedersen - s306631
 * Victor K. Pishva - s325871
 * Nikita Petrovs - s325918
 * Athisaiyan Suresh - s325855
 *
 * @author Fredrik Pedersen
 * @since 03. oktober 2018
 */

import java.util.*;

public class DobbeltLenketListe<T> implements Liste<T> {

    private static final class Node<T> {  // en indre nodeklasse

        // instansvariabler
        private T verdi;
        private Node<T> forrige, neste;

        private Node(T verdi, Node<T> forrige, Node<T> neste) { // konstruktør

            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        protected Node(T verdi) {  // konstruktør
            this(verdi, null, null);
        }

    } // Node

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;   // antall endringer i listen

    // hjelpemetode
    private Node<T> finnNode(int indeks) {

        //hvis indeksen er mindre enn antall/2 går letingen fra hodet mot høyre
        if (indeks < antall/2) {
            Node<T> p = hode;
            for (int i = 0; i < indeks; i++)
                p = p.neste;

            return p;

        } else {

            //letingen går fra halen mot venstre
            Node<T> p = hale;
            for (int i = antall - 1; i > indeks; i--)
                p = p.forrige;

            return p;
        }
    }

    //hjelpemetode
    private static void fratilKontroll(int antall, int fra, int til)  {
        if (fra < 0) // fra er negativ
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") er negativ!");

        if (til > antall) // til er utenfor tabellen
            throw new IndexOutOfBoundsException
                    ("til(" + til + ") > antall(" + antall + ")");

        if (fra > til) // fra er større enn til
            throw new IllegalArgumentException
                    ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }

    // konstruktør
    public DobbeltLenketListe() {
        hode = hale = null;
        antall = 0;
        endringer = 0;
    }

    // konstruktør
    public DobbeltLenketListe(T[] a) {
        this(); //nuller ut alle variabler

       /* LA DETTE STÅ INNTIL VIDERE!
        if (a.length == 0) //sjekker om tabellen er tom og gir feilmelding hvis det stemmer
            throw new NullPointerException("Tabellen du prøver å legge inn er tom!"); */

        Objects.requireNonNull(a, "Tabellen du prøver å legge inn er tom!");

        hode = hale = new Node<>(null);

        for (int i = 0; i < a.length; i++) { //itererer gjennom alle verdiene i tabellen
            if (a[i] != null) { //sjekker at det ikke er en null-verdi,
                hale = hale.neste = new Node<>(a[i], hale, null);
                antall++;
            }
        }

        if (antall == 0) //dersom alle verdier er null blir hode og hale den samme tomme noden
            hode = hale = null;
        else
            (hode = hode.neste).forrige = null; //setter så det ikke er en node foran hode
    }

    // subliste
    public Liste<T> subliste(int fra, int til) {
        fratilKontroll(antall, fra, til);

        DobbeltLenketListe<T> liste = new DobbeltLenketListe<>();
        Node<T> p = finnNode(fra);

        for (int i = fra; i < til; i++)
        {
            liste.leggInn(p.verdi);
            p = p.neste;
        }
        return liste;
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "Verdien du prøver å legge inn er en null verdi");

        if (antall == 0)
            hode = hale = new Node<>(verdi, hale,null); //tom liste
        else
            hale = hale.neste = new Node<>(verdi, hale, null); //legges bakerst


        antall++;
        endringer++;
        return true; //vellykket innlegging
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi, "Ikke tillatt med null-verdier!");
        indeksKontroll(indeks, true);

        if(antall == 0) { //tom liste, oppretter hode/hale
            hode = hale = new Node<>(verdi, null, null);

        } else if (indeks == 0) { //legges forst
            hode = hode.forrige = new Node<>(verdi, null, hode);

        } else if (indeks == antall) { //legges sist
            hale = hale.neste = new Node(verdi, hale, null);

        } else {
            Node<T> p = finnNode(indeks);
            Node<T> q = finnNode(indeks-1);

            q.neste = new Node<T>(verdi, q, p);
            p.forrige = q.neste;

            //p.forrige.neste = p.forrige = new Node<> (verdi, p.forrige,
        }

        antall++;
        endringer++;
    }

    @Override
    public boolean inneholder(T verdi) {
        return indeksTil(verdi) != -1;
    }

    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks, false); //False: indeks = antall er ulovlig.
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        if (verdi == null)
            return -1; //nullverdier eksisterer ikke i listen vi har laget

        Node<T> p = hode;
        //bruker indeksene som en iterasjonsvariabel og sammenligner verdiene i indeksene med parameterverdien
        for (int indeks = 0; indeks < antall; indeks++) {
            if (p.verdi.equals(verdi))
                return indeks;
            p = p.neste;
        }
        return -1; //verdien ble ikke funnet
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        Objects.requireNonNull(nyverdi, "Ikke tillatt med nullverdier!");
        indeksKontroll(indeks, false); //False: indeks = antall er ulovlig.

        Node<T> p = finnNode(indeks);
        T gammelVerdi = p.verdi;

        p.verdi = nyverdi;
        endringer++;
        return gammelVerdi;
    }

    @Override
    public boolean fjern(T verdi) {
        if (verdi == null)
            return false; //vi har ingen null-verdier i listen

        //lokke som opretter en node p, sjekker at den ikke er en null-verdi, og sender den så videre i listen
        for (Node<T> p = hode; p != null; p = p.neste) {

            //if-statement som stopper for-lokken hvis p.verdi er lik parameterverdien
            if (p.verdi.equals(verdi)) {

                //sjekker om verdien ligger i hodet
                if (p == hode) {

                    //haandterer at det kun er en verdi i listen ved aa sette hodet og hale lik hverandre og til null
                    if(antall == 1)
                        hode = hale = null;

                        //det er flere verdier, hodet blir dyttet en plass til venstre. Nuller ut forrige-pekeren til det nye hodet
                    else {
                        hode = hode.neste;
                        hode.forrige = null;
                    }
                }

                //hvis verdien er funnet i halen skal halen flyttes en til venstre og nestepekeren til den nye halen nulles ut
                else if (p == hale) {
                    hale = hale.forrige;
                    hale.neste = null;
                }

                //hvis verdien finnes hvor som helst ellers i listen skal folgende skje:
                else {
                    p.forrige.neste = p.neste; //neste-pekeren til p.forrige flyttes til p.neste
                    p.neste.forrige = p.forrige; //forrige-pekeren til p.neste flyttes til p.forrige
                }

                antall--;
                endringer++;
                return true;
            }
        }
        return false; //verdien ble ikke funnet
    }

    @Override
    public T fjern(int indeks) {
        indeksKontroll(indeks, false); //False: indeks = antall er ulovlig

        Node<T> p = finnNode(indeks);

        if (p == hode) { //forste verdi skal fjernes

            if (antall == 1) {
                hode = hale = null; //hvis det bare er en verdi i listen skal halen nulles ut

            } else {
                hode = hode.neste; //hode flyttes til hoyre
                hode.forrige = null; //forrige-pekeren til hodet nulles ut
            }
        }

        else if(p == hale) {
            hale = hale.forrige;
            hale.neste = null;
        }

        else {
            p.forrige.neste = p.neste; //neste-pekeren til p.forrige flyttes til p.neste
            p.neste.forrige = p.forrige; //forrige-pekeren til p.neste flyttes til p.forrige
        }

        endringer++;
        antall--;
        return p.verdi;
    }

    @Override
    public void nullstill() {

        Node<T> p = hode;
        for(int i = 0; i < antall; i++) {
            Node<T> temp = p.neste;
            p.forrige = null; //fjerner forrige pekeren
            p.neste = null;  //fjerner neste pekkeren
            p.verdi = null;  //fjerner verdien
            p = temp; //setter p til neste verdi
            endringer++;
        }
        hode = hale = null;
        antall = 0;
    }

    @Override
    public String toString() {
        //hentet direkte fra kompendiet...
        StringBuilder s = new StringBuilder();
        s.append('[');

        if (!tom()) {
            Node<T> p = hode;
            s.append(p.verdi);

            p = p.neste;

            while(p != null) { //tar med resten hvis det er noe mer
                s.append(',').append(' ').append(p.verdi);
                p = p.neste;
            }
        }
        s.append(']');

        return s.toString();
    }

    public String omvendtString() {
        StringJoiner omvendt = new StringJoiner(", ", "[", "]");
        for (Node<T> p = hale; p != null; p = p.forrige)
            omvendt.add(p.verdi.toString());
        return omvendt.toString();
    }

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {

        for(int ant = liste.antall(); ant > 0; ant--) {//for-lokke for elementer i listen

            Iterator<T> iterator = liste.iterator();
            T minValue = iterator.next();
            int minIndeks = 0;


            for (int indeks = 1; indeks < ant; indeks++) {//for-lokke for aa finne minste indeks
                T current = iterator.next();

                if (c.compare(current, minValue) < 0) {
                    minValue = current;
                    minIndeks = indeks;
                }
            }
            liste.leggInn(liste.fjern(minIndeks)); //Tar bort verdien som er minst og legger den forst
        }
    }

    @Override
    public Iterator<T> iterator()
    {
        return new DobbeltLenketListeIterator();
    }
    public Iterator<T> iterator(int indeks)
    {
        indeksKontroll(indeks, false);
        return new DobbeltLenketListeIterator(indeks);
    }

    private class DobbeltLenketListeIterator implements Iterator<T> {

        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator() {
            denne = hode;     // denne starter på den forste i listen
            fjernOK = false;  // blir sann naar next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks) {
            denne = finnNode(indeks);     // denne starter paa den forste i listen
            fjernOK = false;  // blir sann naar next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        @Override
        public boolean hasNext() {
            return denne != null;  // denne koden skal ikke endres!
        }

        @Override
        public T next() {
            if(endringer != iteratorendringer) throw new ConcurrentModificationException("endringer og iteratorendringer er ikke like");
            if(!hasNext()) throw new NoSuchElementException("itteratoren er tom");
            fjernOK = true;
            T verdi = denne.verdi;
            denne = denne.neste;
            return verdi;
        }

        @Override
        public void remove() {
            if(!fjernOK) {
                throw new IllegalStateException("Ikke laget ennå!"); //Dersom det ikke er lov aa kalle denne metoden kastes en Exception.
            }
            if (endringer != iteratorendringer){
                throw new ConcurrentModificationException("Endringer og iteratorendringer er forskjelige"); // Dersom endringer og iteratorendringerer er forskjellige, kastes en Concurrent-ModificationException
            }
            fjernOK = false;

            if (antall == 1){
                hode = hale = null;
            }else if(denne == null){
                hale = hale.forrige;
                hale.neste = null;
            }else if(denne.forrige == hode){
                hode = denne;
                denne.forrige = null;
            }else{
                Node<T> nodeSomSkalFjernes = denne.forrige;
                Node<T> nodeEtter = nodeSomSkalFjernes.neste;
                Node<T> nodeFoer = nodeSomSkalFjernes.forrige;

                nodeEtter.forrige = nodeFoer;
                nodeFoer.neste = nodeEtter;
            }
            antall--;
            endringer++;
            iteratorendringer++;
        }

    } //DobbeltLenketListeIterator

} //Oblig2.DobbeltLenketListe
