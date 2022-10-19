package org.bestgym;

import org.bestgym.customer.Customer;
import org.bestgym.customer.CustomerSearch;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CustomerSearchTest {
    CustomerSearch custSearch = new CustomerSearch();
    List<Customer> allCustomers = custSearch.getAllCustomers();

    @Test
    void testGetCustomerByPersonNumberOrName() {
        // Testar med personNr/Namn från första kunden i filen, för funkar detta så vet vi att de funkar för alla.
        // Testar att metoden kastar det fel vi bestämt den skall göra
        String personNr = "7703021234";
        String name = "Alhambra Aromes";
        String name2 = "alHAmBrA aRoMeS";
        Customer testCustomer = allCustomers.get(0);

        assertEquals(testCustomer, custSearch.getCustomerByPersonNrOrName(personNr));
        assertEquals(testCustomer, custSearch.getCustomerByPersonNrOrName(name));
        assertEquals(testCustomer, custSearch.getCustomerByPersonNrOrName(name2));
        assertNotEquals(testCustomer, custSearch.getCustomerByPersonNrOrName("Diamanda Djedi"));
        assertThrows(NoSuchElementException.class, ()-> custSearch.getCustomerByPersonNrOrName("2414ingetpersonnr"));
        assertDoesNotThrow(() -> custSearch.getCustomerByPersonNrOrName(personNr));
    }

    @Test
    void testGetAmountOfDaysBetweenNowAndDate() {
        // Expected 2022-07-01 minus dagens datum , 105 dagar, alltså diffen
        LocalDate latestSub = LocalDate.of(2022, Month.JULY, 1);
        LocalDate now = LocalDate.now();
        long diffActual = custSearch.getAmountOfDaysBetweenNowAndDate(latestSub, now);
        Duration expected = Duration.between(latestSub.atStartOfDay(), now.atStartOfDay());
        System.out.println(diffActual);
        assertEquals(expected.toDays(), diffActual);
    }

    @Test
    void testCustomerHasActiveGymMembership() {
        // Testar lite olika fall där vi vet kunder har giltiga gymkort och inte giltiga!
        String personNr = "7703021234";
        String personNr2 = "7605021234";
        String personNr3 = "4604151234";
        Customer alex = new Customer("9108233333", "Alexander Test", LocalDate.now());
        allCustomers.add(alex);

        boolean hasActiveMemberShip = custSearch.customerHasActiveGymMembership(personNr);
        boolean hasActiveMemberShip2 = custSearch.customerHasActiveGymMembership(personNr2);
        boolean hasNotActiveMemberShip = custSearch.customerHasActiveGymMembership(personNr3);
        boolean alexHasActiveMembership = custSearch.customerHasActiveGymMembership(alex.getPersonNr());

        assertTrue(alexHasActiveMembership);
        assertTrue(hasActiveMemberShip);
        assertTrue(hasActiveMemberShip2);
        assertFalse(hasNotActiveMemberShip);
    }

    @Test
    void testGetAmountOfDaysLeftOnMembership() {
        // Testar metoden att den returnerar korrekt dagar kvar av Gym medlemskap
        Customer alex = new Customer("9108233333", "Alexander Test", LocalDate.of(2022, Month.JANUARY, 14));
        allCustomers.add(alex);
        long daysActual = custSearch.getAmountOfDaysLeftOnMembership(alex);
        System.out.println(daysActual);
        // tid mellan nu och subscribedSince datum + 365 dagar
        Duration expected = Duration.between(LocalDateTime.now(), alex.getSubscribedSince().atStartOfDay().plusDays(365));

        assertEquals(expected.toDays(), daysActual);
        assertNotEquals(365, daysActual);
        assertNotEquals(300, daysActual);
    }

}