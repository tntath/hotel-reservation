package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;
import java.time.LocalDate;

public class ReservationService {

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

//        //check if reservation exists and return the existing reservation
//        for (Reservation reservation : listOfReservations) {
//            if(reservation.equals(newReservation)) {
//                System.out.println("This reservation already exists");
//                return null;
//            }else if(reservation.getRoom().equals(room) &&
//                    reservation.
//            ){
//
//            }
//
//        }
        //Create new reservation and return it to the caller
        newReservation = new Reservation(customer, room, checkInDate, checkOutDate);
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
    public Collection<IRoom> findARoom(LocalDate checkInDate, LocalDate checkOutDate) {
        List<IRoom> availableRooms = new LinkedList<>(mapOfRooms.values());

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
}
