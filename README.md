# SISTEM ZA UPRAVLJANJE LETOVIMA I DETEKCIJU KAŠNJENJA

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
| FIDS sistemi | Prikazuju status letova putnicima i osoblju | Samo evidencija, bez pofrške odlučivanju |
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
    - Kategorija leta (domaći, međunarodni, cargo, privatni)
- Podaci o avionu
    - Tip motora
    - Starost
    - Datum poslednjeg servisa
    - Datum narednog obaveznog servisa
- Podaci o aerodromu
    - Ukupan broj pista
    - Broj raspoloživih pista
    - Broj slobodnih gate-ova
    - Kapacitet aerodroma
- Podaci o posadi
    - Kompletnost posade
    - Radno vreme posade

#### Dinamički podaci

- Vremenski izveštaji
    - Brzina vetra
    - Smer vetra
    - Komponenta bočnog vetra
    - Vidljivost
    - Temperatura
    - Vrsta padavina
    - Intenzitet padavina
    - Pojava leda
- Status piste 
    - Status (otvorena, zatvorena, klizava, u održavanju)
    - Broj pisti u odledavanju
- Tehnički alarmi aviona
    - Tip alarma
    - Ozbiljnost (nizak, srednji, visok, kritičan)
    - Komponenta
    - Vreme prijave alarma
    - Status alarma
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
IF brzina vetra > 50km/h THEN JakVetar
IF vidljivost < 500m THEN KriticnaVidljivost
IF temperatura < 0°C and (kiša or sneg) THEN RizikOdLeda
IF intenzitet padavina > UMEREN THEN JakePadavine
IF komponenta bocnog vetra > 30km/h THEN BocniVetar
IF temperatura < -20°C THEN EkstremnaHladnoca
IF pojavaLeda == true THEN AktivnoLedenje
```

- Tehnički alarmi aviona
```
IF alarm.ozbiljnost == VISOK THEN OzbiljanAlarm
IF alarm.ozbiljnost == KRITIČAN THEN KriticanAlarm
```

- Podaci o avionu
```
IF avion.datumNarednogServisa < danas THEN ServisPrekoracen
IF avion.starost > 20 AND avion.datumNarednogServisa < danas + 30 dana THEN AvionBlizuServisa
IF avion.starost > 25 THEN StarAvion
```

- Posada
```
IF posada.kompletna == false THEN NekompletnaPostada
IF posada.radnoVreme > 8h THEN PosadaUmorna
IF posada.radnoVreme > 12h THEN PosadaPrekoracilaNormu
```

- Infrastruktura aerodroma
```
IF pista.status == ZATVORENA THEN PistaNedostupna
IF pista.status == KLIZAVA THEN PistaRizicna
IF slobodniGateovi == 0 THEN NemaGateova
IF slobodniGateovi <= 2 THEN MaloGateova
IF raspolozivePiste == 1 THEN SamoPistaJedna
IF (raspolozivePiste / ukupnePiste) < 0.5 THEN KriticanBrojPista
```

#### Nivo 2 - Procena stanja sistema (agregacija zaključaka)

Na osnovu primarnih zaključaka sa nivoa 1, generiše se status pojedinih sistema:
```
IF JakVetar AND KriticnaVidljivost THEN LošiVremenskiUslovi
IF LošiVremenskiUslovi AND RizikOdLeda THEN EkstremniUslovi
IF PistaNedostupna OR PistaRizicna THEN PistaProblem
IF OzbiljanAlarm AND KriticanAlarm THEN TehnickiProblem
IF TehnickiProblem AND (avion.doLeta < 72h) THEN AvionNijeSpreman
IF BocniVetar AND (JakePadavine OR AktivnoLedenje) THEN KriticniVremenskiUslovi
IF NekompletnaPostada OR PosadaPrekoracilaNormu THEN PostadaNijeSposobna
IF ServisPrekoracen OR (StarAvion AND AvionBlizuServisa) THEN AvionTehničkiRizičan
IF MaloGateova AND KriticanBrojPista THEN AerodromPodPritiskom
IF AvionTehničkiRizičan AND LošiVremenskiUslovi THEN VisokOperativniRizik
IF PosadaUmorna AND (LošiVremenskiUslovi OR PistaProblem) THEN BezbednostUgrozena
```

#### Nivo 3 - Donošenje finalne odluke

Na osnovu primarnih zaključaka sa nivoa 2, generiše se preporuka akcije:
```
IF EkstremniUslovi AND PistaProblem THEN Preporuka: OTKAŽI
IF LošiVremenskiUslovi AND NOT PistaProblem THEN Preporuka: ODLOŽI
IF AvionNijeSpreman AND PostojiZamena THEN Preporuka: ODLOŽI
IF AvionNijeSpreman AND NOT PostojiZamena THEN Preporuka: OTKAŽI
IF NOT problemi THEN Preporuka: POLETI NA VREME
IF PostadaNijeSposobna AND NOT PostojiZamenaPostade THEN Preporuka: OTKAŽI
IF PostadaNijeSposobna AND PostojiZamenaPostade THEN Preporuka: ODLOŽI
IF BezbednostUgrozena THEN Preporuka: OTKAŽI
IF AerodromPodPritiskom AND LošiVremenskiUslovi THEN Preporuka: PREUSMERI
IF VisokOperativniRizik THEN Preporuka: ODLOŽI
IF EkstremnaHladnoca AND NOT AktivnoLedjenje THEN Preporuka: ODLOŽI (procedura odleđavanja)
IF KriticniVremenskiUslovi THEN Preporuka: OTKAŽI
```

#### CEP - Complex Event Processing

CEP modul prati tok događaja u realnom vremenu i detektuje obrasce koji zahtevaju hitnu reakciju. Podaci se šalju kao kontinuiran stream iz senzora i sistema aerodroma.

- CEP 1: Brzo pogoršanje vremenskih uslova
```
IF (brzina2 - brzina1 > 30km/h)
AND (vidljivost < 800m)
AND (temperatura < 0°C)
WITHIN 20 minuta
THEN KriticnoPogorsavanjeVremena
AND ALARM: Zamrzni sva poletanja
```

- CEP 2: Serija tehničkih alarma na istom avionu
```
IF COUNT(alarm.NIZAK) >= 5
OR COUNT(alarm.SREDNJI) >= 3
OR COUNT(alarm.VISOK) >= 2
OR COUNT(alarm.KRITICAN) >= 1
WITHIN 15 minuta
THEN TehnickiIncident(avionId)
AND ALARM: Povuci avion iz saobraćaja
```

#### Accumulate funkcija

Accumulate se koristi za agregaciju podataka iz radne memorije:

- Ukupno kašnjenje i broj pogođenih letova danas
```
accumulate (
    Let(status == KASNI, datum == danas);
    $ukupnoMin : sum(minutaKasnjenja),
    $brojLetova : count(1)
)
```

- Ukupan broj pogođenih putnika
```
accumulate (
    Let(status == OTKAZAN || minutaKasnjenja > 120);
    $putnici : sum(brojPutnika),
)
```

- Prosečna vidljivost u poslednjih sat vremena
```
accumulate (
    VremenskiIzvestaj() over window:time(1h);
    $prosek : average(vidljivost),
)
```

#### Template mehanizam

Template mehanizam se koristi za definisanje pragova prihvatljivosti za različite kategorije letova. Svaka kategorija ima specifične granice za vremenske uslove i toleranciju kašnjenja.
```
template header
    kategorijaLeta, maxVetar, minVidljivost, maxKasnjenje, prioritet
