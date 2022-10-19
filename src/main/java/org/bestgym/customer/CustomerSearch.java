package org.bestgym.customer;

import org.bestgym.fileservice.GymFileReader;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public class CustomerSearch {
    private final List<Customer> allCustomers;

    public CustomerSearch() {
        // laddar in vår GymFilReader objekt som läser in alla kunder från filen
        Path customersUrl = Path.of("src/main/resources/customers.txt");
        GymFileReader reader = new GymFileReader();
        allCustomers = reader.getAllCustomersToListFromFile(customersUrl);
    }

    // Getter
    public List<Customer> getAllCustomers() {
        return allCustomers;
    }

    public Customer getCustomerByPersonNrOrName(String searchQuery) {
        return allCustomers.stream()
                .filter(customer -> customer.getPersonNr().equals(searchQuery.trim()) || customer.getName().equalsIgnoreCase(searchQuery.trim()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Finns ingen Kund med detta namn eller person nr"));
    }


    public long getAmountOfDaysBetweenNowAndDate(LocalDate subDate, LocalDate now) {
        LocalDateTime nowInLocalDateTime = now.atStartOfDay();
        LocalDateTime target = subDate.atStartOfDay(); // får tiden 00.00 spelar inte så stor roll när vi skall räkna dagar i detta program
        Duration d = Duration.between(target, nowInLocalDateTime); // dagar mellan dessa 2 DateTime Objekt
        return d.toDays();
    }

    // Kör metod för folk som har membership
    public long getAmountOfDaysLeftOnMembership(Customer customer) {
        LocalDateTime subbedDate = customer.getSubscribedSince().atStartOfDay();
        LocalDateTime oneYearForward = subbedDate.plusDays(365);
        LocalDateTime rightNow = LocalDateTime.now();

        Duration d = Duration.between(rightNow, oneYearForward);
        return d.toDays();
    }

    /*
    Om kunden är en nuvarande medlem (dvs om
    årsavgiften betalades för mindre än ett år sedan) == TRUE annars FALSE
    "searchQuery" är uppbyggt så vi kan antigen ge person nr eller namn för slagningar i vår fil
     */
    public boolean customerHasActiveGymMembership(String searchQuery) {
        LocalDate subDate = getCustomerByPersonNrOrName(searchQuery).getSubscribedSince();
        LocalDate now = LocalDate.now();
        // pga om det är minus så har det passerat så som jag har byggt
        return getAmountOfDaysBetweenNowAndDate(subDate, now) < 365 && getAmountOfDaysBetweenNowAndDate(subDate, now) >= 0;
    }

}
