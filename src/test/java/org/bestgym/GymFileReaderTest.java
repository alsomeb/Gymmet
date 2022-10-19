package org.bestgym;

import org.bestgym.customer.Customer;
import org.bestgym.fileservice.GymFileReader;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GymFileReaderTest {

    GymFileReader reader = new GymFileReader();
    Path customerFile = Path.of("src/main/resources/customers.txt");
    List<Customer> allCustomers = reader.getAllCustomersToListFromFile(customerFile);

    @Test
    void testCollectCustomerListFromFileReturnsCorrectData() {
        int customersExpected = 14;

        // testar att det är 14 kunder som vi vet att det skall vara
        assertEquals(customersExpected, allCustomers.size());
        assertNotEquals(13, allCustomers.size());

        // testar att det är en lista av typen Customer som skickas tillbaka vilket vi förväntar oss
        assertEquals("Customer", allCustomers.get(0).getClass().getSimpleName());

        // Kolla att inte något objekt har blivit null vid inläsning, dem skall va objekt av TYPEN Customer
        allCustomers.forEach(customer -> assertNotNull(customer));

        // Provar lite kunder
        Customer firstCustomer = allCustomers.get(0);
        Customer lastCustomer = allCustomers.get(allCustomers.size()-1);
        LocalDate firstCustomerSubscribedSinceExpected = LocalDate.of(2022, Month.JULY, 1);
        LocalDate lastCustomerSubscribedSinceExpected = LocalDate.of(2022, Month.AUGUST, 4);
        assertEquals("Alhambra Aromes", firstCustomer.getName());
        assertEquals("7703021234", firstCustomer.getPersonNr());
        assertEquals(firstCustomerSubscribedSinceExpected, firstCustomer.getSubscribedSince());
        assertEquals("Nahema Ninsson", lastCustomer.getName());
        assertEquals("7805211234", lastCustomer.getPersonNr());
        assertEquals(lastCustomerSubscribedSinceExpected, lastCustomer.getSubscribedSince());
        assertNotEquals("Test", firstCustomer.getName());

        System.out.println(allCustomers);
    }

    @Test
    void testCustomerListContainsCorrectDateType() {
        // testar att subbed date är av typen LocalDate för varje Customer I listan
        allCustomers.forEach(customer -> assertEquals(LocalDate.now().getClass().getSimpleName(), customer.getSubscribedSince().getClass().getSimpleName()));
        // Att datum inte blivit null vid inläsning, då har något koko hänt
        allCustomers.forEach(customer -> assertNotNull(customer.getSubscribedSince()));
    }

    @Test
    void testIsDateReturnsCorrectBool() {
        // Test som kollar att stringen som skickas in kan parsas till LocalDate
        String dateCorrectFormat = "2022-08-14";
        String dateFelFormat = "2022-008-23";
        assertTrue(reader.isDate(dateCorrectFormat));
        assertFalse(reader.isDate(dateFelFormat));
    }

    @Test
    void testConvertStringToDate() {
        // Test som kollar att string vi skickar in blir LocalDate object
        String dateString = "2022-08-14";
        LocalDate expected = LocalDate.of(2022, Month.AUGUST, 14);

        assertEquals(expected, reader.convertStringToDate(dateString));
        assertEquals(expected.getClass().getSimpleName(), reader.convertStringToDate(dateString).getClass().getSimpleName());
        assertNotEquals(dateString.getClass().getSimpleName(), reader.convertStringToDate(dateString).getClass().getSimpleName()); // Kolla så att det är en LocalDate objekt INTE String objekt
        assertThrows(DateTimeException.class, ()-> { // Kolla att den kastar det exception vi angett den skall! Dvs DateTimeException
            reader.convertStringToDate("inget datum");
        });
    }

}