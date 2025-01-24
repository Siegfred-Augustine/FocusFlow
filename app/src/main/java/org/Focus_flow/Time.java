

package org.Focus_flow;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


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
            return "Screentime: " + name + "\nDuration: " + duration + " minutes\nDescription: " + description;
        }
    }

    // Subclass for Productive activities (inherits from Activity)
    class Productive extends Screentime {
        private String category; // e.g., Communication, Office Work

        public Productive(String name, int duration, String description, String category) {
            super(name, duration, description);  // Call Activity constructor
            this.category = category;
        }

        @Override
        public String getDetails() {
            return super.getDetails() + "\nCategory: " + category + "\nType: Productive";
        }
    }    

    // Subclass for Leisure activities (inherits from Activity, adds time limit)
    class Leisure extends Screentime {
        private int timeLimit; // in minutes
        private boolean exceededLimit;

        public Leisure(String name, int duration, String description, int timeLimit) {
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

    class Browser extends Screentime {
        private boolean isProductive; // Flag to check if the browser activity is productive
        private int productiveTime; // Time spent on productive activities (in minutes)
        private int leisureTime; // Time spent on leisure activities (in minutes)
        private int startTime; // Start time in minutes
        private int finishTime; // Finish time in minutes
    
        // Constructor initializes with browser activity details and sets mode (productive or leisure)
        public Browser(String name, int duration, String description, boolean isProductive) {
            super(name, duration, description);
            this.isProductive = isProductive;
            this.startTime = 0; // Initially, no time is tracked
            this.finishTime = duration; // Initially, set the finish time as the given duration
            if (isProductive) {
                this.productiveTime = duration;
                this.leisureTime = 0;
            } else {
                this.leisureTime = duration;
                this.productiveTime = 0;
            }
        }
    
        // Method to track screen time for the browser depending on the mode (productive or leisure)
        @Override
        public void trackScreenTime(int duration) {
            finishTime = startTime + duration; // Calculate finish time
            int timeSpent = finishTime - startTime; // Get the time spent based on finish - start time
            
            // Based on the activity type (productive or leisure), track time accordingly
            if (isProductive) {
                productiveTime += timeSpent;
            } else {
                leisureTime += timeSpent;
            }
            this.duration += timeSpent; // Update the total duration
        }
    
        // Method to switch between productive and leisure activity modes
        public void toggleActivityType() {
            isProductive = !isProductive;
        }
    
        // Get the total productive time
        public int getProductiveTime() {
            return productiveTime;
        }
    
        // Get the total leisure time
        public int getLeisureTime() {
            return leisureTime;
        }
    
        // Method to get the details, including which mode the browser is in (productive or leisure)
        @Override
        public String getDetails() {
            String activityType = isProductive ? "Productive" : "Leisure";
            return super.getDetails() + "\nActivity Type: " + activityType + 
                   "\nProductive Time: " + productiveTime + " minutes" +
                   "\nLeisure Time: " + leisureTime + " minutes";
        }
    
        // Reset browser's duration and time tracking
        public void resetBrowserDuration() {
            super.resetDuration(); // Reset total duration
            this.productiveTime = 0; // Reset productive time
            this.leisureTime = 0; // Reset leisure time
        }
    }
}
