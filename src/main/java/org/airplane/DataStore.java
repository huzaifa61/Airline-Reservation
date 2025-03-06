package org.airplane;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * DataStore holds all in-memory data:
 * 1) A list of flights (FLIGHTS)
 * 2) A list of bookings (bookings)
 */
public class DataStore {

    /**
     * Represents a flight record with (flightNo, source, destination, time, price).
     * Using a Java record (Java 16+) for brevity.
     */
    public record Flight(String flightNo, String source, String destination, String time, int price) {}

    /**
     * Represents a booking record with user + flight info.
     */
    public record Booking(
            String userName,
            String flightNo,
            String source,
            String destination,
            String date,
            String time,
            int totalFare
    ) {}

    // -----------------------------------------------------------------------
    // 1) In-memory flight data
    // -----------------------------------------------------------------------
    public static final List<Flight> FLIGHTS = List.of(
            new Flight("AI101", "DELHI",     "MUMBAI",    "09:00", 5000),
            new Flight("AI102", "DELHI",     "BANGALORE", "12:00", 6000),
            new Flight("AI201", "MUMBAI",    "DELHI",     "14:00", 5500),
            new Flight("AI202", "BANGALORE", "DELHI",     "16:00", 6500),
            new Flight("AI301", "DELHI",     "CHENNAI",   "08:00", 4500),
            new Flight("AI302", "CHENNAI",   "DELHI",     "18:00", 7000)
    );

    // -----------------------------------------------------------------------
    // 2) In-memory bookings list (initially empty)
    // -----------------------------------------------------------------------
    private static final List<Booking> bookings = new ArrayList<>();

    /**
     * Add a new booking to the list.
     */
    public static void addBooking(Booking booking) {
        bookings.add(booking);
    }

    /**
     * Return an *unmodifiable* list of all bookings.
     */
    public static List<Booking> getBookings() {
        return Collections.unmodifiableList(bookings);
    }

    /**
     * Remove a booking by index (admin feature).
     */
    public static void removeBooking(int index) {
        if (index >= 0 && index < bookings.size()) {
            bookings.remove(index);
        }
    }
}
