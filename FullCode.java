import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Main {

    // --- 1. CONFIGURATION ---
    static final String DB_FILE = "university_data.db";
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    // --- 2. DATA STORAGE ---
    static HashMap<String, String> userDatabase = new HashMap<>();
    // REMOVED: userPhoneMap
    static Lecture[][] timetable = new Lecture[6][9];
    
    static String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    
    static String[] defaultTimeSlots = {
        "08:10 - 09:00 (Slot 1)", "09:00 - 09:50 (Slot 2)", "10:00 - 10:50 (Slot 3)", 
        "10:50 - 11:40 (Slot 4)", "11:40 - 12:20 (Slot 5)", "12:20 - 13:10 (Slot 6)", 
        "13:10 - 14:00 (Slot 7)", "14:10 - 15:00 (Slot 8)", "15:00 - 15:50 (Slot 9)"
    };

    static LocalTime[] slotStartTimes = {
        LocalTime.of(8, 10), LocalTime.of(9, 0), LocalTime.of(10, 0),
        LocalTime.of(10, 50), LocalTime.of(11, 40), LocalTime.of(12, 20),
        LocalTime.of(13, 10), LocalTime.of(14, 10), LocalTime.of(15, 0)
    };

    static LocalTime[] slotEndTimes = {
        LocalTime.of(9, 0), LocalTime.of(9, 50), LocalTime.of(10, 50),
        LocalTime.of(11, 40), LocalTime.of(12, 20), LocalTime.of(13, 10),
        LocalTime.of(14, 0), LocalTime.of(15, 0), LocalTime.of(15, 50)
    };

    // --- 3. LECTURE CLASS ---
    static class Lecture implements Serializable {
        private static final long serialVersionUID = 1L;
        String subject, roomNo, teacherName;
        boolean isLunch;

        public Lecture(String subject, String roomNo, String teacherName) {
            this.subject = subject;
            this.roomNo = roomNo;
            this.teacherName = teacherName;
            this.isLunch = false;
        }

        public Lecture(boolean isLunch) {
            this.isLunch = isLunch;
            this.subject = isLunch ? "LUNCH BREAK" : "--- Free ---";
            this.roomNo = "--";
            this.teacherName = "--";
        }

        public String toString() {
            if (isLunch) return " [ LUNCH BREAK ] ";
            if (subject.equals("--- Free ---")) return " [ --- Free --- ] ";
            return String.format("%s (Rm:%s) by %s", subject, roomNo, teacherName);
        }
    }

    // --- 4. MAIN ENTRY POINT ---
    public static void main(String[] args) {
        System.out.println(">> System initializing...");
        
        File f = new File(DB_FILE);
        System.out.println(">> Database Location: " + f.getAbsolutePath());

        if (!loadDatabase()) {
            System.out.println(">> No existing database found. Creating new...");
            setupDefaultSystem(); 
            saveDatabase();
        }

        System.out.println("=========================================");
        System.out.println("    UNIVERSITY PORTAL (Local Storage)    ");
        System.out.println("=========================================");

        while (true) {
            System.out.println("\n--- WELCOME MENU ---");
            System.out.println("1. Login");
            System.out.println("2. Create New Account");
            System.out.println("3. Exit Application");
            System.out.print("Select Option: ");
            
            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine(); 
                continue;
            }

            switch (choice) {
                case 1:
                    if (performLogin()) runDashboard();
                    break;
                case 2:
                    registerUser();
                    break;
                case 3:
                    System.out.println("Saving data and exiting.");
                    saveDatabase(); 
                    return; 
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // --- 5. DATABASE METHODS ---
    public static void saveDatabase() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DB_FILE))) {
            oos.writeObject(userDatabase);
            oos.writeObject(timetable);
            // REMOVED: Writing phone map
        } catch (IOException e) {
            System.out.println(">> Error saving database: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean loadDatabase() {
        File f = new File(DB_FILE);
        if (!f.exists()) return false; 

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            userDatabase = (HashMap<String, String>) ois.readObject();
            timetable = (Lecture[][]) ois.readObject();
            // REMOVED: Reading phone map
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(">> Error loading database. Starting fresh.");
            return false;
        }
    }

    // --- 6. ACCOUNT MANAGEMENT ---
    public static void registerUser() {
        System.out.println("\n--- REGISTER NEW USER ---");
        System.out.print("Enter a Username: ");
        String newUser = scanner.next();

        if (userDatabase.containsKey(newUser)) {
            System.out.println("Error: Username '" + newUser + "' is already taken!");
            return;
        }
        
        System.out.print("Enter a Password: ");
        String newPass = scanner.next();

        // REMOVED: Phone Number Input logic

        // OTP Generation (Simulated security check)
        int otp = 1000 + random.nextInt(9000); 

        System.out.println("\n------------------------------------------");
        System.out.println(" SECURITY CHECK");
        System.out.println(" Generated OTP: " + otp);
        System.out.println("------------------------------------------");

        System.out.print("Enter the OTP above to verify account: ");
        int userOtp = 0;
        try {
            userOtp = scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
        }

        if (userOtp == otp) {
            userDatabase.put(newUser, newPass);
            saveDatabase();
            System.out.println(">> SUCCESS! Account Created.");
        } else {
            System.out.println(">> WRONG OTP. Registration Cancelled.");
        }
    }

    public static boolean performLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String user = scanner.next();
        System.out.print("Password: ");
        String pass = scanner.next();
        
        if (userDatabase.containsKey(user) && userDatabase.get(user).equals(pass)) {
            System.out.println("\n>> Login Successful! Welcome, " + user + ".");
            // REMOVED: Phone display
            return true;
        } else {
            System.out.println(">> Error: Invalid Username or Password.");
            return false;
        }
    }

    // --- 7. DASHBOARD & LOGIC ---
    public static void runDashboard() {
        while (true) {
            System.out.println("\n======= STUDENT DASHBOARD =======");
            System.out.println("1. View Timetable (Full Week)");
            System.out.println("2. View Specific Day");
            System.out.println("3. EDIT Timetable");
            System.out.println("4. VIEW PRESENT CLASS (Live)"); 
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1: printFullWeek(); break;
                case 2: viewDay(); break;
                case 3: editSlot(); break;
                case 4: viewCurrentClass(); break; 
                case 5: 
                    System.out.println("Logging out...");
                    return; 
                default: System.out.println("Invalid choice.");
            }
        }
    }

    public static void setupDefaultSystem() {
        userDatabase.put("admin", "1234");
        // REMOVED: Admin phone setup
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                if (j == 4) timetable[i][j] = new Lecture(true); 
                else timetable[i][j] = new Lecture(false); 
            }
        }
    }

    public static void editSlot() {
        System.out.println("\n--- EDIT CLASS DETAILS ---");
        System.out.println("Select Day (1=Mon ... 6=Sat): ");
        int day = scanner.nextInt() - 1;
        System.out.println("Select Slot (1-9): ");
        int slot = scanner.nextInt() - 1;
        scanner.nextLine(); 

        if (day < 0 || day > 5 || slot < 0 || slot > 8 || slot == 4) {
            System.out.println("Error: Invalid selection or Lunch slot.");
            return;
        }
        System.out.print("Subject: ");
        String sub = scanner.nextLine();
        System.out.print("Room: ");
        String room = scanner.nextLine();
        System.out.print("Teacher: ");
        String teach = scanner.nextLine();

        timetable[day][slot] = new Lecture(sub, room, teach);
        saveDatabase(); 
        System.out.println(">> Updated Successfully.");
    }

    public static void viewCurrentClass() {
        LocalTime now = LocalTime.now();
        LocalDate todayDate = LocalDate.now();
        DayOfWeek day = todayDate.getDayOfWeek();
        int dayIndex = day.getValue() - 1;

        System.out.println("\n--- LIVE CLASS STATUS ---");
        System.out.println("Current Time: " + now.getHour() + ":" + String.format("%02d", now.getMinute()));
        System.out.println("Current Day:  " + day);

        if (dayIndex > 5 || dayIndex < 0) {
            System.out.println(">> NO CLASSES TODAY (Weekend).");
            return;
        }

        boolean classFound = false;
        for (int i = 0; i < 9; i++) {
            boolean isAfterStart = now.compareTo(slotStartTimes[i]) >= 0;
            boolean isBeforeEnd = now.compareTo(slotEndTimes[i]) < 0;

            if (isAfterStart && isBeforeEnd) {
                System.out.println("----------------------------------------");
                System.out.println("ONGOING: " + defaultTimeSlots[i]);
                System.out.println("DETAILS: " + timetable[dayIndex][i].toString());
                System.out.println("----------------------------------------");
                classFound = true;
                break;
            }
        }
        if (!classFound) System.out.println(">> You are currently on a break or outside class hours.");
    }

    public static void viewDay() {
        System.out.println("Select Day (1=Mon ... 6=Sat): ");
        int day = scanner.nextInt();
        if (day >= 1 && day <= 6) printDaySchedule(day - 1);
        else System.out.println("Invalid Day.");
    }

    public static void printDaySchedule(int dayIndex) {
        System.out.println("\nSCHEDULE FOR: " + days[dayIndex]);
        System.out.println("----------------------------------------------------------------------");
        for (int i = 0; i < 9; i++) {
            System.out.printf("%-25s | %s%n", defaultTimeSlots[i], timetable[dayIndex][i].toString());
        }
        System.out.println("----------------------------------------------------------------------");
    }

    public static void printFullWeek() {
        for (int i = 0; i < 6; i++) {
            printDaySchedule(i);
        }
    }
}