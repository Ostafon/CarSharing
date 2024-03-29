package carsharing;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Query {
    public static void createTableCompany() {

        String createTable = "CREATE TABLE IF NOT EXISTS COMPANY(" +
                "ID INT AUTO_INCREMENT," +
                " NAME VARCHAR(20) NOT NULL UNIQUE," +
                " PRIMARY KEY (ID));";
        try {

            DataBase.statement.execute(createTable);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void createTableCar() {
        String createTable = "CREATE TABLE IF NOT EXISTS CAR(" +
                "ID INT AUTO_INCREMENT," +
                " NAME VARCHAR(20) NOT NULL UNIQUE," +
                " COMPANY_ID INT NOT NULL," +
                " FLAG INT DEFAULT 0," +
                " PRIMARY KEY (ID)," +
                " FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID));";
        try {
            DataBase.statement.execute(createTable);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void createTableCustomer() {
        String createTable = "CREATE TABLE IF NOT EXISTS CUSTOMER(" +
                "ID INT AUTO_INCREMENT," +
                " NAME VARCHAR(20) NOT NULL UNIQUE," +
                " RENTED_CAR_ID INT," +
                " PRIMARY KEY (ID)," +
                " FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID));";
        try {
            DataBase.statement.execute(createTable);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void addCompany(String company) throws SQLException {
        String query = "INSERT INTO COMPANY (NAME) VALUES ('"+company+"');";
        DataBase.statement.executeUpdate(query);
    }
    public static void addCar(int companyId, String name) throws SQLException {
        String query = "INSERT INTO CAR (NAME,COMPANY_ID) VALUES ('"+name+"'," + companyId + ");";
        DataBase.statement.executeUpdate(query);
    }
    public static void addCustomer(String name) throws SQLException {
        String query = "INSERT INTO CUSTOMER (NAME) VALUES ('"+name+"');";
        DataBase.statement.executeUpdate(query);
    }
    public static int printCars(int companyId, boolean customer) throws SQLException {
        String query = "SELECT * from CAR WHERE COMPANY_ID="+companyId+" AND FLAG=0;";
        ResultSet res = DataBase.statement.executeQuery(query);
        int cnt = 0,idx=1;
        if(!res.first()) {
            if(customer) System.out.println("No available cars in the company name");
            else System.out.println("The car list is empty!");
            return 0;
        }
        if(customer) {
            System.out.println("Choose car:");
        }
        do {
            if(cnt==0) cnt = res.getInt(1);
            System.out.println(idx + ". " + res.getString(2));
            idx++;
        }while(res.next());
        return cnt;
    }
    public static int printCompanies() throws SQLException {
        String query = "SELECT * from COMPANY";
        ResultSet res = DataBase.statement.executeQuery(query);
        ArrayList<String> companyNames = new ArrayList<>();
        int cnt = 0;
        while(res.next()){
            cnt++;
            companyNames.add(res.getString(2));
        }
        if(cnt==0){
            System.out.println("The company list is empty!");
            return cnt;
        }
        System.out.println("Choose company:");
        for(int i = 0; i < cnt; i++) {
            System.out.println(i+1 + ". " + companyNames.get(i));
        }
        System.out.println("0. Back");
        return cnt;
    }
    public static int printCustomers() throws SQLException {
        String query = "SELECT * from CUSTOMER";
        ResultSet res = DataBase.statement.executeQuery(query);
        ArrayList<String> customerNames = new ArrayList<>();
        int cnt = 0;
        while(res.next()){
            cnt++;
            customerNames.add(res.getString(2));
        }
        if(cnt==0){
            System.out.println("The customer list is empty!");
            return cnt;
        }
        System.out.println("Choose a customer:");
        for(int i = 0; i < cnt; i++) {
            System.out.println(i+1 + ". " + customerNames.get(i));
        }
        System.out.println("0. Back");
        return cnt;
    }
    public static boolean hasCar(int customerId, boolean customer) throws SQLException {
        String query = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID ="  + customerId + " AND RENTED_CAR_ID IS NOT NULL;";
        ResultSet res = DataBase.statement.executeQuery(query);
        int cnt=0;
        while(res.next()) cnt++;
        if(cnt==0) {
            if(!customer)
                System.out.println("You didn't rent a car!");
            return false;
        }
        return true;
    }
    public static void printRentedCars(int customerId) throws SQLException {
        String query = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID ="  + customerId + ";";
        ResultSet res = DataBase.statement.executeQuery(query);
        res.next();

        query = "SELECT NAME,COMPANY_ID FROM CAR WHERE ID=" + res.getInt(1) +";";
        ResultSet result = DataBase.statement.executeQuery(query);
        result.next();
        System.out.println("Your rented car:");
        System.out.println(result.getString(1));
        System.out.println("Company:");
        System.out.println(getName("COMPANY", result.getInt(2)));
    }
    public static void returnRentedCar(int customerId) throws SQLException {
        if(!hasCar(customerId, false)) return;
        String query = "UPDATE CUSTOMER SET RENTED_CAR_ID=NULL WHERE ID="+customerId+";";
        DataBase.statement.executeUpdate(query);
        System.out.println("You've returned a rented car!");
    }
    public static void rentCar(int customerId, int car_id) throws SQLException {
        String query = "UPDATE CUSTOMER SET RENTED_CAR_ID="+car_id+"WHERE ID="+customerId+";";
        DataBase.statement.executeUpdate(query);

        query = "UPDATE CAR SET FLAG=1 WHERE ID="+car_id+";";
        DataBase.statement.executeUpdate(query);

        System.out.println("You rented '" + getName("CAR", car_id) + "'");
    }
    public static String getName(String table,int id) throws SQLException {
        String query1= " WHERE ID=" + id + ";";
        String query2 = "SELECT NAME from " + table + query1;
        ResultSet res=DataBase.statement.executeQuery(query2);
        res.next();
        return res.getString(1);
    }
}