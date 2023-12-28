package it.unicam.cs.ids.loyalty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.unicam.cs.ids.loyalty.view.CustomerDashboard;
import it.unicam.cs.ids.loyalty.view.MerchantDashboard;

import java.util.Scanner;

@SpringBootApplication
public class LoyaltyApplication implements CommandLineRunner {

	@Autowired
	private MerchantDashboard merchantDashboard;
	@Autowired
	private CustomerDashboard customerDashboard;

	public static void main(String[] args) {
		SpringApplication.run(LoyaltyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Benvenuti nella piattaforma fedelta'");
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("Seleziona un'opzione:");
			System.out.println("1. Login come Merchant");
			System.out.println("2. Registrati come Merchant");
			System.out.println("3. Login come Cliente");
			System.out.println("4. Registrati come Cliente");
			System.out.println("0. Esci");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				merchantDashboard.login();
				break;
			case 3:
				customerDashboard.login();
				break;
			case 4:
				customerDashboard.insertCustomer();
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