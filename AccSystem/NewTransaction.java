import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class NewTransaction {
    private final JFrame frame;
    private JPanel bgPanel;
    private JPanel adminPanel;
    private boolean adminVisible = false;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public NewTransaction() {
        frame = new JFrame("Accounting System ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 650);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                String imges = "AccSystem" + File.separator + "images" + File.separator + "pic2.png";
                ImageIcon bg = new ImageIcon(imges);
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(null);

        adminPanel = new JPanel() {
            @Override
            public boolean contains(int x, int y) {
                return true;
            }
        };
        adminPanel.setLayout(null);
        adminPanel.setBackground(Color.WHITE);
        adminPanel.setBounds(750, 0, 350, 650);
        adminPanel.setVisible(false);
        adminPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        adminPanel.setOpaque(true);
        adminPanel.addMouseListener(new MouseAdapter() {});
        adminPanel.addMouseMotionListener(new MouseMotionAdapter() {});

        String imges1 = "AccSystem" + File.separator + "images" + File.separator + "icon1.jpg";
        CircularImagePanel adminImage = new CircularImagePanel(imges1, 100);
        adminImage.setBounds(125, 140, 100, 100);
        adminPanel.add(adminImage);

        JLabel adminLabel = new JLabel("Admin", SwingConstants.CENTER);
        adminLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        adminLabel.setBounds(100, 250, 150, 30);
        adminPanel.add(adminLabel);

        JButton logoutBtn = new JButton("Log Out");
        logoutBtn.setBounds(100, 310, 150, 40);
        logoutBtn.setBackground(new Color(40, 40, 40));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Poppins", Font.BOLD, 13));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setOpaque(true);
        logoutBtn.setContentAreaFilled(true);
        adminPanel.add(logoutBtn);

        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new LOGIN();
        });

bgPanel.add(adminPanel);

String imges2 = "AccSystem" + File.separator + "images" + File.separator + "pic3.png";
ImageIcon adminIcon = new ImageIcon(imges2); 
Image scaledAdminIcon = adminIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

JButton adminBtn = new JButton(new ImageIcon(scaledAdminIcon));
adminBtn.setBounds(995, 18, 65, 35);
adminBtn.setBackground(new Color(40, 40, 40)); 
adminBtn.setFocusPainted(false);
adminBtn.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2, true)); 
adminBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
adminBtn.setContentAreaFilled(true);
adminBtn.setOpaque(true);
bgPanel.add(adminBtn);

