import java.util.Scanner;

public class Example {
    public static void main(String[] args) {        
        try (Scanner scan = new Scanner(System.in)) {
            System.out.print("Input name for Transaction: ");
            String name = TransactionFolder.accountsFolderName(); 

            TransactionFolder.accountsFolder(name);   
            //TransactionFolder.setTransactions(name); 

            System.out.print("Try type (Transaction1): ");
            String file = scan.nextLine();

            TransactionFolder.openFile(file);
        }


    }
}
