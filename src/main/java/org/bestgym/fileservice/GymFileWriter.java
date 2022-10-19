package org.bestgym.fileservice;

import org.bestgym.customer.Customer;
import org.bestgym.terminalcolorservice.ColorPicker;
import org.bestgym.terminalcolorservice.TerminalColor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class GymFileWriter {
    public void writeToFile(Customer currentCustomer, Path path) {
        String lineToWrite = currentCustomer.getName() + ", " + currentCustomer.getPersonNr() + ", " + currentCustomer.getLastCheckInAsPrettyString();

        // Try with resources
        try(BufferedWriter bfWriter = new BufferedWriter(new FileWriter(path.toFile(),true))) {
            bfWriter.write(lineToWrite);
            bfWriter.newLine();
            TerminalColor.message(ColorPicker.GREEN, "\nAdded " + currentCustomer.getName() + " info to PT file.");
        } catch (IOException e) {
            e.printStackTrace();
            TerminalColor.message(ColorPicker.RED, "Error writing to file");
        }
    }
}
