
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class CustomerDatabase extends BankAccount {
    HashMap<Integer, CustomerCreate> customerDB = new HashMap<>();
    HashMap<Integer, Double> customerDBAmount = new HashMap<>();


    public void dbEntry(HashMap<Integer, CustomerCreate> cus, int accNumber) throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String path = "jdbc:oracle:thin:@localhost:1521/XE";
        Connection con = DriverManager.getConnection(path, "hr", "hr");
        Statement st = con.createStatement();

        String[] arr = cus.get(accNumber).toString().split(",");
        st.executeUpdate("INSERT INTO Customers VALUES(" + arr[4] + ",'" + arr[0] + "','" + arr[1] + "','" + arr[2] + "','" + arr[3] + "')");
        st.executeUpdate("INSERT INTO Account VALUES(" + arr[4] + "," + 0 + ")");
        st.close();
    }

    public void dbRead(String username, String password) throws ClassNotFoundException, SQLException, IOException {
        int count = 0;
        int accNo = 0;
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String path = "jdbc:oracle:thin:@localhost:1521/XE";
        Connection con = DriverManager.getConnection(path, "hr", "hr");
        Statement st = con.createStatement();
        String query1 = "SELECT * FROM Customers" +
                " WHERE  name='" + username + "' AND password='" + password + "'";
        ResultSet result = st.executeQuery(query1);

        while (result.next()) {
            count++;
            customerDB.put(result.getInt(1),new CustomerCreate(result.getString(2),result.getString(3),result.getString(4),result.getInt(5),result.getInt(1)));
            accNo = result.getInt(1);
        }


        if (count == 0) {

            System.out.println("Invalid username/password");
            getUserInfo();
        }
        String query2="SELECT * FROM account" +
                    " WHERE accountnumber="+accNo;
        ResultSet result2 = st.executeQuery(query2);

        while (result2.next()) {
            customerDBAmount.put(result2.getInt(1),result2.getDouble(2));
        }


            customerMenu(accNo);

    }


    public void getUserInfo() throws SQLException, ClassNotFoundException, IOException {

        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter your username");
        String username = scan.next();
        System.out.println("Please enter your password");

        String password = scan.next();
        dbRead(username, password);
    }

    private void customerMenu(int accNo) throws IOException, SQLException, ClassNotFoundException {

        System.out.println("==================TRUST BANK============================");
        System.out.println("===================CUSTOMER MENU===========================");
        System.out.println("1-BALANCE 💵");
        System.out.println("2-MONEY TRANSFER 💸");
        System.out.println("3-WITHDRAW 🏧");
        System.out.println("4-DEPOSIT 💰 ");
        System.out.println("5-BANK STATEMENT 📜");
        System.out.println("6-UPDATE PERSONAL INFO 🛠");
        System.out.println("7-MAIN MENU 📓");

        Scanner scan = new Scanner(System.in);
        System.out.println("Please make your choice and enter a number in the menu");
        int choice = scan.nextInt();
        switch (choice) {
            case 1:
                balance(accNo);

                break;
            case 2:
            moneyTransfer(accNo);

               break;

            case 3:
            withdraw(accNo);

                break;

            case 4:
                deposit(accNo);
                break;
            case 5:
                bankStatement(accNo);
                break;
            case 6:
                userUpdate(accNo);
                break;
            case 7:
                Running run=new Running();
                run.menu();
            default:
                break;
        }
    }

    private void bankStatement(int accNo) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String path = "jdbc:oracle:thin:@localhost:1521/XE";
        Connection con = DriverManager.getConnection(path, "hr", "hr");
        Statement st = con.createStatement();
        String statement="SELECT * FROM transactions WHERE accountnumber="+accNo+" ORDER BY trsno";
        ResultSet rs=st.executeQuery(statement);
        System.out.println("Account Number\t \t Transaction Number\t \t Operation Type \t \t Amount\t \t Notes");
        System.out.println("==============     ===================       ==============        ==========    =======");
        while(rs.next()){
            System.out.println(rs.getInt(1)+"\t  \t  \t  \t"+rs.getInt(2)+"\t  \t  \t  \t"+rs.getString(3)+"\t  \t  \t  \t"+rs.getInt(4)+"\t  \t  \t  \t"+rs.getString(5));
        }

    }

    private void userUpdate(int accNo) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String path = "jdbc:oracle:thin:@localhost:1521/XE";
        Connection con = DriverManager.getConnection(path, "hr", "hr");
        Statement st = con.createStatement();

        Scanner scan=new Scanner(System.in);
        System.out.println("Please enter your new username");
        String username=scan.next();
        System.out.println("Please enter your new password ");
        String password=scan.next();
        String query="UPDATE customers SET name='"+username+"' , password='"+password+"'" +
                " WHERE accountnumber="+accNo;
        st.executeUpdate(query);
        System.out.println("Your username and password has been updated");

    }

    private void withdraw(int accNo) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String path = "jdbc:oracle:thin:@localhost:1521/XE";
        Connection con = DriverManager.getConnection(path, "hr", "hr");
        Statement st = con.createStatement();

        Scanner scan=new Scanner(System.in);

        System.out.println("Please enter the amount you want to withdraw");
        double withd=scan.nextDouble();
        BankAccount customerDeposit=new BankAccount();
        while(withd>customerDeposit.getAmountInAccount(customerDBAmount,accNo)) {
            System.out.println("Insufficient Account Balance.\n ");
            System.out.println("Please enter the amount you want to withdraw again:");
            withd=scan.nextDouble();
        }
        customerDeposit.setAmountInAccount(customerDBAmount,accNo,withd*-1);

        st.executeUpdate("UPDATE account SET amount="+customerDeposit.getAmountInAccount(customerDBAmount,accNo) +
                " WHERE accountnumber="+accNo);

        withdrawTransaction(accNo,withd);
        customerMenu(accNo);

    }

    private void withdrawTransaction(int accNo, double withd) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String path = "jdbc:oracle:thin:@localhost:1521/XE";
        Connection con = DriverManager.getConnection(path, "hr", "hr");
        Statement st = con.createStatement();
        Scanner scan=new Scanner(System.in);

        String getTransactionNumber="SELECT MAX(trsno) FROM transactions";

        ResultSet rs=st.executeQuery(getTransactionNumber);
         int trnsNo=0;
        while(rs.next()) {

            trnsNo=rs.getInt(1)+1;

        }
        String query="INSERT INTO transactions VALUES("+accNo+","+trnsNo+",'withdraw',"+withd+",'')";

        ResultSet rs2=st.executeQuery(query);

    }

    private void balance(int accNo) throws SQLException, IOException, ClassNotFoundException {
        System.out.println("Account Number      =====>"+accNo);
        System.out.println("Account Balance     =====>"+customerDBAmount.get(accNo));
        customerMenu(accNo);
    }

    public void deposit(int accNo) throws SQLException, IOException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String path = "jdbc:oracle:thin:@localhost:1521/XE";
        Connection con = DriverManager.getConnection(path, "hr", "hr");
        Statement st = con.createStatement();

        Scanner scan=new Scanner(System.in);
        System.out.println("Please enter the amount you want to deposit");
        double dep=scan.nextDouble();
        BankAccount customerDeposit=new BankAccount();
        customerDeposit.setAmountInAccount(customerDBAmount,accNo,dep);
        st.executeUpdate("UPDATE account SET amount="+customerDeposit.getAmountInAccount(customerDBAmount,accNo) +
                " WHERE accountnumber="+accNo);
        depositTransaction(accNo,dep);
        customerMenu(accNo);
    }

    private void depositTransaction(int accNo, double dep) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String path = "jdbc:oracle:thin:@localhost:1521/XE";
        Connection con = DriverManager.getConnection(path, "hr", "hr");
        Statement st = con.createStatement();
        Scanner scan=new Scanner(System.in);

        String getTransactionNumber="SELECT MAX(trsno) FROM transactions";

        ResultSet rs=st.executeQuery(getTransactionNumber);
        int trnsNo=0;
        while(rs.next()) {

            trnsNo=rs.getInt(1)+1;

        }
        String query="INSERT INTO transactions VALUES("+accNo+","+trnsNo+",'deposit',"+dep+",'')";

        ResultSet rs2=st.executeQuery(query);
    }

    private void moneyTransfer(int accNo) throws IOException, ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String path = "jdbc:oracle:thin:@localhost:1521/XE";
        Connection con = DriverManager.getConnection(path, "hr", "hr");
        Statement st = con.createStatement();
        Scanner scan=new Scanner(System.in);
        System.out.println("Please enter the account number that you want to transfer to:");
        int transferAcc=scan.nextInt();
        String query="SELECT * FROM account WHERE accountnumber="+transferAcc;
        st.executeUpdate(query);
        ResultSet result= st.getResultSet();
        while(result.next()){
            customerDBAmount.put(result.getInt(1),result.getDouble(2));
        }
        System.out.println("Please enter the amount that you want to transfer:");
        double trans=scan.nextDouble();
        BankAccount transfer = new BankAccount();

        while(trans>transfer.getAmountInAccount(customerDBAmount,accNo)) {
            System.out.println("Insufficient Account Balance.\n ");
            System.out.println("Please enter the amount you want to transfer again:");
            trans = scan.nextDouble();
        }



        transfer.setAmountInAccount(customerDBAmount,accNo,trans*-1);
        transfer.setAmountInAccount(customerDBAmount,transferAcc,trans);

        st.executeUpdate("UPDATE account SET amount="+transfer.getAmountInAccount(customerDBAmount,accNo) +
                " WHERE accountnumber="+accNo);
        st.executeUpdate("UPDATE account SET amount="+transfer.getAmountInAccount(customerDBAmount,transferAcc) +
                " WHERE accountnumber="+transferAcc);

        moneyTransferTransaction(accNo,transferAcc,trans);
        customerMenu(accNo);

    }

    private void moneyTransferTransaction(int accNo, int transferAcc, double trans) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String path = "jdbc:oracle:thin:@localhost:1521/XE";
        Connection con = DriverManager.getConnection(path, "hr", "hr");
        Statement st = con.createStatement();
        Scanner scan=new Scanner(System.in);
        System.out.println("Please write your additional note for this transfer:");
        String note=scan.nextLine();
        String getTransactionNumber="SELECT MAX(trsno) FROM transactions";

        ResultSet rs=st.executeQuery(getTransactionNumber);
        int trnsNo=0;
        while(rs.next()) {

            trnsNo=rs.getInt(1)+1;

        }
        String query="INSERT INTO transactions VALUES("+accNo+","+trnsNo+",'transfer',"+trans+",'to:"+transferAcc+" "+note+"')";
        trnsNo++;
        String query2="INSERT INTO transactions VALUES("+transferAcc+","+trnsNo+",'transfer',"+trans+",'from:"+accNo+" "+note+"')";
        ResultSet rs2=st.executeQuery(query);
        ResultSet rs3=st.executeQuery(query2);
    }
}