import java.util.*;       // For Scanner and Random
import java.time.*;       // For LocalDate and LocalTime

// Basic structure of a Java program
public class FundamentalsDemo {

    // --- Variables and Constants ---
    static final String APP_NAME = "University Portal";
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    // --- Arrays ---
    static String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    static String[] slots = {
        "08:10 - 09:00", "09:00 - 09:50", "10:00 - 10:50",
        "10:50 - 11:40", "11:40 - 12:20", "12:20 - 13:10",
        "13:10 - 14:00", "14:10 - 15:00", "15:00 - 15:50"
    };

    // --- Example Inner Class ---
    static class Lecture {
        String subject;
        String room;
        String faculty;

        Lecture(String subject, String room, String faculty) {
            this.subject = subject;
            this.room = room;
            this.faculty = faculty;
        }

        public String toString() {
            return subject + " (" + room + ") by " + faculty;
        }
    }

    // --- Main Method (starting point) ---
    public static void main(String[] args) {
        System.out.println("Welcome to " + APP_NAME);

        // Primitive data types
        int numberOfDays = days.length;
        boolean isActive = true;

        // Conditional example
        if (isActive) {
            System.out.println("System is active for " + numberOfDays + " days.");
        } else {
            System.out.println("System inactive.");
        }

        // Loop example
        for (int i = 0; i < days.length; i++) {
            System.out.println("Day " + (i + 1) + ": " + days[i]);
        }

        // Object creation
        Lecture lec = new Lecture("Java Programming", "A-202", "Mr. Ravi");
        System.out.println("Sample Lecture: " + lec);

        // LocalTime usage
        LocalTime now = LocalTime.now();
        System.out.println("Current time: " + now);

        // Input example
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Hello, " + name + "!");
    }
}
