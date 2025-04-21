/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hotelsystem;

/**
 *
 * @author elias
 */
import java.util.*;
public class CheckInSystem {
    Scanner sc = new Scanner(System.in);
    void processCheckIn(){
        System.out.print("Enter Guest ID: ");
        int guestID = Integer.parseInt(sc.nextLine());
        System.out.print("Payment verified? (Y/N): ");
        boolean paid = sc.nextLine().equalsIgnoreCase("Y");
        if(paid){
            System.out.print("Enter Room Assignment: ");
            String room = sc.nextLine();
            System.out.print("Early Check-In? (Y/N): ");
            boolean early = sc.nextLine().equalsIgnoreCase("Y");
            System.out.println("Check-In complete for Room "+ room + (early ? "(Early)" : ""));
        } else {
            System.out.println("Check-in denied. Payment required.");
        }
    }
}