end template
```
Primer korišćenja:

| kategorijaLeta | maxVetar | minVidljivost | maxKasnjenje | prioritet |
| :--- | ---: | ---: | ---: | ---: |
| Domaći | 60 | 800 | 120 | 2 |
| Međunarodni | 55 | 600 | 180 | 1 |
| Cargo | 70 | 1000 | 240 | 3 |
| Privatni | 45 | 1500 | 60 | 4 |

Takođe se koristi i za generisanje pravila za proveru statusa piste i određivanja da li su poletanje i sletanje dozvoljena, koji je maksimalni dozvoljeni vetar i da li je potrebno pokrenuti proceduru odleđavanja. Na primer, za status Klizava, sistem automatski ograničava maksimalnu brzinu vetra na 50 km/h i zahteva odleđavanje pre nego što se dozvoli operacija.

```
template header
    statusPiste, dozvoljenoPoletanje, dozvoljenoSletanje,
    maxVetar, potrebnoOdledjavanje, prioritetCiscenja
end template
```
Primer korišćenja:

| statusPiste | dozvoljenoPoletanje | dozvoljenoSletanje | maxVetar | potrebnoOdledjavanje | prioritetCiscenja |
| :--- | :---: | :---: | ---: | :---: | ---: |
| Otvorena | true | true | 70 | false | 0 |
| Klizava | true | true | 50 | true | 1 |
| UOdrzavanju | false | false | 0 | false | 2 |
| Zatvorena | false | false | 0 | false | 3 |

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
query "usloviZaPoletanje"(Let $let)
    vremeDozvoljava($let),
    pistaSpremna($let),
    avionSpreman($let),
    posadaKompletna($let),
    kontrolaLetenjaOdobrila($let)
end

// pistaSpremna: pista je dostupna i bezbedna
query "pistaSpremna"(Let $let)
    NOT PistaNedostupna(),
    NOT PistaRizicna(),
    raspolozivePiste($let, $n), eval($n > 0)
end

// avionSpreman: avion je tehnički ispravan i nije prekoračio servis
query "avionSpreman"(Let $let)
    NOT TehnickiProblem(),
    NOT ServisPrekoracen(),
    NOT AvionTehničkiRizičan()
end

// posadaKompletna: posada je prisutna i u stanju da leti
query "posadaKompletna"(Let $let)
    NOT NekompletnaPostada(),
    NOT PosadaPrekoracilaNormu()
end

// kontrolaLetenjaOdobrila: ATC je dao odobrenje za poletanje
query "kontrolaLetenjaOdobrila"(Let $let)
    NOT KriticnoPogorsavanjeVremena(),
    NOT TehnickiIncident($let.avionId),
    NOT AerodromPodPritiskom()
end
```
Sistem automatski utvrđuje koji od navedenih uslova nisu ispunjeni i vraća objašnjenje zašto let ne može da poleti.

