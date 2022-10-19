package org.bestgym;

import org.bestgym.customer.Customer;
import org.bestgym.fileservice.GymFileReader;
import org.bestgym.fileservice.GymFileWriter;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GymFileWriterTest {

    Path testFile = Path.of("src/main/resources/test.txt");
    GymFileWriter gymWriter = new GymFileWriter();
    GymFileReader gymReader = new GymFileReader();


    @Test
    void testWritePersonToFileIsCorrect() {
        // Skapar en ny kund och simulerar ett check in för gympass, testar sedan att den skriver korrekt till fil!
        Customer testCustomer = new Customer("9108233333", "Alexander Brun", LocalDate.now());
        testCustomer.setLastCheckIn(LocalDateTime.now());

        String expectedLine = testCustomer.getName() + ", " + testCustomer.getPersonNr() + ", " + testCustomer.getLastCheckInAsPrettyString();
        gymWriter.writeToFile(testCustomer, testFile);
        List<String> actualContent = gymReader.getContentOfFileAsList(testFile);
        System.out.println(actualContent);
        System.out.println(expectedLine);

        assertTrue(actualContent.contains(expectedLine)); // Innehåller filen min line som jag gav?
        assertFalse(actualContent.contains("Brun Alexander, 9108233333, " + testCustomer.getLastCheckInAsPrettyString())); // Testar lite fel som kanske kunde uppstått
        assertFalse(actualContent.contains("Alexander Brun, 9108233333, " + LocalDateTime.now().plusDays(1)));
        assertFalse(actualContent.contains("Alexander Brun, 9108233333, " + LocalDateTime.now().minusDays(1)));
        assertDoesNotThrow(() -> gymWriter.writeToFile(testCustomer, testFile)); // kör program igen, så blir 2 utskrifter till fil.
    }
}