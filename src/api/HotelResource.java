package api;

import model.RoomCost;
import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import javax.management.InstanceAlreadyExistsException;
import java.util.Collection;
import java.time.LocalDate;

/*
    The The HotelResource should have little to no behavior contained
    inside the class and should make use of the Service classes to implement
    its methods
 */
public final class HotelResource {
    private static final HotelResource hotelResource = new HotelResource();
    private CustomerService customerService = CustomerService.getInstance();
    private ReservationService reservationService = ReservationService.getInstance();


    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);

    }

    public void createACustomer(String email, String firstName, String lastName) throws InstanceAlreadyExistsException, IllegalArgumentException {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, LocalDate checkInDate, LocalDate checkOutDate) {
        Customer customer = customerService.getCustomer(customerEmail);
        return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        Customer customer = customerService.getCustomer(customerEmail);
        return reservationService.getCustomersReservation(customer);
    }

    public Collection<IRoom> findARoom(LocalDate checkIn, LocalDate checkOut, RoomCost userPreferredCost) {
        return reservationService.findARoom(checkIn, checkOut, userPreferredCost);
    }

    public static HotelResource getInstance() {
        return hotelResource;
    }


}
