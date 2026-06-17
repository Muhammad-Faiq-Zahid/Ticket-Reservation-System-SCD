package ticketreservation.util;

import ticketreservation.exception.ReservationException;

public final class InputValidator {
    private InputValidator() {
    }

    public static void requireSeatNumber(String seatNumber) throws ReservationException {
        if (seatNumber == null || seatNumber.trim().isEmpty()) {
            throw new ReservationException("Seat number cannot be empty.");
        }
    }

    public static void requireCustomerName(String customerName) throws ReservationException {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new ReservationException("Customer name cannot be empty.");
        }
    }
}

