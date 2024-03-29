package carsharing;

import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    static Scanner scanner = new Scanner(System.in);
    public static void mainMenu() throws SQLException {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");

        String ans = scanner.next();

        if(ans.equals("1")) {
            Choose();
        }
        if(ans.equals("2")) {
            customerChoose();
        }
        if(ans.equals("3")) {
            createCustomer();
        }
        System.exit(0);
    }
    public static void Choose() throws SQLException {
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
        String answer = scanner.next();
        if(answer.equals("1")) {
            if(Query.printCompanies() != 0) {
                int companyid = scanner.nextInt();
                if(companyid == 0) {
                    Choose();
                }
                CompanyChoose(companyid);

            }
            Choose();
        }
        else if(answer.equals("2")) {
            System.out.println("Enter the company name:");
            scanner.nextLine();
            String company = scanner.nextLine();
            Query.addCompany(company);
            Choose();
        }
        else {
            mainMenu();
        }
    }
    public static void CompanyChoose(int companyid) throws SQLException {
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
        int ans = scanner.nextInt();
        if(ans==1) {
            Query.printCars(companyid, false);
            CompanyChoose(companyid);
        }
        if(ans==2) {
            System.out.println("Enter the car name:");
            scanner.nextLine();
            String carName = scanner.nextLine();
            Query.addCar(companyid,carName);
            CompanyChoose(companyid);
        }
        Choose();

    }
    public static void customerChoose() throws SQLException {
        if(Query.printCustomers()==0) {
            mainMenu();
        }
        int customerId = scanner.nextInt();
        if(customerId==0) mainMenu();
        customerMenu(customerId);
    }
    public static void createCustomer() throws SQLException {
        System.out.println("Enter the customer name:");
        scanner.nextLine();
        String name = scanner.nextLine();
        Query.addCustomer(name);
        mainMenu();
    }
    public static void customerMenu(int customerId) throws SQLException {
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
        int ans = scanner.nextInt();

        if(ans == 1) {
            if(Query.hasCar(customerId, true)) {
                System.out.println("You've already rented a car!");
            }
            else {
                if (Query.printCompanies() != 0) {
                    int companyid = scanner.nextInt();
                    int start_id = Query.printCars(companyid, true);
                    if (start_id != 0) {
                        int car_id = scanner.nextInt();
                        Query.rentCar(customerId, car_id + start_id - 1);
                    }
                }
            }

        }
        if(ans == 2) {
            Query.returnRentedCar(customerId);
        }
        if(ans == 3) {
            if(Query.hasCar(customerId,false)) {

                Query.printRentedCars(customerId);
            }
        }
        if(ans==0) customerChoose();

        customerMenu(customerId);
    }

}