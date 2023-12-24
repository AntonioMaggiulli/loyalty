package it.unicam.cs.ids.loyalty.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.Partnership;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.service.DefaultLoyaltyProgramService;
import it.unicam.cs.ids.loyalty.service.DefaultMerchantService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component
public class MerchantDashboard {

	private final MerchantRepository merchantRepository;
	private DefaultMerchantService merchantService;
	private DefaultLoyaltyProgramService loyaltyProgramService;
	private Scanner scanner = new Scanner(System.in);
	private List<Partnership> partnerships;

	@Autowired
	public MerchantDashboard(MerchantRepository merchantRepository, DefaultMerchantService merchantService,
			DefaultLoyaltyProgramService loyaltyProgramService) {
		this.merchantService = merchantService;
		this.merchantRepository = merchantRepository;
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
				displayOptions(selectedMerchant);

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

	private void displayOptions(Merchant merchant) {
		while (true) {
			System.out.println("Seleziona un'opzione:");
			System.out.println("1. Visualizza programma fedeltà");
			System.out.println("2. Crea nuovo programma fedeltà");
			System.out.println("3. Aggiungi un Benefit al programma Fedeltà");
			System.out.println("0. Esci");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				viewLoyaltyProgram(merchant);
				break;
			case 2:
				createLoyaltyProgram(merchant);
				break;
			case 3:
				createBenefit(merchant);
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

	private void viewLoyaltyProgram(Merchant merchant) {

		System.out.println("Lista dei programmi fedeltà di " + merchant.getName());
		merchantService.viewLoyaltyProgramsOfMerchant(merchant.getId());
	}

	private void createLoyaltyProgram(Merchant merchant) {

		System.out.println("Creazione di un nuovo programma fedeltà per " + merchant.getName());
		System.out.println("Inserisci il nome del programma fedeltà:");
		String loyaltyProgramName = scanner.nextLine();

		System.out.println("Inserisci una breve descrizione del programma:");
		String loyaltyProgramDescription = scanner.nextLine();

		System.out.println("Il programma è aperto a coalizione? (true/false):");
		boolean coalitionOpen = scanner.nextBoolean();
		scanner.nextLine();

		LoyaltyProgram newProgram = merchantService.createLoyaltyProgram(loyaltyProgramName, loyaltyProgramDescription,
				coalitionOpen, merchant.getId());
		boolean addMoreLevels = true;
		while (addMoreLevels) {

			System.out.println("Definisci un nome per il livello di fedeltà del programma:");
			String levelName = scanner.nextLine();

			System.out.println("Definisci una breve descrizione per il livello:");
			String levelDescription = scanner.nextLine();

			loyaltyProgramService.createLevel(newProgram.getId(), levelName, levelDescription);

			System.out.println("Vuoi aggiungere un altro livello? (true/false):");
			addMoreLevels = scanner.nextBoolean();
			scanner.nextLine();
		}

		System.out.println("Programma fedeltà creato con successo con i livelli definiti.");

	}

	private void createBenefit(Merchant merchant) {
		merchantService.viewLoyaltyProgramsOfMerchant(merchant.getId());

		System.out.print("Inserisci il codice del programma di fedeltà: ");
		int programId = scanner.nextInt();

		System.out.println("Livelli nel programma selezionato:");
		List<Level> levels = loyaltyProgramService.getLevelsOfLoyaltyProgram(programId);
		levels.forEach(level -> System.out.println("Codice Livello: " + level.getId() + ", Nome: " + level.getName()));

		System.out.print("Inserisci il codice del livello di fedeltà: ");
		int levelId = scanner.nextInt();

		scanner.nextLine();
		System.out.print("Inserisci il nome del benefit: ");
		String benefitName = scanner.nextLine();

		System.out.print("Inserisci una descrizione per il benefit: ");
		String benefitDescription = scanner.nextLine();

		System.out.print("Inserisci i punti necessari per il benefit (0 se non applicabile): ");
		int pointsRequired = scanner.nextInt();

		System.out.print("Scegli il tipo di benefit (COUPON, CASHBACK, REWARD, POINTS_REWARD): ");
		String benefitType = scanner.next();

		Object[] additionalParams = null;

		switch (benefitType.toUpperCase()) {
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
			System.out.print("Inserisci il tasso di cashback (es. 0.1 per 10%): ");
			double cashBackRate = scanner.nextDouble();
			additionalParams = new Object[] { cashBackRate };
			break;
		case "REWARD":
			System.out.print("Inserisci la quantità disponibile del premio: ");
			int quantity = scanner.nextInt();
			additionalParams = new Object[] { quantity };
			break;
		case "POINTS_REWARD":
			System.out.print("Inserisci il numero di punti guadagnati per ogni euro speso: ");
			int points = scanner.nextInt();
			System.out.print("Inserisci l'importo di denaro speso per guadagnare i punti: ");
			double moneySpent = scanner.nextDouble();
			additionalParams = new Object[] { points, moneySpent };
			break;
		default:
			System.out.println("Tipo di benefit non valido.");
			return;
		}

		Benefit createdBenefit = merchantService.createBenefit(benefitType, benefitName, benefitDescription,
				pointsRequired, merchant.getId(), programId, levelId, additionalParams);

		System.out.println("Benefit creato con successo: " + createdBenefit.getName());
	}

}