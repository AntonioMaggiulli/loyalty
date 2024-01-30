package it.unicam.cs.ids.loyalty.view;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Customer;
import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Membership;
import it.unicam.cs.ids.loyalty.model.Transaction;
import it.unicam.cs.ids.loyalty.repository.BenefitRepository;
import it.unicam.cs.ids.loyalty.repository.CustomerRepository;
import it.unicam.cs.ids.loyalty.repository.MembershipRepository;
import it.unicam.cs.ids.loyalty.service.DefaultLoyaltyProgramService;
import it.unicam.cs.ids.loyalty.util.ReferralCodeGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class CustomerDashboard {

	private final CustomerRepository customerRepository;
	private final BenefitRepository benefitRepository;
	private final MembershipRepository membershipRepository;

	private DefaultLoyaltyProgramService loyaltyProgramService;
	private Scanner scanner = new Scanner(System.in);

	@Autowired
	public CustomerDashboard(CustomerRepository customerRepository, BenefitRepository benefitRepository,
			DefaultLoyaltyProgramService loyaltyProgramService, MembershipRepository membershipRepository) {
		this.customerRepository = customerRepository;
		this.benefitRepository = benefitRepository;
		this.loyaltyProgramService = loyaltyProgramService;

		this.membershipRepository = membershipRepository;
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

			Customer selectedCustomer = customerRepository.findById(customerId)
					.orElseThrow(() -> new EntityNotFoundException("Cliente non trovato."));
			;
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
			System.out.println(customer.getId() + ". " + customer.getCognome() + " " + customer.getNome());
		}
	}

	private void displayOptions(int customerId) {
		int option;
		do {
			System.out.println("Seleziona un'opzione:");
			System.out.println("1. Visualizza il tuo profilo");
			System.out.println("2. Visualizza un catalogo Premi");
			System.out.println("3. Aderisci a un programma fedeltà");
			System.out.println("4. Riscatta un Benefit");
			System.out.println("5. Visualizza Transazioni");
			System.out.println("6. Invita un amico");

			System.out.println("0. Esci");

			try {
				option = scanner.nextInt();
				scanner.nextLine();

				switch (option) {
				case 1:
					viewCustomerProfile(customerId);
					break;
				case 2:
					viewBenefit(customerId);
					break;
				case 3:
					joinLoyaltyProgram(customerId);
					break;
				case 4:
					redeemBenefit(customerId);
					break;
				case 5:
					viewTransactions(customerId);
					break;
				case 6:
					inviteFriend(customerId);
					break;

				case 0:
					System.out.println("Arrivederci!");
					return;
				default:
					System.out.println("Opzione non valida. Riprova.");
					break;
				}
			} catch (InputMismatchException e) {
				System.out.println("Errore: input non valido. Inserisci un numero.");
				scanner.nextLine();
				option = -1;
			}
		} while (option < 0 || option > 5);
	}

	private void inviteFriend(int customerId) {
		String messageString = null;
		viewCustomerLoyaltyPrograms(customerId);

		System.out.print("Seleziona un programma fedeltà per l'invito: ");
		int loyaltyProgramId = scanner.nextInt();
		scanner.nextLine();

		System.out.print("Inserisci l'email della persona che vuoi invitare: ");
		String friendContact = scanner.nextLine();

		try {
			messageString = loyaltyProgramService.inviteFriend(customerId, loyaltyProgramId, friendContact);
		} catch (EntityNotFoundException e) {
			System.err.println("Errore durante l'invito: " + e.getMessage());
		}

		if (messageString != null)
			System.out.println("Invito creato con il seguente testo: " + messageString);
	}

	private void viewCustomerProfile(int customerId) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new EntityNotFoundException("Cliente non trovato."));
		;

		if (customer != null) {
			System.out.println(
					"\n--------------------------------------\nDATI PERSONALI\n--------------------------------------");
			System.out.println("Nome: " + customer.getNome());
			System.out.println("Cognome: " + customer.getCognome());
			System.out.println("Codice Fiscale: " + customer.getCodiceFiscale());
			System.out.println("Data di Nascita: " + customer.getDateOfBirth());

			System.out.println("\nCodice Amico: " + customer.getReferralCodeString());

			System.out.println(
					"\n--------------------------------------\nPROGRAMMI FEDELTA'\n--------------------------------------\n\n");
			List<Membership> memberships = customer.getMemberships();
			if (memberships.isEmpty()) {
				System.out.println("Non sei iscritto a nessun programma fedeltà.");
			} else {
				memberships.forEach(membership -> {
					LoyaltyProgram loyaltyProgram = membership.getLoyaltyProgram();
					System.out.println("Programma: " + loyaltyProgram.getProgramName());
					System.out.println("Punti: " + membership.getMembershipAccount().getLoyaltyPoints());
					System.out.println("Tessera: " + membership.getMemberCard().getCardNumber());
					System.out.println("--------------------------------------");
				});
			}
		} else {
			System.out.println("Cliente non trovato.");
		}
		displayOptions(customerId);
	}

	private void viewTransactions(int customerId) {
		System.out.println("\nVisualizzazione delle transazioni:");
		List<Membership> memberships = viewCustomerLoyaltyPrograms(customerId);
		if (!memberships.isEmpty()) {
			System.out.print("Inserisci il codice del programma di fedeltà: ");
			int programId = scanner.nextInt();
			scanner.nextLine();

			List<Transaction> transactions = loyaltyProgramService.getTransactions(customerId, programId);
			if (transactions.isEmpty()) {
				System.out.println("Nessuna transazione disponibile per questo programma di fedeltà.");
			} else {
				System.out.println("\n===================================================================\n");
				transactions.forEach(transaction -> {
					int pointsChange = transaction.getPointsEarned() - transaction.getPointsSpent();
					String sign = pointsChange >= 0 ? "+ " : "- ";
					System.out.println("ID Transazione: " + transaction.getId() + ", Data: "
							+ transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
							+ ", Descrizione(Benefit): " + transaction.getLoyaltyBenefit().getName() + ", Punti: "
							+ sign + Math.abs(pointsChange));
				});
			}
			System.out.println("===================================================================\n");
		}
		displayOptions(customerId);
	}

	@Transactional
	List<Membership> viewCustomerLoyaltyPrograms(int customerId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new EntityNotFoundException("Cliente non trovato."));
		;
		System.out.println("\n=========================================================\n"
				+ "Lista dei programmi fedeltà di " + customer.getNome() + " " + customer.getCognome() + ":\n");
		List<Membership> memberships = customer.getMemberships();
		if (memberships.isEmpty()) {
			System.out.println("Non sei iscritto a nessun programma fedeltà.");
		} else {
			memberships.forEach(membership -> {
				System.out.println("CODICE: " + membership.getLoyaltyProgram().getId() + " Programma: "
						+ membership.getLoyaltyProgram().getProgramName() + ", Punti: "
						+ membership.getMembershipAccount().getLoyaltyPoints());
			});
		}
		System.out.println("=========================================================\n");
		return memberships;
	}

	private void joinLoyaltyProgram(int customerId) {
		System.out.println("Programmi fedeltà disponibili per l'adesione:");
		List<LoyaltyProgram> availablePrograms = loyaltyProgramService.getAvailableCustomerProgram(customerId);

		for (int i = 0; i < availablePrograms.size(); i++) {
			LoyaltyProgram program = availablePrograms.get(i);
			System.out.println((i + 1) + ". Nome: " + program.getProgramName());
		}

		System.out.print("Scegli il numero del programma di fedeltà a cui vuoi aderire (0 per uscire): ");

		int choice;
		try {
			choice = scanner.nextInt();
			scanner.nextLine();
			if (choice == 0) {
				System.out.println("Uscita dal menu di iscrizione.");
				return;
			} else if (choice < 1 || choice > availablePrograms.size()) {
				System.out.println("Scelta non valida.");
				return;
			}

			LoyaltyProgram selectedProgram = availablePrograms.get(choice - 1);
			System.out.print("Inserisci il codice amico del presentante (se disponibile): ");
			String referralCode = scanner.nextLine();
			try {
				Membership membership = loyaltyProgramService.joinLoyaltyProgram(customerId, selectedProgram.getId(),
						referralCode);
				if (membership != null)
					System.out
							.println("Ti sei iscritto con successo al programma: " + selectedProgram.getProgramName());
			} catch (Exception e) {
				System.err.println("Errore nell'adesione: " + e.getMessage());
			}

		} catch (InputMismatchException e) {
			System.out.println("Errore: input non valido. Inserisci un numero.");
			scanner.nextLine();
		}
		displayOptions(customerId);
	}

	public Customer insertCustomer() {
		System.out.print("Inserisci il cognome del cliente: ");
		String cognome = scanner.nextLine().toUpperCase();

		System.out.print("Inserisci il nome del cliente: ");
		String nome = scanner.nextLine().toUpperCase();

		System.out.print("Inserisci il codice fiscale del cliente: ");
		String codiceFiscale = scanner.nextLine().toUpperCase();

		System.out.print("Inserisci l'email del cliente: ");
		String email = scanner.nextLine();

		System.out.println("altri campi non utili per la demo vengono omessi e sono stati commentati nel codice");
		/*
		 * 
		 * System.out.print("Inserisci il telefono del cliente: "); String telefono =
		 * scanner.nextLine();
		 * 
		 * System.out.print("Inserisci l'indirizzo del cliente: "); String indirizzo =
		 * scanner.nextLine();
		 */

		System.out.print("Inserisci la data di nascita del cliente (dd/MM/yyyy): ");
		Date dateOfBirth = null;
		try {
			dateOfBirth = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
		} catch (ParseException e) {
			System.out.println("Formato data di nascita non valido. Utilizzare il formato 'dd/MM/yyyy'.");
		}
		String codiceAmico = ReferralCodeGenerator.generateReferralCode();

		Customer newCustomer = new Customer();
		newCustomer.setCognome(cognome);
		newCustomer.setNome(nome);
		newCustomer.setCodiceFiscale(codiceFiscale);
		newCustomer.setEmail(email);
		/*
		 * newCustomer.setEmail(email); newCustomer.setTelefono(telefono);
		 * newCustomer.setIndirizzo(indirizzo);
		 */
		newCustomer.setDateOfBirth(dateOfBirth);
		newCustomer.setReferralCodeString(codiceAmico);
		System.out.println("il tuo codice per la campagna \"presenta un Amico\" è " + codiceAmico);

		System.out.println("Cliente Registrato, effettua il login");
		return customerRepository.save(newCustomer);
	}

	private void redeemBenefit(int customerId) {

		viewBenefit(customerId);
		while (true) {
			try {
				System.out.print("inserisci il codice del premio richiesto: ");
				int benefitId = scanner.nextInt();
				Benefit benefit = benefitRepository.findById(benefitId)
						.orElseThrow(() -> new EntityNotFoundException("Benefit non trovato."));
				String type = benefit.getType();
				if (type == "POINTS_REWARD") {
					return;
				}
				Customer customer = customerRepository.findById(customerId)
						.orElseThrow(() -> new EntityNotFoundException("Customer non trovato."));

				Membership membership = membershipRepository
						.findByCustomerAndLoyaltyProgram(customer, benefit.getLoyaltyProgram())
						.orElseThrow(() -> new EntityNotFoundException("Membership non trovata."));
				String cardString = membership.getMemberCard().getCardNumber();

				if (!benefit.getAssociatedLevel().equals(membership.getCurrentLevel())) {
					System.out.println("Il Benefit non è applicabile al tuo livello di fedeltà");
					return;
				}

				if (!benefit.isEligibleForRedemption(membership.getAccount())) {
					System.out.println("Punti insufficienti oppure il premio non è più disponibile");
					return;
				}

				loyaltyProgramService.createTransaction(type, benefit, 0, cardString);
				System.out.println(
						"Premio Riscattato, il tuo nuovo saldo è " + membership.getAccount().getCurrentPoints());
				break;
			} catch (InputMismatchException e) {
				System.out.println("Errore: input non valido. Inserisci un numero.");
				scanner.nextLine();
			}
		}
	}

	private void viewBenefit(int customerId) {
		while (true) {
			try {
				viewCustomerLoyaltyPrograms(customerId);
				System.out.print("Inserisci il codice del programma di fedeltà: ");
				int programId = scanner.nextInt();

				LoyaltyProgram program = loyaltyProgramService.getById(programId)
						.orElseThrow(() -> new RuntimeException("Programma di fedeltà non trovato."));

				Map<Integer, List<Benefit>> benefitsByLevel = loyaltyProgramService
						.getBenefitsByLoyaltyProgram(programId);

				for (Level level : program.getLevels()) {
					System.out.println("\nLivello: " + level.getName());
					List<Benefit> benefits = benefitsByLevel.get(level.getId());
					if (benefits.isEmpty()) {
						System.out.println("  Nessun benefit disponibile per questo livello.");
					} else {
						for (Benefit benefit : benefits) {
							if (!"POINTS_REWARD".equals(benefit.getType()))
								System.out
										.println(benefit.getId() + ".  Punti necessari: " + benefit.getPointsRequired()
												+ " - Nome:" + benefit.getName() + " - " + benefit.getDescription());
						}
					}
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("Errore: input non valido. Inserisci un numero.");
				scanner.nextLine();
			}
		}
	}
}