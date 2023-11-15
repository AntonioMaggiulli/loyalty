package it.unicam.cs.ids.loyalty.model;
public class MemberCard {
	 
	public String getNumero() {
		return numero;
	}
 
	public void setNumero(String numero) {
		this.numero = numero;
	}
 
	public MembershipAccount getConto() {
		return conto;
	}
 
	public void setConto(MembershipAccount conto) {
		this.conto = conto;
	}
 
	private String numero;
	private MembershipAccount conto;
}
