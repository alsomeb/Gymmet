package org.bestgym;

import org.bestgym.customer.Customer;
import org.bestgym.customer.CustomerSearch;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class GymAppTest {

    CustomerSearch custSearch = new CustomerSearch();
    List<Customer> allCustomers = custSearch.getAllCustomers();

    // Används en annan constructor som tar in CustomerSearch klassen, SOM INTE har en scanner aktiverad i sig, så man kan testa!
    // samt finns en testMode parameter av typen boolean
    GymApp gymApp = new GymApp(true, custSearch);

    // testar att denna metod spottar ut rätt String när man söker på kunder
    @Test
    void testPrintSubscribedStatusIsCorrect() {
        Customer testCustomer1 = allCustomers.get(0);
        long daysExpected = custSearch.getAmountOfDaysLeftOnMembership(testCustomer1);

        Customer testCustomer2 = new Customer("9108233333", "Alexander Test", LocalDate.now());
        allCustomers.add(testCustomer2);
        long daysExpected2 = custSearch.getAmountOfDaysLeftOnMembership(testCustomer2);

        Customer testCustomer3 = new Customer("9108233332", "Alexander Test2", LocalDate.of(2021, Month.OCTOBER, 15));
        allCustomers.add(testCustomer3);
        long daysExpected3 = custSearch.getAmountOfDaysLeftOnMembership(testCustomer3);

        assertEquals("Har " + daysExpected + " dagar kvar på sitt medlemskap", gymApp.getSubscribedStatusString(testCustomer1));
        assertEquals("Har " + daysExpected2 + " dagar kvar på sitt medlemskap", gymApp.getSubscribedStatusString(testCustomer2));
        assertNotEquals("Har " + daysExpected3 + " dagar kvar på sitt medlemskap", gymApp.getSubscribedStatusString(testCustomer3));
        assertEquals("Inget aktivt medlemskap, senast betalt: " + testCustomer3.getSubscribedSince(), gymApp.getSubscribedStatusString(testCustomer3));
        System.out.println(gymApp.getSubscribedStatusString(testCustomer1));
        System.out.println(gymApp.getSubscribedStatusString(testCustomer2));
        System.out.println(gymApp.getSubscribedStatusString(testCustomer3));
    }

    @Test
    void testHandleCheckInCustomer() {
        // Alex checkar in på gymmet kl 15.00, 14 oktober, då skall timestamp ges på hans profil, vi kollar att denna "timestamp" verkligen sätts i objektet
        Customer alex = new Customer("9108233333", "Alexander Test", LocalDate.of(2022, Month.OCTOBER, 14));
        LocalDateTime latestCheckIn = LocalDateTime.of(2022, Month.OCTOBER, 14, 15, 0);
        alex.setLastCheckIn(latestCheckIn);
        allCustomers.add(alex);
        System.out.println(alex.getLastCheckInAsPrettyString()); // Printar en String för bättre läsbarhet

        assertNotNull(alex.getLastCheckIn()); // dvs har man aldrig checkat in så blir datumet NULL
        assertEquals(latestCheckIn, alex.getLastCheckIn());
    }

    @Test
    void testGetCheckInMessageForCustomer() {
        // Kollar så vi får rätt meddelanden på felhanteringen av incheckning av kund
        Customer correctCustomer = new Customer("9108233333", "Alexander Test", LocalDate.of(2022, Month.OCTOBER, 14));
        allCustomers.add(correctCustomer);

        // Testar så de går att generera korrekt meddelande med kundens namn samt person nr
        String actual = gymApp.getCheckInMessageForCustomer(correctCustomer.getName());
        String actual2 = gymApp.getCheckInMessageForCustomer(correctCustomer.getPersonNr());
        String expected = "Kund: " + correctCustomer.getName() + "\nIncheckad: " + correctCustomer.getLastCheckInAsPrettyString();

        assertEquals(actual, expected);
        assertEquals(actual2, expected);
        System.out.println(actual);
        System.out.println(actual2);

        // Testar ej giltigt medlemskap felmeddelande
        Customer notWork = custSearch.getCustomerByPersonNrOrName("7911061234");
        String notWorkActual = gymApp.getCheckInMessageForCustomer(notWork.getPersonNr());
        String expectedNotWork = "Kund: " + notWork.getName() + " har ej aktivt medlemskap\nSenast betalat: " + notWork.getSubscribedSince();
        assertEquals(notWorkActual, expectedNotWork);

        // Kollar om man söker på en kund som inte finns genererar detta Felmeddelande till terminalen (String)
        String customerNotExistExpected = "Finns ingen Kund med detta namn eller person nr";
        String actualMessage = gymApp.getCheckInMessageForCustomer("tetetestFelInmatning");
        assertEquals(customerNotExistExpected, actualMessage);
        assertNotEquals(actual, actualMessage);

    }
}