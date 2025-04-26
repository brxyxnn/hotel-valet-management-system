package BoundaryClasses;

import ControlClass.AdminControl;
import java.util.Scanner;

public class AdminUI {
    public static void login(Scanner scanner) {
        System.out.println("\nADMIN LOGIN");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        if (AdminControl.authenticateAdmin(username, password)) {
            System.out.println("\nGenerating daily report...");
            AdminControl.generateSimpleReport();
            System.out.println("Logging out...");
        } else {
            System.out.println("Invalid credentials.");
        }
    }
}