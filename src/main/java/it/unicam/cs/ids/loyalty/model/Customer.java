package it.unicam.cs.ids.loyalty.model;

import java.util.List;

public class Customer {
	public int getId() {
		return id;
	}
 
	public void setId(int id) {
		this.id = id;
	}
 
	public String getUsername() {
		return username;
	}
 
	public void setUsername(String username) {
		this.username = username;
	}
 
	public String getPassword() {
		return password;
	}
 
	public void setPassword(String password) {
		this.password = password;
	}
 
	public String getCognome() {
		return cognome;
	}
 
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
 
	public String getNome() {
		return nome;
	}
 
	public void setNome(String nome) {
		this.nome = nome;
	}
 
	public List<MemberCard> getTessereClientes() {
		return tessereClientes;
	}
 
	public void setTessereClientes(List<MemberCard> tessereClientes) {
		this.tessereClientes = tessereClientes;
	}
 
	private int id;
	private String username;
	private String password;
	private String cognome;
	private String nome;
	private List<MemberCard> tessereClientes;
 
}
