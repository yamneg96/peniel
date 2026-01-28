package main;

import javax.swing.*;
import java.awt.*;
import main.views.*;
import main.services.UserSession;

public class Application extends JFrame {
    public CardLayout cardLayout = new CardLayout();
    public JPanel container = new JPanel(cardLayout);
    
    // Store references to panels for easy access
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private DashboardPanel dashboardPanel;

    public Application() {
        setTitle("BNS - Hospital Management");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Initialize UI with a modern layout
        initUI();
        
        // Show appropriate page based on authentication
        showInitialPage();
        
        setVisible(true);
    }

    private void initUI() {
        // Create main container with CardLayout
        container = new JPanel(cardLayout);
        
        // Create panel instances and add to container
        addPanelsToContainer();
        
        // Create a main wrapper
        JPanel mainWrapper = new JPanel(new BorderLayout());
        mainWrapper.add(container, BorderLayout.CENTER);
        
        add(mainWrapper);
    }

    private void addPanelsToContainer() {
        // Create panel instances
        HomePanel homePanel = new HomePanel(this);
        AboutPanel aboutPanel = new AboutPanel(this);
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        dashboardPanel = new DashboardPanel(this);
        MyAssignmentsPanel assignmentsPanel = new MyAssignmentsPanel(this);
        NotificationsPanel notificationsPanel = new NotificationsPanel(this);
        SupportPanel supportPanel = new SupportPanel(this);
        BedsPanel bedsPanel = new BedsPanel(this);
        
        // Add panels to container
        container.add(homePanel, "HOME");
        container.add(aboutPanel, "ABOUT");
        container.add(loginPanel, "LOGIN");
        container.add(registerPanel, "REGISTER");
        container.add(dashboardPanel, "DASHBOARD");
        container.add(assignmentsPanel, "ASSIGNMENTS");
        container.add(notificationsPanel, "NOTIFS");
        container.add(supportPanel, "SUPPORT");
        container.add(bedsPanel, "BEDS");
    }

    private void showInitialPage() {
        // Check if user is already logged in
        if (UserSession.isLoggedIn()) {
            cardLayout.show(container, "DASHBOARD");
            updateWindowTitle("DASHBOARD");
        } else {
            cardLayout.show(container, "HOME");
            updateWindowTitle("HOME");
        }
    }

