import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class CustomerCreate extends CustomerDatabase{
    public HashMap<Integer, CustomerCreate> customer = new HashMap<>();
    private String name;
    private String password;
    private String address;
    private int phoneNumber;
    private int accNumber;

    CustomerCreate() {

    }

    CustomerCreate(String name, String password, String address, int phoneNumber, int accNumber) {

        this.name = name;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.accNumber = accNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(int accNumber) {
        this.accNumber = accNumber;
    }

    @Override
    public String toString() {
        return name+","+password+","+address+","+phoneNumber+","+accNumber;

    }

    public boolean checkUserId(String username) {
        return false;
    }

    public boolean checkUserpassword(String password) {

        int a = password.length();

        return a >= 8;
    }

    public void createUser() throws SQLException, ClassNotFoundException {

        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter your username");
        String username = scan.nextLine();
        while (checkUserId(username)) {
            System.out.println("Please a valid your username");
            username = scan.nextLine();
        }
        System.out.println("Please enter your password");
        String password = scan.nextLine();
        while (!checkUserpassword(password)) {
            System.out.println("Please enter a valid password");
            password = scan.nextLine();
        }
        System.out.println("Please enter your address");
        String address = scan.nextLine();

        System.out.println("Please enter your phone number");
        int phoneNumber = scan.nextInt();
        Random rnd = new Random();
        do {
            accNumber = rnd.nextInt(1000000);
        }while(accNumber>100000);
        CustomerCreate cusInfo = new CustomerCreate(username, password, address, phoneNumber, accNumber);
        customer.put(cusInfo.getAccNumber(), cusInfo);
        dbEntry(customer,cusInfo.getAccNumber());


    }
}