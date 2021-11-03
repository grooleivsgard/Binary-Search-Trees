# Obligatorisk oppgave 3 i Algoritmer og Datastrukturer

Denne oppgaven er en innlevering i Algoritmer og Datastrukturer. 
Oppgaven er levert av følgende student:
* Gro Oleivsgard, s344222, s344222@oslomet.no


# Oppgavebeskrivelse

I oppgave 1 så gikk jeg frem ved å først sjekke om treet er tomt allerede ved rotnoden
Dersom treet er tomt, kan en ny node legges inn her, med null-peker til forelder. Hvis ikke
flyttes en node-peker p gjennom treet så lenge p ikke er lik null. Dette gjøres
i samarbeid med en q-peker, slik at p alltid har referanse til forelderen sin. Metoden comp.compare
blir brukt til å sammenligne Generics-verdier. Når pekeren til slutt når en null-verdi, sjekkes
det om verdien skal bli lagt til som høyre eller venstre barn, og antall blir til slutt øket med 1
når dette er fullført.

I oppgave 2 så brukte jeg en while-løkke, liknende løkken i inneholder(), til å løpe gjennom løkken for å finne duplikater av en verdi. 
Så fort verdien vi leter etter er funnet, økes antall og node-pekeren p flyttes mot høyre, da det er i denne retningen
en evt lik verdi må ligge. Løkken fortsetter slik til node-peker p er null, dvs på enden av grenen duplikater
skulle ha ligget.

I oppgave 3 brukte jeg pseudokoden fra 5.1.7 h) i kompendiet for å finne første postorden. Her blir en while-løkke
brukt til å traversere mot venstre gjennom treet, og første postorden blir funnet ved første bladnode som ikke har
noen venstre eller høyre barn. For å finne neste postorden, har jeg tatt utgangspunkt i følgende tilfeller, som beskrevet 
i kompendiet, kap. 5.1.7:
- Hvis p ikke har en forelder, er p rotnoden og også den siste noden i postorden.
- Hvis p er høyre barn til sin forelder, er forelder f neste postorden.
- Hvis p er venstre barn til sin forelder, gjelder:
  - Hvis p er enebarn (p.forelder.høyre er null), er forelder neste postorden.
  - Hvis p ikke er enebarn (p.forelder.høyre er ikke null), er det høyre barnet til 
    forelderen (p.forelder.høyre) den neste postorden.

I oppgave 4, for den ikke-rekursive metoden, startet jeg ved rot-noden og flyttet herfra en peker-node p gjennom 
binærtreet i postorden rekkefølge ved hjelp av en while-løkke, frem til siste node, roten, er nådd. For de rekursive
metodene traverserte jeg binærtreet i postorden rekkefølge ved å kalle rekursivt på venstre og høyre barn, og til slutt
skrive ut verdiene med utførOppgave. Basistilfellet er satt til når pekeren p blir null.

I oppgave 5, for metoden serialize(), opprettet jeg både en ArrayList og en kø. Køen ble brukt til å flytte current-pekeren til den neste noden i treet i nivårorden etterhvert som hver node ble lagt inn i ArrayListen og dermed fjernet fra køen. Dette while-løkken foregår så lenge køen ikke er tom, noe den vil være når det ikke er flere noder igjen. I metoden deserialize(), blir verdiene fra en ArrayList lagt inn i et tomt binærtre ved hjelp av en for-løkke og metoden leggInn(). 
