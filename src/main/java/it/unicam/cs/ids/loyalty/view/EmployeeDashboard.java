package it.unicam.cs.ids.loyalty.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unicam.cs.ids.loyalty.model.Customer;
import it.unicam.cs.ids.loyalty.model.Employee;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Membership;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.Transaction;
import it.unicam.cs.ids.loyalty.repository.EmployeeRepository;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.service.DefaultLoyaltyProgramService;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class EmployeeDashboard {

	private final EmployeeRepository employeeRepository;
	private final MerchantRepository merchantRepository;

	@Autowired
	private DefaultLoyaltyProgramService loyaltyProgramService;

	private Scanner scanner = new Scanner(System.in);
	private int merchantId;

	@Autowired
	public EmployeeDashboard(EmployeeRepository employeeRepository, MerchantRepository merchantRepository) {
		this.employeeRepository = employeeRepository;
		this.merchantRepository = merchantRepository;
	}

	public void login() {
		while (true) {
			System.out.println("Elenco degli impiegati censiti");
			List<Merchant> merchants = merchantRepository.findAll();

			merchants.forEach(merchant -> {
				merchant.getEmployees().forEach(employee -> {
					System.out.println("CODE: " + employee.getId() + " - " + employee.getName() + " - Azienda: "
							+ merchant.getName());
				});
			});

			System.out.println("Inserisci l'ID dell'impiegato:");
			int employeeId = scanner.nextInt();
			scanner.nextLine();

			try {
				Employee employee = employeeRepository.findById(employeeId).orElseThrow(EntityNotFoundException::new);
				System.out.println("Impiegato identificato: " + employee.getName());
				merchantId = employee.getMerchant().getId();
				displayOptions(employee);

			} catch (EntityNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void displayOptions(Employee employee) {
		while (true) {
			System.out.println("Seleziona un'opzione:");
			System.out.println("1. Convalida acquisto");
			System.out.println("2. Ricerca Cliente");
			System.out.println("3. Sostituzione Tessera");
			System.out.println("0. Esci");
			int option = 9;
			try {
				option = scanner.nextInt();

			} catch (InputMismatchException e) {
				System.out.println("Errore: per favore inserisci un numero intero.");
				scanner.next();
			}

			switch (option) {
			case 1:
				validatePurchase(merchantId);
				break;
			case 2:
				Map<LoyaltyProgram, List<Membership>> result = searchCustomer(merchantId);
				printCustomersInfo(result);
				break;
			case 3:
				replaceMembershipCard();
				break;
			case 0:
				System.out.println("Arrivederci!");
				System.exit(0);
			default:
				System.out.println("Opzione non valida. Riprova.");
				break;
			}
		}
	}

	private void replaceMembershipCard() {

		String oldCardNumber = handleCustomerSearch(scanner);
		System.out.print("\nInserisci il codice seriale della tessera da associare :");
		String newCardNumber = scanner.nextLine();
		boolean success = loyaltyProgramService.replaceCard(oldCardNumber, newCardNumber);

		if (success) {
			System.out.println("La tessera è stata sostituita con successo. Nuovo numero di tessera: " + newCardNumber);
		} else {
			System.out.println("Si è verificato un errore durante la sostituzione della tessera.");
		}

	}

	private Map<LoyaltyProgram, List<Membership>> searchCustomer(int merchantId) {
		Map<LoyaltyProgram, List<Membership>> result = new HashMap<>();
		System.out.println("Seleziona il criterio di ricerca:");
		System.out.println("1. Per cognome");
		System.out.println("2. Per codice fiscale");
		int searchOption = 0;

		while (true) {
			try {
				if (scanner.hasNextInt()) {
					searchOption = scanner.nextInt();
					break;
				} else {
					scanner.nextLine();
					System.out.println("Input non valido. Inserisci un numero.");
				}
			} catch (InputMismatchException e) {
				scanner.nextLine();
				System.out.println("Input non valido. Inserisci un numero.");
			}
		}
		scanner.nextLine();

		switch (searchOption) {
		case 1:
			result = searchBySurname(merchantId);
			break;
		case 2:
			result = searchByTaxCode(merchantId);
			break;
		default:
			System.out.println("Opzione non valida.");
			break;
		}
		return result;
	}

	private Map<LoyaltyProgram, List<Membership>> searchBySurname(int merchantId) {
		System.out.println("Inserisci il cognome del cliente:");
		String surname = scanner.nextLine().toUpperCase();

		Map<LoyaltyProgram, List<Membership>> loyaltyProgramMembershipsBySurname = loyaltyProgramService
				.findMembershipsBySurnameInMerchantLoyaltyPrograms(surname, merchantId);

		return loyaltyProgramMembershipsBySurname;
	}

	private Map<LoyaltyProgram, List<Membership>> searchByTaxCode(int merchantId) {
		System.out.println("Inserisci il codice fiscale del cliente:");
		String taxCode = scanner.nextLine().toUpperCase();

		Map<LoyaltyProgram, List<Membership>> loyaltyProgramMembershipsByTaxCode = loyaltyProgramService
				.getCustomerByTaxCodeForMerchantPrograms(taxCode, merchantId);
		return loyaltyProgramMembershipsByTaxCode;
	}

	private void printCustomersInfo(Map<LoyaltyProgram, List<Membership>> programMembershipMap) {

		if (programMembershipMap == null) {
			System.out.println("Nessun cliente trovato con il criterio specificato.");
		} else {
			for (Map.Entry<LoyaltyProgram, List<Membership>> entry : programMembershipMap.entrySet()) {
				LoyaltyProgram program = entry.getKey();
				List<Membership> memberships = entry.getValue();

				if (!memberships.isEmpty()) {

					for (Membership membership : memberships) {
						if (membership.getMemberCard().isCardValid()) {
							Customer customer = membership.getCustomer();
							System.out.println("\n-------------------------------------------------------------------\n"
									+ "--------------" + program.getProgramName() + "------------\n"
									+ "---------------------------------------------------------------------\n"
									+ "ID Tessera: " + membership.getMemberCard().getCardNumber() + ", Nome: "
									+ customer.getNome() + ", Cognome: " + customer.getCognome() + ", Codice Fiscale: "
									+ customer.getCodiceFiscale() + ", Livello Fedeltà: "
									+ membership.getCurrentLevel().getName() + ", Punti totali: "
									+ membership.getAccount().getLoyaltyPoints());
						}
					}
				}
			}
		}
	}

	private void validatePurchase(int merchantId) {
		String cardString = null;
		double amount = 0.0;

		while (true) {
			System.out.println("Come desideri identificare il cliente?");
			System.out.println("1. Scansione Tessera");
			System.out.println("2. Ricerca del cliente per cognome/codice fiscale");
			System.out.println("0. Uscire");

			System.out.println("Scegli un'opzione: ");
			int choice = scanner.nextInt();

			switch (choice) {
			case 1:
				handleScanCardEntry();
				cardString = handleCustomerSearch(scanner);
				break;
			case 2:
				cardString = handleCustomerSearch(scanner);
				break;
			case 0:
				System.out.println("Uscita in corso...");
				return;

			default:
				System.out.println("Opzione non valida. Riprova.");
			}
			System.out.print("inserisci l'importo speso (usare il separatore decimale locale \",\") : ");
			try {
				amount = scanner.nextDouble();
			} catch (InputMismatchException e) {
				System.out.println("Input non valido. Inserisci un numero decimale secondo la notazione locale(,).");
				scanner.next();
				continue;
			}
			String type = "POINTS_REWARD";
			Transaction transaction = loyaltyProgramService.createTransaction(type, merchantId, amount, cardString);
			if (transaction == null)
				System.out.println("transazione non creata");
			else
				System.out.println("Punti convalidati: " + transaction.getPointsEarned() + "; il nuovo saldo punti è "
						+ transaction.getMembershipAccount().getCurrentPoints());
		}
	}

	private void handleScanCardEntry() {
		System.out.print(
				"==================0\nNon implementato. Simuliamo l'azione lettura card inserendo il codice tessera\nQuesto è l'elenco tessere clienti:\n");
	}

	private String handleCustomerSearch(Scanner scanner) {
		Map<LoyaltyProgram, List<Membership>> clienti = searchCustomer(merchantId);
		printCustomersInfo(clienti);
		System.out.print("Inserisci il numero della tessera del Cliente: ");
		return scanner.nextLine();

	}
}
