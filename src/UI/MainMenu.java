package UI;

import api.HotelResource;
import model.IRoom;
import model.Reservation;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static UI.InputScan.*;
import static UI.AdminMenu.*;

public class MainMenu {


    /**
     * A method that creates the  Main Menu of the UI. It checks if a customer exists in the
     * database and manages the different actions they can complete.
     *
     * @param customerEmail the email of the current customer using the Application
     */
    public static void selectOption(String customerEmail) {
        HotelResource hotelResource = HotelResource.getInstance();
        boolean customerExists = false;

        //check if customer exists in database
        if (hotelResource.getCustomer(customerEmail) != null) {
            customerExists = true;
        }
        // get the user Main Menu selection
        System.out.println("\n-----------------------------------------------");
        System.out.println("Please select one option:");
        System.out.println("""
                1. Find and reserve a room\s
                2. See my reservations
                3. Create an account\s
                4. Admin
                5.Exit""");

        int userSelection = scanIntegerInput(1, 5);
        System.out.println("User Input: " + userSelection);
        // Switch expression for the 5 different option a user can select
        switch (userSelection) {
            case 1:
                if (customerExists) {
                    findAndReserveARoom(hotelResource, customerEmail);
                } else {
                    System.out.println("Please create an account first\n");
                }
                selectOption(customerEmail);
                break;
            case 2:
                if (customerExists) {
                    Collection<Reservation> allCustomerReservations = hotelResource.getCustomersReservations(customerEmail);
                    if (allCustomerReservations.isEmpty()) {
                        System.out.println("There are no current reservations");
                    } else {
                        System.out.println("Your reservations are:");
                        for (Reservation reservation : allCustomerReservations) {
                            System.out.println(reservation);
                        }
                    }
                } else {
                    System.out.println("Please create an account first\n");
                }
                selectOption(customerEmail);
                break;
            case 3:
                    //Ask the user for his details (email, First Name, Last Name)
                    String[] newUserDetails = scanCustomerDetails();
                    //Create a new Customer object if the customer does not already exist in database
                    try {
                        hotelResource.createACustomer(newUserDetails[0], newUserDetails[1], newUserDetails[2]);
                    }catch (InstanceAlreadyExistsException ex) {
                            System.out.println("User's email already in database. Please select another option.");
                    }catch (IllegalArgumentException ex){
                            System.out.println("Please enter a valid email address");
                    }
                    //redirect the user to select an option from Main Menu
                    //(the new email is forwarded to the Main Menu with the variable newUserDetails[0])
                    selectOption(newUserDetails[0]);
                break;
            case 4:
                //open the admin menu
                selectAdminOption(customerEmail);
                break;
            case 5:
                //exit program
                System.exit(0);
                break;
            default:
                System.out.println("Default option");
        }
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
        LocalDate checkInDate = desiredDates.get(0);
        LocalDate checkOutDate = desiredDates.get(1);
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
                selectOption(customerEmail);
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
                    selectOption(customerEmail);
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
     * @param availableRooms a list with the available rooms for a specific reservation
     */
    private static void makeAReservation(HotelResource hotelResource, String customerEmail, LocalDate checkInDate, LocalDate checkOutDate, Collection<IRoom> availableRooms) {
        boolean wrongRoomSelection=false;

        // show the available rooms
        System.out.println("The available rooms for the requested dates are: ");
        for (IRoom room : availableRooms) {
            System.out.println(room);
        }
        //get user confirmation for these dates or redirect  him to choose new dates
        System.out.println("Should we proceed with this reservation? (Yes/No answer)");
        UserAnswer reservationConfirmation = scanUserAnswer();
        if (reservationConfirmation == UserAnswer.YES) {
            //Get selected room from user
            System.out.println("Please select a room number");
            String roomNumberSelected = scanRoomNumber();
            IRoom selectedRoom = hotelResource.getRoom(roomNumberSelected);
            for(IRoom room:availableRooms) {
                if(room.equals(selectedRoom)){
                    Reservation customerReservation = hotelResource.bookARoom(customerEmail, selectedRoom, checkInDate, checkOutDate);
                    System.out.println("Your reservation is: " + customerReservation);
                    wrongRoomSelection = false;
                    break;
                }else {
                    wrongRoomSelection = true;
                }
            }
            if(wrongRoomSelection){
                System.out.println("This room is not available. Please select another room");
                makeAReservation(hotelResource,customerEmail, checkInDate, checkOutDate, availableRooms);
            }
            //Book a reservation for the selected room and dates
        } else {
            System.out.println("Please select new Check In and Check Out dates.");
            findAndReserveARoom(hotelResource, customerEmail);
        }
    }


}
