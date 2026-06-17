package ticketreservation.actions;

import ticketreservation.model.ReservationManager;

public class BookingAction extends ReservationAction {
    public BookingAction(String seatNumber) {
        super(seatNumber);
    }

    @Override
    public void undo(ReservationManager manager) {
        manager.removeSeatSilently(getSeatNumber());
    }

    @Override
    public String description() {
        return "Booking created for seat " + getSeatNumber();
    }
}

