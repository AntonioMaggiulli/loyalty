package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date date;
    private int rating;
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id") // Assumi che esista una colonna 'writer_id' nella tabella 'feedback'
    private Customer writer;

    public Feedback() {
        // Costruttore vuoto per JPA
    }

    public Feedback(String text, Date date, int rating, Customer writer) {
        this.text = text;
        this.date = date;
        this.rating = rating;
        this.writer = writer;
    }


    // Getters and Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Customer getCustomer() {
        return writer;
    }

    public void setCustomer(Customer customer) {
        this.writer = customer;
    }

	public Customer getWriter() {
		return writer;
	}

	public void setWriter(Customer writer) {
		this.writer = writer;
	}
}