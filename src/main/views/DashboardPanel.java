package main.views;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import main.Application;
import main.views.components.MedicalCard;
import main.services.UserSession;
import main.models.User;

public class DashboardPanel extends JPanel {
    private boolean isSubscribed = true;
    private int daysLeft = 24;
    private Application app;
    
    // UI Components that need updating
    private JLabel userNameLabel;
    private JLabel roleLabel;
    private JLabel emailLabel;
    private JLabel sessionLabel;
    private JPanel centerContainer;
    private JPanel subscriptionBanner;
    private JButton refreshBtn;
    private JPanel profileCard;
    private JLabel roleIcon;
    
// Real Data Mapping - This should already be in your class
private final Map<String, String[]> deptData = new HashMap<>() {{
    put("Internal Medicine", new String[]{"Ward C", "Ward D", "Inpatient", "Stroke", "Kalazar"});
    put("GynObs", new String[]{"Labor", "M1 up", "M1 down", "M2 (post natal)", "M3 (high risk)", "Gyn ward", "Michu", "Fistula"});
    put("Pediatrics", new String[]{"ETAT", "Emergency", "Main ward", "Nutrition and infant", "Hemato Oncology", "NICU"});
    put("Surgery", new String[]{"Trauma EOPD", "Non trauma EOPD", "Recovery", "Surgical ward", "Ortho ward"});
    put("Emergency", new String[]{"Red", "Orange", "Yellow"});
    put("Dermatology", new String[]{"Dermatology"});
    put("Psychiatry", new String[]{"Psychiatry"});
    put("ENT", new String[]{"ENT"});
    put("Ophthalmology", new String[]{"Ophthalmology"});
}};

    public DashboardPanel(Application app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        
        System.out.println("\n=== DASHBOARD PANEL CREATED ===");
        
        // Initialize all UI components
        initializeUI();
        
        // Set initial state based on current session
        updateUIForCurrentSession();
    }
    
    private void initializeUI() {
        // Create subscription banner
        subscriptionBanner = createSubscriptionBanner();
        add(subscriptionBanner, BorderLayout.NORTH);

        centerContainer = new JPanel(new BorderLayout(20, 20));
        centerContainer.setOpaque(false);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(30, 40, 40, 40));

