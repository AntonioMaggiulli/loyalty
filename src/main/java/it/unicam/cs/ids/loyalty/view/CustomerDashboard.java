package it.unicam.cs.ids.loyalty.view;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Customer;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Membership;
import it.unicam.cs.ids.loyalty.repository.CustomerRepository;
import it.unicam.cs.ids.loyalty.service.DefaultCustomerService;
import it.unicam.cs.ids.loyalty.service.DefaultLoyaltyProgramService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component
public class CustomerDashboard {

	private final CustomerRepository customerRepository;
	private DefaultCustomerService customerService;
	private DefaultLoyaltyProgramService loyaltyProgramService;
	private Scanner scanner = new Scanner(System.in);

	@Autowired
	public CustomerDashboard(CustomerRepository customerRepository, DefaultCustomerService customerService,
			DefaultLoyaltyProgramService loyaltyProgramService) {
		this.customerRepository = customerRepository;
		this.customerService = customerService;
		this.loyaltyProgramService = loyaltyProgramService;
	}

	public void login() {
		while (true) {
			System.out.println("Simulazione del LOGIN - questo è l'elenco dei Clienti:");
			List<Customer> customers = customerRepository.findAll();
			displayCustomerList(customers);
			System.out.println("Seleziona un Cliente (inserisci l'ID) o premi 0 per tornare al menu principale:");
			int customerId = scanner.nextInt();
			scanner.nextLine();
			if (customerId == 0) {
				return;
			}

			Customer selectedCustomer = customerRepository.findById(customerId).orElse(null);
			if (selectedCustomer != null) {
				System.out.println(
						"Tesserato selezionato: " + selectedCustomer.getCognome() + " " + selectedCustomer.getNome());
				displayOptions(selectedCustomer.getId());
			} else {
				System.out.println("Cliente non trovato.");
			}
		}
	}

	private void displayCustomerList(List<Customer> customers) {
		for (Customer customer : customers) {
			System.out.println(customer.getId() + ". " + customer.getCognome());
		}
	}

	private void displayOptions(int customerId) {
		while (true) {
			System.out.println("Seleziona un'opzione:");
			System.out.println("1. Visualizza i tuoi programmi fedeltà");
			System.out.println("2. Visualizza i tuoi punti e benefit");
			System.out.println("3. Aderisci a un programma fedeltà");
			System.out.println("0. Esci");

			int option = scanner.nextInt();
			scanner.nextLine();

			switch (option) {
			case 1:
				viewCustomerLoyaltyPrograms(customerId);
				break;
			case 2:
				viewCustomerPoints(customerId);
				break;
			case 3:
				joinLoyaltyProgram(customerId);
				break;
			case 0:
				System.out.println("Arrivederci!");
				return;
			default:
				System.out.println("Opzione non valida. Riprova.");
				break;
			}
		}
	}

	@Transactional
	void viewCustomerLoyaltyPrograms(int customerId) {
		Customer customer = customerRepository.findById(customerId).orElse(null);
		System.out.println("\n=========================================================\n"
				+ "Lista dei programmi fedeltà di " + customer.getNome() + " " + customer.getCognome() + ":\n");
		List<Membership> memberships = customer.getMemberships();
		if (memberships.isEmpty()) {
			System.out.println("Non sei iscritto a nessun programma fedeltà.");
		} else {
			memberships.forEach(membership -> {
				System.out.println("CODICE: " + membership.getLoyaltyProgram().getId() + "Programma: "
						+ membership.getLoyaltyProgram().getProgramName() + ", Punti: "
						+ membership.getMembershipAccount().getLoyaltyPoints());
			});
		}
		System.out.println("=========================================================\n");
	}

	private void viewCustomerPoints(int customerId) {
		Customer customer = customerRepository.findById(customerId).orElse(null);
		List<Membership> memberships = customer.getMemberships();
		memberships.forEach(membership -> {
			System.out.println("\nProgramma: " + membership.getLoyaltyProgram().getProgramName());
		});
	}

	private void joinLoyaltyProgram(int customerId) {
		System.out.println("Programmi fedeltà disponibili per l'adesione:");
		List<LoyaltyProgram> availablePrograms = loyaltyProgramService.getAvailableCustomerProgram(customerId);
		availablePrograms.forEach(
				program -> System.out.println("Codice: " + program.getId() + ", Nome: " + program.getProgramName()));

		System.out.print("Scegli il codice del programma di fedeltà a cui vuoi aderire: ");
		int programId = scanner.nextInt();
		scanner.nextLine();

		LoyaltyProgram selectedProgram = loyaltyProgramService.getById(programId).orElse(null);
		if (selectedProgram != null) {
			loyaltyProgramService.joinLoyaltyProgram(customerId, programId);
			System.out.println("Ti sei iscritto con successo al programma: " + selectedProgram.getProgramName());
		} else {
			System.out.println("Programma di fedeltà non trovato.");
		}
	}

	public Customer insertCustomer() {
		System.out.print("Inserisci il cognome del cliente: ");
		String cognome = scanner.nextLine();

		System.out.print("Inserisci il nome del cliente: ");
		String nome = scanner.nextLine();

		System.out.print("Inserisci il codice fiscale del cliente: ");
		String codiceFiscale = scanner.nextLine();

		System.out.print("Inserisci l'email del cliente: ");
		String email = scanner.nextLine();

		System.out.print("Inserisci il telefono del cliente: ");
		String telefono = scanner.nextLine();

		System.out.print("Inserisci l'indirizzo del cliente: ");
		String indirizzo = scanner.nextLine();

		System.out.print("Inserisci la data di nascita del cliente (dd/MM/yyyy): ");
		Date dateOfBirth = null;
		try {
			dateOfBirth = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
		} catch (ParseException e) {
			System.out.println("Formato data di nascita non valido. Utilizzare il formato 'dd/MM/yyyy'.");
		}

		System.out.print("Inserisci il codice di riferimento del cliente: ");
		String referralCode = scanner.nextLine();

		Customer newCustomer = new Customer();
		newCustomer.setCognome(cognome);
		newCustomer.setNome(nome);
		newCustomer.setCodiceFiscale(codiceFiscale);
		newCustomer.setEmail(email);
		newCustomer.setTelefono(telefono);
		newCustomer.setIndirizzo(indirizzo);
		newCustomer.setDateOfBirth(dateOfBirth);
		newCustomer.setReferralCodeString(referralCode);

		return customerRepository.save(newCustomer);

	}
}
