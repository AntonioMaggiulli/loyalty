package it.unicam.cs.ids.loyalty.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unicam.cs.ids.loyalty.model.Customer;
import it.unicam.cs.ids.loyalty.model.Employee;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Membership;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.repository.EmployeeRepository;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.service.DefaultCustomerService;
import it.unicam.cs.ids.loyalty.service.DefaultLoyaltyProgramService;
import it.unicam.cs.ids.loyalty.service.DefaultMerchantService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class EmployeeDashboard {

	private final EmployeeRepository employeeRepository;
	private final MerchantRepository merchantRepository;
	private DefaultMerchantService merchantService;
	private DefaultCustomerService customerService;
	@Autowired
	private DefaultLoyaltyProgramService loyaltyProgramService;

	private Scanner scanner = new Scanner(System.in);
	private int merchantId;

	@Autowired
	public EmployeeDashboard(EmployeeRepository employeeRepository, MerchantRepository merchantRepository) {
		this.employeeRepository = employeeRepository;
this.merchantRepository=merchantRepository;
	}

	public void login() {
		while (true) {
			System.out.println("Elenco degli impiegati censiti");
			List<Merchant> merchants=merchantRepository.findAll();
			for (Merchant merchant:merchants) {
				List<Employee> employees= merchant.getEmployees();
				for (Employee employee : employees) {
					System.out.println("CODE: "+employee.getId()+" - "+employee.getName());
				}
			}
		
		System.out.println("Inserisci l'ID dell'impiegato:");
		int employeeId = scanner.nextInt();
		scanner.nextLine();

		Employee employee = employeeRepository.findById(employeeId).orElse(null);
		merchantId = employee.getMerchant().getId();
		displayOptions(employee);
		if (employee != null) {
			System.out.println("Impiegato identificato: " + employee.getName());
		
		
	}
		}
		}
	private void displayOptions(Employee employee) {
		while (true) {
			System.out.println("Seleziona un'opzione:");
			System.out.println("1. Convalida acquisto");
			System.out.println("2. Ricerca Cliente");
			System.out.println("3. da implementare");
			System.out.println("0. Esci");
int option=9;
			try {
			    option = scanner.nextInt();
			    // Processa l'input
			} catch (InputMismatchException e) {
			    System.out.println("Errore: per favore inserisci un numero intero.");
			    scanner.next();
			}
			//scanner.nextLine();

			switch (option) {
			case 1:
				validatePurchase(merchantId);
				break;
			case 2:
				searchCustomer(merchantId);
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

	private void searchCustomer(int merchantId) {
		System.out.println("Seleziona il criterio di ricerca:");
		System.out.println("1. Per cognome");
		System.out.println("2. Per codice fiscale");
		int searchOption = scanner.nextInt();
		scanner.nextLine();

		switch (searchOption) {
		case 1:
			searchBySurname(merchantId);
			break;
		case 2:
			searchByTaxCode(merchantId);
			break;
		default:
			System.out.println("Opzione non valida.");
			break;
		}
	}

	private void searchBySurname(int merchantId) {
		System.out.println("Inserisci il cognome del cliente:");
		String surname = scanner.nextLine();

		Map<LoyaltyProgram, List<Membership>> customersByProgram = loyaltyProgramService
				.getCustomersBySurnameForMerchantPrograms(surname, merchantId);
		if (customersByProgram != null) {
			printCustomersInfo(customersByProgram);
		} else {
			System.out.println("Nessun cliente trovato con il cognome specificato.");
		}
	}

	private void searchByTaxCode(int merchantId) {
		System.out.println("Inserisci il codice fiscale del cliente:");
		String taxCode = scanner.nextLine();

		Map<LoyaltyProgram, List<Membership>> membershipByProgram = loyaltyProgramService
				.getCustomerByTaxCodeForMerchantPrograms(taxCode, merchantId);
	    if (membershipByProgram != null) {
	        printCustomersInfo(membershipByProgram);
	    } else {
	        System.out.println("Nessun cliente trovato con il cognome specificato.");
	    }
	}

	private void printCustomersInfo(Map<LoyaltyProgram, List<Membership>> programMembershipMap) {
		
		for (Map.Entry<LoyaltyProgram, List<Membership>> entry : programMembershipMap.entrySet()) {
			LoyaltyProgram program = entry.getKey();
			List<Membership> memberships = entry.getValue();

			if (!memberships.isEmpty()) {
				System.out.println("Programma di fedeltà: " + program.getProgramName());
				for (Membership membership : memberships) {
					Customer customer = membership.getCustomer();
					System.out.println("\n------------------------------------------------------------------\n"+
							"ID Tessera: " + membership.getId() + ", Nome: " + customer.getNome() + ", Cognome: "
									+ customer.getCognome() + ", Codice Fiscale: " + customer.getCodiceFiscale());
				}
			}
		}
	}

	private void validatePurchase(int merchantId) {
		System.out.println("Inserisci l'importo dell'acquisto:");
		double value = scanner.nextDouble();

		// Logica per la convalida dell'acquisto
		// Esempio: employeeService.validatePurchase(purchaseCode, employee);
		System.out.println("Acquisto convalidato.");
	}
}
