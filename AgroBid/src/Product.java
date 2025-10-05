import java.util.*;

// Product class to store product details
class Product {
    private String name;
    private double basePrice;
    private String farmerId;
    private Date bidEndTime;
    private Map<String, Double> bids; // retailerId -> bid amount
    private boolean isSold;

    public Product(String name, double basePrice, String farmerId, long bidDurationMinutes) {
        this.name = name;
        this.basePrice = basePrice;
        this.farmerId = farmerId;
        this.bids = new HashMap<>();
        this.isSold = false;
        this.bidEndTime = new Date(System.currentTimeMillis() + bidDurationMinutes * 60000);
    }

    // Getters and setters
    public String getName() { return name; }
    public double getBasePrice() { return basePrice; }
    public String getFarmerId() { return farmerId; }
    public Date getBidEndTime() { return bidEndTime; }
    public Map<String, Double> getBids() { return bids; }
    public boolean isSold() { return isSold; }
    public void setSold(boolean sold) { isSold = sold; }
}