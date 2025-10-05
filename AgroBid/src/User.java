import java.util.*;

class User {
    private String id;
    private String name;
    private String type; // "Farmer" or "Retailer"
    private Map<String, Integer> ratings; // userId -> rating

    public User(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ratings = new HashMap<>();
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public Map<String, Integer> getRatings() { return ratings; }
}