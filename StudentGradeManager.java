import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

/**
 * StudentGradeManager.java
 * CSE 310 - Module 2: Language - Java
 * Author: Collins Chibuike Okolie
 *
 * A console application that allows users to manage student records,
 * calculate averages, assign letter grades, and save/load data from a file.
 * Demonstrates: classes, ArrayLists, loops, conditionals, methods, and file I/O.
 */
public class StudentGradeManager {

    // -------------------------------------------------------
    // Student class - represents one student with their scores
    // -------------------------------------------------------
    static class Student {
        private String name;        // Student's full name
        private ArrayList<Double> scores; // List of individual scores

        /**
         * Constructor - creates a new Student with an empty score list.
         * @param name The student's name
         */
        public Student(String name) {
            this.name = name;
            this.scores = new ArrayList<>();
        }

        /**
         * Adds a score to the student's score list.
         * @param score The score to add (0-100)
         */
        public void addScore(double score) {
            scores.add(score);
        }

        /**
         * Calculates and returns the student's average score.
         * Returns 0 if no scores have been added yet.
         * @return The average score as a double
         */
        public double getAverage() {
            if (scores.isEmpty()) return 0;
            double total = 0;
            for (double score : scores) {
                total += score;
            }
            return total / scores.size();
        }

        /**
         * Assigns a letter grade based on the student's average score.
         * A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: below 60
         * @return The letter grade as a String
         */
        public String getLetterGrade() {
            double avg = getAverage();
            if (avg >= 90) return "A";
            else if (avg >= 80) return "B";
            else if (avg >= 70) return "C";
            else if (avg >= 60) return "D";
            else return "F";
        }

        /**
         * Returns the student's name.
         * @return name as a String
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the list of scores for this student.
         * @return ArrayList of scores
         */
        public ArrayList<Double> getScores() {
            return scores;
        }

        /**
         * Returns a formatted string representation of the student record.
         * Used for both console display and file saving.
         * @return Formatted student data string
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Name: ").append(name).append("\n");
            sb.append("Scores: ");
            for (int i = 0; i < scores.size(); i++) {
                sb.append(scores.get(i));
                if (i < scores.size() - 1) sb.append(", ");
            }
            sb.append("\n");
            sb.append(String.format("Average: %.2f\n", getAverage()));
            sb.append("Letter Grade: ").append(getLetterGrade());
            return sb.toString();
        }
    }

    // -------------------------------------------------------
    // Main list that stores all students in memory
    // -------------------------------------------------------
    static ArrayList<Student> students = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    // -------------------------------------------------------
    // Main method - entry point of the program
    // -------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Welcome to Student Grade Manager");
        System.out.println("========================================");

        // Load any previously saved students from file on startup
        loadFromFile();

        // Keep showing the menu until the user chooses to exit
        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    addScoresToStudent();
                    break;
                case 3:
                    displayAllStudents();
                    break;
                case 4:
                    displayGradeReport();
                    break;
                case 5:
                    saveToFile();
                    break;
                case 6:
                    System.out.println("\nSaving data and exiting. Goodbye!");
                    saveToFile(); // auto-save on exit
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 6.");
            }
        }

        scanner.close();
    }

    // -------------------------------------------------------
    // Prints the main menu to the console
    // -------------------------------------------------------
    /**
     * Displays the main menu options to the user.
     */
    public static void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add a new student");
        System.out.println("2. Add scores to a student");
        System.out.println("3. View all students");
        System.out.println("4. View grade report");
        System.out.println("5. Save records to file");
        System.out.println("6. Exit");
    }