adminBtn.addActionListener(e -> {
    boolean showing = adminPanel.isVisible();

    if (showing) {
        adminPanel.setVisible(false);
    } else {
        adminPanel.setVisible(true);
        bgPanel.setComponentZOrder(adminBtn, 0);
        bgPanel.setComponentZOrder(adminPanel, 1);
        adminBtn.requestFocusInWindow();
    }

    bgPanel.revalidate();
    bgPanel.repaint();
});

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBounds(15, 85, 840, 510);
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
        contentPanel.add(new NewTransactionCard(), "newTransaction");
        contentPanel.add(new TransactionsCard(), "transactions");
        contentPanel.add(new AccountsCard(), "accounts");
        contentPanel.add(new GeneralJournalCard(), "generalJournal");
        contentPanel.add(new GeneralLedgerCard(), "generalLedger");
        contentPanel.add(new BalanceSheetCard(), "balanceSheet");
        cardLayout.show(contentPanel, "newTransaction");
        bgPanel.add(contentPanel);


        JButton loadBtn = new JButton("Load CSV");
    loadBtn.addActionListener(e -> {
    String accountName = LOGIN.loggedInUser; 
    JTable loadedTable = TransactionFolder.loadCSV(accountName);
    DefaultTableModel loadedModel = (DefaultTableModel) loadedTable.getModel();

    if (loadedModel.getRowCount() == 0) {
        JOptionPane.showMessageDialog(frame, "No transactions found!");
        return;
    }

    List<String[]> transactions = new ArrayList<>();
    for (int i = 0; i < loadedModel.getRowCount(); i++) {
        String[] row = new String[5];
        for (int j = 0; j < 5; j++) {
            row[j] = loadedModel.getValueAt(i, j).toString();
        }
        transactions.add(row);
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    transactions.sort(Comparator.comparing(t -> LocalDate.parse(t[0], formatter)));

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    for (String[] t : transactions) {
        String displayText = t[0] + " | " + t[1] + " | " + t[2] + " -> " + t[3] + " | " + t[4];
        JButton rowBtn = new JButton(displayText);
        rowBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        rowBtn.addActionListener(ev -> JOptionPane.showMessageDialog(frame, "Transaction selected:\n" + displayText));
        panel.add(rowBtn);
    }

    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setPreferredSize(new Dimension(700, 300));

    JOptionPane.showMessageDialog(frame, scrollPane, "Transactions", JOptionPane.PLAIN_MESSAGE);
});

        JButton saveBtn = new JButton("Save CSV");
        saveBtn.setBounds(780, 20, 120, 30);
        styleTextButton(saveBtn);
        saveBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, " save "));
        bgPanel.add(saveBtn);

        JButton aboutBtn = new JButton("About");
        aboutBtn.setBounds(880, 20, 120, 30);
        styleTextButton(aboutBtn);
        aboutBtn.addActionListener(e -> {

    JFrame aboutFrame = new JFrame("About");
    aboutFrame.setSize(400, 150);        
    aboutFrame.setLocationRelativeTo(frame); 
    aboutFrame.setLayout(new BorderLayout());

    JTextArea textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);

    textArea.setText(
        "This Accounting System helps users record, organize, and manage\n"
      + "financial transactions. It allows account creation, transaction\n"
      + "tracking, and secure data storage to support accurate and\n"
      + "efficient bookkeeping."
    );

    JScrollPane scrollPane = new JScrollPane(textArea);

    aboutFrame.add(scrollPane, BorderLayout.CENTER);

    aboutFrame.setVisible(true);
});
        bgPanel.add(aboutBtn);


        JButton newTransBtn = new JButton("New Transactions");
        newTransBtn.setBounds(868, 150, 200, 35);
        styleTextButton(newTransBtn);
        newTransBtn.addActionListener(e -> cardLayout.show(contentPanel, "newTransaction"));
        bgPanel.add(newTransBtn);

        JButton transBtn = new JButton("Transactions");
        transBtn.setBounds(868, 215, 200, 35);
        styleTextButton(transBtn);
        transBtn.addActionListener(e -> cardLayout.show(contentPanel, "transactions"));
        bgPanel.add(transBtn);

        JButton accountsBtn = new JButton("Accounts");
        accountsBtn.setBounds(868, 280, 200, 35);
        styleTextButton(accountsBtn);
        accountsBtn.addActionListener(e -> cardLayout.show(contentPanel, "accounts"));
        bgPanel.add(accountsBtn);

        JButton journalBtn = new JButton("General Journal");
        journalBtn.setBounds(868, 345, 200, 35);
        styleTextButton(journalBtn);
        journalBtn.addActionListener(e -> cardLayout.show(contentPanel, "generalJournal"));
        bgPanel.add(journalBtn);

        JButton ledgerBtn = new JButton("General Ledger");
        ledgerBtn.setBounds(868, 410, 200, 35);
        styleTextButton(ledgerBtn);
        ledgerBtn.addActionListener(e -> cardLayout.show(contentPanel, "generalLedger"));
        bgPanel.add(ledgerBtn);

        JButton balanceBtn = new JButton("Balance Sheet");
        balanceBtn.setBounds(868, 475, 200, 35);
        styleTextButton(balanceBtn);
        balanceBtn.addActionListener(e -> cardLayout.show(contentPanel, "balanceSheet"));
        bgPanel.add(balanceBtn);

        frame.setContentPane(bgPanel);
        frame.setVisible(true);
    }

private void styleTextButton(JButton button) {
    button.setContentAreaFilled(false);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setOpaque(false);
    button.setForeground(Color.WHITE); 
    button.setFont(new Font("Poppins", Font.BOLD, 16));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
}



