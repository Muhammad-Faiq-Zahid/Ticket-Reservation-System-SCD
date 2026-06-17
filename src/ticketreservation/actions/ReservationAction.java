package ticketreservation.actions;

import ticketreservation.model.ReservationManager;

public abstract class ReservationAction {
    private final String seatNumber;

    protected ReservationAction(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public abstract void undo(ReservationManager manager);

    public abstract String description();
}

