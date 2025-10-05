import java.util.*;

class Marketplace {
    private Map<String, User> users;
    private Map<String, Product> products;
    private Scanner scanner;

    public Marketplace() {
        users = new HashMap<>();
        products = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void registerUser() {
        System.out.println("Enter ID:");
        String id = scanner.nextLine();
        System.out.println("Enter Name:");
        String name = scanner.nextLine();
        System.out.println("Enter type (Farmer/Retailer):");
        String type = scanner.nextLine();

        if (!type.equals("Farmer") && !type.equals("Retailer")) {
            System.out.println("Invalid type");
            return;
        }

        users.put(id, new User(id, name, type));
        System.out.println("Registration successful!");
    }

    private void loginUser() {
        System.out.println("Enter ID:");
        String id = scanner.nextLine();
        User user = users.get(id);

        if (user == null) {
            System.out.println("User not found");
            return;
        }

        if (user.getType().equals("Farmer")) {
            showFarmerMenu(user);
        } else {
            showRetailerMenu(user);
        }
    }

    private void showFarmerMenu(User farmer) {
        while (true) {
            System.out.println("\nFarmer Menu:");
            System.out.println("1. Post Product\n2. View Bids\n3. Sell Product\n4. View Retailers\n5. Rate Retailer\n6. Get Price Suggestion\n7. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: postProduct(farmer); break;
                case 2: viewBids(farmer); break;
                case 3: sellProduct(farmer); break;
                case 4: viewRetailers(); break;
                case 5: rateUser(farmer); break;
                case 6: getPriceSuggestion(); break;
                case 7: return;
                default: System.out.println("Invalid choice");
            }
        }
    }

    private void showRetailerMenu(User retailer) {
        while (true) {
            System.out.println("\nRetailer Menu:");
            System.out.println("1. View Products\n2. Place Bid\n3. Rate Farmer\n4. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: viewProducts(); break;
                case 2: placeBid(retailer); break;
                case 3: rateUser(retailer); break;
                case 4: return;
                default: System.out.println("Invalid choice");
            }
        }
    }

    private void postProduct(User farmer) {
        System.out.println("Enter product name:");
        String name = scanner.nextLine();
        System.out.println("Enter base price:");
        double price = scanner.nextDouble();
        System.out.println("Enter bid duration (minutes):");
        long duration = scanner.nextLong();
        scanner.nextLine();

        String productId = UUID.randomUUID().toString();
        products.put(productId, new Product(name, price, farmer.getId(), duration));
        System.out.println("Product posted successfully! ID: " + productId);
    }

    private void viewBids(User farmer) {
        products.values().stream()
                .filter(p -> p.getFarmerId().equals(farmer.getId()))
                .forEach(p -> {
                    System.out.println("\nProduct: " + p.getName());
                    System.out.println("Base Price: " + p.getBasePrice());
                    System.out.println("Bids: " + p.getBids());
                    System.out.println("Ends at: " + p.getBidEndTime());
                });
    }

    private void sellProduct(User farmer) {
        System.out.println("Enter product ID:");
        String productId = scanner.nextLine();
        Product product = products.get(productId);

        if (product == null || !product.getFarmerId().equals(farmer.getId())) {
            System.out.println("Invalid product");
            return;
        }

        if (product.getBids().isEmpty()) {
            System.out.println("No bids yet");
            return;
        }

        String winner = product.getBids().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();

        product.setSold(true);
        System.out.println("Sold to " + users.get(winner).getName() + " for " + product.getBids().get(winner));
    }

    private void viewRetailers() {
        users.values().stream()
                .filter(u -> u.getType().equals("Retailer"))
                .forEach(u -> {
                    System.out.println("\nID: " + u.getId());
                    System.out.println("Name: " + u.getName());
                    System.out.println("Ratings: " + u.getRatings());
                });
    }

    private void rateUser(User rater) {
        System.out.println("Enter user ID to rate:");
        String targetId = scanner.nextLine();
        User target = users.get(targetId);

        if (target == null) {
            System.out.println("User not found");
            return;
        }

        System.out.println("Enter rating (1-5):");
        int rating = scanner.nextInt();
        scanner.nextLine();

        if (rating < 1 || rating > 5) {
            System.out.println("Invalid rating");
            return;
        }

        target.getRatings().put(rater.getId(), rating);
        System.out.println("Rating submitted!");
    }

    private void viewProducts() {
        products.values().stream()
                .filter(p -> !p.isSold())
                .forEach(p -> {
                    System.out.println("\nID: " + p.getName());
                    System.out.println("Farmer: " + users.get(p.getFarmerId()).getName());
                    System.out.println("Base Price: " + p.getBasePrice());
                    System.out.println("Ends at: " + p.getBidEndTime());
                });
    }

    private void placeBid(User retailer) {
        System.out.println("Enter product ID:");
        String productId = scanner.nextLine();
        Product product = products.get(productId);

        if (product == null || product.isSold()) {
            System.out.println("Invalid product");
            return;
        }

        if (new Date().after(product.getBidEndTime())) {
            System.out.println("Bidding has ended");
            return;
        }

        System.out.println("Enter bid amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= product.getBasePrice()) {
            System.out.println("Bid must be higher than base price");
            return;
        }

        product.getBids().put(retailer.getId(), amount);
        System.out.println("Bid placed successfully!");
    }

    private void getPriceSuggestion() {
        System.out.println("Enter product name:");
        String name = scanner.nextLine();
        // Simple suggestion based on existing products
        double avgPrice = products.values().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .mapToDouble(Product::getBasePrice)
                .average()
                .orElse(0.0);

        System.out.println("Suggested price: " + (avgPrice > 0 ? avgPrice : "No data available"));
    }
}