private class NewTransactionCard extends JPanel {
private JComboBox<Integer> dayDropdown;
private JComboBox<String> monthDropdown;
private JComboBox<Integer> yearDropdown;
private JTextField descriptionField;
private JComboBox<String> debitDropdown;
private JComboBox<String> creditDropdown;
private JTextField amountField;
private JTable previewTable;
private DefaultTableModel tableModel;
private final String accountName = LOGIN.loggedInUser;

public NewTransactionCard() {
    setLayout(null);
    setBackground(Color.WHITE);

    JLabel formTitle = new JLabel("New Transactions", SwingConstants.CENTER);
    formTitle.setFont(new Font("Poppins", Font.BOLD, 28));
    formTitle.setBounds(0, 20, 840, 40);
    add(formTitle);

    JLabel dateLabel = new JLabel("Date (MM-DD-YYYY)");
    dateLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
    dateLabel.setBounds(60, 80, 180, 25);
    add(dateLabel);

    monthDropdown = new JComboBox<>(new String[]{
            "01","02","03","04","05","06","07","08","09","10","11","12"});
    monthDropdown.setBounds(240, 80, 80, 30);
    add(monthDropdown);

    dayDropdown = new JComboBox<>();
    for (int i = 1; i <= 31; i++) dayDropdown.addItem(i);
    dayDropdown.setBounds(330, 80, 60, 30);
    add(dayDropdown);

    yearDropdown = new JComboBox<>();
    for (int i = 2020; i <= 2030; i++) yearDropdown.addItem(i);
    yearDropdown.setBounds(400, 80, 80, 30);
    add(yearDropdown);

    JLabel descLabel = new JLabel("Description");
    descLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
    descLabel.setBounds(60, 130, 180, 25);
    add(descLabel);

    descriptionField = new JTextField();
    descriptionField.setBounds(240, 130, 400, 30);
    add(descriptionField);

    JLabel debitLabel = new JLabel("Debit Account");
    debitLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
    debitLabel.setBounds(60, 180, 180, 25);
    add(debitLabel);

    debitDropdown = new JComboBox<>(getDebitAccounts());
    debitDropdown.setBounds(240, 180, 400, 30);
    add(debitDropdown);

    JLabel creditLabel = new JLabel("Credit Account");
    creditLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
    creditLabel.setBounds(60, 230, 180, 25);
    add(creditLabel);

    creditDropdown = new JComboBox<>(getCreditAccounts());
    creditDropdown.setBounds(240, 230, 400, 30);
    add(creditDropdown);

    JLabel amountLabel = new JLabel("Amount");
    amountLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
    amountLabel.setBounds(60, 280, 180, 25);
    add(amountLabel);

    amountField = new JTextField();
    amountField.setBounds(240, 280, 400, 30);
    add(amountField);

    JButton addBtn = new JButton("Add Transaction");
    addBtn.setBounds(330, 330, 200, 40);
    styleMainButton(addBtn);
    add(addBtn);

    String[] columns = {"Date", "Description", "Debit", "Credit", "Amount"};
    tableModel = new DefaultTableModel(columns, 0);
    previewTable = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(previewTable);
    scrollPane.setBounds(40, 380, 745, 120);
    add(scrollPane);

    TransactionFolder.createAccountFolder(accountName);

    addBtn.addActionListener(e -> {
    String date = monthDropdown.getSelectedItem() + "-" +
                  dayDropdown.getSelectedItem() + "-" +
                  yearDropdown.getSelectedItem();
    String description = descriptionField.getText();
    String debit = (String) debitDropdown.getSelectedItem();
    String credit = (String) creditDropdown.getSelectedItem();
    String amount = amountField.getText();

    if (description.isEmpty() || amount.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Description and Amount cannot be empty");
        return;
    }

    TransactionFolder.addTransaction(date, description, debit, credit, amount, accountName); 
    tableModel.addRow(new Object[]{date, description, debit, credit, amount});

    descriptionField.setText("");
    amountField.setText("");
    });
}

}


