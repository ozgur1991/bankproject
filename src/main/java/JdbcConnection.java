import java.sql.*;

public class JdbcConnection {

    private String userName="root";
    private String password="";

    private String db_name="demo";
    private  String host="localhost";
    private  int port=3306;

    private Connection con=null;
    private Statement statement=null;
    private  PreparedStatement preparedStatement=null;

    public  void addEmployee(){

        String name="Haluk";
        String surName="Bilgic";
        String email="ahbilgic1994@gmail.com";
        String query="Insert Into employees (name,surName,email) VALUES (?,?,?)";

        try {
            preparedStatement=con.prepareStatement(query);

            preparedStatement.setString(1,name);
            preparedStatement.setString(2,surName);
            preparedStatement.setString(3,email);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        /* --IT IS USED LIKE ALTERNATIVE CODE--

        try {
        statement= con.createStatement();
        String name="Haluk";
        String surName="Bilgic";
        String email="ahbilgic1994@gamil.com";
        String query="Insert Into employees (name,surName,email) VALUES ("+ "'"+name+"',"+"'"+surName+"',"+"'"+email+"')";
        statement.executeUpdate(query);
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }*/
    }
    public void updateEmployee(){

        try {
            statement=con.createStatement();
            String query="Update employees set email='other@gmail.com' where name='haluk'";
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public  void deleteEmployee(){

        try {
            statement=con.createStatement();
            String query="delete from employees where surName='Bilgic' ";
            int number=statement.executeUpdate(query);
            System.out.println("Number of the employee deleted:"+number);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void getEmployees(){

        String query="Select * From employees ";

        try {
            statement=con.createStatement();
            ResultSet rs=statement.executeQuery(query);

            while (rs.next()){
                int id=rs.getInt("id");
                String name=rs.getString("name");
                String surName=rs.getString("surName");
                String email=rs.getString("email");

                System.out.println("Id:"+id+" Name:"+name+" Surname:"+surName+" Email:"+email);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public JdbcConnection(){

        String url="jdbc:mysql://"+host+":"+port+"/"+db_name;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Not Found Driver");
        }

        try {
            con= DriverManager.getConnection(url,userName,password);
            System.out.println("Connection Successful");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Connection Unsuccessful");
        }
    }
    public static void main(String[] args) {

        JdbcConnection dataBase=new JdbcConnection();

        System.out.println("Before to insert an Employee");
        dataBase.getEmployees();
        dataBase.addEmployee();
        System.out.println("After to insert an Employee");
        dataBase.getEmployees();

        dataBase.updateEmployee();
        System.out.println("After to update an Employee");
        dataBase.getEmployees();
        dataBase.deleteEmployee();
        System.out.println("After to delete an Employee");
        dataBase.getEmployees();
    }

}