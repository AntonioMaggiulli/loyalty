package it.unicam.cs.ids.loyalty.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unicam.cs.ids.loyalty.model.Employee;
import it.unicam.cs.ids.loyalty.repository.EmployeeRepository;

import java.util.Scanner;

@Component
public class EmployeeDashboard {

	private final EmployeeRepository employeeRepository;
	private Scanner scanner = new Scanner(System.in);
	private int merchantId;

	@Autowired
	public EmployeeDashboard(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;

	}

	public void login() {
		System.out.println("Inserisci l'ID dell'impiegato:");
		int employeeId = scanner.nextInt();
		scanner.nextLine();

		Employee employee = employeeRepository.findById(employeeId).orElse(null);
		merchantId = employee.getMerchant().getId();
		if (employee != null) {
			System.out.println("Impiegato identificato: " + employee.getName());
			displayOptions(employee);
		}
	}

	private void displayOptions(Employee employee) {
		while (true) {
			System.out.println("Seleziona un'opzione:");
			System.out.println("1. Convalida acquisto");
			System.out.println("0. Esci");

			int option = scanner.nextInt();
			scanner.nextLine();

			switch (option) {
			case 1:
				validatePurchase(merchantId);
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

	private void validatePurchase(int merchantId) {
		System.out.println("Inserisci l'importo dell'acquisto:");
		double value = scanner.nextDouble();

		// Logica per la convalida dell'acquisto
		// Esempio: employeeService.validatePurchase(purchaseCode, employee);
		System.out.println("Acquisto convalidato.");
	}
}
