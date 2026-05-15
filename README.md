# SISTEM ZA PODRŠKU ODLUČIVANJU PRI POLETANJU AVIONA

### Članovi tima
Luka Vuković - SV37/2022

### Motivacija

Avio-saobraćaj predstavlja jedan od najkompleksnijih logističkih sistema savremenog doba. Svaki dan, hiljade letova prolaze kroz globalne aerodromske mreže, pri čemu svaki let zavisi od skupa međusobno zavisnih faktora: vremenskih uslova, tehničke ispravnosti aviona, raspoloživosti piste, posade i zemaljsko osoblja. Kašnjenja imaju dalekosežne posledice, poput finansijkih gubitaka za avio-kompanije ili lošeg iskustva putnika.

Operativni centri aerodroma se oslanjaju na kombinaciju iskustva dispečera, parcijalnih softverskih alata i ručnih procedura. Ovakav pristup je podložan ljudskim greškama i sporoj reakciji na brze promene uslova.

Motivacija za ovaj projekat je razvoj ekspertskog sistema koji modeluje znanje iskusnog operatora aerodromskog dispečerskog centra i primenjuje ga automatizovano i konzistentno, uz fokus na kritične situacije.

### Pregled problema

Osnovni problem koji se rešava je automatizovano donošenje operativnih odluka u avio-saobraćaju na osnovu velikog broja međusobno zavisnih faktora. Fokus sistema je na:
- Proceni rizika leta na osnovu vremenskih uslova, tehničkog stanja aviona i stanja infrastrukture aerodroma
- Detekciji kritičnih situacija kroz praćenje toka događaja u realnom vremenu
- Generisanju preporuke akcije
- Odgovaranju na upite poput: "Koje uslove let mora da ispuni da bi poleteo?"

#### Pregled postojećih rešenja

Postojeći sistemi se mogu svrstati u nekoliko kategorija:

| Kategorija | Opis | Nedostaci |
| :--- | :--- | :--- |
| FIDS sistemi | Prikazuju status letova putnicima i osoblju | Samo evidencija, bez podrške odlučivanju |
| Meteorološki sistemi | Pružaju vremenske prognoze i upozorenja | Nisu integrisani sa operativnim sistemima |
| AODB sistemi | Centralizovane baze podataka aerodroma | Reaktivni, ne donose proaktivne odluke |
| AI/ML sistemi | Prediktivna analiza kašnjenja zasnovana na istorijskim podacima | Slaba interpretablinost, ne podržavaju eksplicitna pravila struke |

Prednost predloženog rešenja leži u kombinaciji eksplicitno definisanog domenskog znanja (pravila struke), višeslojnog rezonovanja i sposobnosti detekcije kritičnih obrazaca u realnom vremenu.

### Metodologija rada
Sistem će biti implementiran u programskom jeziku Java, uz korišćenje Drools radnog okvira. Postoje dva tipa ulaza u sistem: statički podaci o letovima i infrastrukturi, kao i dinamički podaci simuliranih senzora u realnom vremenu.

### Ulazi u sistem

#### Statički podaci

- Podaci o letu
    - Broj leta
    - Ruta
    - Tip aviona
    - Kapacitet
    - Planiran odlazak
    - Planiran dolazak
    - Kategorija leta
    - Postoji zamena aviona
    - Postoji zamena posade
- Podaci o avionu
    - Starost
    - Datum narednog obaveznog servisa
    - Sati leta od servisa
    - Ciklusi od servisa 
    - Sati leta ukupno 
- Podaci o aerodromu
    - Ukupan broj pista
    - Broj raspoloživih pista
    - Broj slobodnih gate-ova
    - Kapacitet aerodroma
    - Smer piste
    - Dužina piste
    - Postojanje LVTO kapaciteta
    - Postojanje LVTO dozvole
    - Posebno odobrenje
- Podaci o posadi
    - Kompletnost posade
    - FDP (Flight Duty Period: vreme od javljanja na dužnost, u satima)
    - Odmor pre leta
    - Sektori danas        
    - Noćna dužnost

