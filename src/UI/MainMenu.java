package UI;

import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainMenu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HotelResource hotelResource = HotelResource.getInstance();
        AdminResource adminResource = AdminResource.getInstance();
        //boolean programRunning = true;

        System.out.println("Hello and welcome to the best exotic Marigold hotel!\n");
        System.out.println("Please enter your email");
        String customerEmail = scanner.nextLine();
        selectOption(customerEmail, hotelResource, adminResource);


    }

    public static void selectOption(String customerEmail, HotelResource hotelResource, AdminResource adminResource) {
        boolean customerExists;

        //check if customer exists in database
        if (hotelResource.getCustomer(customerEmail) != null) {
            customerExists = true;
        } else {
            customerExists = false;
        }

        // get the user Main Menu selection

        int userSelection = scanIntegerInput(1, 5);
        System.out.println("User Input: " + userSelection);


        switch (userSelection) {
            case 1:
                if (customerExists) {
                    findAndReserveARoom(hotelResource, customerEmail);
                } else {
                    System.out.println("Please create an account first\n");
                    selectOption(customerEmail, hotelResource, adminResource);
                }
                break;
            case 2:
                if (customerExists) {
                    //add code
                } else {
                    System.out.println("Please create an account first\n");
                    selectOption(customerEmail, hotelResource, adminResource);
                }
                break;
            case 3:
                if (customerExists) {
                    System.out.println("Email already in use, please select another option");
                    selectOption(customerEmail, hotelResource, adminResource);
                } else {
                    //ask for the customer details and create an entry in the database
                    String[] newUserDetails = scanCustomerDetails();
                    hotelResource.createACustomer(newUserDetails[0], newUserDetails[1], newUserDetails[2]);

                    //redirect the user to select an option from Main Menu
                    selectOption(newUserDetails[0], hotelResource, adminResource);
                }
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
        System.out.println("-----------------------------------------------");
        System.out.println("Please select one option:");
        System.out.println("""
                1. Find and reserve a room\s
                2. See my reservations
                3. Create an account\s
                4. Admin
                5.Exit""");

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

    /**
     * A method that finds a room for selected dates and proceeds to reserve them if the user decides
     *
     * @param hotelResource the hotelResource instance of the API
     * @param customerEmail the email of the customer that makes the reservation
     */
    public static void findAndReserveARoom(HotelResource hotelResource, String customerEmail) {
        String datePattern = "dd-MM-yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);

        //get the check in date and check out date from the user
        List<LocalDate> desiredDates = scanCheckInDates(formatter, datePattern);
        LocalDate checkInDate = desiredDates.iterator().next();
        LocalDate checkOutDate = desiredDates.iterator().next();

        //get a list with all the available rooms for these dates
        Collection<IRoom> availableRooms = hotelResource.findARoom(checkInDate, checkOutDate);

        //Show the available rooms to the user and proceed with the reservation
        if (availableRooms.isEmpty()) {
            System.out.println("There are no available rooms for these dates");
            //add seven days to the search
            checkInDate = checkInDate.plusDays(7);
            checkOutDate = checkOutDate.plusDays(7);
            // find a Room for the new dates
            Collection<IRoom> availableRoomsWeekLater = hotelResource.findARoom(checkInDate, checkOutDate);
            //restart the process if no available rooms are found or show the available ones for the new search
            if (availableRoomsWeekLater.isEmpty()) {
                System.out.println("And unfortunately there are no available rooms a week later, please choose other dates");
                findAndReserveARoom(hotelResource, customerEmail);
            } else {
                System.out.println("However, there are available rooms a week later");
                System.out.println("New Check in Date: " + checkInDate.format(formatter));
                System.out.println("New Check Out Date: " + checkOutDate.format(formatter));
                System.out.println("Should we proceed with these dates? (Yes/No answer)");
                UserAnswer dateConfirmation = scanUserAnswer();
                if (dateConfirmation == UserAnswer.YES) {
                    makeAReservation(hotelResource, customerEmail, checkInDate, checkOutDate, availableRoomsWeekLater);

                } else if (dateConfirmation == UserAnswer.NO) {
                    System.out.println("Please select new Check In and Check Out dates.");
                    findAndReserveARoom(hotelResource, customerEmail);
                }
            }
        } else {
            makeAReservation(hotelResource, customerEmail, checkInDate, checkOutDate, availableRooms);
        }
    }

    /**
     * Confirms a reservation with the user and adds the entry in the database.
     *
     * @param hotelResource  the Hotel Resource instance of the API
     * @param customerEmail  the current customer email that requests the reservation
     * @param checkInDate    the check in date
     * @param checkOutDate   the check out date
     * @param availableRooms a li
     */
    private static void makeAReservation(HotelResource hotelResource, String customerEmail, LocalDate checkInDate, LocalDate checkOutDate, Collection<IRoom> availableRooms) {
        System.out.println("The available rooms for the requested dates are: ");

        for (IRoom room : availableRooms) {
            System.out.println(room);
        }
        System.out.println("Should we proceed with these dates? (Yes/No answer)");
        UserAnswer reservationConfirmation = scanUserAnswer();

        if (reservationConfirmation == UserAnswer.YES) {
            //Get selected room from user
            System.out.println("Please select a room number");
            String roomNumberSelected = scanRoomNumber();
            //Book a reservation for the selected room and dates
            IRoom roomSelected = hotelResource.getRoom(roomNumberSelected);
            Reservation customerReservation = hotelResource.bookARoom(customerEmail, roomSelected, checkInDate, checkOutDate);
            System.out.println("Your reservation is: " + customerReservation);
        } else {
            System.out.println("Please select new Check In and Check Out dates.");
            findAndReserveARoom(hotelResource, customerEmail);
        }
    }


    /**
     * A method that gets the Check In Dates and Checkout Dates for a reservation
     *
     * @return a List of two LocalDate objects (the chek in date and the check out date)
     */
    public static List<LocalDate> scanCheckInDates(DateTimeFormatter formatter, String datePattern) {
        List<LocalDate> desiredDates = new ArrayList<>();

        System.out.println("Please enter the check in date in \"" + datePattern + "\" format");
        LocalDate checkInDate = scanDate(formatter, datePattern);
        System.out.println("Check In: " + checkInDate.format(formatter));

        System.out.println("Please enter the check out date in \"" + datePattern + "\" format");
        LocalDate checkOutDate = scanDate(formatter, datePattern);
        System.out.println("Check Out: " + checkOutDate.format(formatter));

        desiredDates.add(checkInDate);
        desiredDates.add(checkOutDate);

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

    private static String scanRoomNumber() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();

    }

}