        // Header Section
        JPanel headerGrp = new JPanel(new BorderLayout());
        headerGrp.setOpaque(false);
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel systemTag = new JLabel("HOSPITAL SYSTEM V1.0");
        systemTag.setFont(new Font("SansSerif", Font.BOLD, 10));
        systemTag.setForeground(new Color(37, 99, 235));
        JLabel welcome = new JLabel("Clinical Dashboard");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 32));
        titlePanel.add(systemTag);
        titlePanel.add(welcome);

        // Profile Section
        JPanel rightHeaderSide = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightHeaderSide.setOpaque(false);
        
        // Profile Card
        profileCard = new JPanel(new GridLayout(4, 1, 0, 5));
        profileCard.setOpaque(true);
        profileCard.setBackground(Color.WHITE);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(241, 245, 249), 1),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        
        // User Name
        userNameLabel = new JLabel();
        userNameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        // User Role
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rolePanel.setOpaque(false);
        roleIcon = new JLabel();
        roleIcon.setFont(new Font("SansSerif", Font.PLAIN, 12));
        roleLabel = new JLabel();
        roleLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        rolePanel.add(roleIcon);
        rolePanel.add(roleLabel);
        
        // User Email
        emailLabel = new JLabel();
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        
        // Session Status
        sessionLabel = new JLabel();
        sessionLabel.setFont(new Font("SansSerif", Font.PLAIN, 9));
        
        profileCard.add(userNameLabel);
        profileCard.add(rolePanel);
        profileCard.add(emailLabel);
        profileCard.add(sessionLabel);

        // Buttons
        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to logout?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Logout initiated from Dashboard");
                UserSession.logout();
                updateUIForCurrentSession();
                app.showPage("LOGIN");
            }
        });

        refreshBtn = new JButton("🔄");
        refreshBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBackground(new Color(37, 99, 235));
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        refreshBtn.setToolTipText("Refresh User Session");
        refreshBtn.addActionListener(e -> {
            System.out.println("Refresh button clicked - Rechecking UserSession...");
            updateUIForCurrentSession();
        });

        rightHeaderSide.add(profileCard);
        rightHeaderSide.add(refreshBtn);
        rightHeaderSide.add(logoutBtn);
        headerGrp.add(titlePanel, BorderLayout.WEST);
        headerGrp.add(rightHeaderSide, BorderLayout.EAST);
        centerContainer.add(headerGrp, BorderLayout.NORTH);
        
        add(centerContainer, BorderLayout.CENTER);
    }
    
    private void updateUIForCurrentSession() {
        boolean loggedIn = UserSession.isLoggedIn();
        String userName = UserSession.getCurrentUserName();
        String userRole = UserSession.getCurrentUserRole();
        String userEmail = UserSession.getCurrentUserEmail();
        
        System.out.println("\n=== UPDATING DASHBOARD UI ===");
        System.out.println("Logged in: " + loggedIn);
        System.out.println("User name: " + userName);
        System.out.println("User role: " + userRole);
        System.out.println("User email: " + userEmail);
        
        // Update profile card FIRST
        if (loggedIn) {
            userNameLabel.setText("Dr. " + userName);
            userNameLabel.setForeground(new Color(15, 23, 42));
            
            roleIcon.setText(getRoleIcon(userRole));
            roleLabel.setText(getFormattedRole(userRole));
            roleLabel.setForeground(getRoleColor(userRole));
            
            emailLabel.setText(userEmail);
            emailLabel.setForeground(new Color(100, 116, 139));
            
            sessionLabel.setText("✓ Session Active");
            sessionLabel.setForeground(new Color(34, 197, 94));
        } else {
            userNameLabel.setText("Dr. Not Logged In");
            userNameLabel.setForeground(Color.RED);
            
            roleIcon.setText("✖");
            roleLabel.setText("NOT LOGGED IN");
            roleLabel.setForeground(Color.RED);
            
            emailLabel.setText("No email available");
            emailLabel.setForeground(Color.GRAY);
            
            sessionLabel.setText("⚠ No Active Session");
            sessionLabel.setForeground(new Color(239, 68, 68));
        }
        
        // Update subscription banner
        remove(subscriptionBanner);
        subscriptionBanner = createSubscriptionBanner();
        add(subscriptionBanner, BorderLayout.NORTH);
        
        // Update dashboard content - COMPLETELY rebuild it
        updateDashboardContent();
        
        // Force UI refresh
        revalidate();
        repaint();
        
        System.out.println("UI update complete!");
    }
    
    private void updateDashboardContent() {
        // Remove everything from center container except the header
        Component[] centerComponents = centerContainer.getComponents();
        for (int i = centerComponents.length - 1; i >= 0; i--) {
            Component comp = centerComponents[i];
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getLayout() instanceof BorderLayout) {
                    // Check if this is the header (has the title and profile)
                    boolean isHeader = false;
                    Component[] headerComps = panel.getComponents();
                    for (Component headerComp : headerComps) {
                        if (headerComp instanceof JPanel) {
                            JPanel innerPanel = (JPanel) headerComp;
                            if (innerPanel.getComponentCount() > 0 && 
                                innerPanel.getComponent(0) instanceof JLabel) {
                                JLabel label = (JLabel) innerPanel.getComponent(0);
                                if (label.getText().equals("Clinical Dashboard")) {
                                    isHeader = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!isHeader) {
                        centerContainer.remove(panel);
                    }
                } else {
                    centerContainer.remove(panel);
                }
            }
        }
        
        

        // Check if user is logged in
        if (!UserSession.isLoggedIn() || UserSession.getCurrentUserRole().equals("Guest")) {
            // Show login required message
            JPanel loginRequiredPanel = new JPanel(new BorderLayout());
            loginRequiredPanel.setOpaque(false);
            loginRequiredPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
            
            JLabel loginMessage = new JLabel("<html><div style='text-align: center;'>" +
                "<h2 style='color: #ef4444;'>⚠ Login Required</h2>" +
                "<p>Please login to access the dashboard features.</p>" +
                "<p>Current session status: " + (UserSession.isLoggedIn() ? "Logged In" : "Not Logged In") + "</p>" +
                "</div></html>", SwingConstants.CENTER);
            loginMessage.setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            JButton loginButton = new JButton("Go to Login Page");
            loginButton.setBackground(new Color(37, 99, 235));
            loginButton.setForeground(Color.WHITE);
            loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            loginButton.addActionListener(e -> app.showPage("LOGIN"));
            
            loginRequiredPanel.add(loginMessage, BorderLayout.CENTER);
            loginRequiredPanel.add(loginButton, BorderLayout.SOUTH);
            
            centerContainer.add(loginRequiredPanel, BorderLayout.CENTER);
        } else {
            // User is logged in - show dashboard content
            String userRole = UserSession.getCurrentUserRole();
            String userName = UserSession.getCurrentUserName();
            
            // Module Grid
            JPanel grid = new JPanel(new GridLayout(0, 3, 25, 25)); 
            grid.setOpaque(false);
            
            // Always show Ward Beds for everyone
            grid.add(new MedicalCard("Ward Beds", "Assign and monitor ward beds", new Color(13, 148, 136), () -> app.showPage("BEDS")));
            
            // Role-based access control
            boolean isLimitedAccess = userRole.equalsIgnoreCase("Intern");
            
            if (!isLimitedAccess) {
                // Full access for senior roles
                grid.add(new MedicalCard("Notifications", "New admissions & alerts", new Color(37, 99, 235), () -> app.showPage("NOTIFS")));
                grid.add(new MedicalCard("Assignments", "Your current ward duties", new Color(124, 58, 237), () -> app.showPage("ASSIGNMENTS")));
                grid.add(new MedicalCard("Support", "Get help and FAQs", new Color(234, 88, 12), () -> app.showPage("SUPPORT")));
                grid.add(new MedicalCard("About BNS", "Institutional protocols", new Color(71, 85, 105), () -> app.showPage("ABOUT")));
                
                if (userRole.equalsIgnoreCase("Administrator") || 
                    userRole.equalsIgnoreCase("Medical Staff")) {
                    grid.add(new MedicalCard("Admin Panel", "System management", new Color(220, 38, 38), this::showAdminDialog));
                }
            } else {
                // Limited access for interns/students
                grid.add(new MedicalCard("My Schedule", "View your schedule", new Color(37, 99, 235), () -> {
                    JOptionPane.showMessageDialog(this, 
                        "<html><div style='text-align: center;'>" +
                        "<h3>Schedule for " + userName + "</h3>" +
                        "<p><b>Role:</b> " + userRole + "</p>" +
                        "<p><b>Week Schedule:</b></p>" +
                        "<p>Mon: Ward Rounds (8am-12pm)</p>" +
                        "<p>Tue: Clinical Tutorials</p>" +
                        "<p>Wed: Patient Assignments</p>" +
                        "<p>Thu: Study Sessions</p>" +
                        "<p>Fri: Skills Practice</p>" +
                        "</div></html>", 
                        "My Schedule", 
                        JOptionPane.INFORMATION_MESSAGE);
                }));
                grid.add(new MedicalCard("Support", "Get help and FAQs", new Color(234, 88, 12), () -> app.showPage("SUPPORT")));
                grid.add(new MedicalCard("Learning Materials", "Training resources", new Color(71, 85, 105), () -> {
                    JOptionPane.showMessageDialog(this, 
                        "<html><div style='text-align: center;'>" +
                        "<h3>Learning Resources</h3>" +
                        "<p>Available for: " + userRole + "</p>" +
                        "<p><b>1. Clinical Procedures Handbook</b></p>" +
                        "<p><b>2. Pharmacology Guide</b></p>" +
                        "<p><b>3. Case Studies Library</b></p>" +
                        "<p><b>4. Anatomy Atlas</b></p>" +
                        "</div></html>", 
                        "Training Materials", 
                        JOptionPane.INFORMATION_MESSAGE);
                }));
            }
            
            // Add empty panels for grid alignment
            int cardCount = grid.getComponentCount();
            while (cardCount % 3 != 0) {
                JPanel emptyPanel = new JPanel();
                emptyPanel.setOpaque(false);
                grid.add(emptyPanel);
                cardCount++;
            }
            
            centerContainer.add(grid, BorderLayout.CENTER);

            // Footer
            if (!isLimitedAccess) {
                JButton menuBtn = new JButton("SYSTEM CONFIG");
                menuBtn.setBackground(new Color(15, 23, 42));
                menuBtn.setForeground(Color.WHITE);
                menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                menuBtn.setPreferredSize(new Dimension(150, 45));
                menuBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
                menuBtn.addActionListener(e -> showConfigurationDialog());
                
                JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
                footer.setOpaque(false);
                footer.add(menuBtn);
                centerContainer.add(footer, BorderLayout.SOUTH);
            } else {
                JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
                footer.setOpaque(false);
                JLabel traineeLabel = new JLabel("👨‍🎓 Trainee Mode - " + userRole.toUpperCase());
                traineeLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
                traineeLabel.setForeground(new Color(100, 116, 139));
                footer.add(traineeLabel);
                centerContainer.add(footer, BorderLayout.SOUTH);
            }
        }
    }
    
    // Public method to refresh dashboard from outside (called after login)
    public void refreshDashboard() {
        System.out.println("DashboardPanel.refreshDashboard() called");
        updateUIForCurrentSession();
    }
    
    // Override setVisible to auto-refresh when dashboard is shown
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            System.out.println("DashboardPanel is now visible - refreshing UI...");
            SwingUtilities.invokeLater(() -> {
                updateUIForCurrentSession();
            });
        }
    }

    // ... [Keep all other methods exactly the same: showConfigurationDialog, 
    // showAdminDialog, StyleAdminButton, getFormattedRole, getRoleIcon, 
    // getRoleColor, createSubscriptionBanner] ...

    // Just for completeness, here are the rest of the methods you need to keep:
    
