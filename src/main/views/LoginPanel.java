package main.views;

import javax.swing.*;
import java.awt.*;
import main.Application;
import main.services.AuthService;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passField;
    private AuthService authService = new AuthService();

    public LoginPanel(Application app) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton backBtn = new JButton("<- Back to Portal");
        backBtn.addActionListener(e -> app.showPage("HOME"));
        // Title
        JLabel title = new JLabel("Sign In");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(backBtn, gbc);
        add(title, gbc);

        // Email Label & Field
        gbc.gridwidth = 1; gbc.gridy = 1;
        add(new JLabel("YOUR EMAIL"), gbc);
        emailField = new JTextField(20);
        gbc.gridy = 2;
        add(emailField, gbc);

        // Password Label & Field
        gbc.gridy = 3;
        add(new JLabel("PASSWORD"), gbc);
        passField = new JPasswordField(20);
        gbc.gridy = 4;
        add(passField, gbc);

        // Submit Button
        JButton loginBtn = new JButton("Enter Dashboard ->");
        loginBtn.setBackground(new Color(79, 70, 229)); // indigo-600
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        gbc.gridy = 5; gbc.ipady = 10;
        add(loginBtn, gbc);

        // Redirect to Register
        JButton registerLink = new JButton("New here? Register Facility");
        registerLink.setBorderPainted(false);
        registerLink.setContentAreaFilled(false);
        registerLink.setForeground(new Color(79, 70, 229));
        gbc.gridy = 6; gbc.ipady = 0;
        add(registerLink, gbc);

        registerLink.addActionListener(e -> app.showPage("DASHBOARD"));

        // Actions
        loginBtn.addActionListener(e -> {
            try {
                if(authService.login(emailField.getText(), new String(passField.getPassword()))) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    app.showPage("DASHBOARD");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerLink.addActionListener(e -> app.showPage("REGISTER"));
    }
}