package it.unicam.cs.ids.loyalty.model;

public class Level {
    private int uniqueId;
    private String name;
    private String description;
 
    public Level(int uniqueId, String name, String description) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
    }
 
    public int getUniqueId() {
        return uniqueId;
    }
 
    public String getName() {
        return name;
    }
 
    public String getDescription() {
        return description;
    }
}
 
