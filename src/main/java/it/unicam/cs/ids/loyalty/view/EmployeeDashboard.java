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
import it.unicam.cs.ids.loyalty.service.DefaultMembershipService;
import it.unicam.cs.ids.loyalty.service.DefaultMerchantService;
import jakarta.websocket.ClientEndpoint;

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
	private DefaultMerchantService merchantService;
	private DefaultCustomerService customerService;
	@Autowired
	private DefaultLoyaltyProgramService loyaltyProgramService;
	@Autowired
	private DefaultMembershipService membershipService;

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
				Map<LoyaltyProgram, List<Membership>>result =searchCustomer(merchantId);
				printCustomersInfo(result);
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

	private Map<LoyaltyProgram, List<Membership>> searchCustomer(int merchantId) {
		Map<LoyaltyProgram, List<Membership>> result=new HashMap<>();
		System.out.println("Seleziona il criterio di ricerca:");
		System.out.println("1. Per cognome");
		System.out.println("2. Per codice fiscale");
		int searchOption = scanner.nextInt();
		scanner.nextLine();

		switch (searchOption) {
		case 1:
			result=searchBySurname(merchantId);
			break;
		case 2:
			result=searchByTaxCode(merchantId);
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

		Map<LoyaltyProgram, List<Membership>> filteredmerchantCustomers = loyaltyProgramService
				.getCustomersBySurnameForMerchantPrograms(surname, merchantId);

		return filteredmerchantCustomers;
	}

	private Map<LoyaltyProgram, List<Membership>> searchByTaxCode(int merchantId) {
		System.out.println("Inserisci il codice fiscale del cliente:");
		String taxCode = scanner.nextLine().toUpperCase();

		Map<LoyaltyProgram, List<Membership>> filteredMerchantCustomers = loyaltyProgramService
				.getCustomerByTaxCodeForMerchantPrograms(taxCode, merchantId);
	    return filteredMerchantCustomers;
	}

	private void printCustomersInfo(Map<LoyaltyProgram, List<Membership>> programMembershipMap) {
		
		if (programMembershipMap == null) {
			System.out.println("Nessun cliente trovato con il criterio specificato.");
		} else {
		for (Map.Entry<LoyaltyProgram, List<Membership>> entry : programMembershipMap.entrySet()) {
			LoyaltyProgram program = entry.getKey();
			List<Membership> memberships = entry.getValue();

			if (!memberships.isEmpty()) {
				System.out.println("Programma di fedeltà: " + program.getProgramName());
				for (Membership membership : memberships) {
					Customer customer = membership.getCustomer();
					System.out.println("\n--------------"+program.getProgramName()+"------------\n"+
							"ID Tessera: " + membership.getMemberCard().getCardNumber() + ", Nome: " + customer.getNome() + ", Cognome: "
									+ customer.getCognome() + ", Codice Fiscale: " + customer.getCodiceFiscale());
				}
			}
		}}
	}

	private void validatePurchase(int merchantId) {
		String cardString=null;
		double amount=0.0;
		
		 
		while (true) {
            System.out.println("Come desideri identificare il cliente?");
            System.out.println("1. Scansione Tesseera");
            System.out.println("2. Ricerca del cliente per cognome/codice fiscale");
            System.out.println("0. Uscire");

            System.out.println("Scegli un'opzione: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    handleScanCardEntry();
                    cardString=handleCustomerSearch(scanner);
                    break;
                case 2:
                    cardString=handleCustomerSearch(scanner);
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
                scanner.next(); // Pulisce l'input errato dallo scanner
                continue; // Riprende il ciclo while
            }
            String type ="POINTS_REWARD";
            merchantService.createTransaction(type,merchantId, amount, cardString);
	    }
        }
		

    private void handleScanCardEntry() {
        System.out.print("==================0\nNon implementato. Simuliamo l'azione lettura card inserendo il codice tessera\nQuesto è l'elenco tessere clienti:\n");
    }

    private String handleCustomerSearch(Scanner scanner) {
    	Map<LoyaltyProgram, List<Membership>> clienti=searchCustomer(merchantId);
    	printCustomersInfo(clienti);
       System.out.print("Inserisci il numero della tessera del Cliente: ");
       return scanner.nextLine();
     
       
    }
}