private class TransactionsCard extends JPanel {
    public TransactionsCard() {
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Transactions", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 28));
        title.setBounds(0, 20, 840, 40);
        add(title);

        JLabel searchLabel = new JLabel("Search");
        searchLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        searchLabel.setBounds(130, 80, 50, 25);
        add(searchLabel);

        JTextField searchField = new JTextField();
        searchField.setBounds(190, 80, 380, 30);
        searchField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        add(searchField);

        JButton clearBtn = new JButton("X Clear");
        clearBtn.setBounds(590, 80, 100, 30);
        styleMainButton(clearBtn);
        add(clearBtn);

        String[] columnNames = {"Date", "Description", "Debit Account", "Credit Account"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        JTable transactionTable = new JTable(model);
        transactionTable.setFont(new Font("Poppins", Font.PLAIN, 14));
        transactionTable.setRowHeight(25);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = transactionTable.getTableHeader();
        header.setReorderingAllowed(false);   
        header.setResizingAllowed(false);     

        header.setFont(new Font("Poppins", Font.BOLD, 14));
        header.setBackground(new Color(0, 64, 64)); 
        header.setForeground(Color.WHITE);

        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(150); 
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(250); 
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(175); 
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(175); 

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBounds(40, 130, 745, 330);
        add(scrollPane);
    }
}

    private class AccountsCard extends JPanel 
    {
        public AccountsCard() {
            setLayout(null);
            setBackground(Color.WHITE);
            JLabel title = new JLabel("Accounts", SwingConstants.CENTER);
            title.setFont(new Font("Poppins", Font.BOLD, 28));
            title.setBounds(0, 20, 840, 40);
            add(title);

  String[] columnNames = {"Account", " Type ", "Balance"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        JTable transactionTable = new JTable(model);
        transactionTable.setFont(new Font("Poppins", Font.PLAIN, 14));
        transactionTable.setRowHeight(25);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = transactionTable.getTableHeader();
        header.setReorderingAllowed(false);   
        header.setResizingAllowed(false);     

        header.setFont(new Font("Poppins", Font.BOLD, 14));
        header.setBackground(new Color(0, 64, 64)); 
        header.setForeground(Color.WHITE);

        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(250); 
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(250); 
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(250); 
   

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBounds(40, 80, 745, 380);
        add(scrollPane);


        }
    }

        private class GeneralJournalCard extends JPanel {
        private DefaultTableModel model;

        public GeneralJournalCard() {
            setLayout(null);
            setBackground(Color.WHITE);
            JLabel title = new JLabel("General Journal", SwingConstants.CENTER);
            title.setFont(new Font("Poppins", Font.BOLD, 28));
            title.setBounds(0, 20, 840, 40);
            add(title);

            String[] columnNames = {"Date", "Description", "Debit Account", "Credit Account", "Amount"};
            model = new DefaultTableModel(columnNames, 0);
            JTable table = new JTable(model);
            table.setFont(new Font("Poppins", Font.PLAIN, 14));
            table.setRowHeight(25);

            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Poppins", Font.BOLD, 14));
            header.setBackground(new Color(0, 64, 64));
            header.setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(40, 80, 745, 380);
            add(scrollPane);
        }

        // 🔹 Update/Refresh table when new transaction is added
        public void refreshTable() {
            model.setRowCount(0); // clear table
            for (JournalEntry e : journalEntries) {
                model.addRow(new Object[]{e.date, e.description, e.debitAccount, e.creditAccount, e.amount});
            }
        }
    }

    // === Dummy Cards to Avoid Errors ===
    private class TransactionsCard extends JPanel { public TransactionsCard() { setBackground(Color.LIGHT_GRAY); } }
    private class AccountsCard extends JPanel { public AccountsCard() { setBackground(Color.LIGHT_GRAY); } }
    private class GeneralLedgerCard extends JPanel { public GeneralLedgerCard() { setBackground(Color.LIGHT_GRAY); } }
    private class BalanceSheetCard extends JPanel { public BalanceSheetCard() { setBackground(Color.LIGHT_GRAY); } }

    private String[] getDebitAccounts() {
        return new String[]{"Cash", "Supplies", "Equipment", "Rent Expense"};
    }

    private String[] getCreditAccounts() {
        return new String[]{"Accounts Payable", "Owner’s Capital", "Service Revenue"};
    }

    private void styleTextButton(JButton button) {
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Poppins", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleMainButton(JButton button) {
        button.setBackground(new Color(40, 40, 40));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Poppins", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NewTransaction::new);
    }
}