### Primer rezonovanja

Scenario: Let JU-301 Beograd -> London, planirani polazak 14:00, kategorija: Međunarodni

- Ulaz
    - Vetar: 65 km/h
    - Vidljivost: 450 m
    - Temperatura: -2°C
    - Vrsta padavina: kiša
- Nivo 1
    - INSERT JakVetar (65 > 50)
    - INSERT KriticnaVidljivost (450 < 500)
    - INSERT RizikOdLeda (-2°C + kiša)
- Nivo 2
    - JakVetar + KriticnaVidljivost -> INSERT LosiVremenskiUslovi
    - RizikOdLeda -> INSERT PistaRizicna
    - PistaRizicna -> INSERT PistaProblem
- Nivo 3
    - LosiVremenskiUslovi + PistaRizicna -> Preporuka: ODLOŽI let JU-301
- CEP #1
    - U poslednjih 20 minuta:
        - Vetar: +30km/h
        - Vidljivost: -450m
        - Temperatura: -3°C
    - Detektovano brzo pogoršanje
    - ALARM: Zamrzni sva poletanja
- CEP #2
    - U poslednjih 15 minuta:
        - Prijavljen alarm nivoa SREDNJI (sistem goriva) 
        - Prijavljen alarm nivoa SREDNJI (električna instalacija)
        - Prijavljen alarm nivoa SREDNJI (hidraulika)
    - COUNT(alarm.SREDNJI) >= 3
    - TehnickiIncident(JU-301)
    - ALARM: Povuci avion iz saobraćaja
- Accumulate
    - 5 letova kasni
    - Ukupno 847 putnika pogođeno
    - Ukupno kašnjenje 12h 20min
- Backward
    - Upit: "Šta JU-301 treba da poleti?"
        - Vetar mora < 55km/h
        - Vidljivost mora > 600m
        - Avion ima aktivne alarme
        - Posada kompletna
        - Kontrola letenja mora da odobri
- Izlaz
    - Finalna odluka: LET OTKAZAN - vremenski uslovi i tehnički problemi
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