#### Dinamički podaci

- Vremenski izveštaji
    - Brzina vetra
    - Smer vetra
    - Crosswind
    - Tailwind
    - Vidljivost
    - Temperatura
    - Vrsta padavina
    - Intenzitet padavina
    - Pojava leda
    - Rosište
- Status piste
    - Status (otvorena, zatvorena, u održavanju)
    - RWYCC (integer, 0-6)
    - Broj pisti u odledjivanju
    - Odledjivanje obavljeno
- Tehnički alarmi aviona
    - ID aviona
    - Tip alarma
    - Ozbiljnost (nizak, srednji, visok, kritičan)
    - Komponenta
    - Vreme prijave alarma
    - Status alarma
    - MEL kategorija (A, B, C, D, NIJE_NA_MEL)
- Status leta (na vreme, kasni n minuta, ukrcan, poleteo, sleteo, preusmeren)

### Izlazi iz sistema

- Procena rizika leta: NIZAK, SREDNJI, VISOK, KRITIČAN
- Preporuka akcije: POLETI NA VREME, ODLOŽI, PREUSMERI, OTKAŽI
- Spisak neispunjenih uslova za poletanje (backward chaining)
- Obaveštenje o tipu i uzroku kašnjenja
- Izveštaj o ukupnom broju pogođenih letova i putnika (accumulate)
- CEP alarmi: upozorenje o kritičnom pogoršanju vremena, upozorenje o tehničkim problemima na avionu

### Baza znanja

Baza znanja sistema organizovana je kroz tri nivoa forward chaining rezonovanja, uz CEP modul, template mehanizam i backward chaining upit.

#### Nivo 1 - Detekcija nepravilnosti (analiza sirovih podataka)

Na prvom nivou se vrši evaluacija ulaznih podataka i generisanje primarnih zaključaka:

- Vremenski uslovi
```
IF tailwind > 19 km/h THEN RepniVetar
IF crosswind > 65 km/h THEN KriticanBocniVetar
IF crosswind > 28 km/h THEN BocniVetar
IF vidljivost < 550m THEN NiskaVidljivost
IF vidljivost < 400m THEN KriticnaVidljivost
IF vidljivost < 75m  THEN NulaVidljivost
IF temperatura < 0°C AND (kiša OR sneg OR magla) THEN RizikOdLeda
IF temperatura < 0°C AND rosište <= temperatura THEN RizikOdMraza
IF pojavaLeda == true THEN AktivnoLedjenje
IF temperatura < -25°C THEN EkstremnaHladnoca
IF temperatura < -40°C THEN KriticnaHladnoca
IF vrstaPadavina == LEDENA_KISA THEN LedenaKisa
IF vrstaPadavina == GRAD THEN Grad
IF vrstaPadavina == LEDENE_KUGLICE THEN LedeneKuglice
IF vrstaPadavina == SNEG AND intenzitet == JAK THEN JakSneg
IF vrstaPadavina == KISA AND intenzitet == JAK THEN JakaKisa
IF intenzitet == JAK THEN JakePadavine
```

- Tehnički alarmi aviona
```
IF alarm.ozbiljnost == VISOK THEN OzbiljanAlarm
IF alarm.ozbiljnost == KRITIČAN THEN KriticanAlarm
IF alarm.melKategorija == A THEN MelKategorijaA
IF alarm.melKategorija == NIJE_NA_MEL AND alarm.ozbiljnost != NIZAK THEN NepredvidjenKvar
```

- Podaci o avionu
```
IF satiLetaOdServisa > 600 OR ciklusiOdServisa > 300 THEN ACheckPrekoracen
IF satiLetaOdServisa > 500 OR ciklusiOdServisa > 250 THEN BlizuAChecka
IF datumNarednogServisa < danas THEN KalendarskiServisPrekoracen
IF starost > 25 THEN StarAvion
IF starost > 20 AND datumNarednogServisa < danas + 30 THEN AvionBlizuServisa
```

