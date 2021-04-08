package UI;

import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) {
        boolean wrongInput = false;
        int userInput = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello and welcome to the best exotic Marigold hotel!\n");
        System.out.println("Please select one option:");
        System.out.println("1. Find and reserve a room \n" +
                "2. See my reservations\n" +
                "3. Create an account \n" +
                "4. Admin\n" +
                "5.Exit");

        do {
            try {
                userInput = scanner.nextInt();
                if (userInput >= 1 && userInput <= 5) {
                    System.out.println("User Input: " + userInput);
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

        switch(userInput) {
            case 1:

        }


    }

}
