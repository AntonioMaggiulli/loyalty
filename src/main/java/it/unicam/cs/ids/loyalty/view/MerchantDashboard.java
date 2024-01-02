package it.unicam.cs.ids.loyalty.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Employee;
import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.Partnership;
import it.unicam.cs.ids.loyalty.repository.EmployeeRepository;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.service.DefaultLoyaltyProgramService;
import it.unicam.cs.ids.loyalty.service.DefaultMerchantService;
import jakarta.transaction.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class MerchantDashboard {

	private final MerchantRepository merchantRepository;
	private DefaultMerchantService merchantService;
	private DefaultLoyaltyProgramService loyaltyProgramService;
	private Scanner scanner = new Scanner(System.in);

	@Autowired
	public MerchantDashboard(MerchantRepository merchantRepository, EmployeeRepository employeeRepository,
			DefaultMerchantService merchantService, DefaultLoyaltyProgramService loyaltyProgramService) {
		this.merchantRepository = merchantRepository;
		this.merchantService = merchantService;

		this.loyaltyProgramService = loyaltyProgramService;
	}

	public void login() {

		while (true) {
			System.out.println("Elenco dei Merchant:");

			List<Merchant> merchants = merchantRepository.findAll();
			displayMerchantList(merchants);

			System.out.println("Seleziona un Merchant (inserisci l'ID) o premi 0 per tornare al menu principale:");

			int choice = scanner.nextInt();
			scanner.nextLine();

			if (choice == 0) {
				return;
			}

			Merchant selectedMerchant = merchantRepository.findById(choice).orElse(null);

			if (selectedMerchant != null) {
				System.out.println("Merchant selezionato: " + selectedMerchant.getName());
				displayOptions(choice);

			} else {
				System.out.println("Merchant non trovato. Riprova.");
			}
		}
	}

	private void displayMerchantList(List<Merchant> merchants) {
		for (Merchant merchant : merchants) {
			System.out.println(merchant.getId() + ". " + merchant.getName());
		}
	}

	private void displayOptions(int merchantId) {
		while (true) {
			System.out.println("Seleziona un'opzione:");
			System.out.println("1. Visualizza programma fedeltà");
			System.out.println("2. Crea nuovo programma fedeltà");
			System.out.println("3. Aderisci ad un Programma fedeltà in coalizione");
			System.out.println("4. Aggiungi un Benefit al programma Fedeltà");
			System.out.println("5. Visualizza Benefit di un programma Fedeltà");
			System.out.println("6. Crea utenza per dipendente");
			System.out.println("0. Esci");

			int option = scanner.nextInt();
			scanner.nextLine();

			switch (option) {
			case 1:
				viewMerchantLoyaltyProgram(merchantId);
				break;
			case 2:
				createLoyaltyProgram(merchantId);
				break;
			case 3:
				joinCoalition(merchantId);
				break;
			case 4:
				createBenefit(merchantId);
				break;
			case 5:
				viewBenefit(merchantId);
				break;
			case 6:
				createEmployee(merchantId);
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

	private void viewBenefit(int merchantId) {
		viewMerchantLoyaltyProgram(merchantId);
		System.out.print("Inserisci il codice del programma di fedeltà: ");
		int programId = scanner.nextInt();

		LoyaltyProgram program = loyaltyProgramService.getById(programId)
				.orElseThrow(() -> new RuntimeException("Programma di fedeltà non trovato."));

		Map<Integer, List<Benefit>> benefitsByLevel = loyaltyProgramService.getBenefitsByLoyaltyProgram(programId);

		for (Level level : program.getLevels()) {
			System.out.println("\nLivello: " + level.getName());
			List<Benefit> benefits = benefitsByLevel.get(level.getId());
			if (benefits.isEmpty()) {
				System.out.println("  Nessun benefit disponibile per questo livello.");
			} else {
				for (Benefit benefit : benefits) {
					System.out.println("  Punti necessari: " + benefit.getPointsRequired() + " - Nome:"
							+ benefit.getName() + " - " + benefit.getDescription());
				}
			}
		}

	}
	@Transactional
	private void joinCoalition(int merchantId) {
	    Merchant merchant = merchantRepository.findById(merchantId).orElse(null);
	    if (merchant == null) {
	        System.out.println("Merchant non trovato.");
	        return;
	    }
	    System.out.println("\n\nAdesione ad una Coalizione per " + merchant.getName() + "\nCoalizioni Disponibili:\n");

	    List<LoyaltyProgram> currentCoalitions = merchant.getPartnerships().stream()
	                                                    .map(Partnership::getLoyaltyProgram)
	                                                    .collect(Collectors.toList());

	    List<LoyaltyProgram> coalitions = loyaltyProgramService.getCoalitions().stream()
	              .filter(lp -> !currentCoalitions.contains(lp))
	              .filter(lp -> lp.getPartnerships().stream().noneMatch(p -> p.getMerchant().getId() == merchantId))
	              .collect(Collectors.toList());

	    if (coalitions.isEmpty()) {
	        System.out.println("Non ci sono coalizioni disponibili a cui aderire al momento.");
	        return;
	    }

	    for (int i = 0; i < coalitions.size(); i++) {
	        System.out.println((i + 1) + ". " + coalitions.get(i).getProgramName());
	    }

	    System.out.print("Scegli il numero della coalizione a cui vuoi aderire (0 per uscire): ");
	    try {
	        int choice = scanner.nextInt();
	        scanner.nextLine(); // Pulizia del buffer dopo la lettura di un numero

	        if (choice == 0) {
	            System.out.println("Uscita dalla procedura di adesione.");
	            return;
	        } else if (choice < 1 || choice > coalitions.size()) {
	            System.out.println("Scelta non valida.");
	            return;
	        }

	        LoyaltyProgram selectedProgram = coalitions.get(choice - 1);
	        merchantService.joinCoalition(merchant, selectedProgram);
	        System.out.println("Adesione avvenuta con successo alla coalizione: " + selectedProgram.getProgramName());
	    } catch (InputMismatchException e) {
	        System.out.println("Errore: input non valido. Inserisci un numero.");
	        scanner.nextLine(); // Pulizia del buffer
	    }
	}

	private void viewMerchantLoyaltyProgram(int merchantId) {
		Merchant merchant = merchantRepository.findById(merchantId).orElse(null);
		System.out.println("\n=========================================================\n"
				+ "Lista dei programmi fedeltà di " + merchant.getName() + ":\n");
		List<Partnership> partnerships = merchant.getPartnerships();
		if (partnerships.isEmpty()) {
			System.out.println("Nessun programma fedeltà associato a questo commerciante.");
		} else {
			partnerships.forEach(partnership -> {
				LoyaltyProgram loyaltyProgram = partnership.getLoyaltyProgram();
				System.out.println("CODICE: " + loyaltyProgram.getId() + ", Nome: " + loyaltyProgram.getProgramName());
			});
		}
		System.out.println("=========================================================\n");
	}

	private void createLoyaltyProgram(int merchantId) {
		Merchant merchant = merchantRepository.findById(merchantId).orElse(null);
		System.out.println("Creazione di un nuovo programma fedeltà per " + merchant.getName());
		System.out.println("Inserisci il nome del programma fedeltà:");
		String loyaltyProgramName = scanner.nextLine();

		System.out.println("Inserisci una breve descrizione del programma:");
		String loyaltyProgramDescription = scanner.nextLine();

		System.out.println("Il programma è aperto a coalizione? (true/false):");
		boolean coalitionOpen = scanner.nextBoolean();
		scanner.nextLine();

		LoyaltyProgram newProgram = merchantService.createLoyaltyProgram(loyaltyProgramName, loyaltyProgramDescription,
				coalitionOpen, merchantId);
		boolean addMoreLevels = true;
		while (addMoreLevels) {

			System.out.println("Definisci un nome per il livello di fedeltà del programma:");
			String levelName = scanner.nextLine();

			System.out.println("Definisci una breve descrizione per il livello:");
			String levelDescription = scanner.nextLine();

			System.out.println("Definisci una soglia minima per questo livello:");
			int levelThreshold = scanner.nextInt();

			loyaltyProgramService.createLevel(newProgram.getId(), levelName, levelDescription, levelThreshold);

			System.out.println("Vuoi aggiungere un altro livello? (true/false):");
			addMoreLevels = scanner.nextBoolean();
			scanner.nextLine();
		}

		System.out.println("Programma fedeltà creato con successo con i livelli definiti.");

	}

	private void createBenefit(int merchantId) {

		viewMerchantLoyaltyProgram(merchantId);

		System.out.print("Inserisci il codice del programma di fedeltà: ");
		int programId = scanner.nextInt();

		System.out.println("Livelli nel programma selezionato:");
		List<Level> levels = loyaltyProgramService.getLevelsOfLoyaltyProgram(programId);
		levels.forEach(level -> System.out.println("Codice Livello: " + level.getId() + ", Nome: " + level.getName()));

		System.out.print("Inserisci il codice del livello di fedeltà (0 per qualsiasi livello): ");
		int levelId = scanner.nextInt();

		scanner.nextLine();
		System.out.print("Inserisci il nome del benefit: ");
		String benefitName = scanner.nextLine();

		System.out.print("Inserisci una descrizione per il benefit: ");
		String benefitDescription = scanner.nextLine();

		System.out.print("Inserisci i punti necessari per il benefit (0 se non applicabile): ");
		int pointsRequired = scanner.nextInt();

		System.out.print("Scegli il tipo di benefit (COUPON, CASHBACK, REWARD, POINTS_REWARD): ");
		String benefitType = scanner.next().toUpperCase();

		Object[] additionalParams = null;

		switch (benefitType) {
		case "COUPON":
			System.out.print("Inserisci la data di scadenza del coupon (YYYY-MM-DD): ");
			String expirationDateString = scanner.next();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date expirationDate = null;

			try {
				expirationDate = dateFormat.parse(expirationDateString);
			} catch (ParseException e) {
				System.out.println("Formato della data non valido.");
				return;
			}
			additionalParams = new Object[] { expirationDate };
			break;
		case "CASHBACK":
			System.out.print("Inserisci il tasso di cashback (es. 12,5 per 12,5%): ");
			double cashBackRate = scanner.nextDouble() / 100;
			additionalParams = new Object[] { cashBackRate };
			break;
		case "REWARD":
			System.out.print("Inserisci la quantità disponibile del premio: ");
			int quantity = scanner.nextInt();
			additionalParams = new Object[] { quantity };
			break;
		case "POINTS_REWARD":
			System.out.print("Quanti punti verranno guadagnati?: ");
			int points = scanner.nextInt();
			System.out.print("Per quale importo speso?: ");
			double moneySpent = scanner.nextDouble();
			additionalParams = new Object[] { points, moneySpent };
			break;
		default:
			System.out.println("Tipo di benefit non valido.");
			return;
		}

		try {
			merchantService.createBenefit(benefitType, benefitName, benefitDescription, pointsRequired, merchantId,
					programId, levelId, additionalParams);

			System.out.println("Benefit creato con successo");
		} catch (IllegalArgumentException e) {
		    System.out.println("Errore nella creazione del benefit: " + e.getMessage());

		}


	}

	public void createEmployee(int merchantId) {
		System.out.println("Creazione di un nuovo account dipendente.");

		System.out.print("Inserisci il nome del dipendente: ");
		String name = scanner.nextLine();

		System.out.print("Inserisci la matricola del dipendente: ");
		String matricola = scanner.nextLine();
		String username = "user";
		String password = "password";

		merchantService.createNewEmployee(merchantId, name, matricola, username, password);

		System.out.println("Dipendente creato con successo: " + name);
	}

	public Merchant insertMerchant() {
		System.out.print("inserisci il nome dell'azienda");
		String name = scanner.nextLine();
		System.out.print("inserisci la descrizione");
		String description = scanner.nextLine();
		Merchant newMerchant = new Merchant(name, description);
		System.out.println("Mechant creato con successo");
		return merchantRepository.save(newMerchant);
	}
}