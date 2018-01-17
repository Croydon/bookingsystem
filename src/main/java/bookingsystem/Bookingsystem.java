package bookingsystem;

import java.util.Scanner;

public class Bookingsystem {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int option;
        while (true) {
            // Prints the menu and allows user to choose
            System.out.println("");
            System.out.println("1. Kundennummer eingeben");
            System.out.println("2. Flugziel angeben");
            System.out.println("3. Flug buchen");
            System.out.println("0: Beenden");

            System.out.println("");
            System.out.println("Menüpunkt eingeben: ");
            option = scanner.nextInt();
            System.out.println("");

            switch (option) {
                case 0:
                    System.exit(0);

                default:
                    System.out.println("Ungültige Eingabe!");
            }

        }
    }
}
