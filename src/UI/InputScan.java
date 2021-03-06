package UI;

import model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputScan {

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
                    scanner.nextLine();
                }
            } catch (Exception ex) {
                ex.getLocalizedMessage();
                System.out.println("Please enter an integer number");
                wrongInput = true;
                scanner.nextLine();
            }
        } while (wrongInput);

        return userInput;

    }

    /**
     * A method that gets the Check In Dates and Checkout Dates for a reservation
     *
     * @return a List of two LocalDate objects (the chek in date and the check out date)
     */
    public static List<LocalDate> scanCheckInDates(DateTimeFormatter formatter, String datePattern) {
        List<LocalDate> desiredDates = new ArrayList<>();
        LocalDate todayDate = LocalDate.now();

        System.out.println("Please enter the check in date in \"" + datePattern + "\" format");
        LocalDate checkInDate = scanDate(formatter, datePattern);
        System.out.println("Check In: " + checkInDate.format(formatter));

        System.out.println("Please enter the check out date in \"" + datePattern + "\" format");
        LocalDate checkOutDate = scanDate(formatter, datePattern);
        System.out.println("Check Out: " + checkOutDate.format(formatter));

        if (checkInDate.isBefore(todayDate)) {
            System.out.println("The check in date should not be in the past. Please re-enter the dates.");
            desiredDates = scanCheckInDates(formatter, datePattern);
        } else if (checkOutDate.isBefore(checkInDate)) {
            System.out.println("The check out date  should be after the check in date. Please re-enter the dates");
            desiredDates = scanCheckInDates(formatter, datePattern);
        } else {
            desiredDates.add(checkInDate);
            desiredDates.add(checkOutDate);
        }
        return desiredDates;
    }

    /**
     * Uses the scanner to get a date input from the user.
     *
     * @param datePattern A string with the pattern of the date
     * @param formatter   the formatter of the date used
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

    /**
     * Uses the scanner to convert the user input to the Enum UserAnswer class
     *
     * @return the converted user answer to the Enum UserAnswer class
     */
    public static UserAnswer scanUserAnswer() {
        Scanner scanner = new Scanner(System.in);
        UserAnswer userAnswer = null;
        boolean wrongInput;

        do {
            try {
                String answer = scanner.nextLine().toUpperCase();
                userAnswer = UserAnswer.valueOf(answer);
                wrongInput = false;
            } catch (Exception ex) {
                ex.getLocalizedMessage();
                System.out.println("Please enter the correct answer format (Yes or No)");
                wrongInput = true;
            }
        } while (wrongInput);

        return userAnswer;

    }

    /**
     * Scans the User's Details (email, First Name, Last Name)
     *
     * @return An array of strings containing the email, the first name and last name of the user,
     * in this order (email, firstname, lastname)
     */
    public static String[] scanCustomerDetails() {
        Scanner scanner = new Scanner(System.in);
        String userEmail = null;
        String firstName = null;
        String lastName = null;

        System.out.println("Please enter your email: ");
        userEmail = scanner.nextLine();
        System.out.println("Please enter your First Name");
        firstName = scanner.nextLine();
        System.out.println("Please enter your Last Name");
        lastName = scanner.nextLine();

        System.out.println("Your Details are:");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + userEmail);

        return new String[]{userEmail, firstName, lastName};

    }

    /**
     * Scans the next String (which should be the room number of a room
     *
     * @return a Strinng containing the room number of a room
     */
    public static String scanRoomNumber() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();

    }

    /**
     * Asks the admin user provide details of a new Room scans the user input.
     *
     * @return An IRoom object (either Room or FreeRoom, depending on the user's input.
     */
    public static IRoom scanRoomDetails() {
        Scanner scanner = new Scanner(System.in);
        IRoom newRoom = null;
        String roomNumber = null;
        Double roomPrice = null;
        RoomType roomType = null;
        boolean wrongInput;
        do {
            try {
                System.out.println("Please insert the Room number");
                roomNumber = scanner.nextLine();
                System.out.println("Please insert the room price, or enter 0 if it is a Free room");
                roomPrice = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Please insert the room type (Single or Double)");
                String typeAnswer = scanner.nextLine().toUpperCase();
                roomType = RoomType.valueOf(typeAnswer);
                wrongInput = false;
            } catch (Exception ex) {
                ex.getLocalizedMessage();
                System.out.println("Please enter the correct input types (Room number -> A string, " +
                        "Room price -> A number, Room type-> Either Single or Double");
                wrongInput = true;
            }
        } while (wrongInput);

        if (roomPrice > 0) {
            newRoom = new Room(roomNumber, roomPrice, roomType);
        } else {
            newRoom = new FreeRoom(roomNumber, roomType);
        }
        return newRoom;
    }

    /**
     * A method that converts the user input to RoomCost Enum
     * @return the user's answer regarding the preferred cost in the RoomCost Enum type.
     */
    public static RoomCost scanRoomPreferredCost(){
        Scanner scanner = new Scanner(System.in);
        RoomCost roomCost = null;

        boolean wrongInput;

        do {
            try {
                String answer = scanner.nextLine().toUpperCase();
                roomCost = RoomCost.valueOf(answer);
                wrongInput = false;
            } catch (Exception ex) {
                ex.getLocalizedMessage();
                System.out.println("Please enter the correct answer format (Free, Paid, or Both)");
                wrongInput = true;
            }
        } while (wrongInput);

        return roomCost;

    }

    /**
     * Scans the input and gets an integer from the user.
     * @return The integer provided by the user.
     */
    public static int scanAdditionalDays(){
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        boolean wrongInput;

        do {
            try {
                userInput = scanner.nextInt();
                wrongInput = false;
            } catch (Exception ex) {
                ex.getLocalizedMessage();
                System.out.println("Please enter an integer number");
                wrongInput = true;
                scanner.nextLine();
            }
        } while (wrongInput);

        return userInput;
    }
}
