package Oblig3;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

public interface Beholder<T> extends Iterable<T> {

    public boolean leggInn(T verdi); //legger inn i beholderen
    public boolean inneholder (T verdi); //sjekker om den inneholder verdi
    public boolean fjern(T verdi); // fjerne verdi fra beholderen
    public int antall(); // returnerer antallet i beholderen
    public boolean tom(); //sjekker om beholderen er tom
    public void nullstill(); //tømmer beholderen
    public Iterator<T> iterator(); //returnerer en iterator

    default boolean fjernHvis(Predicate<? super T> p) { //betingelsesgjerning
        Objects.requireNonNull(p);

        boolean fjernet = false;
        for (Iterator<T> i = iterator(); i.hasNext(); ) {
            if (p.test(i.next())) {
                i.remove();
                fjernet = true;
            }
        }
        return fjernet;
    }
} //grensesnittet beholder
