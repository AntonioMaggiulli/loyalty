package it.unicam.cs.ids.loyalty.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;

import java.util.List;
import java.util.Scanner;

@Component
public class MerchantDashboard {

    private final MerchantRepository merchantRepository;

    @Autowired
    public MerchantDashboard(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Elenco dei Merchant:");

            List<Merchant> merchants = merchantRepository.findAll();
            displayMerchantList(merchants);

            System.out.println("Seleziona un Merchant (inserisci l'ID) o premi 0 per tornare al menu principale:");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consuma il newline

            if (choice == 0) {
                return;
            }

            Merchant selectedMerchant = merchantRepository.findById(choice).orElse(null);

            if (selectedMerchant != null) {
                System.out.println("Merchant selezionato: " + selectedMerchant.getName());
                // Puoi aggiungere ulteriori azioni qui
                // Ad esempio, chiamare un metodo per gestire le operazioni del Merchant selezionato
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

		
	}
	