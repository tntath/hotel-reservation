package UI;

import java.util.Scanner;
import static UI.MainMenu.*;

public class HotelApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //boolean programRunning = true;

        System.out.println("Hello and welcome to the best exotic Marigold hotel!\n");
        System.out.println("Please enter your email");
        String customerEmail = scanner.nextLine();
        selectOption(customerEmail);


    }
}
