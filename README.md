# loyalty
Esame IDS Unicam a.a. 2022/23 - Progetto sviluppato da Stefano Cruciani e Antonio Maggiulli
 
Il progetto rappresenta una demo di una piattaforma di gestione di Programmi Fedeltà implementata utilizzando il framework Spring Boot (solo in forma testuale e da riga di comando) in combinazione con un database MySQL. L'applicazione offre funzionalità di gestione diversificate a seconda degli attori che la utilizzano: merchant (le aziende che decidono di offrire un programma fedeltà), Customer (i clienti che accumulano vantaggi) e i dipendenti dell'azienda che devono gestire i rapporti diretti con la clientela.
Il progetto mira a offrire un sistema completo e flessibile di gestione della fedeltà, consentendo alle aziende di collaborare attraverso coalizioni e ai clienti di godere di vantaggi diversificati in base ai programmi a cui aderiscono.
 
 
Elementi Principali del Modello
 
LoyaltyProgram: rappresenta la classe centrale del modello e definisce un programma fedeltà. Ogni programma può avere più livelli di fedeltà, con vantaggi crescenti e partner in coalizione.
 
Merchant (Azienda): diverse istanze di Merchant creano Programmi fedeltà e i relativi benefit come coupon, cashback e premi fisici da riscattare con un sistema a punti.
 
Partnership (Associazione tra Loyalty Program e Merchant): Una classe di associazione che collega i programmi di fedeltà (LoyaltyProgram) ai Merchant che partecipano, consentendo ai clienti di usufruire dei benefici offerti da una o più aziende.
 
Customer (Cliente): Rappresenta le persone che aderiscono ai programmi di fedeltà.
Un cliente può aderire a più programmi fedeltà e l'associazione costituisce una Membership. La MemberCard, rilasciata a una sola persona, è associata alla membership e può essere numerica o basata su QR code.
 
Membership (Associazione Cliente-Programma): Contiene attributi come il livello di fedeltà, memberCard ecc. ed è collegata a un MembershipAccount che tiene traccia del saldo dei punti e registra le transazioni (classe Transaction).
 
 
 
Caratteristiche implementative
 
Architettura Spring Boot:
 
Utilizza il framework Spring Boot per la creazione di un'applicazione Java e per la persistenza dei dati tramite JPA e Hibernate. Tutttavia, essendo una demo, non implementa un vero e proprio pattern mvc di SpringBoot
 

Gestione della persistenza dei Dati:
 
Nel file application.properties sono configurati due ambienti di database alternativi tra loro (per cui uno di essi deve essere commentato e non attivo); è possible attivare o un db locale o un db remoto ospitato sulla piattaforma aiven (https://aiven.io/).
Vengono sfruttate le JPA (Java Persistence API) per la gestione degli oggetti persistenti e Hibernate come implementazione.
 
Il db remoto, inoltre, è popolato con dati fittizi di prova
