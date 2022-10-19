package org.bestgym.customer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Customer {
    private final String personNr;
    private final String name;
    private final LocalDate subscribedSince;
    private LocalDateTime lastCheckIn; // null i början vilket är förväntat, vi sätter den när vi checkar in någon i programmet

    public Customer(String personNr, String name, LocalDate subscribedSince) {
        this.personNr = personNr;
        this.name = name;
        this.subscribedSince = subscribedSince;
    }

    public String getPersonNr() {
        return personNr;
    }

    public String getName() {
        return name;
    }

    public LocalDate getSubscribedSince() {
        return subscribedSince;
    }

    public LocalDateTime getLastCheckIn() {
        return lastCheckIn;
    }

    // Skriver ut tex "Fri 14 Oct 2022, 15:00"
    public String getLastCheckInAsPrettyString() {
        return lastCheckIn.format(DateTimeFormatter.ofPattern("E d MMM yyyy, HH:mm")); // 3st MMM blir månad i text, och E är dagen i text
    }

    public void setLastCheckIn(LocalDateTime lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "personNr='" + personNr + '\'' +
                ", name='" + name + '\'' +
                ", subscribedSince=" + subscribedSince +
                '}';
    }
}
