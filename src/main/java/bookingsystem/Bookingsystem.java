package bookingsystem;

import java.util.Scanner;
import java.sql.*;
import java.io.IOException;

public class Bookingsystem {

    private static final Scanner scanner = new Scanner(System.in);
    private static int usernumber = -1;
    private static Connection conn;

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

        // Check if config file exists, if not create it by taken over the example config
        // This is required so that the CI isn't failing
        // Also nice for easy setup
        // if (Files.exists(Paths.get("src/main/java/bookingsystem/dbconfig.java")) == false) {
        //    Files.copy(Paths.get("src/main/java/bookingsystem/dbconfig.java.example"), Paths.get("src/main/java/bookingsystem/dbconfig.java"), COPY_ATTRIBUTES);
        // }

        // Load the JDBC Driver and establish a DB connection
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:" + dbconfig.typ + "://" + dbconfig.domain + ":" + dbconfig.port + "/" + dbconfig.database + "?user=" + dbconfig.user + "&password=" + dbconfig.password + "&ssl=" + dbconfig.ssl + "");

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

    private static void enterUsernumber() throws SQLException {
        System.out.println("Geben Sie eine Kundennummer ein: ");
        int tmpUsernumber = scanner.nextInt();
        scanner.nextLine();

        PreparedStatement st = conn.prepareStatement("SELECT kundennummer FROM " + dbconfig.database + ".passagier WHERE kundennummer = ?");
        st.setInt(1, tmpUsernumber);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            usernumber = tmpUsernumber;
        } else {
            rs.close();
            st.close();

            System.out.println("Die Kundennummer existiert nicht!");
            System.out.println("Sie können einen neuen Benutzer anlegen.");
            System.out.println("Bitte geben Sie einen Vornamen an:");
            String vorname = scanner.nextLine();
            System.out.println("Bitte geben Sie einen Nachnamen an:");
            String nachname = scanner.nextLine();

            st = conn.prepareStatement("INSERT INTO " + dbconfig.database + ".passagier (vorname, nachname, kundennummer, bonusmeilenkonto) VALUES (?, ?, ?, 0)");
            st.setString(1, vorname);
            st.setString(2, nachname);
            st.setInt(3, tmpUsernumber);
            if (st.executeUpdate() > 0) {
                System.out.println("Der neue Benutzer wurde erfolgreich registriert!");
                usernumber = tmpUsernumber;
            } else {
                System.out.println("Der Benuter könnte nicht erfolgreich registriert werden. Ist die Benutzer-ID bereits vorhanden?");
                usernumber = -1;
            }
        }

        rs.close();
        st.close();
    }

    private static void listPossibleFlights() throws SQLException {
        System.out.println("Bitte geben Sie ein Ziel ein. Entweder als IATA Code oder als Name:");
        String destiny = scanner.nextLine();

        PreparedStatement st = conn.prepareStatement("SELECT kundennummer FROM " + dbconfig.database + ".passagier WHERE kundennummer = ?");
        st.setString(1, destiny);
        st.setString(2, destiny);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {

        }
    }

    private static void book() {
        System.out.println("Um ein Flug zu buchen, bitte geben Sie die Flugnummer und ein Abflugdatum ein.");
        System.out.println("Flugnummer: ");
        String flightnumber = scanner.nextLine();
        System.out.println("Abflugdatum: ");
        String flightdate = scanner.nextLine();

        // TODO: SQL STATMENT FOR BOOKING
    }
}
