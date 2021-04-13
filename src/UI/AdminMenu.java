package UI;

import api.AdminResource;
import api.HotelResource;

import static UI.InputScan.scanIntegerInput;
import static UI.MainMenu.*;

public class AdminMenu {

    public static void selectAdminOption(String userEmail){
        HotelResource hotelResource = HotelResource.getInstance();
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
        if(userIsAdmin){
            System.out.println("User identified as admin, please proceed");
        }else {
            System.out.println("User is not an admin, redirecting to Main Menu");
            userSelection = 5;
        }

        switch (userSelection) {
            case 1: //get all Customers


                break;
            case 2: // get all Rooms

                break;
            case 3: // get all Reservations

                break;
            case 4: // Add A Room

                break;
            case 5:
                selectOption(userEmail);
        }




    }



}
