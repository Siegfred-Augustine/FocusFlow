package org.Focus_flow;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class AppTime {
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

    public static class Time {
        protected String name;
        protected int duration; 
        protected String description;
        protected LocalDateTime lastResetTime;

        public Time(String name, int duration, String description) {
            this.name = name;
            this.duration = duration;
            this.description = description;
            this.lastResetTime = LocalDateTime.now();
        }

        public void trackScreenTime(int duration) {
            checkAndResetDaily();
            this.duration += duration;
        }

        // Check if it's a new day and reset if necessary
        protected void checkAndResetDaily() {
            LocalDateTime now = LocalDateTime.now();
            if (!now.toLocalDate().equals(lastResetTime.toLocalDate())) {
                resetDuration();
                lastResetTime = now;
            }
        }

        // Reset duration
        public void resetDuration() {
            this.duration = 0;
        }

        public int getDuration() {
            checkAndResetDaily();
            return duration;
        }

        public String getDetails() {
            checkAndResetDaily();
            return "Screentime: " + name + "\nDuration: " + duration + " minutes\nDescription: " + description;
        }
    }

    public static class Productive extends Time {
        private String category;
        private int minTime;
        private boolean exceededMin;

        public Productive(String name, int duration, String description, String category) {
            super(name, duration, description);
            this.category = category;
        }

        @Override
        public void trackScreenTime(int duration) {
            checkAndResetDaily();
            super.trackScreenTime(duration);
            this.exceededMin = this.duration > minTime;
        } 

        public void setMinTime(int duration){

        }

        @Override
        public String getDetails() {
            checkAndResetDaily();
            return super.getDetails() + "\nCategory: " + category + "\nType: Productive";
        }
    }

    public static class Leisure extends Time {
        private int timeLimit;
        private boolean exceededLimit;

        public Leisure(String name, int duration, String description, int timeLimit) {
            super(name, duration, description);
            this.timeLimit = timeLimit;
            this.exceededLimit = duration > timeLimit;
        }

        @Override
        public void trackScreenTime(int duration) {
            checkAndResetDaily();
            super.trackScreenTime(duration);
            this.exceededLimit = this.duration > timeLimit;
        }

        @Override
        public String getDetails() {
            checkAndResetDaily();
            String limitStatus = exceededLimit ? "Time limit exceeded!" : "Within time limit.";
            return super.getDetails() + "\nTime Limit: " + timeLimit + " minutes\n" + limitStatus + "\nType: Leisure";
        }

        public void setTimeLimit(int newLimit) {
            this.timeLimit = newLimit;
            this.exceededLimit = duration > newLimit;
        }
    }

    public static class Purposed extends Leisure {
        private int minTimeLimit;
        private boolean addToLeisureTime;

        public Purposed(String name, int duration, String description, int timeLimit, int minTimeLimit, boolean addToLeisureTime) {
            super(name, duration, description, timeLimit);
            this.minTimeLimit = minTimeLimit;
            this.addToLeisureTime = addToLeisureTime;
        }

        @Override
        public void trackScreenTime(int duration) {
            checkAndResetDaily();
            if (duration >= minTimeLimit) {
                super.trackScreenTime(duration);
            } else {
                System.out.println("Time spent is below the minimum limit for " + name);
            }
        }

        @Override
        public String getDetails() {
            checkAndResetDaily();
            return super.getDetails() + "\n" +
                "Minimum Time Limit: " + minTimeLimit + " minutes\n" +
                "Add to Leisure Time: " + addToLeisureTime;
        }

        public boolean shouldAddToLeisureTime() {
            return addToLeisureTime;
        }
    }

    public static class Dynamic extends Time {
        private boolean isProductive;
        private int productiveTime;
        private int leisureTime;
        private int startTime;
        private int finishTime;

        public Dynamic(String name, int duration, String description, boolean isProductive) {
            super(name, duration, description);
            this.isProductive = isProductive;
            this.startTime = 0;
            this.finishTime = duration;
            if (isProductive) {
                this.productiveTime = duration;
                this.leisureTime = 0;
            } else {
                this.leisureTime = duration;
                this.productiveTime = 0;
            }
        }

        @Override
        public void trackScreenTime(int duration) {
            checkAndResetDaily();
            finishTime = startTime + duration;
            int timeSpent = finishTime - startTime;
            
            if (isProductive) {
                productiveTime += timeSpent;
            } else {
                leisureTime += timeSpent;
            }
            this.duration += timeSpent;
            startTime = finishTime;
        }

        @Override
        public void resetDuration() {
            super.resetDuration();
            this.productiveTime = 0;
            this.leisureTime = 0;
            this.startTime = 0;
            this.finishTime = 0;
        }

        public void toggleActivityType() {
            isProductive = !isProductive;
        }

        public int getProductiveTime() {
            checkAndResetDaily();
            return productiveTime;
        }

        public int getLeisureTime() {
            checkAndResetDaily();
            return leisureTime;
        }

        @Override
        public String getDetails() {
            checkAndResetDaily();
            String activityType = isProductive ? "Productive" : "Leisure";
            return super.getDetails() + "\nActivity Type: " + activityType + 
                "\nProductive Time: " + productiveTime + " minutes" +
                "\nLeisure Time: " + leisureTime + " minutes";
        }
    }
}
