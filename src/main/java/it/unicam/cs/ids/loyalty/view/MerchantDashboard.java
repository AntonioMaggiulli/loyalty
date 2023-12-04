package it.unicam.cs.ids.loyalty.view;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.service.DefaultLoyaltyProgramService;

 
import java.util.List;
import java.util.Scanner;
 

 
@Component
public class MerchantDashboard {
 
    private final  MerchantRepository merchantRepository;
	private DefaultLoyaltyProgramService loyaltyProgramService;
 
    @Autowired
    public MerchantDashboard(MerchantRepository merchantRepository,DefaultLoyaltyProgramService loyaltyProgramService) {
        this.loyaltyProgramService = loyaltyProgramService;
        this.merchantRepository = merchantRepository;
    }
 
    public void login() {
        Scanner scanner = new Scanner(System.in);
 
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
            System.out.println("1. Modifica programma fedeltà");
            System.out.println("2. Crea nuovo programma fedeltà");
            System.out.println("3. Visualizza statistiche");
            System.out.println("0. Esci");
 
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine(); 
 
            switch (choice) {
                case 1:
                    editLoyaltyProgram(merchant);
                    break;
                case 2:
                    createLoyaltyProgram(merchant);
                    break;
                case 3:
                    displayStatistics(merchant);
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
 
    private void editLoyaltyProgram(Merchant merchant) {
        // TODO
        System.out.println("Modifica del programma fedeltà di " + merchant.getName());
    }
 
    private void createLoyaltyProgram(Merchant merchant) {
    	
    	Scanner scanner = new Scanner(System.in);
        System.out.println("Creazione di un nuovo programma fedeltà per " + merchant.getName());
        System.out.println("Inserisci il nome del programma fedeltà:");
        String loyaltyProgramName = scanner.nextLine();
 
        System.out.println("Inserisci una breve descrizione del programma:");
        String loyaltyProgramDescription = scanner.nextLine();
 
        System.out.println("Il programma è aperto a coalizione? (true/false):");
        boolean coalitionOpen = scanner.nextBoolean();
        scanner.nextLine(); 
 
        // Chiamiamo il servizio per la creazione del programma fedeltà
        loyaltyProgramService.createLoyaltyProgram(loyaltyProgramName, loyaltyProgramDescription,
                coalitionOpen, merchant.getId());
 
    }
 
    private void displayStatistics(Merchant merchant) {
    	// TODO
        System.out.println("Statistiche per " + merchant.getName());
    }
	}