# Ticket Reservation System

Java Swing desktop application for booking, cancelling, undoing, and viewing ticket seat reservations.

## Student

- Name: Muhammad Faiq Zahid
- Roll No: L1F23BSSE0387

## Features

- Book seats by category: VIP, Premium, Economy
- Cancel an existing booking
- Undo the most recent booking
- Undo the most recent cancellation
- View seat information
- Validate empty seat number and customer name fields
- In-memory storage using Java collections
- Exception handling with user-friendly messages

## OOP Concepts Used

- Classes and objects: `Seat`, `ReservationManager`, `TicketSystem`
- Encapsulation: private fields with getters/setters
- Inheritance: action classes inherit from `ReservationAction`
- Polymorphism: undo operations run through the common `ReservationAction` type
- Abstraction: `ReservationAction` defines the undo contract
- Constructors: model and action objects initialize required state
- Method overloading: `bookSeat(...)` overloads in `ReservationManager`
- Method overriding: action classes override `undo(...)` and `description()`

## Run In Eclipse

1. Open Eclipse.
2. Choose `File > Import > Existing Projects into Workspace`.
3. Select this folder.
4. Run `ticketreservation.TicketSystem`.

## Run From Terminal

```bash
./scripts/run.sh
```

## Build Executable JAR

```bash
./scripts/build.sh
java -jar dist/TicketReservationSystem.jar
```