- Posada
```
IF kompletnostPosade == false THEN NekompletnaPostada
IF odmorPredelta < 12h THEN NedovoljnoOdmoran 
IF nocnaDuznost == false AND fdp > 10h THEN PosadaUmorna
IF nocnaDuznost == true AND fdp > 9h THEN PosadaUmorna
IF nocnaDuznost == false AND fdp > 13h THEN PosadaPrekoracilaNormu
IF nocnaDuznost == true AND fdp > 11h  THEN PosadaPrekoracilaNormu
IF sektoriDanas >= 6 THEN PosadaPreopterecena
```

- Infrastruktura aerodroma
```
IF pista.status == ZATVORENA OR pista.status == U_ODRZAVANJU THEN PistaNedostupna
IF pista.rwycc <= 1 THEN PistaKriticna                          // led, praktično zatvorena
IF pista.rwycc >= 2 AND pista.rwycc <= 3 THEN PistaRizicna      // sneg/bljuzgavica
IF pista.rwycc == 4 THEN PistaOtezana                           // nabijeni sneg, oprez
IF pista.rwycc >= 5 THEN PistaNormalna                          // mokra ili suva
IF slobodniGateovi == 0 THEN NemaGateova
IF slobodniGateovi <= 2 THEN MaloGateova
IF raspolozivePiste == 1 THEN SamoPistaJedna
IF (raspolozivePiste / ukupnePiste) < 0.5 THEN KriticanBrojPista
```

#### Nivo 2 - Procena stanja sistema (agregacija zaključaka)

Na osnovu primarnih zaključaka sa nivoa 1, generiše se status pojedinih sistema:
```
IF (KriticanBocniVetar OR RepniVetar) AND KriticnaVidljivost THEN LošiVremenskiUslovi
IF LošiVremenskiUslovi AND RizikOdLeda THEN EkstremniUslovi
IF RizikOdMraza AND NOT OdledjivanjeObavljeno THEN PovrsineKontaminirane
IF EkstremnaHladnoca AND RizikOdLeda THEN NemaHoldoverTime
IF KriticnaHladnoca THEN MotoriNisuSpremni
IF LedenaKisa OR Grad OR LedeneKuglice THEN NemaHoldoverTime
IF JakSneg THEN NemaHoldoverTime
IF NemaHoldoverTime THEN PoletanjeZabranjeno

IF BocniVetar AND (JakePadavine OR AktivnoLedjenje) THEN KriticniVremenskiUslovi
IF NiskaVidljivost AND (NOT lvtoKapacitet OR NOT lvtoOdobrenje) THEN PoletanjeZabranjeno
IF KriticnaVidljivost AND NOT PosebnoOdobrenje THEN PoletanjeZabranjeno
IF NulaVidljivost THEN PoletanjeZabranjeno

IF PistaNedostupna OR PistaKriticna THEN PistaProblem
IF PistaRizicna AND BocniVetar THEN PistaProblem
IF PistaOtezana AND (JakSneg OR AktivnoLedjenje) THEN PistaProblem
IF pista.duzina < kategorijaLeta.minDuzinaPiste THEN PistaProblem

IF OzbiljanAlarm AND KriticanAlarm THEN TehnickiProblem
// vremeDoLeta se izračunava kao razlika između trenutnog vremena i planiranOdlazak u Java sloju
IF TehnickiProblem AND (vremeDoLeta < 72h) THEN AvionNijeSpreman
IF MelKategorijaA OR NepredvidjenKvar THEN AvionNijeSpreman

IF ACheckPrekoracen OR KalendarskiServisPrekoracen THEN ServisPrekoracen
IF ServisPrekoracen OR (StarAvion AND AvionBlizuServisa) THEN AvionTehničkiRizičan
IF BlizuAChecka AND StarAvion THEN AvionTehničkiRizičan

IF MaloGateova AND KriticanBrojPista THEN AerodromPodPritiskom
IF AvionTehničkiRizičan AND LošiVremenskiUslovi THEN VisokOperativniRizik

IF PosadaUmorna AND (LošiVremenskiUslovi OR PistaProblem) THEN BezbednostUgrozena
IF NekompletnaPostada OR PosadaPrekoracilaNormu OR NedovoljnoOdmoran THEN PostadaNijeSposobna
```

