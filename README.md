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