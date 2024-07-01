package com.example.web_graph_builder.Graph.builder.GraphBuildingModule.csv_processor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MasterModelCSVProcessor {

    public void processMasterModelCSV(String inputFilePath, String outputFilePath) {
        if (inputFilePath == null || inputFilePath.isEmpty()) {
            System.out.println("Input file path is empty or null");
            return;
        }

        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            System.out.println("Input file not found");
            return;
        }
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setMaxCharsPerColumn(200000);
        parserSettings.setLineSeparatorDetectionEnabled(true);
        CsvParser parser = new CsvParser(parserSettings);
        CsvWriter writer = null;
        try (FileReader fileReader = new FileReader(inputFile)) {
            List<String[]> allRows = parser.parseAll(fileReader);
            if (allRows.size() > 3) {
                String[] headerRow = allRows.get(3)[0].split("\\|");
                // System.out.println("Headers: " + Arrays.toString(headerRow));
                CsvWriterSettings writerSettings = new CsvWriterSettings();
                writerSettings.setHeaderWritingEnabled(true);
                File outputFile = new File(outputFilePath);
                writer = new CsvWriter(new FileWriter(outputFile), writerSettings);
                writer.writeHeaders(headerRow);
                for (int i = 4; i < allRows.size(); i++) {
                    String[] row = allRows.get(i)[0].split("\\|");
                    writer.writeRow((Object[]) row);
                }
                System.out.println("CSV file saved locally at: " + outputFilePath);
            } else {
                System.out.println("CSV file does not contain enough rows to extract headers.");
            }
        } catch (IOException e) {
            System.out.println("Error reading or writing CSV file: " + e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}