private void showConfigurationDialog() {
    JPanel menuPanel = new JPanel(new GridLayout(0, 2, 10, 10));
    menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    String[] depts = deptData.keySet().toArray(new String[0]);
    JComboBox<String> deptCombo = new JComboBox<>(depts);
    JComboBox<String> wardCombo = new JComboBox<>(deptData.get(depts[0]));
    
    // Link Wards to Departments
    deptCombo.addActionListener(event -> {
        String selected = (String) deptCombo.getSelectedItem();
        wardCombo.setModel(new DefaultComboBoxModel<>(deptData.get(selected)));
    });

    JTextField deptExpiry = new JTextField("2026-12-31");
    JTextField wardExpiry = new JTextField("2026-06-01");
    JTextField bedsField = new JTextField("20");

    menuPanel.add(new JLabel("Department:"));
    menuPanel.add(deptCombo);
    menuPanel.add(new JLabel("Department Expiry:"));
    menuPanel.add(deptExpiry);
    menuPanel.add(new JLabel("Ward:"));
    menuPanel.add(wardCombo);
    menuPanel.add(new JLabel("Ward Expiry:"));
    menuPanel.add(wardExpiry);
    menuPanel.add(new JLabel("Number of Beds:"));
    menuPanel.add(bedsField);

    int result = JOptionPane.showConfirmDialog(
        this, 
        menuPanel, 
        "System Configuration", 
        JOptionPane.OK_CANCEL_OPTION, 
        JOptionPane.PLAIN_MESSAGE
    );

    if (result == JOptionPane.OK_OPTION) {
        JOptionPane.showMessageDialog(
            this, 
            "Configuration Saved!\n" +
            "Department: " + deptCombo.getSelectedItem() + "\n" +
            "Ward: " + wardCombo.getSelectedItem() + "\n" +
            "Beds: " + bedsField.getText(),
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    } else {
        JOptionPane.showMessageDialog(
            this, 
            "Configuration cancelled.", 
            "Cancelled", 
            JOptionPane.WARNING_MESSAGE
        );
    }
}

    private void showAdminDialog() {
        JPanel adminPanel = new JPanel(new BorderLayout(10, 10));
        adminPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Administration Panel", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(15, 23, 42));
        
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        
        JButton userMgmtBtn = new JButton("👥 User Management");
        JButton reportsBtn = new JButton("📊 Generate Reports");
        JButton settingsBtn = new JButton("⚙️ System Settings");
        
        StyleAdminButton(userMgmtBtn, new Color(37, 99, 235));
        StyleAdminButton(reportsBtn, new Color(13, 148, 136));
        StyleAdminButton(settingsBtn, new Color(124, 58, 237));
        
        userMgmtBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "User Management feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        reportsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Report generation feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        settingsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "System settings feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        buttonPanel.add(userMgmtBtn);
        buttonPanel.add(reportsBtn);
        buttonPanel.add(settingsBtn);
        
        adminPanel.add(title, BorderLayout.NORTH);
        adminPanel.add(buttonPanel, BorderLayout.CENTER);
        
        JOptionPane.showMessageDialog(this, adminPanel, "Administration Panel", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void StyleAdminButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }

    private String getFormattedRole(String role) {
        if (role == null || role.equals("Guest")) {
            return "NOT LOGGED IN";
        }
        
        switch(role.toLowerCase()) {
            case "clinical year 1 (c1)":
                return "Clinical Year 1 (C1)";
            case "clinical year 2 (c2)":
                return "Clinical Year 2 (C2)";
            case "medical staff (intern)":
                return "Medical Intern";
            case "medical staff":
                return "Medical Staff";
            case "administrator":
                return "Administrator";
            case "doctor":
                return "Doctor";
            case "nurse":
                return "Nurse";
            case "surgeon":
                return "Surgeon";
            case "resident":
                return "Resident";
            case "medical student":
                return "Medical Student";
            default:
                return role;
        }
    }
    
    private String getRoleIcon(String role) {
        if (role == null || role.equals("Guest")) {
            return "✖";
        }
        
        switch(role.toLowerCase()) {
            case "administrator":
                return "👑";
            case "doctor":
            case "surgeon":
                return "👨‍⚕️";
            case "nurse":
                return "👩‍⚕️";
            case "medical staff":
            case "medical staff (intern)":
                return "🩺";
            case "clinical year 1 (c1)":
            case "clinical year 2 (c2)":
            case "medical student":
                return "🎓";
            case "resident":
                return "👨‍🎓";
            default:
                return "👤";
        }
    }
    
    private Color getRoleColor(String role) {
        if (role == null || role.equals("Guest")) {
            return Color.RED;
        }
        
        switch(role.toLowerCase()) {
            case "administrator":
                return new Color(220, 38, 38);
            case "doctor":
            case "surgeon":
                return new Color(13, 148, 136);
            case "nurse":
                return new Color(124, 58, 237);
            case "medical staff":
            case "medical staff (intern)":
                return new Color(37, 99, 235);
            case "clinical year 1 (c1)":
            case "clinical year 2 (c2)":
                return new Color(234, 88, 12);
            case "resident":
                return new Color(8, 145, 178);
            case "medical student":
                return new Color(168, 85, 247);
            default:
                return new Color(71, 85, 105);
        }
    }

    private JPanel createSubscriptionBanner() {
        JPanel banner = new JPanel();
        banner.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        String userRole = UserSession.getCurrentUserRole();
        boolean isLoggedIn = UserSession.isLoggedIn();
        
        JLabel label = new JLabel();
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        if (!isLoggedIn || userRole.equals("Guest")) {
            banner.setBackground(new Color(254, 226, 226));
            label.setText("⚠ NO ACTIVE SESSION • PLEASE LOGIN TO CONTINUE");
            label.setForeground(new Color(185, 28, 28));
        } else {
            boolean isSubscribed = !userRole.equalsIgnoreCase("Intern") && 
                                  !userRole.equalsIgnoreCase("Medical Student");
            
            if (isSubscribed) {
                banner.setBackground(new Color(209, 250, 229));
                label.setText("✓ ACTIVE SUBSCRIPTION • " + daysLeft + " DAYS REMAINING • ROLE: " + userRole.toUpperCase());
                label.setForeground(new Color(6, 95, 70));
            } else {
                banner.setBackground(new Color(254, 243, 199));
                label.setText("⚠ LIMITED ACCESS • VIEW-ONLY MODE • ROLE: " + userRole.toUpperCase());
                label.setForeground(new Color(146, 64, 14));
            }
        }
        
        banner.add(label);
        return banner;
    }
}