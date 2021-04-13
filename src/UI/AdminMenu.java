package UI;

import api.AdminResource;
import model.Customer;
import model.IRoom;
import java.util.ArrayList;
import java.util.List;
import static UI.InputScan.*;
import static UI.MainMenu.*;

public class AdminMenu {

    public static void selectAdminOption(String userEmail) {
        AdminResource adminResource = AdminResource.getInstance();
        boolean userIsAdmin = true;

        //future implementation, check if user is admin

        // get the user Main Menu selection
        System.out.println("-----------------------------------------------");
        System.out.println("Please select one option:");
        System.out.println("""
                1. See all Customers\s
                2. See all Rooms
                3. See all Reservations\s
                4. Add a Room
                5. Back to Main Menu""");

        int userSelection = scanIntegerInput(1, 5);
        System.out.println("User Input: " + userSelection);

        // Give admin access only to certified users
        if (userIsAdmin) {
            System.out.println("User identified as admin, please proceed\n");
        } else {
            System.out.println("User is not an admin, redirecting to Main Menu\n");
            userSelection = 5;
        }

        switch (userSelection) {
            case 1: //get all Customers
                System.out.println("The current customers in database are:");
                for (Customer customer : adminResource.getAllCustomers()) {
                    System.out.println(customer);
                }
                selectAdminOption(userEmail);
                break;
            case 2: // get all Rooms
                System.out.println("The current rooms in database are: ");
                for (IRoom room : adminResource.getAllRooms()) {
                    System.out.println(room);
                }
                selectAdminOption(userEmail);
                break;
            case 3: // get all Reservations
                System.out.println("The current reservations in database are:");
                adminResource.displayAllReservations();
                selectAdminOption(userEmail);
                break;
            case 4: // Add A Room
                adminResource.addRoom(addManyRooms());
                selectAdminOption(userEmail);
                break;
            case 5:
                selectOption(userEmail);

        }
    }

    /**
     * A method that asks the admin user the details of the rooms he wishes to add.
     *
     * @return a list of IRoom objects
     */
    public static List<IRoom> addManyRooms() {
        List<IRoom> allNewRooms = new ArrayList<>();
        boolean addAnotherRoom;

        do {
            //call scanRoomDetails and add the room to the list
            allNewRooms.add(scanRoomDetails());
            //ask the user if he wishes to add another room
            System.out.println("Would you like to add another room?");

            //ask user to add another room and update the addAnotherRoom value
            addAnotherRoom = (scanUserAnswer() == UserAnswer.YES);
        } while (addAnotherRoom);

        return allNewRooms;
    }


}
