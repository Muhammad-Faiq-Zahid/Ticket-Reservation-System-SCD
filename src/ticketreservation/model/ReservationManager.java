package ticketreservation.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import ticketreservation.actions.BookingAction;
import ticketreservation.actions.CancellationAction;
import ticketreservation.actions.ReservationAction;
import ticketreservation.exception.ReservationException;
import ticketreservation.util.InputValidator;

public class ReservationManager {
    private final Map<String, Seat> seats = new LinkedHashMap<>();
    private final ArrayDeque<ReservationAction> bookingHistory = new ArrayDeque<>();
    private final ArrayDeque<ReservationAction> cancellationHistory = new ArrayDeque<>();

    public Seat bookSeat(String category, String seatNumber, String customerName) throws ReservationException {
        return bookSeat(SeatCategory.valueOf(category.toUpperCase()), seatNumber, customerName);
    }

    public Seat bookSeat(SeatCategory category, String seatNumber, String customerName) throws ReservationException {
        String normalizedSeat = normalizeSeatNumber(seatNumber);
        String normalizedCustomer = customerName == null ? "" : customerName.trim();

        InputValidator.requireSeatNumber(normalizedSeat);
        InputValidator.requireCustomerName(normalizedCustomer);

        if (seats.containsKey(normalizedSeat)) {
            throw new ReservationException("Seat " + normalizedSeat + " is already booked.");
        }

        Seat seat = new Seat(category, normalizedSeat, normalizedCustomer);
        seats.put(normalizedSeat, seat);
        bookingHistory.push(new BookingAction(normalizedSeat));
        return seat;
    }

    public Seat cancelSeat(String seatNumber) throws ReservationException {
        String normalizedSeat = normalizeSeatNumber(seatNumber);
        InputValidator.requireSeatNumber(normalizedSeat);

        Seat seat = seats.remove(normalizedSeat);
        if (seat == null) {
            throw new ReservationException("Seat " + normalizedSeat + " is not currently booked.");
        }

        seat.setBooked(false);
        cancellationHistory.push(new CancellationAction(seat));
        return seat;
    }

    public String undoBooking() throws ReservationException {
        return undoFromHistory(bookingHistory, "No booking operation is available to undo.");
    }

    public String undoCancellation() throws ReservationException {
        return undoFromHistory(cancellationHistory, "No cancellation operation is available to undo.");
    }

    public Seat getSeat(String seatNumber) throws ReservationException {
        String normalizedSeat = normalizeSeatNumber(seatNumber);
        InputValidator.requireSeatNumber(normalizedSeat);
        return seats.get(normalizedSeat);
    }

    public Collection<Seat> getAllSeats() {
        return Collections.unmodifiableList(new ArrayList<>(seats.values()));
    }

    public void removeSeatSilently(String seatNumber) {
        seats.remove(normalizeSeatNumber(seatNumber));
    }

    public void restoreSeatSilently(Seat seat) {
        seats.putIfAbsent(seat.getSeatNumber(), seat);
    }

    private String undoFromHistory(ArrayDeque<ReservationAction> history, String emptyMessage)
            throws ReservationException {
        ReservationAction action = history.poll();
        if (action == null) {
            throw new ReservationException(emptyMessage);
        }
        action.undo(this);
        return "Undone: " + action.description();
    }

    private String normalizeSeatNumber(String seatNumber) {
        return seatNumber == null ? "" : seatNumber.trim().toUpperCase();
    }
}
