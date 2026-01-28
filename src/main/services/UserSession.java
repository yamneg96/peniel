package main.services;

import main.models.User;

public class UserSession {
    private static User currentUser = null;
    private static boolean isLoggedIn = false;
    private static String sessionToken = null;
    
    // Login user and create session
    public static void login(User user) {
        if (user == null) {
            System.err.println("ERROR: Cannot login with null user!");
            return;
        }
        
        currentUser = user;
        isLoggedIn = true;
        sessionToken = generateSessionToken();
        
        System.out.println("=== USER SESSION CREATED ===");
        System.out.println("User: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole());
        System.out.println("Session Token: " + sessionToken);
        System.out.println("============================");
    }
    
    // Logout user and clear session
    public static void logout() {
        System.out.println("Logging out user: " + (currentUser != null ? currentUser.getEmail() : "Unknown"));
        currentUser = null;
        isLoggedIn = false;
        sessionToken = null;
    }
    
    // Check if user is logged in
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    // Get current user
    public static User getCurrentUser() {
        return currentUser;
    }
    
    // Get current user's name
    public static String getCurrentUserName() {
        if (currentUser == null) {
            System.out.println("WARNING: getCurrentUserName() called but currentUser is null!");
            return "Guest";
        }
        return currentUser.getName();
    }
    
    // Get current user's email
    public static String getCurrentUserEmail() {
        if (currentUser == null) {
            return "";
        }
        return currentUser.getEmail();
    }
    
    // Get current user's role
    public static String getCurrentUserRole() {
        if (currentUser == null) {
            System.out.println("WARNING: getCurrentUserRole() called but currentUser is null!");
            return "Guest";
        }
        return currentUser.getRole();
    }
    
    // Get session token
    public static String getSessionToken() {
        return sessionToken;
    }
    
    // Generate a simple session token (in real app, use JWT or similar)
    private static String generateSessionToken() {
        return "SESSION_" + System.currentTimeMillis() + "_" + Math.random();
    }
    
    // Check if user has specific role
    public static boolean hasRole(String role) {
        if (currentUser == null) return false;
        return currentUser.getRole().equals(role);
    }
    
    // Get user info as string
    public static String getUserInfo() {
        if (currentUser == null) return "No user logged in";
        return String.format("Name: %s, Email: %s, Role: %s", 
            currentUser.getName(), currentUser.getEmail(), currentUser.getRole());
    }
}