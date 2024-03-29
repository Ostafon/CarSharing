package carsharing;

public class Main {

    public static void main(String[] args) {
        try{
            DataBase.createConnection();
            Query.createTableCompany();
            Query.createTableCar();
            Query.createTableCustomer();
            Menu.mainMenu();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}