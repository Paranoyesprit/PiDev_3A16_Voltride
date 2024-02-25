package org.example;

import entities.Reservation_b;
import entities.borne;
import service.BorneService;
import service.ReservationBService;
import utils.Datasource;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {
        ReservationBService reservationBService = new ReservationBService();

        // Creating a reservation_b object (replace with actual values)
        Reservation_b reservation = new Reservation_b();
        borne myBorne = new borne();
        myBorne.setEmplacement("Manouba");

// Setting the borne object to the reservation
        reservation.setBorne(myBorne);
        LocalDate date_d = LocalDate.of(2022, 2, 5);
        LocalDate date_f = LocalDate.of(2022, 2, 5);
        LocalTime time_d = LocalTime.of(19, 00);
        LocalTime time_f = LocalTime.of(20, 00);// Adjust the hour and minute accordingly

        // Create LocalDateTime objects using the specified dates and times
        LocalDateTime dateTime_d = LocalDateTime.of(date_d, time_d);
        LocalDateTime dateTime_f = LocalDateTime.of(date_f, time_f);

        reservation.setDate_d(dateTime_d);
        reservation.setDate_f(dateTime_f);

        // Adding the reservation
        //reservationBService.addpst(reservation);
        //LocalDate dateInstallation = LocalDate.of(2022, 1, 29);
        //borne b1=new borne("Ben Arous",1,"En Maintenance ",dateInstallation );
        //BorneService bs =new BorneService();
        //bs.addpst(b1);
        //bs.readAll().forEach(System.out::println);
        //bs.delete(4);
       //reservationBService.delete(4);
        //bs.update(3,b1);
        reservationBService.readAll().forEach(System.out::println);


        // Update both start and end dates in a single call
        Reservation_b updatedReservation = new Reservation_b();
        LocalDate udate = LocalDate.of(2023, 2, 10);
        LocalTime utime = LocalTime.of(14, 30);
        LocalDateTime newDateTime = LocalDateTime.of(udate, utime);
        updatedReservation.setDate_d(newDateTime);

        LocalDate newEndDate = LocalDate.of(2023, 2, 15);
        LocalTime newEndTime = LocalTime.of(16, 30);
        LocalDateTime newEndDateTime = LocalDateTime.of(newEndDate, newEndTime);
        updatedReservation.setDate_f(newEndDateTime);

        // Call the update method
        reservationBService.update(4, updatedReservation);







    }
}