#### Nivo 3 - Donošenje finalne odluke

Na osnovu primarnih zaključaka sa nivoa 2, generiše se preporuka akcije:
```
IF EkstremniUslovi AND PistaProblem THEN Preporuka: OTKAŽI
IF LošiVremenskiUslovi AND NOT PistaProblem THEN Preporuka: ODLOŽI
IF AvionNijeSpreman AND PostojiZamena THEN Preporuka: ODLOŽI
IF AvionNijeSpreman AND NOT PostojiZamena THEN Preporuka: OTKAŽI
IF NOT problemi THEN Preporuka: POLETI NA VREME
IF PostadaNijeSposobna AND NOT PostojiZamenaPosade THEN Preporuka: OTKAŽI
IF PostadaNijeSposobna AND PostojiZamenaPosade THEN Preporuka: ODLOŽI
IF BezbednostUgrozena THEN Preporuka: OTKAŽI
IF AerodromPodPritiskom AND LošiVremenskiUslovi THEN Preporuka: PREUSMERI
IF VisokOperativniRizik THEN Preporuka: ODLOŽI
IF EkstremnaHladnoca AND NOT AktivnoLedjenje THEN Preporuka: ODLOŽI (procedura odleđivanja)
IF KriticniVremenskiUslovi THEN Preporuka: OTKAŽI
IF PovrsineKontaminirane THEN Preporuka: ODLOŽI (odleđivanje obavezno)
IF NemaHoldoverTime THEN Preporuka: OTKAŽI
IF MotoriNisuSpremni THEN Preporuka: OTKAŽI
IF PoletanjeZabranjeno THEN Preporuka: OTKAŽI
```

Napomena: U slučaju konflikta između preporuka, sistem primenjuje hijerarhiju prioriteta putem Drools salience mehanizma:
```
OTKAŽI (salience 100) > ODLOŽI (salience 50) 
                      = PREUSMERI (salience 50) 
                      > POLETI NA VREME (salience 10)
```
Pravila nižeg prioriteta se aktiviraju samo ako nije generisana preporuka višeg prioriteta.

#### CEP - Complex Event Processing

CEP modul prati tok događaja u realnom vremenu i detektuje obrasce koji zahtevaju hitnu reakciju. Podaci se šalju kao kontinuiran stream iz senzora i sistema aerodroma.

- CEP 1: Brzo pogoršanje vremenskih uslova
```
IF postoje dva merenja VremenskiIzvestaj unutar 20 minuta
AND (merenje2.brzinVetra - merenje1.brzinVetra > 30 km/h)
AND merenje2.vidljivost < 800m
AND merenje2.temperatura < 0°C
THEN KriticnoPogorsavanjeVremena
AND ALARM: Zamrzni sva poletanja
```

- CEP 2: Serija tehničkih alarma na istom avionu
```
IF COUNT(alarm.NIZAK)   >= 5
OR COUNT(alarm.SREDNJI) >= 3
OR COUNT(alarm.VISOK)   >= 2
WITHIN 15 minuta
THEN TehnickiIncident(avionId)
AND ALARM: Povuci avion iz saobraćaja
```

#### Accumulate funkcija

Accumulate se koristi za agregaciju podataka iz radne memorije:

- Ukupno kašnjenje i broj pogođenih letova danas
```
accumulate (
    // datumPoletanja = planiranOdlazak.toDate()
    Flight(status == KASNI, datumPoletanja == danas);
    $ukupnoMin : sum(minutaKasnjenja),
    $brojLetova : count(1)
)
```

- Ukupan broj pogođenih putnika
```
accumulate (
    Flight(status == OTKAZAN OR minutaKasnjenja > 120);
    $putnici : sum(brojPutnika)
)
```

- Prosečna vidljivost u poslednjih sat vremena
```
accumulate (
    VremenskiIzvestaj() over window:time(1h);
    $prosek : average(vidljivost)
)
```

