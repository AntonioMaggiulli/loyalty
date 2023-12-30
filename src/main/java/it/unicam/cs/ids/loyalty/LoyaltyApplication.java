package it.unicam.cs.ids.loyalty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.unicam.cs.ids.loyalty.view.CustomerDashboard;
import it.unicam.cs.ids.loyalty.view.EmployeeDashboard;
import it.unicam.cs.ids.loyalty.view.MerchantDashboard;

import java.util.Scanner;

@SpringBootApplication
public class LoyaltyApplication implements CommandLineRunner {

	@Autowired
	private MerchantDashboard merchantDashboard;
	@Autowired
	private CustomerDashboard customerDashboard;
	@Autowired
	private EmployeeDashboard employeeDashboard;

	public static void main(String[] args) {
		SpringApplication.run(LoyaltyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("\n\n=========================================================\n"
				+ "Benvenuti nella DEMO piattaforma fedelta'\n"
				+ "In questa DEMO per semplicità il login è soltanto simulato.\n"
				+ "Per tutti i tipi di utenti registrati sarà sufficente selezionare un utente attraverso il suo codice\n\n");
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("Seleziona un'opzione:");
			System.out.println("1. Login come Merchant");
			System.out.println("2. Registrati come Merchant");
			System.out.println("3. Login come Cliente");
			System.out.println("4. Registrati come Cliente");
			System.out.println("5. Login come Impegato");
			System.out.println("0. Esci");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				merchantDashboard.login();
				break;
			case 2:
				merchantDashboard.insertMerchant();
				break;
			case 3:
				customerDashboard.login();
				break;
			case 4:
				customerDashboard.insertCustomer();
				break;
			case 5:
				employeeDashboard.login();
				break;
			case 0:
				System.out.println("Arrivederci!");
				System.exit(0);
			default:
				System.out.println("Opzione non implementata. Riprova.");
				break;
			}

		}

	}

}