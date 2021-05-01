package service;

import model.*;

import java.util.*;
import java.time.LocalDate;

public final class ReservationService {

    private Map<String, IRoom> mapOfRooms = new HashMap<>();
    private List<Reservation> listOfReservations = new ArrayList<Reservation>();
    private static final ReservationService reservationService = new ReservationService();

    public void addRoom(IRoom room) {
        mapOfRooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {

        return mapOfRooms.get(roomId);
    }

    public Collection<IRoom> getAllRooms(){
        return mapOfRooms.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room, LocalDate checkInDate, LocalDate checkOutDate) {
        Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);

        listOfReservations.add(newReservation);
        return newReservation;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        List<Reservation> customerReservations = new ArrayList<>();

        for (Reservation reservation : listOfReservations) {
            if (reservation.getCustomer().getEmail().equals(customer.getEmail())) {
                customerReservations.add(reservation);
            }
        }

        return customerReservations;

    }

    /**
     * A method that creates a Linked List and populates it with all the available rooms.
     * Then checks the reservation list for any rooms already booked for the desired dates and removes them
     * from the list. Finally, it returns the list with all the available rooms for the selected dates.
     *
     * @param checkInDate  the date for checking in
     * @param checkOutDate the date for checking out
     * @return a list of Rooms available for the selected dates
     */
    public Collection<IRoom> findARoom(LocalDate checkInDate, LocalDate checkOutDate, RoomCost userPreferredCost) {
        List<IRoom> availableRooms = new LinkedList<>(mapOfRooms.values());

        //Remove Free or Paid room, according to user's selection
        if(userPreferredCost == RoomCost.PAID){
            availableRooms.removeIf(IRoom::isFree);
        }else if(userPreferredCost == RoomCost.FREE){
            availableRooms.removeIf(room -> !room.isFree());
        }

        // check if the Check Out Date of the search is between the Check In Date and The Check Out Date
        // of any other existing reservation and remove that reserved room from the list of
        // available rooms
        for (Reservation reservation : listOfReservations) {
            if (checkInDate.isEqual(reservation.getCheckInDate()) ||
                    checkOutDate.isEqual(reservation.getCheckOutDate()) ||
                        (checkInDate.isAfter(reservation.getCheckInDate()) &&
                            checkInDate.isBefore(reservation.getCheckOutDate()) )||
                        (checkOutDate.isAfter(reservation.getCheckInDate()) &&
                         checkOutDate.isBefore(reservation.getCheckOutDate())) ||
                    (checkInDate.isBefore(reservation.getCheckInDate()) &&
                            checkOutDate.isAfter(reservation.getCheckOutDate()))
            ) {
                availableRooms.remove(reservation.getRoom());
            }
        }
        return availableRooms;
    }

    public void printAllReservation() {
        for (Reservation reservation : listOfReservations) {
            System.out.println(reservation);
        }
    }

    public static ReservationService getInstance() {

        return reservationService;
    }

    public void populateWithTestData(){
        LocalDate todayDate = LocalDate.now();
        CustomerService customerService = CustomerService.getInstance();

        //create test data
        Customer ann = new Customer("Anna", "First", "anna@email.com");
        Customer benn = new Customer("Benn", "Second", "benn@email.com");
        Customer cope = new Customer("Cope", "Third", "cope@email.com");

        IRoom room1 = new Room("101", 120.0, RoomType.SINGLE);
        IRoom room2 = new FreeRoom("102", RoomType.SINGLE);
        IRoom room3 = new Room("200", 140.7, RoomType.DOUBLE);
        IRoom room4 = new FreeRoom("201", RoomType.SINGLE);
        IRoom room5 = new Room("303", 240.0, RoomType.DOUBLE);

        Reservation reservation1 = new Reservation(ann, room1, LocalDate.of(2021, 4, 15), LocalDate.of(2021, 4, 22));
        Reservation reservation2 = new Reservation(benn, room2, todayDate.plusDays(35), todayDate.plusDays(45));
        Reservation reservation3 = new Reservation(cope, room5, todayDate.plusDays(35), todayDate.plusDays(45));
        Reservation reservation4 = new Reservation(cope, room3, todayDate.plusDays(35), todayDate.plusDays(45));

        //Add data in data structure
        customerService.mapOfCustomers.put("anna@email.com", ann);
        customerService.mapOfCustomers.put("benn@email.com", benn);
        customerService.mapOfCustomers.put("cope@email.com", cope);

        mapOfRooms.put("101", room1);
        mapOfRooms.put("102", room2);
        mapOfRooms.put("200", room3);
        mapOfRooms.put("201", room4);
        mapOfRooms.put("303", room5);

        listOfReservations.add(reservation1);
        listOfReservations.add(reservation2);
        listOfReservations.add(reservation3);
        listOfReservations.add(reservation4);

    }
}
