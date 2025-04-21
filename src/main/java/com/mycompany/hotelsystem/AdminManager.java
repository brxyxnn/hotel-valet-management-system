
package com.mycompany.hotelsystem;

import java.util.Scanner;

/**
 *
 * @author elias
 */
public class AdminManager {
    Scanner sc = new Scanner(System.in);
    void generateReports() {
        System.out.print("Report type (Daily/Weekly/Monthly): ");
        System.out.println(sc.nextLine() + " report generated.");
    }
    void manageStaff() {
        System.out.println("Staff management placeholder.");
    }
}
