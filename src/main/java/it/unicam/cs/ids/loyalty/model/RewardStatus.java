package it.unicam.cs.ids.loyalty.model;

public enum RewardStatus {
	PENDING("In Attesa"), DELIVERED("Consegnato"), USED("Utilizzato");

	private final String displayValue;

	RewardStatus(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}
}