#### Template mehanizam

Template mehanizam se koristi za definisanje pragova prihvatljivosti za različite kategorije letova. Svaka kategorija ima specifične granice za vremenske uslove i toleranciju kašnjenja.
```
template header
    kategorijaLeta, maxCrossWind, maxTailWind, minVidljivost, minDuzinaPiste, maxKasnjenje, prioritet
end template
```
Primer korišćenja:

| kategorijaLeta | maxCrossWind | maxTailWind | minVidljivost | minDuzinaPiste | maxKasnjenje | prioritet |
| :--- | ---: | ---: | ---: | ---: | ---: | ---: |
| SCHEDULED_COMMERCIAL | 65 | 19 | 550 | 1800 | 120 | 1 |
| BUSINESS_AVIATION | 55 | 19 | 550 | 1500 | 60 | 2 |
| CARGO | 65 | 19 | 550 | 1800 | 240 | 3 |
| GENERAL_AVIATION | 28 | 14 | 4800 | 800 | 60 | 4 |

Takođe se koristi i za generisanje pravila za proveru statusa piste i određivanja da li su poletanje i sletanje dozvoljena, koji je maksimalni dozvoljeni vetar. Na primer, za status UOdrzavanju, poletanje nije dozvoljeno.

```
template header
    statusPiste, dozvoljenoPoletanje,
    maxCrosswind, prioritetCiscenja
end template
```
Primer korišćenja:

| statusPiste | dozvoljenoPoletanje | maxCrosswind | prioritetCiscenja |
| :--- | :---: | ---: | ---: |
| Otvorena | true | 65 | 0 |
| UOdrzavanju | false | 0 | 1 |
| Zatvorena | false | 0 | 2 |

Template ispod definiše operativne procedure za svaki nivo ozbiljnosti alarma. Vrednost maxDozvoljenihAlarma direktno se koristi u CEP modulu. Kada broj aktivnih alarma istog nivoa u definisanom vremenskom prozoru premaši dozvoljeni maksimum, sistem generiše eskalaciju.

```
template header
    ozbiljnost, maxDozvoljenihAlarma, rokReakcijeMin,
    blokirajPoletanje, zahtevajServis
end template
```

Primer korišćenja:

| ozbiljnost | maxDozvoljenihAlarma | rokReakcijeMin | blokirajPoletanje | zahtevajServis |
| :--- | ---: | ---: | :---: | :---: |
| Nizak | 5 | 60 | false | false |
| Srednji | 3 | 30 | false | true |
| Visok | 2 | 10 | true | true |
| Kritican | 1 | 5 | true | true |

#### Backward chaining

