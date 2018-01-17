package bookingsystem;

import java.util.Scanner;

public class Bookingsystem {

    private static Scanner scanner = new Scanner(System.in);
    private static int usernumber = -1;
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
            scanner.nextLine();
            System.out.println("");

            switch (option) {
                case 1:
                    enterUsernumber();
                    break;

                case 2:
                    if (checkLogin() == true) {
                        listPossibleFlights();
                    }
                    break;

                case 3:
                    if (checkLogin() == true) {

                    }
                    break;

                case 0:
                    System.exit(0);

                default:
                    System.out.println("Ungültige Eingabe!");
                    break;
            }

        }
    }

    private static boolean checkLogin() {
        if (usernumber == -1) {
            System.out.println("Sie müssen zuerst eine Kundennummer angeben!");
            return false;
        }

        return true;
    }

    private static void enterUsernumber() {
        System.out.println("Geben Sie eine Kundennummer ein: ");
        int tmpUsernumber = scanner.nextInt();
        scanner.nextLine();

        if (true) // TODO: SQL STATEMENT IF EXISTS
        {

        } else {
            System.out.println("Die Kundennummer existiert nicht!");
            System.out.println("Sie können einen neuen Benutzer anlegen.");
            System.out.println("Bitte geben Sie einen Vornamen an:");
            String vorname = scanner.nextLine();
            System.out.println("Bitte geben Sie einen Nachnamen an:");
            String nachname = scanner.nextLine();

            // TODO: SQL STATEMENT TO CREATE A NEW USER
            // tmpUsernumber = nextnumber;
            System.out.println("Der neue Benutzer wurde erfolgreich registriert!");
        }

        usernumber = tmpUsernumber;
    }

    private static void listPossibleFlights() {
        System.out.println("Bitte geben Sie ein Ziel ein. Entweder als IATA Code oder als Name:");
        String destiny = scanner.nextLine();

        // TODO: Implement listing logic
    }

    private static void book() {
        System.out.println("Um ein Flug zu buchen, bitte geben Sie die Flugnummer und ein Abflugdatum ein.");
        System.out.println("Flugnummer: ");
        String flightnumber = scanner.nextLine();
        System.out.println("Abflugdatum: ");
        String flightdate = scanner.nextLine();

        // TODO: SQL STAMTE FOR BOOKING
    }
}
