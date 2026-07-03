package dao;

import model.Booking;

public interface BookingDAO {
    int create(Booking booking) throws Exception;
}
