
package com.mycompany.hotelsystem;
import java.util.Scanner;

public class CheckOutSystem { // made public
    Scanner sc = new Scanner(System.in);

    public void processCheckOut() {
        System.out.print("Is the room clean and undamaged? (Y/N): ");
        boolean clean = sc.nextLine().equalsIgnoreCase("Y");

        System.out.print("Was the keycard returned? (Y/N): ");
        boolean cardReturned = sc.nextLine().equalsIgnoreCase("Y");

        System.out.print("Is there a late checkout request? (Y/N): ");
        boolean lateCheckout = sc.nextLine().equalsIgnoreCase("Y");

        if (clean && cardReturned && !lateCheckout) {
            System.out.println("Checkout complete. Thank you!");
        } else {
            System.out.println("Checkout requires further review.");
        }
    }
}
