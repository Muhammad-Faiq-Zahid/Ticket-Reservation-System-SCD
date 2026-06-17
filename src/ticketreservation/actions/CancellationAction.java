package ticketreservation.actions;

import ticketreservation.model.ReservationManager;
import ticketreservation.model.Seat;

public class CancellationAction extends ReservationAction {
    private final Seat cancelledSeat;

    public CancellationAction(Seat cancelledSeat) {
        super(cancelledSeat.getSeatNumber());
        this.cancelledSeat = cancelledSeat;
    }

    @Override
    public void undo(ReservationManager manager) {
        cancelledSeat.setBooked(true);
        manager.restoreSeatSilently(cancelledSeat);
    }

    @Override
    public String description() {
        return "Booking cancelled for seat " + getSeatNumber();
    }
}

