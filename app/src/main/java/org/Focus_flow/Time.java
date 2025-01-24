

package org.Focus_flow;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Time {
    public static Map<String, Long> readCSV(String fileName) {
        Map<String, Long> appScreenTime = new HashMap<>();
    
        try (FileReader reader = new FileReader(fileName, StandardCharsets.UTF_8)) {
            CSVParser csvParser = CSVFormat.Builder.create()
                    .setHeader("App Name", "Hours", "Minutes", "Seconds")
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);
                
            for (CSVRecord record : csvParser) {
                String appName = record.get("App Name");
                int hours = Integer.parseInt(record.get("Hours"));
                int minutes = Integer.parseInt(record.get("Minutes"));
                int seconds = Integer.parseInt(record.get("Seconds"));
                    
                long totalTimeInSeconds = (hours * 3600) + (minutes * 60) + seconds;
    
                // Merge into the map (or add if not present)
                appScreenTime.merge(appName, totalTimeInSeconds, Long::sum);
            }
        } catch (FileNotFoundException e) {
                System.err.println("File not found: " + fileName);
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }

        return appScreenTime;
    }



}
