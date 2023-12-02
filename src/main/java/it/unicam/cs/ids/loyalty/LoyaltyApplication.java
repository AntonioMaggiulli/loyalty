package it.unicam.cs.ids.loyalty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.unicam.cs.ids.loyalty.view.MerchantDashboard;

import java.util.Scanner;

@SpringBootApplication
public class LoyaltyApplication implements CommandLineRunner {

    @Autowired
    private MerchantDashboard merchantDashboard;

    public static void main(String[] args) {
        SpringApplication.run(LoyaltyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        
        System.out.println("Hello, Spring Boot CommandLine App!");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Benvenuto! Seleziona un'opzione:");
            System.out.println("1. Login come Merchant");
            System.out.println("2. Registrati come Merchant");
            System.out.println("3. Login come Cliente");
            System.out.println("4. Registrati come Cliente");
            System.out.println("0. Esci");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consuma il newline

            switch (choice) {
                case 1:
                    loginMerchant();
                    break;
                // Altre opzioni del menu
                case 0:
                    System.out.println("Arrivederci!");
                    System.exit(0);
                default:
                    System.out.println("Opzione non valida. Riprova.");
                    break;
            }
        }
    }

    private void loginMerchant() {
        merchantDashboard.start();
    }
}
