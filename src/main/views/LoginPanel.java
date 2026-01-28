package main.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;
import main.Application;
import main.services.AuthService;
import main.services.UserSession;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passField;
    private JButton loginBtn;
    private JCheckBox rememberMeCheckbox;
    private JLabel errorLabel;
    private AuthService authService = new AuthService();
    
    private Color indigo600 = new Color(79, 70, 229);
    private Color indigo100 = new Color(224, 231, 255);
    private Color slate900 = new Color(15, 23, 42);
    private Color slate500 = new Color(100, 116, 139);
    private Color slate100 = new Color(241, 245, 249);
    private Color red500 = new Color(239, 68, 68);
    private Color green500 = new Color(34, 197, 94);

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    public LoginPanel(Application app) {
        setLayout(new GridLayout(1, 2));
        setBackground(Color.WHITE);

        // --- LEFT SIDE: BRANDING PANEL ---
        JPanel brandingPanel = new GradientOverlayPanel();
        brandingPanel.setLayout(new BoxLayout(brandingPanel, BoxLayout.Y_AXIS));
        brandingPanel.setBorder(new EmptyBorder(60, 60, 60, 60));

        JLabel version = new JLabel("v2.5.0 • ENTERPRISE SECURE");
        version.setForeground(new Color(255, 255, 255, 150));
        version.setFont(new Font("SansSerif", Font.BOLD, 10));

        JLabel heroText = new JLabel("<html><body style='width: 300px'>"
                + "<h1 style='color:white; font-size:40pt; font-style:italic;'>Efficiency in <br>"
                + "<font color='#818CF8'>Every Ward.</font></h1><br>"
                + "<p style='color:#CBD5E1; font-size:14pt;'>Streamlining bed management and patient notifications with real-time intelligence.</p>"
                + "</body></html>");
        
        // User count badge
        JLabel userCount = new JLabel("← " + authService.getAllUsers().size() + " medical professionals connected");
        userCount.setForeground(new Color(255, 255, 255, 180));
        userCount.setFont(new Font("SansSerif", Font.PLAIN, 11));
        
        brandingPanel.add(version);
        brandingPanel.add(Box.createVerticalGlue());
        brandingPanel.add(heroText);
        brandingPanel.add(Box.createVerticalGlue());
        brandingPanel.add(userCount);

        // --- RIGHT SIDE: LOGIN FORM ---
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 40, 5, 40);

        // --- BACK BUTTON (Row 0) ---
        JButton backBtn = new JButton("← Back to Portal");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        backBtn.setForeground(indigo600);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(79, 70, 229, 100), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> app.showPage("HOME"));
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 40, 30, 40);
        formWrapper.add(backBtn, gbc);

        // Header Section (Row 1)
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel signinTitle = new JLabel("Sign In");
        signinTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        gbc.gridy = 1; gbc.insets = new Insets(5, 40, 5, 40);
        formWrapper.add(signinTitle, gbc);

        JLabel subtitle = new JLabel("<html>Access your hospital dashboard.</html>");
        subtitle.setForeground(slate500);
        gbc.gridy = 2; gbc.insets = new Insets(0, 40, 30, 40);
        formWrapper.add(subtitle, gbc);

        // Error message label (initially hidden)
        errorLabel = new JLabel("");
        errorLabel.setForeground(red500);
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        errorLabel.setVisible(false);
        gbc.gridy = 3; gbc.insets = new Insets(0, 40, 20, 40);
        formWrapper.add(errorLabel, gbc);

        // Email Field (Row 4 & 5)
        gbc.insets = new Insets(5, 40, 5, 40);
        JLabel emailLabel = new JLabel("YOUR EMAIL");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        emailLabel.setForeground(slate500);
        gbc.gridy = 4;
        formWrapper.add(emailLabel, gbc);

        emailField = new StyledTextField("yourname@gmail.com");
        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginBtn.doClick();
                }
                hideError();
            }
        });
        gbc.gridy = 5;
        formWrapper.add(emailField, gbc);

        // Password Field (Row 6 & 7)
        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        passLabel.setForeground(slate500);
        gbc.gridy = 6; gbc.insets = new Insets(20, 40, 5, 40);
        formWrapper.add(passLabel, gbc);

        passField = new StyledPasswordField();
        passField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginBtn.doClick();
                }
                hideError();
            }
        });
        gbc.gridy = 7; gbc.insets = new Insets(5, 40, 5, 40);
        formWrapper.add(passField, gbc);

        // Show Password & Remember Me (Row 8)
        JPanel optionsPanel = new JPanel(new GridLayout(1, 2));
        optionsPanel.setOpaque(false);
        
        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setFont(new Font("SansSerif", Font.PLAIN, 12));
        showPass.setBackground(Color.WHITE);
        showPass.addActionListener(e -> {
            if (showPass.isSelected()) {
                passField.setEchoChar((char) 0);
            } else {
                passField.setEchoChar('•');
            }
        });
        
        rememberMeCheckbox = new JCheckBox("Remember Me");
        rememberMeCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rememberMeCheckbox.setBackground(Color.WHITE);
        
        optionsPanel.add(showPass);
        optionsPanel.add(rememberMeCheckbox);
        gbc.gridy = 8;
        formWrapper.add(optionsPanel, gbc);

        // Login Button (Row 9)
        loginBtn = new JButton("Enter Dashboard →");
        loginBtn.setPreferredSize(new Dimension(0, 50));
        loginBtn.setBackground(indigo600);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(67, 56, 202));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(indigo600);
            }
        });
        
        loginBtn.addActionListener(e -> handleLogin(app));
        gbc.gridy = 9; gbc.insets = new Insets(30, 40, 10, 40);
        formWrapper.add(loginBtn, gbc);

        // Register Link & Forgot Password (Row 10)
        JPanel linksPanel = new JPanel(new GridLayout(1, 2));
        linksPanel.setOpaque(false);
        
        JButton regBtn = new JButton("New here? Register Facility");
        regBtn.setForeground(indigo600);
        regBtn.setContentAreaFilled(false);
        regBtn.setBorderPainted(false);
        regBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        regBtn.addActionListener(e -> app.showPage("REGISTER"));
        
        JButton forgotBtn = new JButton("Forgot Password?");
        forgotBtn.setForeground(slate500);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setBorderPainted(false);
        forgotBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        forgotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotBtn.addActionListener(e -> showForgotPasswordDialog());
        
        linksPanel.add(regBtn);
        linksPanel.add(forgotBtn);
        gbc.gridy = 10;
        formWrapper.add(linksPanel, gbc);

        // Test Credentials (Row 11 - only in development)
        if (System.getProperty("development", "false").equals("true")) {
            JPanel testPanel = new JPanel();
            testPanel.setBackground(indigo100);
            testPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(indigo100, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            
            JLabel testLabel = new JLabel("<html><b>Test Credentials:</b><br>"
                + "• admin@hospital.com / password123<br>"
                + "• doctor@hospital.com / securepass</html>");
            testLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            testLabel.setForeground(new Color(55, 48, 163));
            testPanel.add(testLabel);
            
            gbc.gridy = 11; gbc.insets = new Insets(20, 40, 0, 40);
            formWrapper.add(testPanel, gbc);
        }

        add(brandingPanel);
        add(formWrapper);
        
        // Auto-focus on email field
        SwingUtilities.invokeLater(() -> emailField.requestFocus());
    }

    private void handleLogin(Application app) {
        String email = emailField.getText().trim();
        String password = new String(passField.getPassword()).trim();
        
        // Clear previous errors
        hideError();
        
        // Validate email format
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            showError("Please enter a valid email address");
            emailField.requestFocus();
            return;
        }
        
        // Check for empty password
        if (password.isEmpty()) {
            showError("Password cannot be empty");
            passField.requestFocus();
            return;
        }
        
        // Disable button during login attempt
        loginBtn.setEnabled(false);
        loginBtn.setText("Logging in...");
        
        // Simulate network delay
        Timer timer = new Timer(500, e -> {
            try {
                if (authService.login(email, password)) {
                    // Login successful
                    loginBtn.setText("Login Successful!");
                    loginBtn.setBackground(green500);
                    
                    // Clear sensitive data
                    passField.setText("");
                    
                    // Debug: Check UserSession immediately
                    System.out.println("After login - UserSession status:");
                    System.out.println("Is logged in: " + UserSession.isLoggedIn());
                    System.out.println("User name: " + UserSession.getCurrentUserName());
                    System.out.println("User role: " + UserSession.getCurrentUserRole());
                    
                    // Show success message
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(LoginPanel.this,
                            "Login successful!\n" +
                            "Welcome, " + UserSession.getCurrentUserName() + "!\n" +
                            "Role: " + UserSession.getCurrentUserRole(),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    });
                    
                    // Navigate to dashboard after delay
                    Timer successTimer = new Timer(1000, ev -> {
                        app.showPage("DASHBOARD");
                        // Reset button state
                        loginBtn.setText("Enter Dashboard →");
                        loginBtn.setBackground(indigo600);
                        loginBtn.setEnabled(true);
                    });
                    successTimer.setRepeats(false);
                    successTimer.start();
                    
                }
            } catch (Exception ex) {
                // Login failed
                showError(ex.getMessage());
                loginBtn.setText("Enter Dashboard →");
                loginBtn.setBackground(indigo600);
                loginBtn.setEnabled(true);
                passField.requestFocus();
                passField.selectAll();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void showError(String message) {
        errorLabel.setText("<html>⚠ " + message + "</html>");
        errorLabel.setVisible(true);
        
        // Auto-hide error after 5 seconds
        Timer timer = new Timer(5000, e -> hideError());
        timer.setRepeats(false);
        timer.start();
    }
    
    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }
    
    private void showForgotPasswordDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        JTextField emailField = new JTextField();
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(slate100, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        panel.add(new JLabel("Enter your email:"));
        panel.add(emailField);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Reset Password",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String email = emailField.getText().trim();
            if (Pattern.matches(EMAIL_PATTERN, email)) {
                JOptionPane.showMessageDialog(this,
                    "Password reset instructions have been sent to " + email,
                    "Check Your Email",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address",
                    "Invalid Email",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Clear form method (can be called from Application)
    public void clearForm() {
        emailField.setText("");
        passField.setText("");
        hideError();
        loginBtn.setText("Enter Dashboard →");
        loginBtn.setBackground(indigo600);
        loginBtn.setEnabled(true);
    }

    class GradientOverlayPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, new Color(15, 23, 42), getWidth(), getHeight(), new Color(79, 70, 229));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    class StyledTextField extends JTextField {
        public StyledTextField(String placeholder) {
            setText(placeholder);
            setForeground(slate500);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(slate100, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(slate900);
                    }
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(indigo600, 2),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                    ));
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(slate500);
                    }
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(slate100, 2),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                    ));
                }
            });
        }
    }

    class StyledPasswordField extends JPasswordField {
        public StyledPasswordField() {
            setEchoChar('•');
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(slate100, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(indigo600, 2),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                    ));
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(slate100, 2),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                    ));
                }
            });
        }
    }
}