    private class GeneralLedgerCard extends JPanel 
    {
        public GeneralLedgerCard() 
        {
            setLayout(null);
            setBackground(Color.WHITE);
            JLabel title = new JLabel("General Ledger", SwingConstants.CENTER);
            title.setFont(new Font("Poppins", Font.BOLD, 28));
            title.setBounds(0, 20, 840, 40);
            add(title);

 JLabel searchLabel = new JLabel("Select Account");
        searchLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        searchLabel.setBounds(120, 80, 110, 25);
        add(searchLabel);


  JComboBox<String> allDropdown = new JComboBox<>(getAllAccounts());
        allDropdown.setBounds(250, 80, 400, 30);
        allDropdown.setFont(new Font("Poppins", Font.PLAIN, 14));
        add(allDropdown);
        

        String[] columnNames = {"Date", "Description", "Debit Account", "Credit Account","Amount","Balance"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        JTable transactionTable = new JTable(model);
        transactionTable.setFont(new Font("Poppins", Font.PLAIN, 14));
        transactionTable.setRowHeight(25);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = transactionTable.getTableHeader();
        header.setReorderingAllowed(false);   
        header.setResizingAllowed(false);     

        header.setFont(new Font("Poppins", Font.BOLD, 14));
        header.setBackground(new Color(0, 64, 64)); 
        header.setForeground(Color.WHITE);

        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(100); 
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(130); 
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(130); 
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(130); 
        transactionTable.getColumnModel().getColumn(4).setPreferredWidth(130); 
        transactionTable.getColumnModel().getColumn(5).setPreferredWidth(130); 


        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBounds(40, 130, 745, 330);
        add(scrollPane);

        }
    }

    private class BalanceSheetCard extends JPanel 
    {
        public BalanceSheetCard() 
        {
            setLayout(null);
            setBackground(Color.WHITE);
            JLabel title = new JLabel("Balance Sheet", SwingConstants.CENTER);
            title.setFont(new Font("Poppins", Font.BOLD, 28));
            title.setBounds(0, 20, 840, 40);
            add(title);

            JLabel side = new JLabel("Assets");
            side.setFont(new Font("Poppins", Font.BOLD, 21));
            side.setBounds(180, 70, 400, 40);
            add(side);

            JLabel side1 = new JLabel("Liabilities and Equity");
            side1.setFont(new Font("Poppins", Font.BOLD, 21));
            side1.setBounds(500, 70, 400, 40);
            add(side1);

        String[] columnNames1 = {"Asset", "Amount"};
        DefaultTableModel model1 = new DefaultTableModel(columnNames1, 0)
         {
             @Override
            public boolean isCellEditable(int row, int column) 
            {
             return false; 
            }
        };

        JTable assetTable = new JTable(model1);
        assetTable.setFont(new Font("Poppins", Font.PLAIN, 14));
        assetTable.setRowHeight(25);
        assetTable.setFillsViewportHeight(true);
        assetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header1 = assetTable.getTableHeader();
        header1.setReorderingAllowed(false); 
        header1.setResizingAllowed(false);   
        header1.setFont(new Font("Poppins", Font.BOLD, 14));
        header1.setBackground(new Color(0, 64, 64)); 
        header1.setForeground(Color.WHITE);  
        assetTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        assetTable.getColumnModel().getColumn(1).setPreferredWidth(130);

        JScrollPane scrollPane1 = new JScrollPane(assetTable);
        scrollPane1.setBounds(40, 105, 350, 330);
        add(scrollPane1);

    String[] columnNames2 = {"Liabilities & Equity", "Amount"};
    DefaultTableModel model2 = new DefaultTableModel(columnNames2, 0) 
    {
         @Override
         public boolean isCellEditable(int row, int column)
          {
            return false;
          }
    };

    JTable liabilityTable = new JTable(model2);
    liabilityTable.setFont(new Font("Poppins", Font.PLAIN, 14));
    liabilityTable.setRowHeight(25);
    liabilityTable.setFillsViewportHeight(true);
    liabilityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    JTableHeader header2 = liabilityTable.getTableHeader();
    header2.setReorderingAllowed(false);
    header2.setResizingAllowed(false);
    header2.setFont(new Font("Poppins", Font.BOLD, 14));
    header2.setBackground(new Color(0, 64, 64));
    header2.setForeground(Color.WHITE);

    liabilityTable.getColumnModel().getColumn(0).setPreferredWidth(180);
    liabilityTable.getColumnModel().getColumn(1).setPreferredWidth(180);

    JScrollPane scrollPane2 = new JScrollPane(liabilityTable);
    scrollPane2.setBounds(420, 105, 370, 330); 
    add(scrollPane2);

         JButton clearBtn = new JButton("Print");
        clearBtn.setBounds(350, 450, 100, 30);
        styleMainButton(clearBtn);
        add(clearBtn);

        }
    }


