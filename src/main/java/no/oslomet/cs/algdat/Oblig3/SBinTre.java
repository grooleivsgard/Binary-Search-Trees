package no.oslomet.cs.algdat.Oblig3;


import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class SBinTre<T> {

    public static void main(String[] args) {
        SBinTre<Integer> tre =
                new SBinTre<>(Comparator.naturalOrder());

        // legger inn verdier i treet
        int[] a = {10, 6, 14, 1, 8, 12, 3, 7, 9, 11, 13, 2, 5, 4};
        for (int verdi : a) tre.leggInn(verdi);
        //Denne lambda-funksjonen skriver ut mellomrom før nodens verdi.
        AtomicReference<String> postorden = new AtomicReference<>();
        Oppgave<Integer> oppgave = c -> postorden.set(postorden.get() + " " + c.toString()) ;

        tre.postordenRecursive(oppgave);
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


        if (rot == null) {
            //Treet er tomt
            rot = new Node<>(verdi, null);
        } else {
            //Treet er ikke tomt
            //Node pekere p og q blir initialisert,
            // samt variabel cmp til sammenligning av verdier.
            Node<T> p = rot;
            Node<T> q = null;
            int cmp;

            while (p != null) {
                //setter q-pekeren til p, og flytter p slik at q peker på forelder til p
                q = p;
                cmp = comp.compare(verdi, p.verdi);
                if (cmp < 0) //verdiene er like
                {
                    p = p.venstre;
                } else //verdiene er ikke like
                {
                    p = p.høyre;
                }
            }
            //finner plassen i treet hvor verdien skal legges inn
            cmp = comp.compare(verdi, q.verdi);
            if (cmp == -1) { //verdi < q.verdi
                q.venstre = new Node<>(verdi, q);
            } else { //verdi >= q.verdi
                q.høyre = new Node<>(verdi, q);
            }


        }
        endringer++;
        antall++;
        return true;
    }

    /**
     * Metoden skal fjerne en verdi tilhørende en node. Bruker programkode 5.2.8 d), men har lagt til
     * en if-setning som bytter forelder-pekeren til barnet (som skal fjernes).
     * @param verdi som skal fjernes.
     * @return true dersom verdien er funnet og fjernet.
     */
    public boolean fjern(T verdi) {

        if (verdi == null) return false;

        //Se på video på canvas, bare merk at det er en feil i videoen
        Node<T> node = rot;
        Node <T> forelder = null;

        //Bruker en while løkke til å finne verdien i treet
        while (node != null) {
            int cmp = comp.compare(verdi, node.verdi); //sammenligner verdier
            if (cmp < 0) { //verdi < node.verdi
                forelder = node;
                node = node.venstre; // går til venstre
            } else if (cmp > 0) { //verdi > node.verdi
                forelder = node;
                node = node.høyre;
            } else break; // verdi = node.verdi (cmp = 0)
        }

        if (node == null) return false; //finner ikke verdien -- hva er poenget med denne?

        //Sjekker om noden kun har ett barn
        if (node.venstre == null || node.høyre == null) {
            Node<T> barn;

            if (node.venstre != null) { //Barnet ligger til venstre
                barn = node.venstre;
            } else {
                barn = node.høyre; //Barnet ligger til høyre
            }

            //Kun ett barn / bladnode
            if(barn != null){
                barn.forelder = forelder;
            }

            if (node == rot) {
                rot = barn; //Noden er rotnoden
            } else if (node == forelder.venstre) { //Hvis node er en venstre node
                forelder.venstre = barn; //bytter venstre node med barnet funnet ovenfor

            } else { //Node er høyre node
                forelder.høyre = barn; //bytter høyre node med barnet funnet ovenfor
            }
        }
        //Har to barn eller ingen barn
        else {
            Node<T> sub_forelder = node;
            Node <T> sub_node = node.høyre;

            //Finner neste in orden
            while (sub_node.venstre != null) {
                sub_forelder = sub_node;
                sub_node = sub_node.venstre;
            }

            node.verdi = sub_node.verdi; //r er neste in orden

            if (sub_forelder != node) { //hvis
                sub_forelder.venstre = sub_node.høyre;
            } else {
                sub_forelder.høyre = sub_node.høyre;
            }
        }

        antall--;
        return true;
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
        if (!tom() && inneholder(verdi)) {

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
     *
     * @param p   er rotpekeren.
     * @param <T> Node er av type Generics.
     * @return første node i postorden.
     */
    private static <T> Node<T> førstePostorden(Node<T> p) {

        //Bruker Programkode 5.1.7 h) i kompendiet.
        //Finner første postorden ved å traverse til bladnoden lengst til venstre i treet.

        while (true) {
            if (p.venstre != null) {
                p = p.venstre;
            } else if (p.høyre != null) {
                p = p.høyre;
            } else {
                return p;
            }
        }
    }

    /**
     * Metoden finner neste node i postorden.
     *
     * @param p   nodepekeren vi bruker til å finne neste postorden.
     * @param <T> Noden er av type Generics.
     * @return noden som kommer etter p i postorden.
     */
    private static <T> Node<T> nestePostorden(Node<T> p) {

        //hvis nestePostOrden er roten
        if (p.forelder == null) {
            return null;
        }

        //hvis p er høyre barn til forelder
        if (p == p.forelder.høyre) {
            return p.forelder;
        }
        //hvis p er venstre barn til forelder
        else if (p == p.forelder.venstre) {
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

    /**
     * Metoden finner først førstePostorden, for deretter å
     * gå inn i en while løkke som finner nestePostorden og skriver ut disse i postorden
     * frem til noden sin verdi er null.
     *
     * @param oppgave som skriver ut treet.
     */
    public void postorden(Oppgave<? super T> oppgave) {

        if(tom()) return;

        Node<T> p = førstePostorden(rot); //setter nodepeker p til første postorden

        //Så lenge nestePostorden ikke er null, skrives p.verdi ut
        // og p blir endret til nestePostorden.
        while (nestePostorden(p) != null) {
            oppgave.utførOppgave(p.verdi);
            p = nestePostorden(p);
        }

        //p.verdi skrives ut
        oppgave.utførOppgave(p.verdi);
    }

    /**
     * Metodene har et rekursivt kall som traverserer treet i postorden rekkefølge.
     *
     * @param oppgave som skriver ut treet.
     */
    public void postordenRecursive(Oppgave<? super T> oppgave) {

        postordenRecursive(rot, oppgave);
    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {

        //Basistilfellet
        if (p == null) return;

        //Traverserer binærtreet etter postorden

        //Rekursivt kall for venstre barn
        postordenRecursive(p.venstre, oppgave);
        //Rekursivt kall for høyre barn
        postordenRecursive(p.høyre, oppgave);
        //Utfører oppgaven på denne noden
        oppgave.utførOppgave(p.verdi);
    }

    /**
     * Metoden serialize går iterativt gjennom et binærtre i nivåorden og legger verdier inn i en ArrayList
     * fortløpende.
     * @return ArrayList med verdier sortert i nivåorden
     */
    public ArrayList<T> serialize() {

        ArrayList<T> toList = new ArrayList<>();
        toList.add(rot.verdi);


        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(rot);

        Node<T> current;

        while (!queue.isEmpty()) {
            current = queue.remove();
            if (current.venstre != null) {
                toList.add(current.venstre.verdi);
                queue.add(current.venstre);
            }
            if (current.høyre != null) {
                toList.add(current.høyre.verdi);
                queue.add(current.høyre);
            }
        }
        return toList;
    }

    /**
     * Metoden deserialize legger verdier fra en ArrayList med generiske verdier, K, inn i en ArrayList
     * for å gjenskape et BST i nivåorden.
     * @param data verdiene fra ArrayList som blir lagt inn som noder i treet.
     * @param c Comparator brukes til å sortere verdiene i nivåorden
     * @param <K> ArrayListen med generiske verdier vi skal gjøre om til et BST.
     * @return Binærtreet.
     */
    static <K> SBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {

        /**
         * Deserialize skal da ta dette
         * arrayet, og legge inn alle verdiene (igjen i nivå orden), og dermed gjenskape treet.
         */

        //ALT 1
        //Legger verdi på indeks en inn i rot-node
        //Legger neste indeks inn i rot-node.venstre barn
        //Legger neste indeks inn i rot-node.høyre barn

        //oppretter tre
        SBinTre<K> tre = new SBinTre<>(c);


        for (int i = 0; i < data.size(); i++) {
            tre.leggInn(data.get(i));
        }

        //Hvordan returnere treet?
        return tre;

    }

}
