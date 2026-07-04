package dao;

import model.Booking;

import java.util.List;

public interface BookingDAO {
    int create(Booking booking) throws Exception;
    List<Booking> findByCustomerId(int customerId) throws Exception;
}
