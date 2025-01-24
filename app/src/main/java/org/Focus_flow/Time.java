

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

    // Parent class for general activity
    class Screentime {
        protected String name;
        protected int duration; // in minutes
        protected String description;

        public Screentime(String name, int duration, String description) {
            this.name = name;
            this.duration = duration;
            this.description = description;
        }

        public void trackScreenTime(int duration) {
            this.duration += duration;
        }

        // Reset duration
        public void resetDuration() {
            this.duration = 0;
        }

        // Get the total time spent on the activity
        public int getDuration() {
            return duration;
        }

        // Common method to get details of an activity
        public String getDetails() {
            return "Activity: " + name + "\nDuration: " + duration + " minutes\nDescription: " + description;
        }
    }

    // Subclass for Productive activities (inherits from Activity)
    class Productive extends Screentime {
        private String category; // e.g., Communication, Office Work

        public ProductiveActivity(String name, int duration, String description, String category) {
            super(name, duration, description);  // Call Activity constructor
            this.category = category;
        }

        @Override
        public String getDetails() {
            return super.getDetails() + "\nCategory: " + category + "\nType: Productive";
        }
    }    

    // Subclass for Leisure activities (inherits from Activity, adds time limit)
    class Leisure extends Activity {
        private int timeLimit; // in minutes
        private boolean exceededLimit;

        public LeisureActivity(String name, int duration, String description, int timeLimit) {
            super(name, duration, description); // Call Activity constructor
            this.timeLimit = timeLimit;
            this.exceededLimit = duration > timeLimit;
        }

        @Override
        public String getDetails() {
            String limitStatus = exceededLimit ? "Time limit exceeded!" : "Within time limit.";
            return super.getDetails() + "\nTime Limit: " + timeLimit + " minutes\n" + limitStatus + "\nType: Leisure";
        }

        // Method to change time limit dynamically
        public void setTimeLimit(int newLimit) {
            this.timeLimit = newLimit;
            this.exceededLimit = duration > newLimit;
        }
    }

    class Hobby extends Leisure {
        private int minTimeLimit; // Minimum time required for the hobby (in minutes)
        private boolean addToLeisureTime; // If this hobby should be added to the overall leisure time
        private List<Integer> timeHistory; // History of time spent on the hobby
    
        // Constructor for Hobby/Habit class
        public Hobby(String name, int duration, String description, int minTimeLimit, boolean addToLeisureTime) {
            super(name, duration, description);
            this.minTimeLimit = minTimeLimit;
            this.addToLeisureTime = addToLeisureTime;
            this.timeHistory = new ArrayList<>();
        }
    
        // Getter method for hobby details (includes minimum time and add-to-leisure flag)
        @Override
        public String getDetails() {
            return super.getDetails() + "\n" +
                   "Minimum Time Limit: " + minTimeLimit + " minutes\n" +
                   "Add to Leisure Time: " + addToLeisureTime + "\n" +
                   "Time Spent History: " + timeHistory;
        }
    
        // Track time spent on the hobby (add to history if valid)
        @Override
        public void trackScreenTime(int duration) {
            if (duration >= minTimeLimit) {
                super.trackScreenTime(duration); // Add to overall time spent
                timeHistory.add(duration); // Save the time to history
            } else {
                System.out.println("Time spent is below the minimum limit for " + super.getDetails());
            }
        }
    
        // Check if this hobby should add to overall leisure time
        public boolean shouldAddToLeisureTime() {
            return addToLeisureTime;
        }
    
        // Get all history of times spent on the hobby
        public List<Integer> getHistory() {
            return timeHistory;
        }
    
        //generate report
    }

    class Browser extends Activity {
        private boolean isProductive; // If the browser activity is productive
        private int productiveDuration; // Time spent on productive browsing
        private int nonProductiveDuration; // Time spent on non-productive browsing
    
        // Constructor for Browser class
        public Browser(String name, String description) {
            super(name, description);
            this.isProductive = false; // Default is non-productive
            this.productiveDuration = 0;
            this.nonProductiveDuration = 0;
        }
    
        // Set the browser to productive or non-productive
        public void setProductivityStatus(boolean isProductive) {
            this.isProductive = isProductive;
        }
    
        // Track time spent on the browser and separate into productive and non-productive
        @Override
        public void trackScreenTime(int startTime, int endTime) {
            super.trackScreenTime(startTime, endTime); // Calculate the overall time spent
            int timeSpent = endTime - startTime; // Time spent on the browser
            if (isProductive) {
                productiveDuration += timeSpent;
            } else {
                nonProductiveDuration += timeSpent;
            }
        }
    
        // Get browser details including productive and non-productive times
        @Override
        public String getDetails() {
            return super.getDetails() + "\n" +
                   "Productive Time: " + productiveDuration + " minutes\n" +
                   "Non-Productive Time: " + nonProductiveDuration + " minutes\n" +
                   "Current Productivity Status: " + (isProductive ? "Productive" : "Non-Productive");
        }
    
        // Reset the browser's productive and non-productive time
        public void resetBrowserDuration() {
            productiveDuration = 0;
            nonProductiveDuration = 0;
        }
    
        // Get the productive time separately
        public int getProductiveDuration() {
            return productiveDuration;
        }
    
        // Get the non-productive time separately
        public int getNonProductiveDuration() {
            return nonProductiveDuration;
        }
}