Backward chaining se koristi za odgovaranje na upite tipa "Šta je potrebno da let poleti?". Sistem unazad traži koji uslovi nisu ispunjeni:
```
// Glavni uslovi za poletanje
insert(new Uslov("poletanjeDozvoljeno", "vremeDozvoljava"));
insert(new Uslov("poletanjeDozvoljeno", "pistaSpremna"));
insert(new Uslov("poletanjeDozvoljeno", "avionSpreman"));
insert(new Uslov("poletanjeDozvoljeno", "posadaSposobna"));

// Pod-uslovi vremenskih uslova
insert(new Uslov("vremeDozvoljava", "vetarPrihvatljiv"));
insert(new Uslov("vremeDozvoljava", "vidljivostPrihvatljiva"));
insert(new Uslov("vremeDozvoljava", "nemaAktivnogLedenja"));
insert(new Uslov("vremeDozvoljava", "padavineBezbedne")); 

// Pod-uslovi piste
insert(new Uslov("pistaSpremna", "pistaNijeZatvorena"));
insert(new Uslov("pistaSpremna", "pistaRwyCCPrihvatljiv"));
insert(new Uslov("pistaSpremna", "pistaDovoljnaDuzina"));

// Pod-uslovi aviona
insert(new Uslov("avionSpreman", "nemaKriticnihAlarma"));
insert(new Uslov("avionSpreman", "melKategorijaOk"));
insert(new Uslov("avionSpreman", "servisNijePrekoracen"));

// Pod-uslovi posade
insert(new Uslov("posadaSposobna", "posadaKompletna"));
insert(new Uslov("posadaSposobna", "fdpNijePrekoracen")); 
insert(new Uslov("posadaSposobna", "odmorDovoljan"));
```
```
// Vetar
UslovIspunjen("vetarPrihvatljiv", crosswind < kategorijaLeta.maxCrossWind AND tailwind < kategorijaLeta.maxTailWind)

// Vidljivost
UslovIspunjen("vidljivostPrihvatljiva", vidljivost >= kategorijaLeta.minVidljivost)

// Led
UslovIspunjen("nemaAktivnogLedenja", !AktivnoLedjenje AND !RizikOdMraza)

// Padavine
UslovIspunjen("padavineBezbedne", !LedenaKisa AND !Grad AND !LedeneKuglice AND !JakSneg)

// Pista
UslovIspunjen("pistaNijeZatvorena", !PistaNedostupna)
UslovIspunjen("pistaRwyCCPrihvatljiv", !PistaKriticna AND !PistaRizicna)
UslovIspunjen("pistaDovoljnaDuzina", pista.duzina >= kategorijaLeta.minDuzinaLete)

// Avion
UslovIspunjen("nemaKriticnihAlarma", !TehnickiProblem)
UslovIspunjen("melKategorijaOk", !MelKategorijaA AND !NepredvidjenKvar)
UslovIspunjen("servisNijePrekoracen", !ServisPrekoracen)

// Posada
UslovIspunjen("posadaKompletna", !NekompletnaPostada)
UslovIspunjen("fdpNijePrekoracen", !PosadaPrekoracilaNormu AND !PosadaPreopterecena)
UslovIspunjen("odmorDovoljan", !NedovoljnoOdmoran)
```
```
query "neispunjeniUslovi"(String $roditelj, String $dete)

    // Bazni slučaj: $roditelj direktno zahteva $dete, i $dete nije ispunjeno
    Uslov($roditelj, $dete;)
    and UslovIspunjen($dete, false;)

    or

    // Rekurzivni slučaj: $roditelj zahteva $srednji, a unutar $srednji postoji neki neispunjeni uslov $dete — pozivamo se rekurzivno
    (Uslov($roditelj, $srednji;)
    and neispunjeniUslovi($srednji, $dete;))

end
```

Sistem automatski utvrđuje koji od navedenih uslova nisu ispunjeni i vraća objašnjenje zašto let ne može da poleti.

### Primer rezonovanja

Scenario: Let JU-301, Beograd → London, 14:00, SCHEDULED_COMMERCIAL

- Ulazi:
    - Crosswind: 60 km/h
    - Tailwind: 22 km/h
    - Vidljivost: 380m
    - Temperatura: -3°C
    - Rosište: -3°C
    - Vrsta padavina: KISA
    - RWYCC: 3
    - LVTO kapacitet: false
    - Posebno odobrenje: false
    - FDP posade: 12h
    - Noćna dužnost: false
    - Alarm: SREDNJI (sistem goriva), SREDNJI (hidraulika), SREDNJI (elektrika) — u poslednjih 15 minuta

- Nivo 1
    - INSERT RepniVetar (22 > 19)
    - INSERT KriticnaVidljivost (380 < 400)
    - INSERT NiskaVidljivost (380 < 550)
    - INSERT RizikOdLeda (-3°C + kiša)
    - INSERT RizikOdMraza (rosište == temperatura)
    - INSERT PistaRizicna (RWYCC == 3)
    - INSERT PosadaUmorna (nocnaDuznost == false AND fdp > 10h)
    - INSERT BocniVetar (60 > 28)

