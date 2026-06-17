package ticketreservation.model;

public enum SeatCategory {
    VIP(5000),
    PREMIUM(3000),
    ECONOMY(1500);

    private final int price;

    SeatCategory(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        String lower = name().toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}

