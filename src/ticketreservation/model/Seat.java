package ticketreservation.model;

public class Seat {
    private final SeatCategory category;
    private final String seatNumber;
    private String customerName;
    private boolean booked;

    public Seat(SeatCategory category, String seatNumber, String customerName) {
        this.category = category;
        this.seatNumber = seatNumber;
        this.customerName = customerName;
        this.booked = true;
    }

    public SeatCategory getCategory() {
        return category;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public String getDisplayText() {
        return "Seat Number: " + seatNumber
                + "\nCategory: " + category
                + "\nPrice: Rs. " + category.getPrice()
                + "\nCustomer: " + customerName
                + "\nStatus: " + (booked ? "Booked" : "Available");
    }

    @Override
    public String toString() {
        return seatNumber + " - " + category + " - " + customerName;
    }
}

