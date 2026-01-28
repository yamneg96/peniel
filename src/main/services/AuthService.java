package main.services;

import main.models.User;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class AuthService {
    private static final String USERS_CSV_FILE = "data/users.csv";
    private static final String CSV_HEADER = "name,email,password,phone,role,created_at";
    private Map<String, User> userDatabase = new HashMap<>();
    
    public AuthService() {
        ensureDataDirectory();
        loadUsersFromCSV();
        System.out.println("AuthService initialized with " + userDatabase.size() + " users");
        // Debug: Print all loaded users
        for (String email : userDatabase.keySet()) {
            User user = userDatabase.get(email);
            System.out.println("Loaded user: " + user.getEmail() + " - " + user.getName() + " - " + user.getRole());
        }
    }
    
    private void ensureDataDirectory() {
        try {
            Path dataDir = Paths.get("data");
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
                System.out.println("Created data directory: " + dataDir.toAbsolutePath());
            }
            
            Path usersFile = Paths.get(USERS_CSV_FILE);
            if (!Files.exists(usersFile)) {
                // Create file with header and default admin user
                try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_CSV_FILE))) {
                    writer.println(CSV_HEADER);
                    // Add default admin user
                    User admin = new User("Admin User", "admin@hospital.com", "password123", 
                                        "1234567890", "Medical Staff");
                    writer.println(admin.toCSV());
                    userDatabase.put(admin.getEmail().toLowerCase(), admin);
                }
                System.out.println("Created users CSV file with default admin");
            }
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }
    
    private void loadUsersFromCSV() {
        userDatabase.clear();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(USERS_CSV_FILE), StandardCharsets.UTF_8))) {
            
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                
                if (!line.trim().isEmpty()) {
                    User user = User.fromCSV(line);
                    if (user != null) {
                        userDatabase.put(user.getEmail().toLowerCase(), user);
                        System.out.println("Loaded from CSV: " + user.getEmail() + " as " + user.getRole());
                    } else {
                        System.err.println("Failed to parse CSV line: " + line);
                    }
                }
            }
            
            System.out.println("Loaded " + userDatabase.size() + " users from CSV");
            
        } catch (FileNotFoundException e) {
            System.err.println("Users CSV file not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading users CSV: " + e.getMessage());
        }
    }
    
    private synchronized void saveUsersToCSV() {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(USERS_CSV_FILE), StandardCharsets.UTF_8))) {
            
            writer.println(CSV_HEADER);
            
            for (User user : userDatabase.values()) {
                writer.println(user.toCSV());
            }
            
            writer.flush();
            System.out.println("Saved " + userDatabase.size() + " users to CSV");
            
        } catch (IOException e) {
            System.err.println("Error saving users to CSV: " + e.getMessage());
        }
    }
    
    public boolean login(String email, String password) throws Exception {
        // Validate input
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Email cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("Password cannot be empty");
        }
        
        email = email.trim().toLowerCase();
        
        // Validate email format
        if (!isValidEmail(email)) {
            throw new Exception("Please enter a valid email address");
        }
        
        // Load fresh data in case file was modified
        loadUsersFromCSV();
        
        // Debug: Print all users in database
        System.out.println("Looking for user: " + email);
        System.out.println("Users in database: " + userDatabase.keySet());
        
        // Check if user exists
        User user = userDatabase.get(email);
        if (user == null) {
            throw new Exception("User not found. Please check your email or register.");
        }
        
        // Debug: Print user details
        System.out.println("Found user: " + user.getEmail() + " - Name: " + user.getName() + " - Role: " + user.getRole());
        
        // Check password (in real app, you'd hash passwords)
        if (!user.getPassword().equals(password)) {
            throw new Exception("Invalid password. Please try again.");
        }
        
        // Create user session - THIS IS THE CRITICAL PART
        System.out.println("Calling UserSession.login() for: " + user.getEmail());
        UserSession.login(user);
        
        // Verify session was created
        if (UserSession.isLoggedIn()) {
            System.out.println("UserSession confirmed - Current user: " + UserSession.getCurrentUserName());
            System.out.println("UserSession role: " + UserSession.getCurrentUserRole());
        } else {
            System.out.println("WARNING: UserSession not set properly!");
        }
        
        // Log login activity
        logLoginActivity(email, true);
        
        System.out.println("Login successful for: " + email);
        return true;
    }
    
    public boolean register(String name, String email, String password, String phone, String role) throws Exception {
        // Validate all fields
        validateRegistration(name, email, password, phone, role);
        
        email = email.trim().toLowerCase();
        
        // Load fresh data
        loadUsersFromCSV();
        
        // Check if user already exists
        if (userDatabase.containsKey(email)) {
            throw new Exception("An account with this email already exists. Please use a different email or login.");
        }
        
        // Validate password strength
        if (!isValidPassword(password)) {
            throw new Exception("Password must be at least 8 characters long and contain letters and numbers");
        }
        
        // Create new user
        User newUser = new User(name.trim(), email, password, phone.trim(), role);
        
        // Add to database
        userDatabase.put(email, newUser);
        
        // Save to CSV file
        saveUsersToCSV();
        
        // Log registration activity
        logRegistrationActivity(email, role);
        
        System.out.println("New user registered: " + email + " (" + role + ")");
        System.out.println("Total users in database: " + userDatabase.size());
        
        return true;
    }
    
    private void validateRegistration(String name, String email, String password, String phone, String role) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Full name is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Email is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("Password is required");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new Exception("Phone number is required");
        }
        if (role == null || role.trim().isEmpty() || role.equals("Select Role")) {
            throw new Exception("Please select your professional role");
        }
        
        // Validate email format
        if (!isValidEmail(email)) {
            throw new Exception("Please enter a valid email address (e.g., user@example.com)");
        }
        
        // Validate phone number (basic check)
        if (!isValidPhone(phone)) {
            throw new Exception("Please enter a valid phone number (10-15 digits)");
        }
        
        // Validate name length
        if (name.trim().length() < 2) {
            throw new Exception("Name must be at least 2 characters long");
        }
    }
    
    public boolean emailExists(String email) {
        loadUsersFromCSV(); // Refresh from file
        return userDatabase.containsKey(email.trim().toLowerCase());
    }
    
    public User getUserByEmail(String email) {
        return userDatabase.get(email.trim().toLowerCase());
    }
    
    public Map<String, User> getAllUsers() {
        loadUsersFromCSV(); // Refresh from file
        return new HashMap<>(userDatabase);
    }
    
    public int getUserCount() {
        return userDatabase.size();
    }
    
    // Update user profile
    public boolean updateProfile(String email, String name, String phone, String role) throws Exception {
        User user = userDatabase.get(email);
        if (user == null) {
            throw new Exception("User not found");
        }
        
        user.setName(name);
        user.setPhone(phone);
        user.setRole(role);
        
        saveUsersToCSV();
        
        System.out.println("Profile updated for: " + email);
        return true;
    }
    
    // Delete user (admin function)
    public boolean deleteUser(String email) throws Exception {
        if (!userDatabase.containsKey(email)) {
            throw new Exception("User not found");
        }
        
        userDatabase.remove(email);
        saveUsersToCSV();
        
        System.out.println("User deleted: " + email);
        return true;
    }
    
    // Activity logging
    private void logLoginActivity(String email, boolean success) {
        try {
            Path logDir = Paths.get("data/logs");
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            
            String logFile = "data/logs/auth_log.csv";
            boolean fileExists = Files.exists(Paths.get(logFile));
            
            try (PrintWriter writer = new PrintWriter(
                    new FileWriter(logFile, true))) {
                
                if (!fileExists) {
                    writer.println("timestamp,activity,email,success");
                }
                
                String timestamp = java.time.LocalDateTime.now().toString();
                writer.println(String.join(",",
                    timestamp,
                    "LOGIN",
                    email,
                    String.valueOf(success)
                ));
            }
        } catch (IOException e) {
            System.err.println("Error logging activity: " + e.getMessage());
        }
    }
    
    private void logRegistrationActivity(String email, String role) {
        try {
            Path logDir = Paths.get("data/logs");
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            
            String logFile = "data/logs/auth_log.csv";
            boolean fileExists = Files.exists(Paths.get(logFile));
            
            try (PrintWriter writer = new PrintWriter(
                    new FileWriter(logFile, true))) {
                
                if (!fileExists) {
                    writer.println("timestamp,activity,email,role");
                }
                
                String timestamp = java.time.LocalDateTime.now().toString();
                writer.println(String.join(",",
                    timestamp,
                    "REGISTER",
                    email,
                    role
                ));
            }
        } catch (IOException e) {
            System.err.println("Error logging activity: " + e.getMessage());
        }
    }
    
    // Validation helpers
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }
    
    private boolean isValidPassword(String password) {
        // At least 8 characters, contains letters and numbers
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$";
        return password != null && password.matches(passwordRegex);
    }
    
    private boolean isValidPhone(String phone) {
        // Basic phone validation: 10-15 digits, may include +, spaces, dashes
        String cleanedPhone = phone.replaceAll("[\\s\\-()+]", "");
        return cleanedPhone.matches("^\\d{10,15}$");
    }
}