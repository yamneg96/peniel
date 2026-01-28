package main.models;

public class User {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private String createdAt;
    
    public User(String name, String email, String password, String phone, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.createdAt = java.time.LocalDateTime.now().toString();
    }
    
    public User(String name, String email, String password, String phone, String role, String createdAt) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    // Convert to CSV format
    public String toCSV() {
        return String.join(",",
            escapeCSV(name),
            escapeCSV(email),
            escapeCSV(password),
            escapeCSV(phone),
            escapeCSV(role),
            escapeCSV(createdAt)
        );
    }
    
    // Create from CSV line
    public static User fromCSV(String csvLine) {
        String[] parts = parseCSVLine(csvLine);
        if (parts.length >= 6) {
            return new User(
                unescapeCSV(parts[0]),
                unescapeCSV(parts[1]),
                unescapeCSV(parts[2]),
                unescapeCSV(parts[3]),
                unescapeCSV(parts[4]),
                unescapeCSV(parts[5])
            );
        }
        return null;
    }
    
    // Helper methods for CSV handling
    private static String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    private static String unescapeCSV(String value) {
        if (value == null || value.isEmpty()) return "";
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            value = value.replace("\"\"", "\"");
        }
        return value;
    }
    
    private static String[] parseCSVLine(String line) {
        java.util.List<String> result = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            
            if (ch == '"') {
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(ch);
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }
    
    @Override
    public String toString() {
        return String.format("User{name='%s', email='%s', role='%s', created='%s'}", 
            name, email, role, createdAt);
    }
}