- Nivo 2
    - RepniVetar AND KriticnaVidljivost 
        -> INSERT LošiVremenskiUslovi
    - LošiVremenskiUslovi AND RizikOdLeda 
        -> INSERT EkstremniUslovi
    - RizikOdMraza AND NOT odledavanjeObavljeno 
        -> INSERT PovrsineKontaminirane
    - NiskaVidljivost AND (NOT lvtoKapacitet) 
        -> INSERT PoletanjeZabranjeno
    - PistaRizicna AND BocniVetar
        -> INSERT PistaProblem
    - PosadaUmorna AND LošiVremenskiUslovi 
        -> INSERT BezbednostUgrozena

- Nivo 3
    - EkstremniUslovi AND PistaProblem 
        -> Preporuka: OTKAŽI
    - PovrsineKontaminirane 
        -> Preporuka: ODLOŽI (odleđivanje obavezno)
    - PoletanjeZabranjeno 
        -> Preporuka: OTKAŽI
    - BezbednostUgrozena 
        -> Preporuka: OTKAŽI
    // Finalna odluka: OTKAŽI — više konkurentnih razloga

- CEP #1
    - Nema podataka o brzom pogoršanju u ovom scenariju

- CEP #2
    - U poslednjih 15 minuta:
        - Alarm SREDNJI (sistem goriva)
        - Alarm SREDNJI (hidraulika)
        - Alarm SREDNJI (elektrika)
    - COUNT(alarm.SREDNJI) >= 3
    - TehnickiIncident(JU-301)
    - ALARM: Povuci avion iz saobraćaja

- Accumulate
    - JU-301 kasni, datumPoletanja == danas
    - Ukupno pogođenih letova: 1 (i ostali koji kasne danas)
    - Putnici JU-301 pogođeni: npr. 180
    - Ukupno kašnjenje: raste

- Backward chaining
    - Upit: "Šta JU-301 treba da poleti?"
    - Neispunjeni uslovi:
        - vetarPrihvatljiv: tailwind mora < 19 km/h
        - vidljivostPrihvatljiva: vidljivost mora >= 550m
        - nemaAktivnogLedenja: prisutan RizikOdMraza
        - pistaRwyCCPrihvatljiv: RWYCC mora biti >= 4
        - nemaKriticnihAlarma: CEP detektovao tehnički incident (3 SREDNJA alarma u 15 minuta)
        - fdpNijePrekoracen: FDP > 10h (posada umorna)

- Izlaz
    - Finalna odluka: LET OTKAZAN
    - Razlozi: ekstremni vremenski uslovi, problem sa pistom, tehnički incident, bezbednost posade ugrožena
    - Obavešteni putnici
    - Inicirana procedura preusmeravanja

### Izvori za postojeće sisteme:

- FIDS
    - [Wikipedia](https://en.wikipedia.org/wiki/Flight_information_display_system)
    - [TAV Technologies (primer)](https://tavtechnologies.aero/en-EN/products/airport-operations/pages/fids-products-services)
- AODB
    - [Copenhagen Optimization](https://copenhagenoptimization.com/blog/what-is-an-airport-operational-database-aodb)
    - [Amadeus (primer)](https://amadeus.com/en/travel-glossary/airport-operational-database)
- Meteorološki sistemi
    - [ResearchGate](https://www.researchgate.net/publication/228455668)
- AI/ML sistemi
    - [BILSTM rad](https://commons.erau.edu/jaaer/vol32/iss2/4/)

### Izvori za pravila:

- ICAO Annex 6, Part I — Operational minima: https://www.howtoflyairplanes.com/blogs/ifr/ifr-planning-minimas
- Crosswind limits by aircraft type: https://pilotinstitute.com/weather-minimums-for-pilots/
- ICAO Manual of All-Weather Operations (Doc 9365): https://skybrary.aero/sites/default/files/bookshelf/2983.pd
- Simple Flying — Flight Time Limitation rules: https://simpleflying.com/cabin-crew-hours-rules-regulations/
- Wikipedia — Aircraft maintenance checks: https://en.wikipedia.org/wiki/Aircraft_maintenance_checks
- FAR 91.155 — Basic VFR weather minimums: https://www.law.cornell.edu/cfr/text/14/91.155