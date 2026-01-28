package main.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import main.Application;
import main.services.AuthService;

public class RegisterPanel extends JPanel {
    // Colors from React Theme
    private Color indigo600 = new Color(79, 70, 229);
    private Color indigo50 = new Color(238, 242, 255);
    private Color indigo100 = new Color(224, 231, 255);
    private Color slate900 = new Color(15, 23, 42);
    private Color slate500 = new Color(100, 116, 139);
    private Color slate100 = new Color(241, 245, 249);
    private Color red500 = new Color(239, 68, 68);
    private Color green500 = new Color(34, 197, 94);

    // Components
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passField;
    private JPasswordField confirmPassField;
    private JComboBox<String> roleCombo;
    private JLabel errorLabel;
    private JButton submitBtn;
    private JLabel strengthLabel;
    private AuthService authService = new AuthService();
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    public RegisterPanel(Application app) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Create main split pane
        JPanel splitPane = new JPanel(new GridLayout(1, 2));

        // 1. LEFT SIDE: BRANDING (FIXED)
        JPanel leftPanel = new GradientBrandingPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(40, 60, 60, 60));

        // --- BACK BUTTON ---
        JButton backBtn = createGhostButton("← Back to Portal", Color.WHITE);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> app.showPage("HOME"));
        leftPanel.add(backBtn);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel badge = new JLabel("🛡️ HIPAA COMPLIANT ENCRYPTION");
        badge.setForeground(new Color(255, 255, 255, 180));
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTxt = new JLabel("<html><body style='width: 350px'>"
                + "<h1 style='color:white; font-size:42pt; font-style:italic;'>Join the <br>"
                + "<font color='#818cf8'>Future of Care.</font></h1><br>"
                + "<p style='color:#CBD5E1; font-size:14pt;'>Empowering medical professionals with real-time bed intelligence.</p>"
                + "</body></html>");
        welcomeTxt.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Stats - shows real user count from CSV
        JLabel statsLabel = new JLabel("← " + authService.getUserCount() + " professionals already registered");
        statsLabel.setForeground(new Color(255, 255, 255, 180));
        statsLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(badge);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(welcomeTxt);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(statsLabel);

        // 2. RIGHT SIDE: FORM WITH SCROLL
        JPanel rightPanelContainer = new JPanel(new BorderLayout());
        rightPanelContainer.setBackground(Color.WHITE);

        // Create a wrapper panel for the form
        JPanel formWrapper = new JPanel();
        formWrapper.setLayout(new BoxLayout(formWrapper, BoxLayout.Y_AXIS));
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton loginLink = new JButton("Already have an account? Sign in here");
        loginLink.setForeground(indigo600);
        loginLink.setFont(new Font("SansSerif", Font.BOLD, 13));
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginLink.addActionListener(e -> app.showPage("LOGIN"));

        formWrapper.add(title);
        formWrapper.add(Box.createRigidArea(new Dimension(0, 5)));
        formWrapper.add(loginLink);
        formWrapper.add(Box.createRigidArea(new Dimension(0, 30)));

        // Error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(red500);
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        errorLabel.setVisible(false);
        formWrapper.add(errorLabel);
        formWrapper.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel fieldGrid = new JPanel(new GridLayout(0, 1, 0, 15));
        fieldGrid.setOpaque(false);
        fieldGrid.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

        // Initialize fields
        nameField = new StyledTextField("Dr. John Doe");
        emailField = new StyledTextField("yourname@gmail.com");
        phoneField = new StyledTextField("0911223344");
        passField = new StyledPasswordField();
        confirmPassField = new StyledPasswordField();
        
        // Add password strength indicator
        strengthLabel = new JLabel("");
        strengthLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        
        // Password strength listener
        passField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updatePasswordStrength();
            }
        });

        fieldGrid.add(createLabeledField("FULL NAME", nameField));
        fieldGrid.add(createLabeledField("WORK EMAIL", emailField));
        fieldGrid.add(createLabeledField("PHONE NUMBER", phoneField));
        fieldGrid.add(createLabeledField("PASSWORD", passField));
        fieldGrid.add(strengthLabel);
        fieldGrid.add(createLabeledField("CONFIRM PASSWORD", confirmPassField));

        JLabel roleLabel = new JLabel("PROFESSIONAL ROLE");
        roleLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        roleLabel.setForeground(slate500);
        String[] roles = {"Select Role", "Clinical Year 1 (C1)", "Clinical Year 2 (C2)", 
                         "Medical Staff (Intern)", "Nurse", "Administrator", "Doctor", 
                         "Resident", "Medical Student", "Clinical Officer", 
                         "Pharmacist", "Radiologist", "Lab Technician", "Surgeon"};
        roleCombo = new JComboBox<>(roles);
        roleCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        roleCombo.setBackground(Color.WHITE);
        roleCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(slate100, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        roleCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(new EmptyBorder(5, 10, 5, 10));
                if (index == 0) {
                    setForeground(slate500);
                } else {
                    setForeground(slate900);
                }
                return this;
            }
        });
        fieldGrid.add(roleLabel);
        fieldGrid.add(roleCombo);

        formWrapper.add(fieldGrid);
        formWrapper.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password requirements
        JPanel passwordTips = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordTips.setBackground(new Color(249, 250, 251));
        passwordTips.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(slate100, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        JLabel tipsLabel = new JLabel("<html><b>Password Requirements:</b><br>"
            + "• Minimum 8 characters<br>"
            + "• Must include letters AND numbers<br>"
            + "• Special characters (@$!%*#?&) are optional but recommended<br>"
            + "• Avoid using personal information in your password</html>");
        tipsLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tipsLabel.setForeground(slate500);
        passwordTips.add(tipsLabel);
        passwordTips.setMaximumSize(new Dimension(500, 120));
        formWrapper.add(passwordTips);

        formWrapper.add(Box.createRigidArea(new Dimension(0, 20)));

        // Status box
        JPanel statusBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBox.setBackground(indigo50);
        statusBox.setBorder(BorderFactory.createLineBorder(new Color(199, 210, 254), 1));
        JLabel statusLbl = new JLabel("✓ Verified Medical Access Enabled • Instant Activation • HIPAA Compliant");
        statusLbl.setFont(new Font("SansSerif", Font.ITALIC | Font.BOLD, 12));
        statusLbl.setForeground(new Color(49, 46, 129));
        statusBox.add(statusLbl);
        statusBox.setMaximumSize(new Dimension(500, 60));
        formWrapper.add(statusBox);

        formWrapper.add(Box.createRigidArea(new Dimension(0, 25)));

        // Submit button
        submitBtn = new JButton("CREATE ACCOUNT →");
        submitBtn.setMaximumSize(new Dimension(500, 55));
        submitBtn.setBackground(indigo600);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Hover effect
        submitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitBtn.setBackground(new Color(67, 56, 202));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitBtn.setBackground(indigo600);
            }
        });
        
        submitBtn.addActionListener(e -> handleRegister(app));
        formWrapper.add(submitBtn);

        // Terms and conditions
        JLabel termsLabel = new JLabel("<html><font size='2'>By creating an account, you agree to our <font color='#4f46e5'>Terms of Service</font> and <font color='#4f46e5'>Privacy Policy</font>. Your data is encrypted and stored securely.</font></html>");
        termsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        termsLabel.setBorder(new EmptyBorder(15, 0, 0, 0));
        termsLabel.setMaximumSize(new Dimension(500, 50));
        formWrapper.add(termsLabel);

        // Add vertical glue to push content up when scrolled
        formWrapper.add(Box.createVerticalGlue());

        // Create a scroll pane for the form
        JScrollPane scrollPane = new JScrollPane(formWrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Style the scrollbar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
        verticalScrollBar.setBackground(Color.WHITE);
        verticalScrollBar.setForeground(slate100);
        
        // Remove the border from viewport
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());

        rightPanelContainer.add(scrollPane, BorderLayout.CENTER);

        splitPane.add(leftPanel);
        splitPane.add(rightPanelContainer);
        add(splitPane, BorderLayout.CENTER);
        
        // Auto-focus on name field
        SwingUtilities.invokeLater(() -> nameField.requestFocus());
    }

    private void handleRegister(Application app) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passField.getPassword()).trim();
        String confirmPassword = new String(confirmPassField.getPassword()).trim();
        String phone = phoneField.getText().trim();
        String role = (String) roleCombo.getSelectedItem();
        
        // Clear previous error
        hideError();
        
        // Validate all fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            showError("All fields are required");
            return;
        }
        
        // Check if role is selected
        if (role == null || role.equals("Select Role")) {
            showError("Please select your professional role");
            roleCombo.requestFocus();
            return;
        }
        
        // Validate email format
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            showError("Please enter a valid email address (e.g., user@hospital.com)");
            emailField.requestFocus();
            emailField.selectAll();
            return;
        }
        
        // Check password match
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            passField.requestFocus();
            passField.selectAll();
            return;
        }
        
        // Validate password strength
        String passwordStrength = checkPasswordStrength(password);
        if (passwordStrength.startsWith("Weak")) {
            showError("Password is too weak. " + passwordStrength.substring(5));
            passField.requestFocus();
            passField.selectAll();
            return;
        }
        
        // Validate phone number
        if (!isValidPhone(phone)) {
            showError("Please enter a valid phone number (10-15 digits)");
            phoneField.requestFocus();
            phoneField.selectAll();
            return;
        }
        
        // Validate name
        if (name.length() < 2 || name.length() > 100) {
            showError("Name must be between 2 and 100 characters");
            nameField.requestFocus();
            nameField.selectAll();
            return;
        }
        
        // Check if email already exists (using AuthService)
        if (authService.emailExists(email)) {
            showError("This email is already registered. Please use a different email or login.");
            emailField.requestFocus();
            emailField.selectAll();
            return;
        }
        
        // Disable button during registration
        submitBtn.setEnabled(false);
        submitBtn.setText("Creating Account...");
        
        // Show processing animation
        final Timer[] progressTimer = {null};
        final int[] dots = {0};
        progressTimer[0] = new Timer(500, e -> {
            dots[0] = (dots[0] + 1) % 4;
            submitBtn.setText("Creating Account" + ".".repeat(dots[0]));
        });
        progressTimer[0].start();
        
        // Simulate network delay and process registration
        Timer processTimer = new Timer(1500, e -> {
            try {
                // Stop the progress animation
                if (progressTimer[0] != null) {
                    progressTimer[0].stop();
                }
                
                // Register the user (this saves to CSV)
                if (authService.register(name, email, password, phone, role)) {
                    // Registration successful
                    submitBtn.setText("✓ Account Created!");
                    submitBtn.setBackground(green500);
                    
                    // Show success message
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(RegisterPanel.this,
                            "<html><div style='text-align: center;'>"
                            + "<h3>Registration Successful!</h3>"
                            + "<p><b>" + name + "</b></p>"
                            + "<p>Email: " + email + "</p>"
                            + "<p>Role: " + role + "</p>"
                            + "<p>Your account has been created and saved.</p>"
                            + "<p>You can now login with your credentials.</p>"
                            + "</div></html>",
                            "Account Created",
                            JOptionPane.INFORMATION_MESSAGE);
                    });
                    
                    // Clear form after delay
                    Timer clearTimer = new Timer(2000, ev -> {
                        clearForm();
                        app.showPage("LOGIN");
                    });
                    clearTimer.setRepeats(false);
                    clearTimer.start();
                    
                }
            } catch (Exception ex) {
                // Registration failed
                if (progressTimer[0] != null) {
                    progressTimer[0].stop();
                }
                showError(ex.getMessage());
                submitBtn.setText("CREATE ACCOUNT →");
                submitBtn.setBackground(indigo600);
                submitBtn.setEnabled(true);
                
                // Highlight the problematic field
                if (ex.getMessage().contains("email")) {
                    emailField.requestFocus();
                    emailField.selectAll();
                } else if (ex.getMessage().contains("password")) {
                    passField.requestFocus();
                    passField.selectAll();
                } else if (ex.getMessage().contains("phone")) {
                    phoneField.requestFocus();
                    phoneField.selectAll();
                }
            }
        });
        processTimer.setRepeats(false);
        processTimer.start();
    }
    
    private void updatePasswordStrength() {
        String password = new String(passField.getPassword());
        if (password.isEmpty()) {
            strengthLabel.setText("");
            strengthLabel.setForeground(slate500);
            return;
        }
        
        String strength = checkPasswordStrength(password);
        if (strength.startsWith("Strong")) {
            strengthLabel.setText("✓ " + strength);
            strengthLabel.setForeground(green500);
        } else if (strength.startsWith("Good")) {
            strengthLabel.setText("✓ " + strength);
            strengthLabel.setForeground(new Color(234, 179, 8)); // yellow-500
        } else if (strength.startsWith("Weak")) {
            strengthLabel.setText("⚠ " + strength);
            strengthLabel.setForeground(red500);
        } else {
            strengthLabel.setText(strength);
            strengthLabel.setForeground(slate500);
        }
    }
    
    private String checkPasswordStrength(String password) {
        int score = 0;
        
        // Length check
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;
        
        // Character variety
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[@$!%*#?&].*");
        
        if (hasLower) score++;
        if (hasUpper) score++;
        if (hasDigit) score++;
        if (hasSpecial) score++;
        
        // Additional rules
        if (password.length() >= 8 && hasLower && hasUpper && hasDigit) score += 2;
        
        // Determine strength
        if (score >= 8) {
            return "Strong password";
        } else if (score >= 5) {
            return "Good password";
        } else if (score >= 3) {
            return "Weak - Add numbers and uppercase letters";
        } else {
            return "Very weak - Use at least 8 characters with letters and numbers";
        }
    }
    
    private boolean isValidPhone(String phone) {
        // Remove all non-digit characters
        String digits = phone.replaceAll("[^0-9]", "");
        // Check if we have 10-15 digits
        return digits.length() >= 10 && digits.length() <= 15;
    }
    
    private void showError(String message) {
        errorLabel.setText("<html><span style='color:#ef4444;'>⚠ " + message + "</span></html>");
        errorLabel.setVisible(true);
        
        // Scroll to show error
        errorLabel.scrollRectToVisible(errorLabel.getBounds());
        
        // Auto-hide error after 8 seconds
        Timer timer = new Timer(8000, e -> hideError());
        timer.setRepeats(false);
        timer.start();
    }
    
    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }
    
    // Clear form method (can be called from Application)
    public void clearForm() {
        nameField.setText("Dr. John Doe");
        nameField.setForeground(slate500);
        emailField.setText("yourname@gmail.com");
        emailField.setForeground(slate500);
        phoneField.setText("0911223344");
        phoneField.setForeground(slate500);
        passField.setText("");
        confirmPassField.setText("");
        strengthLabel.setText("");
        roleCombo.setSelectedIndex(0);
        hideError();
        submitBtn.setText("CREATE ACCOUNT →");
        submitBtn.setBackground(indigo600);
        submitBtn.setEnabled(true);
    }

    private JButton createGhostButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setForeground(color);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setForeground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 150), 1),
                    BorderFactory.createEmptyBorder(8, 20, 8, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setForeground(color);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100), 1),
                    BorderFactory.createEmptyBorder(8, 20, 8, 20)
                ));
            }
        });
        
        return btn;
    }

    private JPanel createLabeledField(String label, JTextField field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        l.setForeground(slate500);
        l.setBorder(new EmptyBorder(0, 5, 5, 0));
        p.add(l);
        p.add(field);
        return p;
    }

    private JPanel createLabeledField(String label, JPasswordField field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        l.setForeground(slate500);
        l.setBorder(new EmptyBorder(0, 5, 5, 0));
        p.add(l);
        p.add(field);
        return p;
    }

    class GradientBrandingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, slate900, getWidth(), getHeight(), indigo600);
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
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
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
                        BorderFactory.createEmptyBorder(12, 15, 12, 15)
                    ));
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(slate500);
                    }
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(slate100, 2),
                        BorderFactory.createEmptyBorder(12, 15, 12, 15)
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
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
            ));
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(indigo600, 2),
                        BorderFactory.createEmptyBorder(12, 15, 12, 15)
                    ));
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(slate100, 2),
                        BorderFactory.createEmptyBorder(12, 15, 12, 15)
                    ));
                }
            });
        }
    }
}