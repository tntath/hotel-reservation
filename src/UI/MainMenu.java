package UI;

import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) {

        System.out.println("Hello and welcome to the best exotic Marigold hotel!\n");
        System.out.println("Please select one option:");
        System.out.println("""
                1. Find and reserve a room\s
                2. See my reservations
                3. Create an account\s
                4. Admin
                5.Exit""");

        int userInput = scanIntegerInput();
        System.out.println("User Input: " + userInput);





    }

    /**
     * A method that uses the Scanner class to get an integer input
     * from the user. Handles exceptions of invalid input or out of limits input.
     * @return the integer retrieved from the input
     */
    public static int scanIntegerInput(){
        int userInput = 0;
        boolean wrongInput;
        Scanner scanner = new Scanner(System.in);

        do {
            try {
                userInput = scanner.nextInt();
                if (userInput >= 1 && userInput <= 5) {
                    wrongInput = false;
                } else {
                    wrongInput = true;
                    System.out.println("Please enter a number between 1 and 5.");
                }
            } catch (Exception ex) {
                ex.getLocalizedMessage();
                System.out.println("Please enter a valid number");
                wrongInput = true;
                scanner.nextLine();
            }
        } while (wrongInput);

        scanner.close();

        return userInput;

    }

}