    // -------------------------------------------------------
    // Adds a new student to the students list
    // -------------------------------------------------------
    /**
     * Prompts the user for a student name and adds a new Student
     * object to the students ArrayList.
     */
    public static void addStudent() {
        System.out.print("\nEnter student name: ");
        String name = scanner.nextLine().trim();

        // Check if a student with this name already exists
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                System.out.println("A student named " + name + " already exists.");
                return;
            }
        }

        // Create and add the new student
        students.add(new Student(name));
        System.out.println("Student \"" + name + "\" added successfully!");
    }

    // -------------------------------------------------------
    // Adds scores to an existing student
    // -------------------------------------------------------
    /**
     * Lets the user select a student by number and enter one or
     * more scores for that student. Validates input range (0-100).
     */
    public static void addScoresToStudent() {
        if (students.isEmpty()) {
            System.out.println("\nNo students found. Please add a student first.");
            return;
        }

        // Display the list of students so the user can pick one
        System.out.println("\nSelect a student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).getName());
        }

        int index = getIntInput("Enter student number: ") - 1;

        // Validate the selection
        if (index < 0 || index >= students.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Student selected = students.get(index);

        // Ask how many scores to add
        int count = getIntInput("How many scores do you want to add? ");
        for (int i = 0; i < count; i++) {
            double score = -1;

            // Keep asking until the user enters a valid score between 0 and 100
            while (score < 0 || score > 100) {
                score = getDoubleInput("Enter score " + (i + 1) + " (0-100): ");
                if (score < 0 || score > 100) {
                    System.out.println("Score must be between 0 and 100. Try again.");
                }
            }
            selected.addScore(score);
        }

        System.out.println("Scores added to " + selected.getName() + " successfully!");
    }

    // -------------------------------------------------------
    // Displays all student records in the list
    // -------------------------------------------------------
    /**
     * Loops through the students ArrayList and prints each
     * student's full record including scores, average, and grade.
     */
    public static void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("\nNo students to display.");
            return;
        }

        System.out.println("\n======== All Student Records ========");
        for (int i = 0; i < students.size(); i++) {
            System.out.println("\nStudent " + (i + 1) + ":");
            System.out.println(students.get(i).toString());
            System.out.println("-------------------------------------");
        }
    }

    // -------------------------------------------------------
    // Displays a summary grade report for all students
    // -------------------------------------------------------
    /**
     * Prints a formatted summary table showing each student's
     * name, average score, and letter grade side by side.
     */
    public static void displayGradeReport() {
        if (students.isEmpty()) {
            System.out.println("\nNo students to report.");
            return;
        }

        System.out.println("\n======== Grade Report ========");
        System.out.printf("%-20s %-10s %-10s%n", "Name", "Average", "Grade");
        System.out.println("--------------------------------------");

        // Loop through every student and print their summary row
        for (Student s : students) {
            System.out.printf("%-20s %-10.2f %-10s%n",
                s.getName(),
                s.getAverage(),
                s.getLetterGrade());
        }

        System.out.println("======================================");
    }

    // -------------------------------------------------------
    // Saves all student records to a text file
    // -------------------------------------------------------
    /**
     * Writes all student data to "grades.txt" in the project folder.
     * Each student's data is separated by a line of dashes.
     * This allows records to persist after the program closes.
     */
    public static void saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("grades.txt"));

            for (Student s : students) {
                // Write the student name
                writer.println("NAME:" + s.getName());

                // Write each score on a new line
                for (double score : s.getScores()) {
                    writer.println("SCORE:" + score);
                }

                // Separator between students
                writer.println("---");
            }

            writer.close();
            System.out.println("\nRecords saved to grades.txt successfully!");

        } catch (IOException e) {
            // Handle file writing errors gracefully
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // Loads student records from the text file on startup
    // -------------------------------------------------------
    /**
     * Reads student data from "grades.txt" if it exists.
     * Reconstructs Student objects and re-adds their scores.
     * Called automatically when the program starts.
     */
    public static void loadFromFile() {
        File file = new File("grades.txt");

        // If no save file exists yet, skip loading
        if (!file.exists()) {
            System.out.println("No saved records found. Starting fresh.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            Student current = null;

            // Read the file line by line and reconstruct student objects
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("NAME:")) {
                    // Start a new student
                    current = new Student(line.substring(5));
                    students.add(current);
                } else if (line.startsWith("SCORE:") && current != null) {
                    // Add the score to the current student
                    double score = Double.parseDouble(line.substring(6));
                    current.addScore(score);
                }
                // Lines with "---" are separators — skip them
            }

            reader.close();
            System.out.println("Loaded " + students.size() + " student(s) from saved records.");

        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // Helper method - safely reads an integer from the user
    // -------------------------------------------------------
    /**
     * Prompts the user with a message and reads an integer input.
     * Keeps asking until valid integer input is provided.
     * @param prompt The message to display to the user
     * @return The valid integer entered by the user
     */
    public static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                // Input was not a valid number — ask again
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    // -------------------------------------------------------
    // Helper method - safely reads a decimal number from the user
    // -------------------------------------------------------
    /**
     * Prompts the user with a message and reads a double input.
     * Keeps asking until valid decimal input is provided.
     * @param prompt The message to display to the user
     * @return The valid double entered by the user
     */
    public static double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                // Input was not a valid decimal — ask again
                System.out.println("Please enter a valid number.");
            }
        }
    }
}