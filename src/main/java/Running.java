import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Running {

    public void menu() throws SQLException, ClassNotFoundException, IOException {

        System.out.println("==================TRUST BANK============================");
        System.out.println("===================WELCOME===========================");
        System.out.println("1-OPEN AN ACCOUNT");
        System.out.println("2-LOG IN");
        System.out.println("3-EXIT");

        Scanner scan = new Scanner(System.in);
        System.out.println("Please make your choice and enter a number in the menu");
        int choice = scan.nextInt();

        switch (choice) {
            case 1:
                CustomerCreate cc = new CustomerCreate();
                cc.createUser();
                menu();
                break;
            case 2:
                CustomerDatabase user = new CustomerDatabase();
                user.getUserInfo();
               break;
            case 3:
                exit();
                break;
            case 5:
                Running run=new Running();
                run.customerMenu();
                break;
            default:
                System.out.println("Please enter a valid number");
                menu();
                break;
        }

    }

    private void exit() {
        System.out.println("Have a nice day");
    }


    private void customerMenu() throws IOException {

        System.out.println("==================TRUST BANK============================");
        System.out.println("===================CUSTOMER MENU===========================");
        System.out.println("1-MONEY TRANSFER");
        System.out.println("2-WITHDRAW");
        System.out.println("3-DEPOSIT");
        System.out.println("4-UPDATE PERSONAL INFO");
        System.out.println("5-MAIN MENU");
    }
}