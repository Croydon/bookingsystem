package bookingsystem;

import java.util.Scanner;
import java.sql.*;
import java.io.IOException;
import java.text.ParseException;

public class Bookingsystem {

    private static final Scanner scanner = new Scanner(System.in);
    private static int usernumber = -1;
    private static Connection conn;

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, ParseException {

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
                        book();
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

        PreparedStatement st = conn.prepareStatement("SELECT flugnummer, datum_abflug, iata_start, iata_ziel, flughafenname FROM " + dbconfig.database + ".abflug NATURAL JOIN " + dbconfig.database + ".flug JOIN " + dbconfig.database + ".flughafen ON iata_ziel = iata WHERE iata_ziel LIKE CONCAT(?, '%') OR flughafenname LIKE CONCAT(?, '%');", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        st.setString(1, destiny);
        st.setString(2, destiny);
        ResultSet rs = st.executeQuery();

        System.out.println("");

        if (!rs.next()) {
            System.out.println("Keine Abflüge gefunden!");
        } else {
            System.out.println(returnStringWithMinLength("Flugnummer") + returnStringWithMinLength("Abflugsdatum") + returnStringWithMinLength("IATA Start") + returnStringWithMinLength("IATA Ziel") + returnStringWithMinLength("Flughafennamen"));
        }
        rs.beforeFirst();

        while (rs.next()) {
            System.out.println(returnStringWithMinLength(rs.getString(1)) + returnStringWithMinLength(rs.getString(2)) + returnStringWithMinLength(rs.getString(3)) + returnStringWithMinLength(rs.getString(4)) + returnStringWithMinLength(rs.getString(5)));
        }
    }

    private static void book() throws SQLException, ParseException {
        System.out.println("Um ein Flug zu buchen, bitte geben Sie die Flugnummer und ein Abflugdatum ein.");
        System.out.println("Flugnummer: ");
        String flightnumber = scanner.nextLine();
        System.out.println("Abflugdatum: ");
        String flightdate = scanner.nextLine();
        System.out.println("");

        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO " + dbconfig.database + ".buchung (kundennummer, flugnummer, datum_abflug, preis) VALUES (?, ?, ?, ?::numeric)");
            st.setInt(1, usernumber);
            st.setString(2, flightnumber);
            st.setDate(3, Date.valueOf(flightdate));
            st.setInt(4, 129);
            if (st.executeUpdate() > 0) {
                System.out.println("Der Flug wurde erfolgreich gebucht!");
            } else {
                System.out.println("Der Flug konnte nicht gebucht werden.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("");
            System.out.println("Der Flug konnte nicht gebucht werden.");
        }
    }

    private static String returnStringWithMinLength(String outputString) {
        int minLenght = 15;

        if (outputString.length() < minLenght) {
            for (int i = outputString.length(); i < minLenght; i++) {
                outputString = outputString + " ";
            }
        }

        return outputString;
    }
}
