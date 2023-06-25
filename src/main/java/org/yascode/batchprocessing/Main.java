package org.yascode.batchprocessing;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final String CSV_FILE = "C:\\Users\\Yassin\\Technical_Expert\\Stack_Java\\Spring_Batch\\Youtube\\Professeur_Mohamed_YOUSSFI\\projects\\perso\\batch-processing\\src\\main\\resources\\data\\24-06-2023.csv";


    /*public static void main(String[] args) {
        writeLinesToCsv();
    }*/


    public static void writeLinesToCsv() {
        List<String> firstName = Arrays.asList("John", "Emma", "Michael", "Olivia", "William", "Sophia", "James", "Isabella", "David", "Mia");
        List<String> lastName = Arrays.asList("Lucas", "Emma", "Charlotte", "Gabriel", "Léa", "Noah", "Sophia", "Liam", "Olivia", "Samuel");
        List<String> countries = Arrays.asList("Indonesia", "Mexico", "Kenya", "Brazil", "Poland", "Thailand", "Canada", "Egypt", "Australia");

        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
            for (int i = 0; i < 40; i++) {
                String f = firstName.get((int) (Math.random() * 9));
                String l = lastName.get((int) (Math.random() * 9));
                String c = countries.get((int) (Math.random() * 9));

                String[] line = {f, l, String.valueOf((int) (Math.random() * 60)), c};
                System.out.println(line.toString());
                writer.writeNext(line);
                System.out.println(line);
            }
            System.out.println("Lines written to CSV file successfully!");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

}
