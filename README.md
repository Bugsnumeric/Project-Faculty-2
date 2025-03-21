# Project-Faculty-2
Al doilea proiect OOP
In cuprinsul proiectului se regasesc sase comenzi (pe care le voi explica mai jos):

    -> add runway in use
    -> allocate plane
    -> flight info
    -> runway info
    -> permission maneuver
    -> exit

Clasele implementate:
1. Airplane are definite campuri ce desemneaza informatia introdusa de utilizator pentru un avion si un LinkedList de avioane;
   este declarata o metoda toString ce afiseaza informatii despre avion, ceea ce are mai special aceasta metoda este ultima parte cu timpul,
   aceasta afiseaza timpul in ore, minute si secunde, o declaratie explicita intrucat implicit se afiseaza doar ora si minutul pentru
   o variabila declarata cu LocalTime (hh:mm); HH:mm:ss specifica si fusul orar care este unul de 24h (HH), 12h (hh, pentru 12AM, respectiv 12PM).
   Sunt implementate si doua metode, una verifica daca ID-ul introdus de utilizator corespunde cu un avion din lista (check_ID_airplane), avand ca parametrii
   o lista ce poate avea ca parinte (upper bound) Airplane si ID-ul utilizatorului pentru a cauta in lista introdusa; a doua metoda este
   (status_setter) care verifica statusul unui avion si il modifica corespunzator in "DEPARTED" daca are "WAITING_FOR_TAKEOFF" sau "LANDED" daca are "WAITING_FOR_LANDING",
   ca parametrii avem o lista declarata ca mai sus cu un upper bound, un parametru de tip boolean care ne spune daca avionul este pentru takeoff sau nu,
   ID-ul avionului si un **string** al timpului pentru a fi setat ulterior si afisat.
   In aceasta clasa se afla o clasa interioara pe care am folosit-o pentru a implementa un design pattern de tip builder, singurul camp
   optional fiind "urgent", avand declaratie implicita not_urgent;
   In aceasta clasa se gaseste si **metoda de comparare** ce ordoneaza crescator dupa timp sau dupa urgenta avionului, avand prioritate sporita daca este urgent.
2. Runway ca si la airplane sunt definite campurile ce contin informatii despre datele introduse de utilizator pentru pista
   si doua ArrayList pentru pistele takeoff, respectiv landing; Este implementata o metoda ce verifica daca exista ID-ul intr-o astfel de lista (check_ID_runway);
   Implementarea claselor de exceptie se regasesc in aceasta clasa **IncorrectRunwayException**, cu un constructor ce suprascrie clasa de baza,
   metoda **verification_runway** care verifica daca ID-ul ultimului avion introdus se regaseste in lista de runway_takeoff in cazul in care destinatia este Bucuresti,
   respectiv cu runway_landing in cazul in care locatia de inceput este Bucuresti, am mers pe o implementare de tipul **se gaseste elementul? atunci throw**
   in defavoarea unei implementari care sa-mi spuna **daca** se gaseste elementul in lista;
   **UnavailableRunwayException** cu implementarea metodei **verification_maneuver** verifica daca timpul este mai mic-egal (<=) cu 5 minute
   in cazul in care e de "takeoff" sau 10 in caz de "landing", implementarea se bazeaza pe faptul ca stiu dinainte in ce runway ma aflu prin
   parametrul, declarat in metoda, de tip boolean;
3. Clasele NarrowAirplane si WideAirplane sunt asemanatoare si scurte, le voi aborda impreuna; fiecare are implementat un LinkedList corespunzator, iar metodele
   sunt declarate dinainte in clasa parinte, care o extind fiecare, tot ce e diferit e metoda toString care specifica tipul avionului inainte de a apela super();
   Tot aici se regasesc si getter si setter pentru timp si status, cu super declarate ambele;

Am declarat mai multe variabile, campuri, metode cu protected pentru vizibilitate la nivel de package, respectiv private doar pentru nivelul clasei,
majoritatea au si **static** inainte de declarare astfel incat sa-mi fie mai rapid la implementare (private nu poate fi vazut decat daca implementam un get);

La nivelul metodei main am deschis fisierele pentru citire/scriere astfel incat sa fie vizibile ulterior in fiecare comanda, doar la comanda runway_info s-a deschis la nivel local cate un fisier
pentru fiecare nou comanda data de tipul runway info;
**protected static int aux_airplane = 0;** este declarat astfel incat sa-si modifice valoarea intr-o metoda ulterior apelata si a lucra
in respectivul LinkedList cu elementul situat la acest index auxiliar; In acelasi mod am lucrat cu **private static LocalTime time_maneuver_takeoff**
si **private static LocalTime time_maneuver_land**, dar acestea lucreaza doar la nivel de clasa spre deosebire de indexul precedent care e la nivel de package vizibil,
acestea din urma isi schimba valoarea in functie de tipul pistei pe care se da permission for maneuver, in cazul in care e valid se modifica valoarea si se verifica cu una
ulterior introdusa, pentru a da check la runway si a trata exceptia in caz de eroare;

1. Pentru aceasta comanda de "add runway in use" se verifica daca tipul pistei este de "takeoff" sau de "landing"
   si se adauga intr-un ArrayList conform specificatiilor date;
2. Allocate plane se ocupa de inserarea unui avion intr-o astfel de lista, pentru constructia avionului am folosit
   un design pattern de tip builder, ramura de "urgent" fiind tratata ca optional si avand declarata implicit "not_urgent"
   indicand ca nu este urgent, in cazul in care se aloca un avion ce contine "urgent" atunci se seteaza urgent in locul lui "not_urgent"; Ulterior verific
   si tipul avionului daca este "wide" sau "narrow" pentru a-l introduce intr-o lista cu acest tip de avioane pentru rezolvarea
   task-urilor ulterioare;
3. Flight info scrie in fisier detalii despre un anumit avion verificand ID-ul primit ca input, in cazul in care avionul este "DEPARTED" sau "LANDED" se dau informatii corespunzatoare (cand a fost data comanda) si apoi
   se iese din switch prin break, in caz contrar se ofera informatii fara a fi precizat timpul comenzii (intrucat nu a fost data sau era eronata);
4. Runway info ofera informatii despre viitoare zboruri/aterizari si specifica daca pista este libera sau ocupata,
   la un moment de timp daca se da comanda aceasta si nu a trecut timpul minim de asteptare pentru o pista takeoff (5 minute) sau
   landing (10 minute) se scrie occupied, free in caz contrar; **in aceasta comanda se folosesc acele variabile declarate anterior pentru timpi**.
5. Permission for maneuver este o comanda simpla intrucat majoritatea implementarii am facut-o prin metode definite mai sus in clase, se verifica pentru exceptie,
   daca nu se arunca exceptia atunci se schimba si valoarea time_maneuver_takeoff daca pista e de takeoff, in caz contrar pentru cealalta pista de landing, la final
   seteaza un nou status pentru primul element din lista respectiva, lista mereu va fi ordonata dupa timp sau dupa campul "urgent", cel din urma avand prioritate;
6.  Exit este comanda care iese din program resetand toate valorile.
