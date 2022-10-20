package org.bestgym;

import org.bestgym.customer.Customer;
import org.bestgym.fileservice.GymFileWriter;
import org.bestgym.terminalcolorservice.ColorPicker;
import org.bestgym.terminalcolorservice.TerminalColor;
import org.bestgym.customer.CustomerSearch;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GymApp {
    private Scanner scan;
    private final CustomerSearch searchService; // Vi behöver ej skapa en GymFileReader pga den finns i denna service
    private final GymFileWriter writerService = new GymFileWriter(); // Så vi kan skriva till PT filen;
    private final Path personalTrainerFile = Path.of("src/main/resources/personal_trainer.txt");
    private final boolean testMode;


    public GymApp(boolean testMode) {
        this.testMode = testMode;
        scan = new Scanner(System.in);
        searchService = new CustomerSearch(); // Så vi kan göra slagningar mm. I Gym App
        handleInput();

    }

    // Constructor som används för test, scanner körs aldrig i denna, pga vi vill testa
    public GymApp(boolean testmode, CustomerSearch searchService) {
        this.testMode = testmode;
        this.searchService = searchService;
    }

    private void printMenu() {
        TerminalColor.message(ColorPicker.YELLOW, "\n---Best Gym Ever---");
        System.out.println("1. Kolla om kund har giltigt medlemskap\n2. Checka in befintlig kund för träningspass\n0. Avsluta");
    }

    // Pga val så gå vi vidare i programmet med det vi vill göra, Göra slagningar på kund / Checka in kund osv
    public void handleInput() {
        while (true) {
            printMenu();
            String choice = scan.nextLine();

            switch (choice) {
                case "1" -> handleFetchCustomer();
                case "2" -> handleCheckInCustomer();
                case "0" -> {
                    TerminalColor.message(ColorPicker.BLUE, "\nClosing program");
                    System.exit(0);
                }
                default -> TerminalColor.message(ColorPicker.RED, "Håll dig mellan alternativ 0-2");
            }
        }
    }

    // Slagningar på kund
    private void handleFetchCustomer() {
        String searchQuery = prompt("Ange person nr 10 siffror eller fullt namn: ");
        try {
            Customer currentCustomer = searchService.getCustomerByPersonNrOrName(searchQuery);
            TerminalColor.message(ColorPicker.YELLOW, "Kund: " + currentCustomer.getName() + " " + currentCustomer.getPersonNr());
            System.out.println(getSubscribedStatusString(currentCustomer));

        } catch (NoSuchElementException e) {
            TerminalColor.message(ColorPicker.RED, e.getMessage()); // den har error meddelandets STRING inbyggt när jag kastar felet NoSuchElementException i metoden
        }
    }

    // Metod som skriver ut en string beroende på om man har gymmedlemskap eller inte
    public String getSubscribedStatusString(Customer customer) {
        boolean isSubscribed = searchService.customerHasActiveGymMembership(customer.getPersonNr());
        long days = searchService.getAmountOfDaysLeftOnMembership(customer);
        return isSubscribed ? "Har " + days + " dagar kvar på sitt medlemskap" : "Inget aktivt medlemskap, senast betalt: " + customer.getSubscribedSince();
    }

    public void handleCheckInCustomer() {
        String searchQuery = prompt("Ange person nr 10 siffror eller fullt namn: ");
        System.out.println(getCheckInMessageForCustomer(searchQuery)); // Ger Meddelande till Användaren om KundInfo samt felhantering
    }

    // Metod som register kund för gympass(skriver även ner dem för PTn i hans fil) returnerar string pga TDD, kunna testa detta.
    public String getCheckInMessageForCustomer(String searchQuery){

        try {
            Customer currentCustomer = searchService.getCustomerByPersonNrOrName(searchQuery);
            boolean hasActiveMembership = searchService.customerHasActiveGymMembership(currentCustomer.getPersonNr());
            if(hasActiveMembership) {
                currentCustomer.setLastCheckIn(LocalDateTime.now());
                // här skriver vi in person information till Personliga Tränarens fil om vi inte har testMode aktiverat
                writeCustomerToFile(testMode, currentCustomer);
                return "Kund: " + currentCustomer.getName() + "\nIncheckad: " + currentCustomer.getLastCheckInAsPrettyString();
            } else {
                return "Kund: " + currentCustomer.getName() + " har ej aktivt medlemskap\nSenast betalat: " + currentCustomer.getSubscribedSince();
            }
        } catch (NoSuchElementException e) {
            return e.getMessage(); // felmeddelandet om inte kund finns!
        }
    }

    private String prompt(String message) {
        TerminalColor.message(ColorPicker.GREEN, message);
        return scan.nextLine();
    }

    private void writeCustomerToFile(boolean testMode, Customer customer) {
        if(!testMode) {
            writerService.writeToFile(customer, personalTrainerFile);
        }
    }
}
