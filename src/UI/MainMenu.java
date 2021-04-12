package UI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello and welcome to the best exotic Marigold hotel!\n");
        System.out.println("Please select one option:");
        System.out.println("""
                1. Find and reserve a room\s
                2. See my reservations
                3. Create an account\s
                4. Admin
                5.Exit""");

        int userInput = scanIntegerInput(1, 5);
        System.out.println("User Input: " + userInput);

        switch (userInput) {
            case 1:
                findAndReserveARoom();
            case 2:
                System.out.println("Case 2");
                break;
            case 3:
                System.out.println("Case 3");
                break;
            case 4:
                System.out.println("Case 4");
                break;
            case 5:
                System.out.println("Case 5");
                break;
            default:
                System.out.println("Ending program");
        }


    }


    /**
     * A method that uses the Scanner class to get an integer input
     * from the user. The integer must be withing a lower and an upper limit.
     * Handles exceptions of invalid input or out of limits input.
     *
     * @param lowerLimit the lower limit of the integer input
     * @param upperLimit the upper limit of the integer input
     * @return the integer retrieved from the input
     */
    public static int scanIntegerInput(int lowerLimit, int upperLimit) {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        boolean wrongInput;

        do {
            try {
                userInput = scanner.nextInt();
                if (userInput >= lowerLimit && userInput <= upperLimit) {
                    wrongInput = false;
                } else {
                    wrongInput = true;
                    System.out.println("Please enter a number between " + lowerLimit + " and " + upperLimit);
                }
            } catch (Exception ex) {
                ex.getLocalizedMessage();
                System.out.println("Please enter an integer number");
                wrongInput = true;
                String date = scanner.nextLine();
            }
        } while (wrongInput);

        return userInput;

    }

    public static void findAndReserveARoom() {
        String datePattern = "dd-MM-yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);

        System.out.println("Please enter the check in date in \"" + datePattern + "\" format");
        LocalDate checkInDate = scanDate(formatter, datePattern);
        System.out.println("Check In: " + checkInDate.format(formatter));

        System.out.println("Please enter the check out date in \"" + datePattern + "\" format");
        LocalDate checkOutDate = scanDate(formatter, datePattern);
        System.out.println("Check Out: " + checkOutDate.format(formatter));


    }

    /**
     * Uses the scanner to get a date input from the user.
     * @param datePattern A string with the pattern of the date
     * @param formatter the formatter of the date used
     * @return a LocalDate object obtained from the user.
     */
    public static LocalDate scanDate(DateTimeFormatter formatter, String datePattern) {
        Scanner scanner = new Scanner(System.in);
        LocalDate inputDate = LocalDate.now();
        boolean wrongInput;
        do {
            try {
                String date = scanner.nextLine();
                inputDate = LocalDate.parse(date, formatter);
                wrongInput = false;
            } catch (Exception ex) {
                ex.getLocalizedMessage();
                System.out.println("Please enter the correct date format \"" + datePattern + "\"");
                wrongInput = true;
            }
        } while (wrongInput);

        return inputDate;
    }

}
