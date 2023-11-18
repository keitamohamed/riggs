package com.keita.riggs.service;

import com.keita.riggs.exception.NotFoundException;
import com.keita.riggs.exception.UnprocessableDataException;
import com.keita.riggs.mapper.ResponseMessage;
import com.keita.riggs.model.*;
import com.keita.riggs.repo.BookingPriceRepo;
import com.keita.riggs.repo.BookingRepo;
import com.keita.riggs.util.Util;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepo bookingRepo;
    private final RoomService roomService;
    private final UserService userService;
    private final BookingPriceRepo bookingPriceRepo;

    @Autowired
    public BookingService(BookingRepo bookingRepo, RoomService roomService, UserService userService, BookingPriceRepo bookingPriceRepo) {
        this.bookingRepo = bookingRepo;
        this.roomService = roomService;
        this.userService = userService;
        this.bookingPriceRepo = bookingPriceRepo;
    }

    public ResponseEntity<?> save (long userID, Booking booking, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new UnprocessableDataException("Unable to Register User", bindingResult);
        }

        long bookingID = Util.generateID(9999999);
        while (isBookingExist(bookingID).isPresent()) {
            bookingID = Util.generateID(9999999);
        }

        booking.setBookingID(bookingID);
        booking.setBookDate(new Date());

        User user = userService.getUser(userID);
        booking.setUser(user);

        Room findRoom;
        int index = 0;
        List<Room> roomList = new ArrayList<>();

        while (booking.getRooms().size() > index) {
            Room r = booking.getRooms().get(index);
            findRoom = roomService.getRoom(r.getRoomID());
            roomList.add(findRoom);
            index++;
        }
        booking.setRooms(roomList);
        Booking bookingResult = bookingRepo.save(booking);

        roomList.forEach(r -> roomService.updateBooking(r, bookingResult));

        String message = String.format("New booking have been created #%s", bookingResult.getBookingID());
        ResponseMessage responseMessage = new ResponseMessage(bookingResult.getBookingID(), message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public Optional<Booking> findBookingByID (Long id, HttpServletResponse response) {
        return bookingRepo.findById(id)
                .map(Optional::of)
                .orElseThrow(() -> new NotFoundException("No booking found with an id " + id));
    }

    public List<Booking> bookingList() {
        List<Booking> bookings = bookingRepo.getAllBookingr();
        if (bookings.isEmpty()) {
            throw new NotFoundException("No bookings available in the database");
        }
        return bookings;
    }

    public List<ChartMonthlyTarget> chartMonthlyTargets () {
        List<ChartMonthlyTarget> chartMonthlyTargetList = new ArrayList<>();
        List<String> monthList = Arrays.stream(Month.values())
                .map(Month::toString)
                .toList();
        monthList.forEach(m -> chartMonthlyTargetList.add((new ChartMonthlyTarget(m.substring(0, 3), 0, 0))));

        List<Booking> bookingList = bookingList();

        return combinePrice(chartMonthlyTargetList, bookingList, monthList);
    }

    private List<ChartMonthlyTarget> combinePrice(List<ChartMonthlyTarget> monthlyTargets, List<Booking> bookings, List<String> months) {
        bookings.sort(Comparator.comparing(Booking::getBookDate));
        for (Booking booking : bookings) {
            Calendar calendar = Util.getDate(booking.getBookDate());
            int month = calendar.get(Calendar.MONTH);
            booking.getPrices().forEach(p -> System.out.println(p.getPrice()));
            double total = booking.getPrices().stream().mapToDouble(BookingPrice::getPrice).sum();
            for (int in = 1; in < bookings.size(); in++) {
                int m = Util.getDate(bookings.get(in).getBookDate()).get(Calendar.MONTH);
                if (month == m) {
                    total += bookings.get(in).getPrices().stream().mapToDouble(BookingPrice::getPrice).sum();
                }
            }
            monthlyTargets.get(month).setAmount(total);
        }
        return monthlyTargets;
    }

    public ResponseEntity<?> deleteBooking (Long id) {
        Optional<Booking> findBooking = isBookingExist(id);
        findBooking.orElseThrow(() -> new NotFoundException("No booking exist with an id " + id));

        String message = String.format("Booking #%s deleted successfully", id);
        Booking booking = findBooking.get();

        int index = 0;
        List<Room> roomList = booking.getRooms();

        while (roomList.size() > index) {
            roomService.updateBooking(roomList.get(index), null);
            index++;
        }

        index = 0;
        List<BookingPrice> bookingPrices = booking.getPrices();
        while (bookingPrices.size() > index) {
            BookingPrice bookingPrice = bookingPrices.get(index);
            bookingPrice.setPrices(null);
            bookingPriceRepo.save(bookingPrice);
            index++;
        }

        bookingPrices.forEach(e -> bookingPriceRepo.deleteBookingPriceById(e.getId()));

        bookingRepo.deleteBookingByBookingID(booking.getBookingID());
        ResponseMessage responseMessage = new ResponseMessage(booking.getBookingID(), message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public Optional<Booking> isBookingExist(Long id) {
        return bookingRepo.findById(id);
    }

}