    public void showPage(String pageName) {
        // Define which pages require authentication
        String[] protectedPages = {"DASHBOARD", "ASSIGNMENTS", "NOTIFS", "BEDS"};
        
        // Check if the requested page requires authentication
        boolean requiresAuth = false;
        for (String protectedPage : protectedPages) {
            if (protectedPage.equals(pageName)) {
                requiresAuth = true;
                break;
            }
        }
        
        // Redirect to login if accessing protected page without authentication
        if (requiresAuth && !UserSession.isLoggedIn()) {
            int option = JOptionPane.showConfirmDialog(this,
                "This page requires login. Would you like to login now?",
                "Authentication Required",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (option == JOptionPane.YES_OPTION) {
                cardLayout.show(container, "LOGIN");
                updateWindowTitle("LOGIN");
                // Clear any previous login data
                if (loginPanel != null) {
                    loginPanel.clearForm();
                }
            }
            return;
        }
        
        // Handle logout
        if (pageName.equals("LOGOUT")) {
            handleLogout();
            return;
        }
        
        // Handle login success from LoginPanel
        if (pageName.equals("LOGIN_SUCCESS")) {
            handleLoginSuccess();
            return;
        }
        
        // Handle registration success from RegisterPanel
        if (pageName.equals("REGISTER_SUCCESS")) {
            handleRegisterSuccess();
            return;
        }
        
        // Show the requested page
        cardLayout.show(container, pageName);
        updateWindowTitle(pageName);
    }
    
    public void handleLoginSuccess() {
        // Show welcome message
        String userName = UserSession.getCurrentUserName();
        String userRole = UserSession.getCurrentUserRole();
        
        JOptionPane.showMessageDialog(this,
            "<html><div style='text-align: center;'>"
            + "<h3>Welcome back, " + userName + "!</h3>"
            + "<p>Role: " + userRole + "</p>"
            + "<p>You have successfully logged in to the BNS Hospital Management System.</p>"
            + "</div></html>",
            "Login Successful",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Navigate to dashboard
        cardLayout.show(container, "DASHBOARD");
        updateWindowTitle("DASHBOARD");
        
        // Clear login form
        if (loginPanel != null) {
            loginPanel.clearForm();
        }
    }
    
    private void handleRegisterSuccess() {
        // Show success message
        JOptionPane.showMessageDialog(this,
            "<html><div style='text-align: center;'>"
            + "<h3>Registration Successful!</h3>"
            + "<p>Your account has been created successfully.</p>"
            + "<p>Please login with your credentials to access the system.</p>"
            + "</div></html>",
            "Account Created",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Navigate to login page
        cardLayout.show(container, "LOGIN");
        updateWindowTitle("LOGIN");
        
        // Clear registration form
        if (registerPanel != null) {
            registerPanel.clearForm();
        }
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "<html><div style='text-align: center;'>"
            + "<p>Are you sure you want to logout?</p>"
            + "</div></html>",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Get user info before logging out
            String userName = UserSession.getCurrentUserName();
            
            // Logout from session
            UserSession.logout();
            
            // Reset panels if needed
            resetPanels();
            
            // Navigate to login page
            cardLayout.show(container, "LOGIN");
            updateWindowTitle("LOGIN");
            
            // Show logout confirmation
            JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>"
                + "<p>You have been logged out successfully.</p>"
                + "<p>Goodbye, " + userName + "!</p>"
                + "</div></html>",
                "Logged Out",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void resetPanels() {
        // Clear sensitive data from panels
        if (loginPanel != null) {
            loginPanel.clearForm();
        }
        if (registerPanel != null) {
            registerPanel.clearForm();
        }
        // Add more panel resets as needed
    }
    
    private void updateWindowTitle(String pageName) {
        String baseTitle = "BNS - Hospital Management";
        String pageTitle = "";
        
        switch (pageName) {
            case "HOME":
                pageTitle = "Home";
                break;
            case "LOGIN":
                pageTitle = "Login";
                break;
            case "REGISTER":
                pageTitle = "Register";
                break;
            case "DASHBOARD":
                pageTitle = "Dashboard";
                break;
            case "ASSIGNMENTS":
                pageTitle = "My Assignments";
                break;
            case "NOTIFS":
                pageTitle = "Notifications";
                break;
            case "BEDS":
                pageTitle = "Bed Management";
                break;
            case "ABOUT":
                pageTitle = "About";
                break;
            case "SUPPORT":
                pageTitle = "Support";
                break;
            default:
                pageTitle = pageName;
        }
        
        // Add user info if logged in
        if (UserSession.isLoggedIn()) {
            String userInfo = String.format(" | User: %s (%s)", 
                UserSession.getCurrentUserName(), 
                UserSession.getCurrentUserRole());
            setTitle(baseTitle + " - " + pageTitle + userInfo);
        } else {
            setTitle(baseTitle + " - " + pageTitle);
        }
    }
    
    // Method to handle login from external sources
    public void performLogin(String email, String password) {
        // This method can be called from other parts of the application
        // For now, just show login page
        showPage("LOGIN");
    }
    
    // Method to get current page name (simplified)
    public String getCurrentPage() {
        // In a real implementation, you would track the current page
        return "UNKNOWN";
    }
    
    // Refresh the current view
    public void refreshView() {
        String currentPage = getCurrentPage();
        if (!currentPage.equals("UNKNOWN")) {
            showPage(currentPage);
        }
    }

    public static void main(String[] args) {
        // Set system look and feel for modern appearance
        try {
            // Try to set the system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Customize some UI defaults for better appearance
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("ProgressBar.arc", 8);
            UIManager.put("TextComponent.arc", 5);
            
            // Set default font
            Font defaultFont = new Font("Segoe UI", Font.PLAIN, 12);
            UIManager.put("Button.font", defaultFont);
            UIManager.put("Label.font", defaultFont);
            UIManager.put("TextField.font", defaultFont);
            UIManager.put("PasswordField.font", defaultFont);
            UIManager.put("TextArea.font", defaultFont);
            UIManager.put("ComboBox.font", defaultFont);
            UIManager.put("CheckBox.font", defaultFont);
            UIManager.put("RadioButton.font", defaultFont);
            UIManager.put("ToggleButton.font", defaultFont);
            UIManager.put("Menu.font", defaultFont);
            UIManager.put("MenuItem.font", defaultFont);
            UIManager.put("MenuBar.font", defaultFont);
            UIManager.put("PopupMenu.font", defaultFont);
            UIManager.put("OptionPane.font", defaultFont);
            UIManager.put("TitledBorder.font", defaultFont);
            UIManager.put("ToolTip.font", defaultFont);
            
            // Set default colors
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("OptionPane.messageForeground", new Color(15, 23, 42));
            
        } catch (Exception e) {
            System.err.println("Warning: Could not set system look and feel. Using default.");
            e.printStackTrace();
        }
        
        // Start the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            Application app = new Application();
            
            // Set application icon if available
            try {
                // You can add an icon file in your assets folder
                // ImageIcon icon = new ImageIcon("path/to/icon.png");
                // app.setIconImage(icon.getImage());
            } catch (Exception e) {
                // Icon not found, continue without it
            }
            
            // Center the window on screen
            app.setLocationRelativeTo(null);
        });
    }
}