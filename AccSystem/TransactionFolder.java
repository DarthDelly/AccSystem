import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionFolder {
    public static final String MAIN_FOLDER = "AccSystem";

    public static void createAccountFolder(String accountName) {
        File mainFolder = new File(MAIN_FOLDER);
        if (!mainFolder.exists()) mainFolder.mkdir();

        File accountFolder = new File(mainFolder, "Accounts_" + accountName);
        if (!accountFolder.exists()) accountFolder.mkdir();

        File transactionFile = new File(accountFolder, "Transactions.csv");
        if (!transactionFile.exists()) {
            try (PrintWriter pw = new PrintWriter(transactionFile)) {
                pw.println("Date,Description,Debit,Credit,Amount"); 
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error creating Transactions.csv: " + e.getMessage());
            }
        }
    }

    public static void addTransaction(String date, String description, String debit, String credit, String amount, String accountName) {
        File transactionFile = new File(MAIN_FOLDER + "/Accounts_" + accountName + "/Transactions.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(transactionFile, true))) { 
            pw.println(date + "," + description + "," + debit + "," + credit + "," + amount);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving transaction: " + e.getMessage());
        }
    }

    public static JTable loadCSV(String accountName) {
        File transactionFile = new File(MAIN_FOLDER + "/Accounts_" + accountName + "/Transactions.csv");
        List<String[]> rows = new ArrayList<>();

        try (Scanner reader = new Scanner(transactionFile)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] columns = line.split(",");
                rows.add(columns);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading CSV: " + e.getMessage());
        }

        if (rows.isEmpty()) return new JTable();

        String[] headers = rows.get(0);
        DefaultTableModel model = new DefaultTableModel(headers, 0);
        for (int i = 1; i < rows.size(); i++) {
            model.addRow(rows.get(i));
        }

        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        return table;
    }
}
