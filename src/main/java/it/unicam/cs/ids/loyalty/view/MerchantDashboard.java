package it.unicam.cs.ids.loyalty.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Customer;
import it.unicam.cs.ids.loyalty.model.Employee;
import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.Partnership;
import it.unicam.cs.ids.loyalty.model.Transaction;
import it.unicam.cs.ids.loyalty.repository.EmployeeRepository;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.service.DefaultLoyaltyProgramService;
import it.unicam.cs.ids.loyalty.service.DefaultMerchantService;
import it.unicam.cs.ids.loyalty.util.PasswordGenerator;
import jakarta.transaction.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
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
	private final EmployeeRepository employeeRepository;
	private DefaultMerchantService merchantService;
	private DefaultLoyaltyProgramService loyaltyProgramService;
	private Scanner scanner = new Scanner(System.in);

	@Autowired
	public MerchantDashboard(MerchantRepository merchantRepository, EmployeeRepository employeeRepository,
			DefaultMerchantService merchantService, DefaultLoyaltyProgramService loyaltyProgramService) {
		this.merchantRepository = merchantRepository;
		this.merchantService = merchantService;
		this.employeeRepository = employeeRepository;
		this.loyaltyProgramService = loyaltyProgramService;
	}

	public void login() {
		while (true) {
			System.out.println("Elenco dei Merchant:");

			List<Merchant> merchants = merchantRepository.findAll();
			displayMerchantList(merchants);

			System.out.println("Seleziona un Merchant (inserisci l'ID) o premi 0 per tornare al menu principale:");

			int choice;
			try {
				choice = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Input non valido. Inserisci un numero intero.");
				return;
			}

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
			System.out.println("4. Gestisci i livelli dei tuoi programma Fedeltà");
			System.out.println("5. Aggiungi un Benefit al programma Fedeltà");
			System.out.println("6. Visualizza Benefit di un programma Fedeltà");
			System.out.println("7. Crea utenza per dipendente");
			System.out.println("8. Monitoraggio delle Transazioni dei clienti");
			
			System.out.println("0. Esci");

			int option;
			try {
				option = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Input non valido. Inserisci un numero intero.");
				return;
			}

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
				manageLevels(merchantId);
				break;
			case 5:
				createBenefit(merchantId);
				break;
			case 6:
				viewBenefit(merchantId);
				break;
			case 7:
				createEmployee(merchantId);
				break;
			case 8:
			    monitorTransaction(merchantId);
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

	private void monitorTransaction(int merchantId) {
		
		viewMerchantLoyaltyProgram(merchantId);
		int programId = getProgramIdInput();

		LoyaltyProgram program = getLoyaltyProgram(programId);
		if (program == null) {
			return;
		}
		Map<String, List<Transaction>> transactionsByBenefitType = loyaltyProgramService.getTransactionsByBenefitType(program);

	    for (Map.Entry<String, List<Transaction>> entry : transactionsByBenefitType.entrySet()) {
	        String benefitType = entry.getKey();
	        List<Transaction> transactions = entry.getValue();

	        System.out.println("Benefit Type: " + benefitType);
	        for (Transaction transaction : transactions) {
	            Customer customer=transaction.getMembershipAccount().getMembership().getCustomer();
	            int pointsChange = transaction.getPointsEarned() - transaction.getPointsSpent();
				String sign = pointsChange >= 0 ? "+ " : "- ";
				System.out.println("Cliente: "+ customer.getCognome()+
						" "+customer.getNome()+
				" ID Transazione: " + transaction.getId() + ", Data: "
						+ transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
						+ ", Descrizione(Benefit): " + transaction.getLoyaltyBenefit().getName() + ", Punti: " + sign
						+ Math.abs(pointsChange));
			};
	            System.out.println(); 
	        }
	    }
        
    

	private void manageLevels(int merchantId) {
		viewMerchantLoyaltyProgram(merchantId);

		int programId = getProgramIdInput();

		LoyaltyProgram program = getLoyaltyProgram(programId);
		if (program == null) {
			return;
		}

		List<Level> levels = getLevels(programId);
		if (levels.isEmpty()) {
			System.out.println("Nessun livello disponibile per il programma selezionato.");
			return;
		}

		displayLevels(levels);

		int choice = getOptionChoice();

		switch (choice) {
		case 1:
			modifyExistingLevel(program);
			break;
		case 2:
			addNewLevel(program);
			break;
		case 3:
			System.out.println("Nessuna modifica effettuata.");
			break;
		default:
			System.out.println("Opzione non valida. Riprova.");
			break;
		}
	}

	private void modifyExistingLevel(LoyaltyProgram program) {
		int levelId;
		try {
			System.out.print("Inserisci l'ID del livello da modificare: ");
			levelId = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("Input non valido. Inserisci un numero intero.");
			return;
		}

		System.out.println("Quale attributo vuoi modificare?");
		System.out.println("1. Nome del livello");
		System.out.println("2. Descrizione del livello");
		System.out.println("3. Soglia minima del livello");
		System.out.println("4. Modifica tutto");

		int choice;
		try {
			choice = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("Input non valido. Inserisci un numero intero.");
			return;
		}

		String newLevelName = null;
		String newLevelDescription = null;
		Integer newThreshold = null;

		switch (choice) {
		case 1:
			System.out.print("Inserisci il nuovo nome del livello: ");
			newLevelName = scanner.nextLine();
			break;
		case 2:
			System.out.print("Inserisci la nuova descrizione del livello: ");
			newLevelDescription = scanner.nextLine();
			break;
		case 3:
			System.out.print("Inserisci la nuova soglia minima del livello: ");
			try {
				newThreshold = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Input non valido. Inserisci un numero intero.");
				return;
			}
			break;
		case 4:
			System.out.print("Inserisci il nuovo nome del livello: ");
			newLevelName = scanner.nextLine();

			System.out.print("Inserisci la nuova descrizione del livello: ");
			newLevelDescription = scanner.nextLine();

			System.out.print("Inserisci la nuova soglia minima del livello: ");
			try {
				newThreshold = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Input non valido. Inserisci un numero intero.");
				return;
			}
			break;
		default:
			System.out.println("Opzione non valida. Riprova.");
			return;
		}

		try {
			loyaltyProgramService.updateLevel(levelId, newLevelName, newLevelDescription, newThreshold);
			System.out.println("Livello modificato con successo.");
		} catch (IllegalArgumentException e) {
			System.out.println("Errore nella modifica del livello: " + e.getMessage());
		}
	}

	private int getProgramIdInput() {
		try {
			System.out.print("Inserisci il codice del programma di fedeltà: ");
			return scanner.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Input non valido. Inserisci un numero intero.");
			return -1;
		} finally {
			scanner.nextLine();
		}
	}

	private LoyaltyProgram getLoyaltyProgram(int programId) {
		try {
			return loyaltyProgramService.getById(programId)
					.orElseThrow(() -> new RuntimeException("Programma di fedeltà non trovato."));
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private List<Level> getLevels(int programId) {
		try {
			return loyaltyProgramService.getLevelsOfLoyaltyProgram(programId);
		} catch (RuntimeException e) {
			System.out.println("Errore nella lettura dei livelli del programma di fedeltà: " + e.getMessage());
			return Collections.emptyList();
		}
	}

	private void displayLevels(List<Level> levels) {
		System.out.println("Livelli nel programma selezionato:");
		levels.forEach(level -> System.out.println("Codice Livello: " + level.getId() + ", Nome: " + level.getName()
				+ ", Soglia minima: " + level.getPointsThreshold()));
	}

	private int getOptionChoice() {
		try {
			System.out.println("Seleziona un'opzione:");
			System.out.println("1. Modifica un livello esistente");
			System.out.println("2. Aggiungi un nuovo livello");
			System.out.println("3. Non apportare modifiche");
			return scanner.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Input non valido. Inserisci un numero intero.");
			return -1;
		} finally {
			scanner.nextLine();
		}
	}

	private void viewBenefit(int merchantId) {
		viewMerchantLoyaltyProgram(merchantId);

		int programId;
		try {
			System.out.print("Inserisci il codice del programma di fedeltà: ");
			programId = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("Input non valido. Inserisci un numero intero.");
			return;
		}

		LoyaltyProgram program;
		try {
			program = loyaltyProgramService.getById(programId)
					.orElseThrow(() -> new RuntimeException("Programma di fedeltà non trovato."));
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			return;
		}

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

		List<LoyaltyProgram> currentCoalitions = merchant.getPartnerships().stream().map(Partnership::getLoyaltyProgram)
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
			scanner.nextLine();

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
			scanner.nextLine();
		}
	}

	private List<Partnership> viewMerchantLoyaltyProgram(int merchantId) {
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
		return partnerships;
	}

	private void createLoyaltyProgram(int merchantId) {
		Merchant merchant = merchantRepository.findById(merchantId).orElse(null);

		if (merchant == null) {
			System.out.println("Merchant non trovato.");
			return;
		}

		System.out.println("Creazione di un nuovo programma fedeltà per " + merchant.getName());

		System.out.println("Inserisci il nome del programma fedeltà:");
		String loyaltyProgramName = scanner.nextLine();

		System.out.println("Inserisci una breve descrizione del programma:");
		String loyaltyProgramDescription = scanner.nextLine();

		System.out.println("Inserisci la data di scadenza del programma (formato YYYY-MM-DD):");
		String expirationDateStr = scanner.nextLine();

		LocalDate expirationDate;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			expirationDate = LocalDate.parse(expirationDateStr, formatter);
		} catch (DateTimeParseException e) {
			System.out.println("Formato data non valido. Si prega di inserire la data nel formato YYYY-MM-DD.");
			return;
		}

		System.out.println("Il programma è aperto a coalizione? (true/false):");
		boolean coalitionOpen;
		try {
			coalitionOpen = scanner.nextBoolean();
		} catch (InputMismatchException e) {
			System.out.println("Input non valido. Inserisci true o false.");
			return;
		} finally {
			scanner.nextLine();
		}

		LoyaltyProgram newProgram = merchantService.createLoyaltyProgram(loyaltyProgramName, loyaltyProgramDescription,
				coalitionOpen, merchantId, expirationDate);

		createLevelsForProgram(newProgram);

		System.out.println("Programma fedeltà creato con successo con i livelli definiti.");
	}

	private void createLevelsForProgram(LoyaltyProgram program) {
		boolean addMoreLevels = true;
		while (addMoreLevels) {
			try {
				addNewLevel(program);
			} catch (IllegalArgumentException e) {
		            System.out.println("Errore: " + e.getMessage());
		            System.out.println("Inserisci una soglia diversa per il livello.");
			}

			try {
				System.out.println("Vuoi aggiungere un altro livello? (true/false):");
				addMoreLevels = scanner.nextBoolean();
			} catch (InputMismatchException e) {
				System.out.println("Input non valido. Inserisci true o false.");
				return;
			} finally {
				scanner.nextLine();
			}
		}
	}

	private void addNewLevel(LoyaltyProgram program) {
		System.out.println("Definisci un nome per il livello di fedeltà del programma:");
		String levelName = scanner.nextLine();

		System.out.println("Definisci una breve descrizione per il livello:");
		String levelDescription = scanner.nextLine();

		int levelThreshold;
		try {
			System.out.println("Definisci una soglia minima per questo livello:");
			levelThreshold = scanner.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Input non valido. Inserisci un numero intero.");
			return;
		} finally {
			scanner.nextLine();
		}

		loyaltyProgramService.createLevel(program.getId(), levelName, levelDescription, levelThreshold);
	}

	private void createBenefit(int merchantId) {
		List<Partnership> lista = viewMerchantLoyaltyProgram(merchantId);
		if (lista.isEmpty())
			return;

		System.out.print("Inserisci il codice del programma di fedeltà: ");
		int programId;
		try {
			programId = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("Input non valido. Inserisci un numero intero.");
			return;
		}

		System.out.println("Livelli nel programma selezionato:");
		List<Level> levels;
		try {
			levels = loyaltyProgramService.getLevelsOfLoyaltyProgram(programId);
			levels.forEach(
					level -> System.out.println("Codice Livello: " + level.getId() + ", Nome: " + level.getName()));
		} catch (RuntimeException e) {
			System.out.println("Errore nella lettura dei livelli del programma di fedeltà: " + e.getMessage());
			return;
		}

		System.out.print("Inserisci il codice del livello di fedeltà (0 per qualsiasi livello): ");
		int levelId;
		try {
			levelId = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("Input non valido. Inserisci un numero intero.");
			return;
		}

		System.out.print("Inserisci il nome del benefit: ");
		String benefitName = scanner.nextLine();

		System.out.print("Inserisci una descrizione per il benefit: ");
		String benefitDescription = scanner.nextLine();

		System.out.print("Inserisci i punti necessari per il benefit (0 se non sono richiesti punti da spendere): ");
		int pointsRequired;
		try {
			pointsRequired = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("Input non valido. Inserisci un numero intero.");
			return;
		}

		System.out.print("Scegli il tipo di benefit (COUPON, CASHBACK, REWARD, POINTS_REWARD): ");
		String benefitType = scanner.next().toUpperCase();

		Object[] additionalParams = null;

		switch (benefitType) {
		case "COUPON":
			System.out.print("Inserisci la data di scadenza del coupon (YYYY-MM-DD): ");
			String expirationDateString = scanner.next();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date expirationDate;
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
			double cashBackRate;
			try {
				cashBackRate = scanner.nextDouble() / 100;
			} catch (InputMismatchException e) {
				System.out.println("Input non valido. Inserisci un numero.");
				return;
			}
			additionalParams = new Object[] { cashBackRate };
			break;
		case "REWARD":
			System.out.print("Inserisci la quantità disponibile del premio: ");
			int quantity;
			try {
				quantity = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Input non valido. Inserisci un numero intero.");
				return;
			}
			additionalParams = new Object[] { quantity };
			break;
		case "POINTS_REWARD":
			System.out.print("Quanti punti verranno guadagnati?: ");
			int points;
			try {
				points = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Input non valido. Inserisci un numero intero.");
				return;
			}
			System.out.print("Per quale importo speso?: ");
			double moneySpent;
			try {
				moneySpent = scanner.nextDouble();
			} catch (InputMismatchException e) {
				System.out.println("Input non valido. Inserisci un numero.");
				return;
			}
			additionalParams = new Object[] { points, moneySpent };
			break;
		default:
			System.out.println("Tipo di benefit non valido.");
			return;
		}

		try {
			loyaltyProgramService.createBenefit(benefitType, benefitName, benefitDescription, pointsRequired,
					merchantId, programId, levelId, additionalParams);

			System.out.println("Benefit creato con successo");
		} catch (IllegalArgumentException e) {
			System.out.println("Errore nella creazione del benefit: " + e.getMessage());
		}
	}

	private void createEmployee(int merchantId) {
		boolean usernameChosen = false;
		String password = null;
		Merchant merchant = merchantRepository.findById(merchantId)
				.orElseThrow(() -> new IllegalArgumentException("Merchant non trovato."));
		String username = null;
		System.out.println("Creazione di un nuovo account dipendente.");

		System.out.print("Inserisci il nome del dipendente: ");
		String name = scanner.nextLine();

		System.out.print("Inserisci la matricola del dipendente: ");
		String matricola = scanner.nextLine();

		while (!usernameChosen) {
			System.out.print("Scegli una username per il dipendente: ");
			username = scanner.nextLine();
			Optional<Employee> employeeOptional = employeeRepository.findByUsername(username);

			if (employeeOptional.isPresent()) {
				Employee existingEmployee = employeeOptional.get();

				if (existingEmployee.getMerchant().equals(merchant)) {
					System.out.println(
							"Hai già un dipendente con questa username. Vuoi procedere ad un reset password? (S per sì, altro per scegliere una nuova username)");
					String choice = scanner.nextLine();
					if ("S".equalsIgnoreCase(choice)) {
						password = PasswordGenerator.generatePassword();
						existingEmployee.setPassword(password);
						employeeRepository.save(existingEmployee);
						System.out.println("La nuova password è: " + password);
						return;
					}

				} else {

					System.out.println("Username non disponibile, sceglierne un'altra.");
				}
			} else {


				merchantService.createNewEmployee(merchantId, name, matricola, username, password);
				System.out.println("Dipendente creato con successo: " + name + " con username: " + username
						+ " e password: " + password);
				usernameChosen = true;
			}
		}
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