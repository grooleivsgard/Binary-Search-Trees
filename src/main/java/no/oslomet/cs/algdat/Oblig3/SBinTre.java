package no.oslomet.cs.algdat.Oblig3;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.StringJoiner;

public class SBinTre<T> {

    public static void main(String[] args) {
        SBinTre<Integer> tre =
                new SBinTre<>(Comparator.naturalOrder());
        int[] a = {6, 14, 1, 8, 12, 3, 7, 9, 11, 13, 2, 5, 4};
        for (int verdi : a) tre.leggInn(verdi);
        String s = tre.toStringPostOrder();

        System.out.println(s);
    }

    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public SBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }

    // --------- OPPGAVE 1 --------------

    /**
     * Oppgave 1 bruker kildekode fra 5.3.2 i kompendiet.
     *
     * @param verdi skal legges inn som ny node i binærtreet.
     * @return true når verdien er lagt inn.
     */
    public boolean leggInn(T verdi) {

        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");


        if(rot == null){
            //Treet er tomt
            rot = new Node<>(verdi, null);
        }
        else {
            //Treet er ikke tomt
            //Node pekere p og q blir initialisert,
            // samt variabel cmp til sammenligning av verdier.
            Node<T> p = rot;
            Node <T> q = null;
            int cmp;

            while(p != null){
                //setter q-pekeren til p, og flytter p slik at q peker på forelder til p
                q = p;
                cmp = comp.compare(verdi, p.verdi);
                if(cmp < 0) //verdiene er like
                {
                    p = p.venstre;
                } else //verdiene er ikke like
                {
                    p = p.høyre;
                }
            }
            //finner plassen i treet hvor verdien skal legges inn
            cmp = comp.compare(verdi, q.verdi);
            if(cmp == -1){ //verdi < q.verdi
                q.venstre = new Node<>(verdi, q);
            } else { //verdi >= q.verdi
                q.høyre = new Node<>(verdi, q);
            }

        }
        antall++;
        return true;
    }

    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    // ------- OPPGAVE 2 ------------------
    /**
     * Denne metoden teller antall forekomster av en verdi i et BST.
     * Metoden bruker en liknende while-løkke som i inneholder(), men den fortsetter
     * etter verdien er funnet, ved å flytte pekeren mot høyre barn.
     *
     * @param verdi sjekkes for om antall forekomster i treet.
     * @return antall forekomster av verdien. Antall er initialisert til 0 ved start slik at det blir returnert
     * 0 dersom det ikke er noen forekomster av verdien.
     */
    public int antall(T verdi) {

        //initialiserer antall til 0. Dersom verdi ikke blir funnet er det dette som returneres.
        int antall = 0;


        //sjekker først om listen er tom, og at listen inneholder verdien vi leter etter
        if(!tom() && inneholder(verdi)){

            //setter node-peker p til rot-noden
            Node<T> p = rot;

            //løper gjennom treet så lenge p ikke er null
            while (p != null) {
                int cmp = comp.compare(verdi, p.verdi);
                if (cmp < 0) p = p.venstre; //verdi er mindre p.verdi
                else if (cmp > 0) p = p.høyre; //verdi er større enn p.verdi
                else {
                    //Vi har funnet verdien. Dersom det er flere av samme verdi, må disse ligge mot høyre.
                    antall++;
                    p = p.høyre;
                }
            }
        }
        return antall;

    }

    public void nullstill() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    // ------- OPPGAVE 3 ------------------
    /**
     * Metoden finner første postorden i et Binary Search Tree.
     * @param p er rotpekeren.
     * @param <T> Node er av type Generics.
     * @return første node i postorden.
     */
    private static <T> Node<T> førstePostorden(Node<T> p) {

        //Bruker Programkode 5.1.7 h) i kompendiet.
        //Finner første postorden ved å traverse til bladnoden lengst til venstre i treet.

        while(true) {
            if (p.venstre != null){
                p = p.venstre;
            } else if (p.høyre != null){
                p = p.høyre;
            } else {
                return p;
            }
        }
    }

    /**
     * Metoden finner neste node i postorden.
     * @param p nodepekeren vi bruker til å finne neste postorden.
     * @param <T> Noden er av type Generics.
     * @return noden som kommer etter p i postorden.
     */
    private static <T> Node<T> nestePostorden(Node<T> p) {

        //hvis nestePostOrden er roten
        if(p.forelder == null){
            return null;
        }

        //hvis p er høyre barn til forelder
        if(p == p.forelder.høyre) {
           return p.forelder;
        }
        //hvis p er venstre barn til forelder
        else if (p == p.forelder.venstre){
            //hvis venstre barn er enebarn
            if (p.forelder.høyre == null) {
                return p.forelder;
            }
            //p er ikke enebarn / høyre barn er ikke null
            else {
                return førstePostorden(p.forelder.høyre);
            }

        } else {
            //nås aldri
            return null;
        }

    }

    // ------- OPPGAVE 4 ------------------
    public void postorden(Oppgave<? super T> oppgave) {
        /**
         * Du skal implementere den første funksjonen uten bruk av
         * rekursjon og uten bruk av hjelpevariabler som stack / queue. Du skal bruke funksjonen
         * nestePostorden fra forrige oppgave. Start med å finne den første noden p i postorden.
         * Deretter vil (f.eks. i en while-løkke) setningen: p = nestePostorden(p); gi den neste. Osv.
         * til p blir null.
         */

        if(tom()) return;

        Node<T> p = førstePostorden(rot); //setter nodepeker p til første postorden

        while(nestePostorden(p) != null){
            oppgave.utførOppgave(p.verdi);
            p = nestePostorden(p);
        }

        oppgave.utførOppgave(p.verdi);
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        /**
         * For den rekursive metoden skal du lage et rekursivt kall som traverserer treet
         * i postorden rekkefølge.
         */

        postordenRecursive(rot, oppgave);
    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {

        //Basistilfellet
        if(p == null) return;

        //Traverserer binærtreet etter postorden

        //Rekursivt kall for venstre barn
        postordenRecursive(p.venstre, oppgave);
        //Rekursivt kall for høyre barn
        postordenRecursive(p.høyre, oppgave);
        //Utfører oppgaven på denne noden
        oppgave.utførOppgave(p.verdi);

    }

    public ArrayList<T> serialize() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    static <K> SBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


} // ObligSBinTre
