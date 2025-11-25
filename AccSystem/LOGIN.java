import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LOGIN {

    private JFrame frame;
    private JPanel bgPanel;
    private JButton getStartedBtn;
    private JPanel loginCard;
    private JPanel registerCard;
    private JPanel changePassCard;

    private static final String LOGIN_FOLDER = "AccSystem" + File.separator + "LoginFolder";

    public static String loggedInUser;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LOGIN::new);
    }

    public LOGIN() {
        createLoginFolderIfNotExist();
        frame = new JFrame("Accounting System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 650);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        showMainScreen();
        frame.setVisible(true);
    }

    private void createLoginFolderIfNotExist() {
        File folder = new File(LOGIN_FOLDER);
        if (!folder.exists()) folder.mkdirs();
    }

    private void showMainScreen() {
        bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                String imges = "AccSystem" + File.separator + "images" + File.separator + "pic1.png";
                ImageIcon bg = new ImageIcon(imges);
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(null);

        getStartedBtn = new JButton("Get Started");
        getStartedBtn.setBounds(100, 380, 160, 45);
        styleMainButton(getStartedBtn);

        getStartedBtn.addActionListener(e -> {
            getStartedBtn.setVisible(false);
            showLoginOverlay();
        });
        bgPanel.add(getStartedBtn);

        frame.setContentPane(bgPanel);
        bgPanel.revalidate();
        bgPanel.repaint();
    }

    private void showLoginOverlay() {
        if (loginCard != null && loginCard.isVisible()) return;

        int overlayX = 600;
        int overlayWidth = frame.getWidth() - overlayX;
        int overlayY = 0;
        int overlayHeight = frame.getHeight();

        loginCard = new JPanel();
        loginCard.setLayout(null);
        loginCard.setBackground(Color.WHITE);
        loginCard.setBounds(overlayX, overlayY, overlayWidth, overlayHeight);
        loginCard.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));

        JLabel closeX = new JLabel("✕");
        closeX.setFont(new Font("SansSerif", Font.BOLD, 20));
        closeX.setBounds(overlayWidth - 45, 10, 20, 20);
        closeX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeX.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                loginCard.setVisible(false);
                getStartedBtn.setVisible(true);
            }
        });
        loginCard.add(closeX);

        JLabel welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(new Font("Poppins", Font.BOLD, 30));
        welcomeLabel.setBounds(180, 40, 200, 40);
        loginCard.add(welcomeLabel);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        userLabel.setBounds(40, 110, 200, 30);
        loginCard.add(userLabel);

        RoundedTextField usernameField = new RoundedTextField(20);
        usernameField.setBounds(40, 140, overlayWidth - 80, 35);
        loginCard.add(usernameField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        passLabel.setBounds(40, 190, 200, 30);
        loginCard.add(passLabel);

        RoundedPasswordField passwordField = new RoundedPasswordField(20);
        passwordField.setBounds(40, 220, overlayWidth - 80, 35);
        loginCard.add(passwordField);

        JLabel forgotLabel = new JLabel("Forgot Password?");
        forgotLabel.setFont(new Font("Poppins", Font.PLAIN, 12));
        forgotLabel.setForeground(new Color(0, 102, 153));
        forgotLabel.setBounds(40, 270, 150, 20);
        forgotLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginCard.add(forgotLabel);

        forgotLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showChangePasswordOverlay();
            }

            public void mouseEntered(MouseEvent e) {
                forgotLabel.setForeground(new Color(0, 51, 102));
            }

            public void mouseExited(MouseEvent e) {
                forgotLabel.setForeground(new Color(0, 102, 153));
            }
        });

        JButton loginBtn = new JButton("Log In");
        loginBtn.setBounds((overlayWidth - 200) / 2, 310, 200, 45);
        styleLoginButton(loginBtn, new Color(0, 64, 64));
        loginBtn.addActionListener(e -> {
    String username = usernameField.getText().trim();
    String password = new String(passwordField.getPassword());

    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Username and Password are required!");
        return;
    }

    File userFile = new File(LOGIN_FOLDER, username + ".csv");
    if (!userFile.exists()) {
        JOptionPane.showMessageDialog(frame, "Account does not exist! Please register first.");
        return;
    }

    boolean valid = false;
    try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
        br.readLine(); 
        String line = br.readLine();
        if (line != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3 && parts[2].equals(password)) {
                valid = true;
            }
        }
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(frame, "Error reading account data!");
        return;
    }

    if (!valid) {
        JOptionPane.showMessageDialog(frame, "Invalid password!");
        return;
    }

    loggedInUser = username;

    loginCard.setVisible(false);
    frame.dispose();
    new NewTransaction(); 
});

        loginCard.add(loginBtn);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds((overlayWidth - 200) / 2, 370, 200, 45);
        styleLoginButton(registerBtn, new Color(45, 45, 45));
        registerBtn.addActionListener(e -> showRegisterOverlay());
        loginCard.add(registerBtn);

        bgPanel.add(loginCard, Integer.valueOf(2));
        bgPanel.repaint();
        bgPanel.revalidate();
    }

    private void showRegisterOverlay() {
        if (loginCard != null) loginCard.setVisible(false);
        if (registerCard != null) {
            registerCard.setVisible(true);
            return;
        }

        int overlayX = 600;
        int overlayWidth = frame.getWidth() - overlayX;
        int overlayY = 0;
        int overlayHeight = frame.getHeight();

        registerCard = new JPanel();
        registerCard.setLayout(null);
        registerCard.setBackground(Color.WHITE);
        registerCard.setBounds(overlayX, overlayY, overlayWidth, overlayHeight);
        registerCard.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));

        JLabel closeX = new JLabel("✕");
        closeX.setFont(new Font("SansSerif", Font.BOLD, 20));
        closeX.setBounds(overlayWidth - 45, 10, 20, 20);
        closeX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeX.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                registerCard.setVisible(false);
                getStartedBtn.setVisible(true);
            }
        });
        registerCard.add(closeX);

        JLabel regLabel = new JLabel("Registration");
        regLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        regLabel.setBounds(60, 40, 300, 40);
        registerCard.add(regLabel);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        emailLabel.setBounds(40, 100, 200, 30);
        registerCard.add(emailLabel);

        RoundedTextField emailField = new RoundedTextField(20);
        emailField.setBounds(40, 130, overlayWidth - 80, 35);
        registerCard.add(emailField);

        JLabel phoneLabel = new JLabel("Phone Number");
        phoneLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        phoneLabel.setBounds(40, 180, 200, 30);
        registerCard.add(phoneLabel);

        RoundedTextField phoneField = new RoundedTextField(20);
        phoneField.setBounds(40, 210, overlayWidth - 80, 35);
        registerCard.add(phoneField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        passLabel.setBounds(40, 260, 200, 30);
        registerCard.add(passLabel);

        RoundedPasswordField passField = new RoundedPasswordField(20);
        passField.setBounds(40, 290, overlayWidth - 80, 35);
        registerCard.add(passField);

        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        confirmLabel.setBounds(40, 340, 200, 30);
        registerCard.add(confirmLabel);

        RoundedPasswordField confirmField = new RoundedPasswordField(20);
        confirmField.setBounds(40, 370, overlayWidth - 80, 35);
        registerCard.add(confirmField);

        JButton doneBtn = new JButton("Done");
        doneBtn.setBounds((overlayWidth - 200) / 2, 430, 200, 45);
        styleLoginButton(doneBtn, new Color(0, 64, 64));
        doneBtn.addActionListener(e -> {
    String email = emailField.getText().trim();
    String phone = phoneField.getText().trim();
    String pass = new String(passField.getPassword());
    String confirm = new String(confirmField.getPassword());

    if (!email.contains("@") || !email.contains(".")) {
        JOptionPane.showMessageDialog(registerCard, "Invalid email format!");
        return;
    }

    if (!phone.matches("\\d{11}")) {
        JOptionPane.showMessageDialog(registerCard, "Phone number must be 11 digits!");
        return;
    }
   
    if (!pass.equals(confirm)) {
        JOptionPane.showMessageDialog(registerCard, "Passwords do not match!");
        return;
    }

    File mainFolder = new File("AccSystem");
    if (!mainFolder.exists()) {
        mainFolder.mkdirs();
    }

    File loginFolder = new File(mainFolder, "LoginFolder");
    if (!loginFolder.exists()) {
        loginFolder.mkdirs();
    }

    try {
        File userFile = new File(loginFolder, email + ".csv");

        if (!userFile.exists()) {
            userFile.createNewFile();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile))) {
            bw.write("email,phone,password");
            bw.newLine();
            bw.write(email + "," + phone + "," + pass);
        }

        JOptionPane.showMessageDialog(registerCard, "Registration successful!");
        registerCard.setVisible(false);
        showLoginOverlay();

    } catch (IOException ex) {
        JOptionPane.showMessageDialog(registerCard, "Failed to save account!");
    }
});


        registerCard.add(doneBtn);

        bgPanel.add(registerCard, Integer.valueOf(2));
        bgPanel.repaint();
        bgPanel.revalidate();
    }

    private void showChangePasswordOverlay() {
        if (loginCard != null) loginCard.setVisible(false);
        if (changePassCard != null) {
            changePassCard.setVisible(true);
            return;
        }

        int overlayX = 600;
        int overlayWidth = frame.getWidth() - overlayX;
        int overlayY = 0;
        int overlayHeight = frame.getHeight();

        changePassCard = new JPanel();
        changePassCard.setLayout(null);
        changePassCard.setBackground(Color.WHITE);
        changePassCard.setBounds(overlayX, overlayY, overlayWidth, overlayHeight);
        changePassCard.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));

        JLabel closeX = new JLabel("✕");
        closeX.setFont(new Font("SansSerif", Font.BOLD, 20));
        closeX.setBounds(overlayWidth - 45, 10, 20, 20);
        closeX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeX.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                changePassCard.setVisible(false);
                showLoginOverlay();
            }
        });
        changePassCard.add(closeX);

        JLabel titleLabel = new JLabel("Change Password");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        titleLabel.setBounds(60, 40, 300, 40);
        changePassCard.add(titleLabel);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        userLabel.setBounds(40, 100, 200, 30);
        changePassCard.add(userLabel);

        RoundedTextField userField = new RoundedTextField(20);
        userField.setBounds(40, 130, overlayWidth - 80, 35);
        changePassCard.add(userField);

        JLabel currPassLabel = new JLabel("Current Password");
        currPassLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        currPassLabel.setBounds(40, 180, 200, 30);
        changePassCard.add(currPassLabel);

        RoundedPasswordField currPassField = new RoundedPasswordField(20);
        currPassField.setBounds(40, 210, overlayWidth - 80, 35);
        changePassCard.add(currPassField);

        JLabel newPassLabel = new JLabel("New Password");
        newPassLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        newPassLabel.setBounds(40, 260, 200, 30);
        changePassCard.add(newPassLabel);

        RoundedPasswordField newPassField = new RoundedPasswordField(20);
        newPassField.setBounds(40, 290, overlayWidth - 80, 35);
        changePassCard.add(newPassField);

        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        confirmLabel.setBounds(40, 340, 200, 30);
        changePassCard.add(confirmLabel);

        RoundedPasswordField confirmField = new RoundedPasswordField(20);
        confirmField.setBounds(40, 370, overlayWidth - 80, 35);
        changePassCard.add(confirmField);

        JButton doneBtn = new JButton("Done");
        doneBtn.setBounds((overlayWidth - 200) / 2, 430, 200, 45);
        styleLoginButton(doneBtn, new Color(0, 64, 64));
        doneBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(changePassCard, "Password changed successfully!");
            changePassCard.setVisible(false);
            showLoginOverlay();
        });
        changePassCard.add(doneBtn);

        bgPanel.add(changePassCard, Integer.valueOf(2));
        bgPanel.repaint();
        bgPanel.revalidate();
    }

    class RoundedTextField extends JTextField {
        private int radius;
        public RoundedTextField(int radius) {
            this.radius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.LIGHT_GRAY);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
        }
    }

    class RoundedPasswordField extends JPasswordField {
        private int radius;
        public RoundedPasswordField(int radius) {
            this.radius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),radius,radius);
            super.paintComponent(g);
        }
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.LIGHT_GRAY);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,radius,radius);
        }
    }

    private void styleMainButton(JButton button) {
        button.setBackground(new Color(45, 45, 45));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Poppins", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    private void styleLoginButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Poppins", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }
}
