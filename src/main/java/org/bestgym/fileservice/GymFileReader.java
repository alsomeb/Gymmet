package org.bestgym.fileservice;

import org.bestgym.customer.Customer;
import org.bestgym.terminalcolorservice.ColorPicker;
import org.bestgym.terminalcolorservice.TerminalColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GymFileReader {
    public List<Customer> getAllCustomersToListFromFile(Path filePath) {
        List<Customer> customersList = new ArrayList<>();
        int counter = 0;
        String currentLine;
        StringBuilder sb = new StringBuilder();

        // Try with resources
        try(BufferedReader reader = Files.newBufferedReader(filePath)) {
            while ((currentLine = reader.readLine()) != null) {
                sb.append(currentLine.trim()).append(", ");
                counter++;
                if(counter >= 2) {
                    sb.replace(sb.length() - 2, sb.length() - 1, ""); // Tar bort det onödiga "," i slutet på varje rad
                    String[] customerLines = sb.toString().split(","); // Så vi kan få de vi skall lägga i constructor på Customer
                    addPersonToList(customerLines, customersList); // SKapar person och append in i customers listan
                    counter = 0; // reset counter
                    sb.setLength(0); // reset SB
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); //ger kodaren info vad som gick fel i terminalen
            TerminalColor.message(ColorPicker.RED, "Error reading file");
        }
        return customersList;
    }

    // För bättre läsbarhet, kortare kod i metoden ovan
    private void addPersonToList(String[] customerLines, List<Customer> customerList) {
        String personNr = customerLines[0].trim();
        String name = customerLines[1].trim();
        LocalDate subbedSince = convertStringToDate(customerLines[2].trim());
        customerList.add(new Customer(personNr, name, subbedSince));
    }

    // används för validering av datum i metoden convertStringToDate()
    public boolean isDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        }catch (DateTimeParseException e) {
            return false;
        }
    }

    public LocalDate convertStringToDate(String date) {
        if(isDate(date)) {
            return LocalDate.parse(date);
        }
        throw new DateTimeException("Wrong format of date");
    }


    // Används för testning
    public List<String> getContentOfFileAsList(Path path) {
        List<String> contentOfFile = new ArrayList<>();

        try {
            Scanner scan = new Scanner(path);
            while (scan.hasNextLine()) {
                contentOfFile.add(scan.nextLine().trim());
            }

        } catch (IOException e) {
            TerminalColor.message(ColorPicker.RED, "Error reading file");
            e.printStackTrace();
        }
        return contentOfFile;
    }
}
