import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionFolder {
    public static final String MAIN_FOLDER = "Accounting_System";
    public static Scanner scan = new Scanner(System.in);
    public static int accountNum = 1;
    private static boolean running = false;

    public static String accountsFolderName() {
        return scan.nextLine(); 
    }

    /* Here is the Account Folder, this method creates the Folder for Accounts 
    (Account_"Name", Account_Delly and etc) */
    public static void accountsFolder(String name) {
        File mainFolder = new File(MAIN_FOLDER);
        if (!mainFolder.exists()) mainFolder.mkdir();

        File fileFolder;    
        do {
            String folder = "Accounts_" + name + "_" + accountNum;
            fileFolder = new File(mainFolder, folder);
            accountNum++;
        } while (fileFolder.exists());

        fileFolder.mkdir();
    }

    /* Over here creates the File Transactions, example (Transactions1.csv, Transactions2.csv, etc.) */   
    public static void setTransactions(String name) {
        running = true;
        
        while (running) {
            File mainFolder = new File(MAIN_FOLDER);
            File fileFolder = new File(mainFolder, "Accounts_" + name);
            
            static String nNew1 (String n) {
                String nName = scan.nextLine();
                return nName;
            }

            File transactionFile = new File(fileFolder, "Transactions" + "\n"  + nNew1() + accountNum + ".csv");

            try {
                if (transactionFile.createNewFile()) {
                    System.out.println("Transaction file created." + transactionFile.getAbsolutePath());
                } else {
                    System.out.println("Transaction file already exists.");
                }
                accountNum++;
                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void transactionStop() {
        running = false;
    }

    /* This method is what you gonna call to verofy the files you want to know exists */
    public static String openFile(String path) {
        File file = new File(path);

        if (file.exists() && file.isFile()) {
            System.out.print("Files exists." + file.getAbsolutePath());
        }
        else {
            System.out.print("File does not exist." + path);
        }
        return path;

    }

    /* Call this method to gain acces to the contents of CSV file, it will be shown in a list */
    public static List<String[]> readCSV(String path) {
    List<String[]> rows = new ArrayList<>();
    try (Scanner reader = new Scanner(new File(path))) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            String[] columns = line.split(",");
            rows.add(columns);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return rows;
}
}  