    private String[] getDebitAccounts() {
        return new String[]{
                "Cash – (ASSETS)",
                "Accounts Receivable – (ASSETS)",
                "Supplies – (ASSETS)",
                "Equipment – (ASSETS)",
                "Inventory – (ASSETS)",
                "Land – (ASSETS)",
                "Building – (ASSETS)",
                "Prepaid Expenses – (ASSETS)",
                "Rent Expense – (EXPENSES)",
                "Salaries Expense – (EXPENSES)",
                "Utilities Expense – (EXPENSES)",
                "Advertising Expense – (EXPENSES)",
                "Supplies Expense – (EXPENSES)",
                "Depreciation Expense – (EXPENSES)",
                "Owner’s Drawings – (OWNER’S EQUITY)"
        };
    }



    private String[] getCreditAccounts() {
        return new String[]{
                "Accounts Payable – (LIABILITIES)",
                "Notes Payable – (LIABILITIES)",
                "Salaries Payable – (LIABILITIES)",
                "Taxes Payable – (LIABILITIES)",
                "Unearned Revenue – (LIABILITIES)",
                "Owner’s Capital – (OWNER’S EQUITY)",
                "Service Revenue – (REVENUE)",
                "Sales Revenue – (REVENUE)",
                "Interest Income – (REVENUE)",
                "Fees Earned – (REVENUE)"
        };
    }

    private String[] getAllAccounts() {
        return new String[]{
                "Cash – (ASSETS)",
                "Accounts Receivable – (ASSETS)",
                "Supplies – (ASSETS)",
                "Equipment – (ASSETS)",
                "Inventory – (ASSETS)",
                "Land – (ASSETS)",
                "Building – (ASSETS)",
                "Prepaid Expenses – (ASSETS)",
                "Rent Expense – (EXPENSES)",
                "Salaries Expense – (EXPENSES)",
                "Utilities Expense – (EXPENSES)",
                "Advertising Expense – (EXPENSES)",
                "Supplies Expense – (EXPENSES)",
                "Depreciation Expense – (EXPENSES)",
                "Owner’s Drawings – (OWNER’S EQUITY)",
                "Accounts Payable – (LIABILITIES)",
                "Notes Payable – (LIABILITIES)",
                "Salaries Payable – (LIABILITIES)",
                "Taxes Payable – (LIABILITIES)",
                "Unearned Revenue – (LIABILITIES)",
                "Owner’s Capital – (OWNER’S EQUITY)",
                "Service Revenue – (REVENUE)",
                "Sales Revenue – (REVENUE)",
                "Interest Income – (REVENUE)",
                "Fees Earned – (REVENUE)"
        };
    }




    private void styleSmallButton(JButton button) {
        button.setBackground(new Color(40, 40, 40));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Poppins", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    private void styleMainButton(JButton button) {
        button.setBackground(new Color(40, 40, 40));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Poppins", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    private void styleMenuButton(JButton button) {
        button.setBackground(new Color(40, 40, 40));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Poppins", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NewTransaction::new);
    }
  
    class CircularImagePanel extends JPanel
    {
        private final Image image;

        public CircularImagePanel(String imagePath, int diameter) 
        {
            ImageIcon icon = new ImageIcon(imagePath);
            this.image = icon.getImage().getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(diameter, diameter));
            setOpaque(false);
        }
 
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape circle = new Ellipse2D.Double(0, 0, getWidth(), getHeight());
            g2.setClip(circle);
            g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            g2.setClip(null);
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(Color.LIGHT_GRAY);
            g2.draw(circle);

            g2.dispose();
        }
    }
}
